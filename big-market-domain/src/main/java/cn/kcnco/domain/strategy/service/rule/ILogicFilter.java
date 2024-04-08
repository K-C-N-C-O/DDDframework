package cn.kcnco.domain.strategy.service.rule;


import cn.kcnco.domain.strategy.model.entity.RuleActionEntity;
import cn.kcnco.domain.strategy.model.entity.RuleMatterEnitity;

//抽奖规则过滤接口
public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {


    RuleActionEntity<T> filter(RuleMatterEnitity ruleMatterEnitity);


}
