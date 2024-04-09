package cn.kcnco.domain.strategy.service.rule.chain.factory;


import cn.kcnco.domain.strategy.model.entity.StrategyEntity;
import cn.kcnco.domain.strategy.repository.IStrategyRepository;
import cn.kcnco.domain.strategy.service.rule.chain.ILogicChain;
import org.springframework.stereotype.Service;

import java.util.Map;

//默认工厂

@Service
public class DefaultChainFactory {

    private final Map<String, ILogicChain> logicChainGroup;

    protected IStrategyRepository repository;

    public DefaultChainFactory(Map<String, ILogicChain> logicChainGroup, IStrategyRepository repository) {
        this.logicChainGroup = logicChainGroup;
        this.repository = repository;
    }

    public ILogicChain openLogicChain(Long strateguId){
        StrategyEntity strategy=repository.queryStrategyEntityByStrategyId(strateguId);
        String[] ruleModels=strategy.ruleModels();
        if(ruleModels==null||ruleModels.length==0){
            return logicChainGroup.get("default");
        }
        ILogicChain logicChain=logicChainGroup.get(ruleModels[0]);
        ILogicChain current=logicChain;
        for(int i=1;i<ruleModels.length;i++){
            ILogicChain nextChain=logicChainGroup.get(ruleModels[i]);
            current=current.appendNext(nextChain);
        }

        current.appendNext(logicChainGroup.get("default"));

        return logicChain;
    }
}
