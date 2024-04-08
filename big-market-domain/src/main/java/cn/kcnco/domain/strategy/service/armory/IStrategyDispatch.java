package cn.kcnco.domain.strategy.service.armory;


//策略抽奖调度
public interface IStrategyDispatch {

    Integer getRandomAwardId(String key);

    Integer getRandomAwardId(Long strategyId, String ruleWeightValue);


}
