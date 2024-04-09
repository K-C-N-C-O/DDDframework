package cn.kcnco.domain.strategy.service.rule.tree.impl;

import cn.kcnco.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.kcnco.domain.strategy.service.rule.tree.ILogicTreeNode;
import cn.kcnco.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

//保底奖励节点

@Slf4j
@Component("rule_stock")
public class RuleStockLogicTreeNode implements ILogicTreeNode {
    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId) {
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .build();
    }
}
