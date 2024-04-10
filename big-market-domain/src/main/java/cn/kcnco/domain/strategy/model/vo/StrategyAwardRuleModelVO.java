package cn.kcnco.domain.strategy.model.vo;

//抽奖策略对应值对象，没有唯一ID，仅限于从数据库查询对象


import cn.kcnco.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardRuleModelVO {

    private String ruleModels;


}
