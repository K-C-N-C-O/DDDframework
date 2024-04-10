package cn.kcnco.domain.strategy.service.rule.chain;

//责任链接口


import cn.kcnco.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

public interface ILogicChain extends ILogicChainArmory{
    /*
    责任链接口
    用户Id
    策略ID
    返回奖品ID
     */
    DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId);


}
