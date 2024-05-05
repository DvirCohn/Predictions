package engine.action.entitiesActions;

import engine.action.api.AbstractAction;
import engine.action.api.Action;
import engine.action.api.ActionType;
import engine.action.condition.ConditionAction.ActionsToDo;
import engine.action.condition.api.Condition;
import engine.context.Context;
import engine.context.build.rules.RuleContext;
import engine.context.build.rules.SecondaryEntityContext;
import engine.conventer.Converter;
import engine.entity.definition.EntityDefinition;
import engine.entity.instance.EntityInstance;
import engine.environment.definition.EnvVariablesManager;
import engine.expression.Expression;
import engine.expression.ReturnValueType;
import generated.PRDAction;
import generated.PRDActions;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProximityAction extends AbstractAction {

    private String sourceName;
    private String targetName;
    private Expression depthEx;
    private Integer depth;
    private List<Action> actions;
    private EntityInstance[][] map;

    // TODO: 9/2/2023 VALIDATE INPUT
    public ProximityAction(EntityDefinition entityDefinition,
                           String depth, String sourceName, String targetName, PRDActions generatedActions,
                           List<EntityDefinition> entities, EnvVariablesManager environment, SecondaryEntityContext secondaryEntityContext, RuleContext ruleContext) {
        super(ActionType.PROXIMITY, entityDefinition, secondaryEntityContext);
        this.sourceName = sourceName;
        this.targetName = targetName;
        this.depthEx = new Expression(depth);
        this.depthEx.updateTypeFromExpression(ruleContext); // transfer rule context
        checkValidDepthExpression(this.depthEx);
        actions = new ArrayList<>();
        createActions(generatedActions,entities,environment);
    }
    public ProximityAction(EntityDefinition entityDefinition, String depth, String sourceName,
                           String targetName, PRDActions generatedActions,
                           List<EntityDefinition> entities, EnvVariablesManager environment,RuleContext ruleContext ) {
        super(ActionType.PROXIMITY, entityDefinition);
        this.sourceName = sourceName;
        this.targetName = targetName;
        this.depthEx = new Expression(depth);
        this.depthEx.updateTypeFromExpression(ruleContext); // transfer rule context
        checkValidDepthExpression(this.depthEx);
        actions = new ArrayList<>();
        createActions(generatedActions,entities,environment);


    }

    public void checkValidDepthExpression(Expression depthEx){
        if (!depthEx.getType().equals(ReturnValueType.FLOAT) && !depthEx.getType().equals(ReturnValueType.INTEGER)){
            throw new IllegalArgumentException("Invalid input in Proximity action depth");
        }
    }

    private void createActions(PRDActions prdActions, List<EntityDefinition> entities, EnvVariablesManager env){
        for(PRDAction prdAction: prdActions.getPRDAction()){
            actions.add(Converter.buildAction(prdAction,entities,env));
        }
    }


    @Override
    public void invoke(Context context) throws Exception {

        Float temp = (Float) this.depthEx.translateExpression(context);
        Integer tempDepth = new Integer(Integer.valueOf(temp.intValue()));
        setDepth(Integer.valueOf(tempDepth));
        map = context.getWorldMap();
        EntityInstance primaryEntityInstance = context.getPrimaryEntityInstance();
        EntityInstance targetInstance = checkProximity(primaryEntityInstance, map);
        if(targetInstance != null){
            context.setSecondaryEntityInstance(targetInstance);
            for(Action action : actions){
                    action.invoke(context);
            }
        }
    }

    public EntityInstance checkProximity(EntityInstance primary, EntityInstance[][] map){

        Point primaryEntityPosition = primary.getEntityLocation();
        int startX,startY;
        int mapRows = map.length;
        int mapCols = map[0].length;
        startX = (primaryEntityPosition.x - depth + mapCols) % mapCols;
        startY = (primaryEntityPosition.y - depth + mapRows) % mapRows;
        for(int row = 0; row < depth * 2 + 1; row++){
            for(int col = 0; col < depth * 2 + 1; col++){
                if (map[(startY + row) % mapRows][(startX + col) % mapCols] != null &&
                        map[(startY + row) % mapRows][(startX + col) % mapCols].getEntityDefinition().getName().toLowerCase().equals(targetName.toLowerCase())){
                    return map[(startY + row) % mapRows][(startX + col) % mapCols];
                }
            }
        }
        return null;

    }

    public String getSourceName() {
        return sourceName;
    }

    public String getTargetName() {
        return targetName;
    }

    public Integer getDepth() {
        return depth;
    }

    public Integer getActionsNumber(){
        return new Integer(actions.size());
    }

    public void setDepth(Integer depth){
        this.depth = depth;
    }

    public Expression getDepthEx(){
        return this.depthEx;
    }

}
