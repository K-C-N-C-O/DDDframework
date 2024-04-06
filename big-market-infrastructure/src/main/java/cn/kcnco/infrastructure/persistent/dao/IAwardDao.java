package cn.kcnco.infrastructure.persistent.dao;


import cn.kcnco.infrastructure.persistent.po.Award;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/*
奖品表Dao
 */
@Mapper
public interface IAwardDao {
    List<Award> queryAwardList();
}
