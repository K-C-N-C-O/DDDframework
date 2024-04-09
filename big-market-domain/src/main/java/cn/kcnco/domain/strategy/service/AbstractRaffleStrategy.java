package cn.kcnco.domain.strategy.service;


//抽奖策略抽象类

import cn.kcnco.domain.strategy.model.entity.RaffleAwardEntity;
import cn.kcnco.domain.strategy.model.entity.RaffleFactorEntity;
import cn.kcnco.domain.strategy.model.entity.RuleActionEntity;
import cn.kcnco.domain.strategy.model.entity.StrategyEntity;
import cn.kcnco.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.kcnco.domain.strategy.model.vo.StrategyAwardRuleModelVO;
import cn.kcnco.domain.strategy.repository.IStrategyRepository;
import cn.kcnco.domain.strategy.service.IRaffleStrategy;
import cn.kcnco.domain.strategy.service.armory.IStrategyDispatch;
import cn.kcnco.domain.strategy.service.rule.chain.ILogicChain;
import cn.kcnco.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import cn.kcnco.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import cn.kcnco.types.enums.ResponseCode;
import cn.kcnco.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {

    // 策略仓储服务 -> domain层像一个大厨，仓储层提供米面粮油
    protected IStrategyRepository repository;
    // 策略调度服务 -> 只负责抽奖处理，通过新增接口的方式，隔离职责，不需要使用方关心或者调用抽奖的初始化
    protected IStrategyDispatch strategyDispatch;

    private DefaultChainFactory defaultChainFactory;

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory) {
        this.repository = repository;
        this.strategyDispatch = strategyDispatch;
        this.defaultChainFactory = defaultChainFactory;
    }

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch) {
        this.repository = repository;
        this.strategyDispatch = strategyDispatch;
    }

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        // 1. 参数校验
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if (null == strategyId || StringUtils.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        //2.责任链处理抽奖
        ILogicChain logicChain=defaultChainFactory.openLogicChain(strategyId);
        Integer awardId=logicChain.logic(userId,strategyId);

        //3.查询奖品规则【抽奖中（拿到奖品ID时过滤规则）、抽奖后（扣减完奖品库存后过滤，抽奖中拦截和无库存走保底】
        StrategyAwardRuleModelVO strategyAwardRuleModelVO=repository.queryStrategyAwardRuleModelVO(strategyId,awardId);

        //4.抽奖中，规则过滤
        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionCenterEntity=this.doCheckRaffleCenterLogic(RaffleFactorEntity.builder()
                .userId(userId)
                .strategyId(strategyId)
                .awardId(awardId)
                .build(), strategyAwardRuleModelVO.raffleCenterRuleModelList());

        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionCenterEntity.getCode())){
            log.info("【临时日志】中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。");
            return RaffleAwardEntity.builder()
                    .awardDesc("中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。")
                    .build();
        }

        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();
    }
    protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String... logics);

    protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String... logics);
}