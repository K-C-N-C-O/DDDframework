package cn.kcnco.domain.strategy.service.armory;


import cn.kcnco.domain.strategy.model.entity.StrategyAwardEntity;
import cn.kcnco.domain.strategy.model.entity.StrategyEntity;
import cn.kcnco.domain.strategy.model.entity.StrategyRuleEntity;
import cn.kcnco.domain.strategy.repository.IStrategyRepository;
import cn.kcnco.types.common.Constants;
import cn.kcnco.types.enums.ResponseCode;
import cn.kcnco.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

/*
策略装配库，负责初始化策略计算
 */
@Service
@Slf4j
public class StrategyArmoryDispatch implements IStrategyArmory,IStrategyDispatch{
    @Resource
    private IStrategyRepository repository;

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        //1.查询策略配置
        List<StrategyAwardEntity> strategyAwardEntities=repository.queryStrategyAwardList(strategyId);
        assembleLotteryStrategy(String.valueOf(strategyId),strategyAwardEntities);
        //2.缓存奖品库存,用于decr扣减库存使用
        for(StrategyAwardEntity strategyAward:strategyAwardEntities){
            Integer awardId=strategyAward.getAwardId();
            Integer awardCount=strategyAward.getAwardCount();
            cacheStrategyAwardCount(strategyId,awardId,awardCount);
        }



        //3.1默认装配配置[全量抽奖概率]
        assembleLotteryStrategy(String.valueOf(strategyId),strategyAwardEntities);

        //3.2 权重配置策略 -适用于rule_weight 权重配置规则
        StrategyEntity  strategyEntity=repository.queryStrategyEntityByStrategyId(strategyId);
        String ruleWeight=strategyEntity.getRuleWeight();
        if(ruleWeight==null) return true;
        StrategyRuleEntity strategyRuleEntity=repository.queryStrategyRule(strategyId,ruleWeight);
        if(strategyRuleEntity==null){
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(),ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
        }
        Map<String,List<Integer>> ruleWeightValueMap=strategyRuleEntity.getRuleWeightValues();
        Set<String> keys=ruleWeightValueMap.keySet();
        for(String key:keys){
            List<Integer> ruleWeightValues=ruleWeightValueMap.get(key);
            ArrayList<StrategyAwardEntity> strategyAwardEntitiesClone=new ArrayList<>(strategyAwardEntities);
            strategyAwardEntitiesClone.removeIf(entity->!ruleWeightValues.contains(entity.getAwardId()));
            assembleLotteryStrategy(String.valueOf(strategyId).concat("_").concat(key),strategyAwardEntitiesClone);
        }

        return true;
    }

    private void cacheStrategyAwardCount(Long strategyId, Integer awardId, Integer awardCount) {
        String cacheKey=Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY+strategyId+Constants.UNDERLINE+awardId;
        repository.cacheStrategyAwardCount(cacheKey,awardCount);

    }

    private void assembleLotteryStrategy(String key,List<StrategyAwardEntity> strategyAwardEntities){
        //1.获取最小概率值
        BigDecimal minAwardRate=strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        //2.获取概率值总和
        BigDecimal totalAwardRate=strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        //3.用1%0.0001获取概率范围，百分位，千分位，万分位
        BigDecimal rateRange=totalAwardRate.divide(minAwardRate,0,RoundingMode.CEILING);

        //4.
        ArrayList<Integer> strategyAwardSearchRateTables=new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAward : strategyAwardEntities) {
            Integer awardId=strategyAward.getAwardId();
            BigDecimal awardRate=strategyAward.getAwardRate();
            //计算出每个概率值需要存放到查找表的数量，循环填充
            for(int i=0;i<rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue();i++){
                strategyAwardSearchRateTables.add(awardId);
            }
        }
        //5.乱序
        Collections.shuffle(strategyAwardSearchRateTables);

        //6.
        HashMap<Integer,Integer> shuffleStrategyAwardSearchRateTables=new HashMap<>();
        for(int i=0;i<strategyAwardSearchRateTables.size();i++){
            shuffleStrategyAwardSearchRateTables.put(i,strategyAwardSearchRateTables.get(i));
        }
        //7.存储到Redis
        repository.storeStrategyAwardSearchRateTables(key,new BigDecimal(shuffleStrategyAwardSearchRateTables.size()),shuffleStrategyAwardSearchRateTables);

    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        // 分布式部署下，不一定为当前应用做的策略装配。也就是值不一定会保存到本应用，而是分布式应用，所以需要从 Redis 中获取。
        int rateRange = repository.getRateRange(strategyId);
        // 通过生成的随机值，获取概率值奖品查找表的结果
        return repository.getStrategyAwardAssemble(String.valueOf(strategyId), new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
        String key = String.valueOf(strategyId).concat("_").concat(ruleWeightValue);
        // 分布式部署下，不一定为当前应用做的策略装配。也就是值不一定会保存到本应用，而是分布式应用，所以需要从 Redis 中获取。
        int rateRange = repository.getRateRange(key);
        // 通过生成的随机值，获取概率值奖品查找表的结果
        return repository.getStrategyAwardAssemble(key, new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Boolean subtractionAwardStock(Long strategyId, Integer awardId) {
        String cacheKey=Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY+strategyId+Constants.UNDERLINE+awardId;
        return repository.subtractionAwardStock(cacheKey);
    }
}
