package dto.implement.action.condition;

import dto.implement.action.AbstractActionDTO;

public class AbstractConditionActionDTO extends AbstractActionDTO {


    private String logicOperator;

    public AbstractConditionActionDTO(String type, String primaryEntityName, boolean isSecondaryEntityExists,
               String secondaryEntityName, String logicOperator) {
        super(type, primaryEntityName, isSecondaryEntityExists, secondaryEntityName);
        this.logicOperator = logicOperator;
    }
}
