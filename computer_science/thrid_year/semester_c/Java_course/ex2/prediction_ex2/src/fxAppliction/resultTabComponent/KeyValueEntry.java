package fxAppliction.resultTabComponent;

public class KeyValueEntry {
    private String key;
    private Integer value;

    public KeyValueEntry(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Integer getValue() {
        return value;
    }
}