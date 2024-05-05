package engine.conventer.xml.loader;

import engine.property.definition.type.BooleanProperty;
import engine.world.WorldDefinition;
import generated.PRDWorld;
import generated.PRDEntities;
import generated.PRDEntity;
import generated.PRDProperty;
import generated.PRDProperties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class Loader {


    private final static String JAXB_XML_GAME_PACKAGE_NAME = "generated";
    private final String path;
    public Loader(String path){
        this.path = path;
    }

    public PRDWorld setLoadWorld() throws FileNotFoundException, JAXBException{
        PRDWorld loadWorld = null;
        try {
            loadWorld = new PRDWorld();
            InputStream inputStream = new FileInputStream(new File(path)); // change to path
            loadWorld = deserializeFrom(inputStream);
            return loadWorld;

        } catch (JAXBException | FileNotFoundException e) {
            throw e;
        }
    }
    public PRDWorld deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }
}

