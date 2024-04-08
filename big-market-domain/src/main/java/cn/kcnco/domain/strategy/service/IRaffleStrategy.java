package cn.kcnco.domain.strategy.service;


import cn.kcnco.domain.strategy.model.entity.RaffleAwardEntity;
import cn.kcnco.domain.strategy.model.entity.RaffleFactorEntity;

//抽奖策略接口
public interface IRaffleStrategy {
    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity);

}
