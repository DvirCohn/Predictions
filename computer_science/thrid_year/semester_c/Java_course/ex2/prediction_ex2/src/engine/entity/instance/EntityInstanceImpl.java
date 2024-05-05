package engine.entity.instance;

import engine.property.definition.PropertyDefinition;
import engine.property.instance.PropertyInstance;
import engine.entity.definition.EntityDefinition;
import engine.property.instance.PropertyInstanceImpl;

import java.awt.*;
import java.util.*;
import java.util.List;

public class EntityInstanceImpl implements EntityInstance {

    private static int counter = 0;
    private final EntityDefinition entityDefinition;
    private final int id;
    private Map<String, PropertyInstance> properties;
    private Point entityLocation;
    private boolean isAlive = true;


    public EntityInstanceImpl(EntityDefinition entityDefinition) {
        this.entityDefinition = entityDefinition;
        this.id = ++counter;
        this.properties = createProperties(entityDefinition.getProps());
        this.entityLocation = new Point();

    }

    private Map<String, PropertyInstance> createProperties(List<PropertyDefinition> props) {
        Map<String, PropertyInstance>  propertyMap = new HashMap<String, PropertyInstance>();
        for (PropertyDefinition prop: props) {
            propertyMap.put(prop.getName() ,new PropertyInstanceImpl(prop));
        }
        return propertyMap;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public PropertyInstance getPropertyByName(String name) throws IllegalArgumentException {
        if (!properties.containsKey(name)) {
            throw new IllegalArgumentException("for entity of type " + entityDefinition.getName() + " has no property named " + name);
        }

        return properties.get(name);
    }

    @Override
    public void addPropertyInstance(PropertyInstance propertyInstance) {
        properties.put(propertyInstance.getPropertyDefinition().getName(), propertyInstance);
    }
    @Override
    public Map<String, PropertyInstance> getProperties(){
        return properties;
    }

    @Override
    public EntityDefinition getEntityDefinition() {
        return this.entityDefinition;
    }

    @Override
    public Point getEntityLocation(){
        return this.entityLocation;
    }

    @Override
    public void setLocation(int x, int y) {
        entityLocation.setLocation(x, y);
    }

    @Override
    public void setAlive(boolean alive) {
        isAlive = alive;
    }
    @Override
    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public void makeMove(EntityInstance[][] map){

        int mapRows = map.length;
        int mapCols = map[0].length;
        Directions[] sides = { Directions.UP, Directions.DOWN, Directions.RIGHT, Directions.LEFT };
        List<Directions> consts = Arrays.asList(sides);
        Directions direct;
        int currentX = this.getEntityLocation().x;
        int currentY = this.getEntityLocation().y;
        Integer nextX = null;
        Integer nextY = null;

        boolean flag = true;
        while(consts.size() > 0 && flag) {
            direct = randomDirection();
            if (consts.contains(direct)) {
                consts = updateConsts(consts, direct);

                switch (direct) {
                    case UP: {
                        int newY = (currentY - 1 + mapRows) % mapRows;
                        if (map[newY][currentX] == null) {
                            nextX = new Integer(currentX);
                            nextY = new Integer(newY);
                            flag = false;
                        }
                        break;
                    }
                    case DOWN: {
                        int newY = (currentY + 1) % mapRows;
                        if (map[newY][currentX] == null) {
                            nextX = new Integer(currentX);
                            nextY = new Integer(newY);
                            flag = false;
                        }
                        break;
                    }
                    case LEFT: {
                        int newX = (currentX - 1 + mapCols) % mapCols;
                        if (map[currentY][newX] == null) {
                            nextX = new Integer(newX);
                            nextY = new Integer(currentY);
                            flag = false;
                        }
                        break;
                    }
                    case RIGHT: {
                        int newX = (currentX + 1) % mapCols;
                        if (map[currentY][newX] == null) {
                            nextX = new Integer(newX);
                            nextY = new Integer(currentY);
                            flag = false;
                        }
                        break;
                    }
                }

                if (nextX != null && nextY != null){
                    map[nextY][nextX] = this;
                    map[currentY][currentX] = null;
                    this.setLocation(nextX, nextY);
                }
            }
        }

    }

    private List<Directions> updateConsts(List<Directions> consts, Directions direct) {
        List<Directions> newConsts = new ArrayList<>();
        for (Directions direction: consts){
            if (!direction.equals(direct)){
                newConsts.add(direction);
            }
        }
        return newConsts;
    }

    @Override
    public void setLocation(Point point){
        this.entityLocation = point;
    }
    @Override
    public Point getLocation() {
        return this.entityLocation;
    }

    public Directions randomDirection(){
        Random random  = new Random();
        Directions[] consts = {Directions.UP,Directions.DOWN,Directions.RIGHT,Directions.LEFT};
        int randomDirect = random.nextInt(consts.length);
        return consts[randomDirect];
    }
}
