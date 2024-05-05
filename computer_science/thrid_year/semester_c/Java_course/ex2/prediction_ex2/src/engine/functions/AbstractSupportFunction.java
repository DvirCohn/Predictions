package engine.functions;

public abstract class AbstractSupportFunction {

    private String functionName;
    private FunctionType functionType;
    public AbstractSupportFunction(String funcName,FunctionType functionType ) {
        this.functionName = funcName;
        this.functionType = functionType;
    }

}
