package cn.kcnco.domain.strategy.model.entity;


import cn.kcnco.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//规则动作实体
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleActionEntity <T extends  RuleActionEntity.RaffleEntity>{

    private String code= RuleLogicCheckTypeVO.ALLOW.getCode();
    private String info=RuleLogicCheckTypeVO.ALLOW.getInfo();
    private String ruleModel;
    private T data;

    static public class RaffleEntity{

    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    //抽奖前
    static public class RaffleBeforeEntity extends RaffleEntity{

        //策略ID
        private Long strategyId;

        //权重Key值，用于抽奖时可以选择权重抽奖
        private String ruleWeightValueKey;

        //奖品ID
        private Integer awardId;


    }
    //抽奖中
    static public class RaffleCenterEntity extends RaffleEntity{


    }
    //抽奖后
    static public class RaffleAfterEntity extends RaffleEntity{


    }

}
