package engine.property.definition.generator.random.impl.string;

import engine.property.definition.generator.defenition.ValueGenerator;
import engine.property.definition.generator.random.api.AbstractRandomValueGenerator;
import generated.PRDProperty;

import java.util.Random;

public class RandomStringValueGenerator extends AbstractRandomValueGenerator<String> {

    final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!?,_-().";
    final int maxLength = 50;

    @Override
    public String generateValue() {
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();
        int length = random.nextInt(maxLength);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }

    @Override
    public boolean isRandomInit() {
        return true;
    }
}
