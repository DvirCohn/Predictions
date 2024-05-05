package engine.functions;


import engine.environment.instance.ActiveEnvironment;
import engine.property.instance.PropertyInstance;

public class EnvironmentFunction extends AbstractSupportFunction {


    private final String funcName = "environment";

    private ActiveEnvironment activeEnv;

    public EnvironmentFunction(String funcName, ActiveEnvironment activeEnv) {
        super(funcName,FunctionType.ENVIRONMENT);
        this.activeEnv = activeEnv;
    }

    public PropertyInstance invoke(String propName){
      return this.activeEnv.getProperty(propName);
    }

}
