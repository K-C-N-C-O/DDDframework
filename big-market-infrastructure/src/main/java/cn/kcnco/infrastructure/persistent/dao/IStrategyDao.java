package cn.kcnco.infrastructure.persistent.dao;


import cn.kcnco.infrastructure.persistent.po.Strategy;
import cn.kcnco.infrastructure.persistent.po.StrategyAward;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/*
策略规则Dao
 */
@Mapper
public interface IStrategyDao {
    List<Strategy> queryStrategyList();
}
