package cn.kcnco.domain.strategy.service.rule.tree.factory;


import cn.kcnco.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.kcnco.domain.strategy.model.vo.RuleTreeVO;
import cn.kcnco.domain.strategy.service.rule.tree.ILogicTreeNode;
import cn.kcnco.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import cn.kcnco.domain.strategy.service.rule.tree.factory.engine.impl.DecisionTreeEngine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

//规则树工厂
@Service
public class DefaultTreeFactory {
    private final Map<String,ILogicTreeNode> logicTreeNodeGroup;

    public DefaultTreeFactory(Map<String, ILogicTreeNode> logicTreeNodeGroup) {
        this.logicTreeNodeGroup = logicTreeNodeGroup;
    }

    public IDecisionTreeEngine openLogicTree(RuleTreeVO ruleTreeVO) {
        return new DecisionTreeEngine(logicTreeNodeGroup, ruleTreeVO);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public  static class TreeActionEntity{
        private RuleLogicCheckTypeVO ruleLogicCheckType;
        private StrategyAwardData strategyAwardData;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAwardData {
        //抽奖奖品ID，内部流转使用
        private Integer awardId;
        //抽奖奖品规则
        private String awardRuleValue;
    }
}
