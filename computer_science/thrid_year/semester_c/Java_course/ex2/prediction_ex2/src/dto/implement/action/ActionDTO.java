package dto.implement.action;

public interface ActionDTO {
    String getName();
    String getPrimaryEntity();
    boolean isSecondaryEntityExists();

    String getSecondaryEntity();
    StringBuffer getActionInfo();
}
