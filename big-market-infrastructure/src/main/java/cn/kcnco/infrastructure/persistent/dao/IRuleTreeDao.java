package cn.kcnco.infrastructure.persistent.dao;

import cn.kcnco.infrastructure.persistent.po.RuleTree;
import org.apache.ibatis.annotations.Mapper;

/**
规则树表DAO
 */
@Mapper
public interface IRuleTreeDao {

    RuleTree queryRuleTreeByTreeId(String treeId);

}
