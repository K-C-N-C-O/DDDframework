package cn.kcnco.domain.strategy.service.rule.tree.factory.engine;

//规则树组合接口

import cn.kcnco.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

public interface IDecisionTreeEngine {
    DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Integer awardId);

}
