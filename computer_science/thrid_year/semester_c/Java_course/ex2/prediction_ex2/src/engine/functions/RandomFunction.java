package engine.functions;

import java.util.Random;

public class RandomFunction extends AbstractSupportFunction {

    private final String funcName = "random";
    private static final int from = 0;
    protected Random random;

    public RandomFunction(String funcName) {
        super(funcName, FunctionType.RANDOM);
        random = new Random();
    }


    public Integer invoke(int toNumber){

        return from + random.nextInt(toNumber - from);
    }



}
