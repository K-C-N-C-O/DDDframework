package cn.kcnco.domain.strategy.service.rule.chain;

//责任链接口


public interface ILogicChain extends ILogicChainArmory{
    /*
    责任链接口
    用户Id
    策略ID
    返回奖品ID
     */
    Integer logic(String userId,Long strategyId);


}
