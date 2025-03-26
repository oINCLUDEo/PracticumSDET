package helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.javafaker.Faker;

public class generateData {
    private static final Faker faker = new Faker();
    private static Logger log = LoggerFactory.getLogger(generateData.class);

    public static String generatePostCode() {
        String postCode = faker.number().digits(10);
        log.info("Сгенерирован Post Code: {}", postCode);

        return postCode;
    }

    public static String createFirstNameFromPostCode(String postCode) {
        StringBuilder firstName = new StringBuilder();

        for (int i = 0; i < 10; i += 2) {
            int twoDigits = Integer.parseInt(postCode.substring(i, i + 2));
            char letter = (char) ('a' + (twoDigits % 26));

            firstName.append(letter);
        }
        log.info("Сгенерирован First Name '{}' из Post Code '{}'", firstName, postCode);

        return firstName.toString();
    }
}
