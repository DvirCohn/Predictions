package dto.implement.action;

import engine.action.api.AbstractAction;
import engine.action.numericAction.Divide;
import engine.action.numericAction.Multiply;

public class CalculationActionDTO extends AbstractActionDTO {

    private String type;
    private String firstArg;
    private String secondArg;

    public CalculationActionDTO(Divide divideAction){
        super("Calculation", divideAction.getEntityDefinition().getName(),
                false, null);
        this.type = "divide";
        this.firstArg = divideAction.getArg1Expression();
        this.secondArg = divideAction.getArg2Expression();
        this.getNameOfArgumentToValue().put("Operation type", "divide");
        this.getNameOfArgumentToValue().put("first argument for function", firstArg);
        this.getNameOfArgumentToValue().put("second argument for function", secondArg);
        this.getNameOfArgumentToValue().put("result-prop", divideAction.getResultProperty());
    }
    public CalculationActionDTO(Multiply multiplyAction){
        super("Calculation", multiplyAction.getEntityDefinition().getName(),
                false, null);
        this.type = "multiply";
        this.firstArg = multiplyAction.getArg1Expression();
        this.secondArg = multiplyAction.getArg2Expression();
        this.getNameOfArgumentToValue().put("Operation type", "Multiply");
        this.getNameOfArgumentToValue().put("first argument for function", firstArg);
        this.getNameOfArgumentToValue().put("second argument for function", secondArg);
        this.getNameOfArgumentToValue().put("result-prop", multiplyAction.getResultProperty());
    }

    @Override
    public StringBuffer getActionInfo() {
        StringBuffer calcInfo = super.getActionInfo();
        calcInfo.append("Calculation type: "+this.type+"\n");
        return calcInfo;
    }
}
