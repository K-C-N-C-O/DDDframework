package cn.kcnco.domain.strategy.service.rule.filter.impl;

import cn.kcnco.domain.strategy.model.entity.RuleActionEntity;
import cn.kcnco.domain.strategy.model.entity.RuleMatterEnitity;
import cn.kcnco.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.kcnco.domain.strategy.repository.IStrategyRepository;
import cn.kcnco.domain.strategy.service.annotation.LogicStrategy;
import cn.kcnco.domain.strategy.service.rule.filter.ILogicFilter;
import cn.kcnco.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

//用户抽奖n次后对应奖品可解锁抽奖
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_LOCK)
public class RuleLockLogicFilter implements ILogicFilter<RuleActionEntity.RaffleCenterEntity> {

    @Resource
    private IStrategyRepository repository;

    private Long userRaffleCount=0L;
    @Override
    public RuleActionEntity<RuleActionEntity.RaffleCenterEntity> filter(RuleMatterEnitity ruleMatterEnitity) {
        log.info("规则过滤-次数锁 userId:{} strategyId；{} ruleModel:{}",ruleMatterEnitity.getUserId(),ruleMatterEnitity.getStrategyId(),ruleMatterEnitity.getRuleModel());

        String ruleValue=repository.queryStrategyRuleValue(ruleMatterEnitity.getStrategyId(),ruleMatterEnitity.getAwardId(),ruleMatterEnitity.getRuleModel());
        long raffleCount=Long.parseLong(ruleValue);
        if(userRaffleCount>=raffleCount){
            return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }

        return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                .build();
    }


}
