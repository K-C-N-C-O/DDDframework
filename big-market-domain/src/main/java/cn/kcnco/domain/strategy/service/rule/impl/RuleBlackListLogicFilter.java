package cn.kcnco.domain.strategy.service.rule.impl;

import cn.kcnco.domain.strategy.model.entity.RuleActionEntity;
import cn.kcnco.domain.strategy.model.entity.RuleMatterEnitity;
import cn.kcnco.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.kcnco.domain.strategy.repository.IStrategyRepository;
import cn.kcnco.domain.strategy.service.annotation.LogicStrategy;
import cn.kcnco.domain.strategy.service.rule.ILogicFilter;
import cn.kcnco.domain.strategy.service.rule.factory.DefaultLogicFactory;
import cn.kcnco.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_BLACKLIST)
public class RuleBlackListLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {

    @Resource
    private IStrategyRepository repository;

    @Override
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEnitity ruleMatterEnitity) {
        log.info("规则过滤-黑名单 userId:{} strategyId:{} ruleModel:{}", ruleMatterEnitity.getUserId(), ruleMatterEnitity.getStrategyId(), ruleMatterEnitity.getRuleModel());

        String userId = ruleMatterEnitity.getUserId();

        // 查询规则值配置
        String ruleValue = repository.queryStrategyRuleValue(ruleMatterEnitity.getStrategyId(), ruleMatterEnitity.getAwardId(), ruleMatterEnitity.getRuleModel());
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Integer awardId = Integer.parseInt(splitRuleValue[0]);

        // 过滤其他规则
        String[] userBlackIds = splitRuleValue[1].split(Constants.SPLIT);
        for (String userBlackId : userBlackIds) {
            if (userId.equals(userBlackId)) {
                return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                        .ruleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())
                        .data(RuleActionEntity.RaffleBeforeEntity.builder()
                                .strategyId(ruleMatterEnitity.getStrategyId())
                                .awardId(awardId)
                                .build())
                        .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                        .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                        .build();
            }
        }
        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();
    }
}
