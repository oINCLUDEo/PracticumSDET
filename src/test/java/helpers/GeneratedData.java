package helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.javafaker.Faker;

import java.util.Comparator;
import java.util.List;

import static com.codeborne.selenide.Selenide.$$;

public class GeneratedData {
    private static final Faker faker = new Faker();
    private static Logger log = LoggerFactory.getLogger(GeneratedData.class);

    public static class CustomerForDeletion {
        public final int index;
        public final String firstName;
        public final String lastName;

        public CustomerForDeletion(int index, String firstName, String lastName) {
            this.index = index;
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

    public static String generatePostCode() {
        String postCode = faker.number().digits(10);

        log.info("Сгенерирован Post Code: '{}'", postCode);
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

    public static List<String> getCustomersFirstNames() {
        List<String> customersFirstNames = $$("tbody tr td:nth-child(1)").texts();

        log.info("Получен список First Names: {}", customersFirstNames);
        return customersFirstNames;
    }

    public static List<String> getCustomersLastNames() {
        List<String> customersLastNames = $$("tbody tr td:nth-child(2)").texts();

        log.info("Получен список Last Names: {}", customersLastNames);
        return customersLastNames;
    }

    public static CustomerForDeletion getClientForDeletion(List<String> customersFirstNames, List<String> customersLastNames) {
        if (customersFirstNames == null || customersFirstNames.isEmpty()) {
            log.error("Список клиентов пуст. Невозможно определить клиента для удаления.");
            return null;
        }

        double averageLength = customersFirstNames.stream()
                .mapToInt(String::length)
                .average()
                .orElse(0.0);

        int closestFirstNameIndex = 0;
        double smallestDiff = Double.MAX_VALUE;
        boolean hasDuplicateCandidates = false;


        for (int i = 0; i < customersFirstNames.size(); i++) {
            int currentLength = customersFirstNames.get(i).length();
            double diff = Math.abs(currentLength - averageLength);

            if (diff < smallestDiff) {
                smallestDiff = diff;
                closestFirstNameIndex = i;
                hasDuplicateCandidates = false;
                log.info("Новый кандидат: [{}] {} (разница: {})",
                        i, customersFirstNames.get(i), diff);
            } else if (diff == smallestDiff) {
                hasDuplicateCandidates = true;
                log.info("Альтернативный кандидат: [{}] {} (разница: {})",
                        i, customersFirstNames.get(i), diff);
            }
        }
        String selectedFirstName = customersFirstNames.get(closestFirstNameIndex);
        String selectedLastName = customersLastNames.get(closestFirstNameIndex);

        log.info("Выбран клиент для удаления: [{}] {} {}", closestFirstNameIndex, selectedFirstName, selectedLastName);
        return new CustomerForDeletion(closestFirstNameIndex, selectedFirstName, selectedLastName);
    }
}
