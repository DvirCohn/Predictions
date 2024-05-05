package dto.implement;

import engine.entity.definition.EntityDefinition;
import engine.rule.api.Rule;
import engine.world.WorldDefinition;

import java.util.ArrayList;
import java.util.List;

public class WorldInfoDTO {
    private List<EntityDTO> entDTO;
    private List<RuleDTO> rulesDTO;
    private TerminationDTO terminationDTO;
    private EnvironmentDTO envDTO;
    private  GridDTO gridDTO;

    public WorldInfoDTO(WorldDefinition worldDef) {
        this.terminationDTO = new TerminationDTO(worldDef.getTermination());
        this.rulesDTO = createRulesDTOList(worldDef.getRules());
        this.entDTO = createEntityDTOList(worldDef.getEntityDefinition());
        this.envDTO = new EnvironmentDTO(worldDef.getEnvironment());
        this.gridDTO  = new GridDTO(worldDef.getCols(), worldDef.getRows());
    }

    public GridDTO getGridDTO() {
        return gridDTO;
    }

    public EnvironmentDTO getEnvDTO() {
        return envDTO;
    }

    public List<EntityDTO> getEntDTO() {
        return entDTO;
    }

    public List<RuleDTO> getRulesDTO() {
        return rulesDTO;
    }

    public TerminationDTO getTerminationDTO() {
        return terminationDTO;
    }

    private List<EntityDTO> createEntityDTOList(List<EntityDefinition> entityDefinition) {
        List<EntityDTO> entityDTOList= new ArrayList<>();

        for (EntityDefinition entDef: entityDefinition) {
            EntityDTO entityDTO = new EntityDTO(entDef);
            entityDTOList.add(entityDTO);
        }

        return entityDTOList;
    }

    private List<RuleDTO> createRulesDTOList(List<Rule> rules) {
        List<RuleDTO> ruleDTOList= new ArrayList<>();

        for (Rule rule: rules) {
            RuleDTO ruleDTO = new RuleDTO(rule);
            ruleDTOList.add(ruleDTO);
        }

        return ruleDTOList;
    }


}
