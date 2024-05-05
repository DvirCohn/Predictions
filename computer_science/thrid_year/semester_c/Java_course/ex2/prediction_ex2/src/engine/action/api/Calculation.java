package engine.action.api;


import engine.context.build.rules.SecondaryEntityContext;
import engine.property.definition.PropertyType;
import engine.property.instance.PropertyInstance;
import engine.entity.definition.EntityDefinition;

public abstract class Calculation extends AbstractAction{

    protected String resultProperty;

    public Calculation(EntityDefinition entityDefinition, String resultProperty, ActionType actionType
            , SecondaryEntityContext secondaryEntityContext) {
        super(actionType, entityDefinition, secondaryEntityContext);
        this.resultProperty = resultProperty;
    }

    public Calculation(EntityDefinition entityDefinition, String resultProperty, ActionType actionType) {
        super(actionType, entityDefinition);
        this.resultProperty = resultProperty;
    }

    public String getResultProperty(){
        return this.resultProperty;
    }



}
