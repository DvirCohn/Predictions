package dto.implement;

import java.util.Date;

public class EndOfSimulationDTO {

    boolean endBecauseOfTicks;
    boolean endBecauseOfSeconds;
    Date dateOfEnd;

    public EndOfSimulationDTO(boolean endBecauseOfTicks, boolean endBecauseOfSeconds, Date dateOfEnd) {
        this.endBecauseOfTicks = endBecauseOfTicks;
        this.endBecauseOfSeconds = endBecauseOfSeconds;
        this.dateOfEnd = dateOfEnd;
    }



    public boolean isEndBecauseOfTicks() {
        return endBecauseOfTicks;
    }

    public boolean isEndBecauseOfSeconds() {
        return endBecauseOfSeconds;
    }

    public Date getDateOfEnd() {
        return dateOfEnd;
    }
}
