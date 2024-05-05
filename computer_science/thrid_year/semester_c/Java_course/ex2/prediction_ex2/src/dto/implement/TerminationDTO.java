package dto.implement;

import engine.termination.TerminationDefinition;

public class TerminationDTO {
    private Integer terminateAfterSeconds;
    private Integer terminateAfterTicks;

    public TerminationDTO(Integer terminateAfterSeconds, Integer terminateAfterTicks) {
        this.terminateAfterSeconds = terminateAfterSeconds;
        this.terminateAfterTicks = terminateAfterTicks;
    }

    public TerminationDTO(TerminationDefinition termDef) {
        this.terminateAfterSeconds = termDef.getSecondsLimit();
        this.terminateAfterTicks = termDef.getTicksLimit();
    }

    public Integer getTerminateAfterSeconds() {
        return terminateAfterSeconds;
    }

    public Integer getTerminateAfterTicks() {
        return terminateAfterTicks;
    }

    public StringBuffer getTerminationInfo(){
        StringBuffer info = new StringBuffer();
        info.append("Termination:"+"\n\n");
        info.append("Active by ticks - "+this.getTerminateAfterTicks()+"\n");
        info.append("Active by probability - "+this.getTerminateAfterSeconds()+"\n");
        return info;
    }
}
