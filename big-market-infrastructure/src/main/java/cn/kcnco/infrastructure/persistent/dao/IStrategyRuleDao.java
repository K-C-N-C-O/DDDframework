package cn.kcnco.infrastructure.persistent.dao;


import cn.kcnco.infrastructure.persistent.po.Strategy;
import cn.kcnco.infrastructure.persistent.po.StrategyRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/*
抽奖策略Dao
 */
@Mapper
public interface IStrategyRuleDao {
    List<StrategyRule> queryStrategyRuleList();

    StrategyRule queryStrategyRule(StrategyRule strategyRuleReq);

    String queryStrategyRuleValue(StrategyRule strategyRule);
}
