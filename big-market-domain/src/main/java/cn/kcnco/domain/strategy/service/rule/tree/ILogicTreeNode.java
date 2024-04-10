package cn.kcnco.domain.strategy.service.rule.tree;


//规则树接口

import cn.kcnco.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

public interface ILogicTreeNode {

    DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId,String ruleValue);

}
