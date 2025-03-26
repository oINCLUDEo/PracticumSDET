package helpers;

import com.github.javafaker.Faker;

public class generateData {
    private static final Faker faker = new Faker();

    public static String generatePostCode() {
        String postCode = faker.number().digits(10);
        return postCode;
    }
}
