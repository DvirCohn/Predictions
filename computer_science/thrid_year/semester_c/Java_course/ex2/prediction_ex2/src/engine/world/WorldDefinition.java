package engine.world;


import engine.entity.definition.EntityDefinition;
import engine.entity.definition.EntityDefinitionImpl;
import engine.environment.definition.EnvVariableManagerImpl;
import engine.environment.definition.EnvVariablesManager;
import engine.rule.RuleImpl;
import engine.rule.api.Rule;
import engine.termination.Termination;
import generated.PRDEntities;
import generated.PRDEntity;
import generated.PRDRule;
import generated.PRDWorld;

import java.util.ArrayList;
import java.util.List;

public class WorldDefinition {
    private EnvVariablesManager environment;
    private List<EntityDefinition> entities;
    private List<Rule> rules;
    private Termination termination;
    private Integer rows;
    private Integer cols;
    private Integer threadsCount;

    public WorldDefinition(PRDWorld generatedWorld){
        environment = new EnvVariableManagerImpl(generatedWorld.getPRDEnvironment());
        termination = new Termination(generatedWorld.getPRDTermination());
        entities = getEntitiesFromGeneratedEntities(generatedWorld.getPRDEntities());
        rules = getRulesFromGeneratedRules(generatedWorld.getPRDRules().getPRDRule(), entities, environment);
        try{
            rows = new Integer(generatedWorld.getPRDGrid().getRows());
        }
        catch (NumberFormatException exception){
            throw new NumberFormatException("invalid input in the grid rows in XML file");
        }
        try{
            cols = new Integer(generatedWorld.getPRDGrid().getColumns());
        }
        catch (NumberFormatException exception){
            throw new NumberFormatException("invalid input in the grid cols in XML file");
        }try{
            threadsCount = new Integer(generatedWorld.getPRDThreadCount());
        }
        catch (NumberFormatException exception){
            throw new NumberFormatException("invalid input in the thread count in XML file");
        }



    }

    private List<Rule> getRulesFromGeneratedRules(List<PRDRule> prdRules, List<EntityDefinition> entities, EnvVariablesManager environment) {
        List<Rule> ruleList = new ArrayList<>();
        for (PRDRule generatedRule: prdRules) {
            Rule addRule = new RuleImpl(generatedRule , entities, environment);
            ruleList.add(addRule);
        }
        return ruleList;
    }

    private List<EntityDefinition> getEntitiesFromGeneratedEntities(PRDEntities prdEntities) {
        List<EntityDefinition> entities = new ArrayList<>();
        for (PRDEntity generatedEntity: prdEntities.getPRDEntity()) {
            EntityDefinition entity = new EntityDefinitionImpl(generatedEntity);
            entities.add(entity);
        }
        return entities;
    }

    public EnvVariablesManager getEnvironment() {
        return environment;
    }

    public Termination getTermination(){
        return this.termination;
    }

    public List<Rule> getRules(){
        return this.rules;
    }

    public List<EntityDefinition> getEntityDefinition(){
        return this.entities;
    }

    public Integer getCols() {
        return cols;
    }

    public Integer getRows() {
        return rows;
    }

    public Integer getThreadsCount() {
        return threadsCount;
    }
}
