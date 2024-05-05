package engine.simulator.api;

import java.io.InputStream;

public interface DataLoader {

    void setRootObject(String path);

    Object deserializeFrom(InputStream in);

}
