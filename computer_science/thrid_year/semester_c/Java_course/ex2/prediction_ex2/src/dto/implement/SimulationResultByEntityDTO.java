package dto.implement;

public class SimulationResultByEntityDTO {
    int beginPopulation;
    int endPopulation;
    String EntityName;

    public SimulationResultByEntityDTO(int beginPopulation, int endPopulation, String entityName) {
        this.beginPopulation = beginPopulation;
        this.endPopulation = endPopulation;
        EntityName = entityName;
    }

    public int getBeginPopulation() {
        return beginPopulation;
    }

    public int getEndPopulation() {
        return endPopulation;
    }

    public String getEntityName() {
        return EntityName;
    }
}
