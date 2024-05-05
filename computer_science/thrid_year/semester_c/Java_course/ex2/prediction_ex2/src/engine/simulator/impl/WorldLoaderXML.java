package engine.simulator.impl;

import engine.conventer.xml.loader.Loader;
import engine.simulator.api.DataLoader;
import generated.PRDWorld;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class WorldLoaderXML implements DataLoader {

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "generated";
    private Loader loader;

    public WorldLoaderXML() {
    }

    @Override
    public void setRootObject(String path) {

        try {
            PRDWorld loadWorld = new PRDWorld();
            InputStream inputStream = new FileInputStream(new File(path));
            loadWorld = (PRDWorld) deserializeFrom(inputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public Object deserializeFrom(InputStream in) {

        try {
            JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
            Unmarshaller u = jc.createUnmarshaller();
            return (PRDWorld) u.unmarshal(in);
        } catch (JAXBException ex) {

            return null;
        }
    }
}




