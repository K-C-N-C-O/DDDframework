package cn.kcnco.domain.strategy.service.armory;


/*
策略装配库，负责初始化策略计算
 */
public interface IStrategyArmory {
    void assembleLotteryStrategy(Long strategyId);

    Integer getRandomAwardId(Long strategyId);
}
