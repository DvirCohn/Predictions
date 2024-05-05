package engine.action.api;


import engine.context.build.rules.SecondaryEntityContext;
import engine.entity.definition.EntityDefinition;
import engine.property.definition.PropertyType;
import engine.property.instance.PropertyInstance;

import java.util.Optional;

public abstract class AbstractAction implements Action {

    private final ActionType actionType;
    private final EntityDefinition entityDefinition;
    private SecondaryEntityContext secondaryEntityContext;


    public AbstractAction(ActionType actionType, EntityDefinition entityDefinition, SecondaryEntityContext secondaryEntityContext) {
        this.actionType = actionType;
        this.entityDefinition = entityDefinition;
        this.secondaryEntityContext = secondaryEntityContext;
    }

    public AbstractAction(ActionType actionType, EntityDefinition entityDefinition){
       this(actionType,entityDefinition,null);
    }

    @Override
    public Optional <SecondaryEntityContext>  getSecondaryEntityContext() {
        return Optional.ofNullable(this.secondaryEntityContext);
    }

    @Override
    public boolean isSecondEntityExists() {
        return secondaryEntityContext.isSecondaryEntity();
    }

    @Override
    public EntityDefinition getEntityDefinition() {
        return this.entityDefinition;
    }

    @Override
    public ActionType getActionType() {
        return this.actionType;
    }

    @Override
    public EntityDefinition getContextEntity() {
        return this.entityDefinition;
    }

    protected boolean verifyNumericPropertyType(PropertyInstance propertyValue)
    {
        return PropertyType.DECIMAL.equals(propertyValue.getPropertyDefinition().getType()) ||
                PropertyType.FLOAT.equals(propertyValue.getPropertyDefinition().getType());
    }
}
