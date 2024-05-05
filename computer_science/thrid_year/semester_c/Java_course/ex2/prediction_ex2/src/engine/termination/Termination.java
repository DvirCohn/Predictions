package engine.termination;

import generated.PRDBySecond;
import generated.PRDByTicks;
import generated.PRDTermination;
import java.time.Duration;
import java.time.Instant;

public class Termination implements TerminationDefinition {
    private Integer tickLimit;
    private Integer secondsLimit;
    private Instant endTime;

    public Termination(PRDTermination generatedTerm){
        for (Object value: generatedTerm.getPRDBySecondOrPRDByTicks()){
            if(value.getClass() == PRDByTicks.class){
                tickLimit = ((PRDByTicks) value).getCount();
            }
            else if(value.getClass() == PRDBySecond.class){
                secondsLimit = ((PRDBySecond) value).getCount();
                endTime = Instant.now();
            }
        }
    }

    public Termination(Integer ticks, Integer seconds){
        this.tickLimit = ticks;
        this.secondsLimit = seconds;

        if(secondsLimit != null){
            Duration timeInterval = Duration.ofSeconds(seconds);
            Instant startTime = Instant.now();
            endTime = startTime.plus(timeInterval);
        }
        else {
            endTime = null;
        }
    }

    public void setEndTime() {
        if (secondsLimit != null) {
            Duration timeInterval = Duration.ofSeconds(secondsLimit);
            Instant startTime = Instant.now();
            endTime = startTime.plus(timeInterval);
        }
    }

    public Instant getEndTime() {
        return endTime;
    }

    @Override
    public Integer getTicksLimit() {
        return tickLimit;
    }

    @Override
    public Integer getSecondsLimit() {
        return secondsLimit;
    }

    boolean isSimulationOverByTicks(int ticksPassed){
        return tickLimit < ticksPassed;
    }

    private boolean isSimulationOverBySeconds(){
        return Instant.now().isAfter(endTime);
    }

    public boolean isSimulationOver(int ticks){
        boolean overBySeconds = false;
        boolean overByTicks = false;

        if (getTicksLimit()!= null){
            overByTicks = isSimulationOverByTicks(ticks);
        }

        if (getSecondsLimit() != null){
            overBySeconds = isSimulationOverBySeconds();
        }

        return overBySeconds || overByTicks;
    }

}
