package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.visible;

public class CustomersPage {
    private static Logger log = LoggerFactory.getLogger(CustomersPage.class);

    @FindBy(tagName = "table")
    private SelenideElement customersTable;

    @FindBy(css = "tbody tr")
    private ElementsCollection customersTableRows;

    @FindBy(css = "a[ng-click*='fName']")
    private SelenideElement firstNameSortButton;

    @FindBy(css = "span.fa-caret-down[ng-show*='fName'][ng-show*='!sortReverse']")
    private SelenideElement sortAscIcon;

    @FindBy(css = "span.fa-caret-up[ng-show*='fName'][ng-show*='sortReverse']")
    private SelenideElement sortDescIcon;

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

    @Step("Проверка отображения таблицы клиентов")
    public CustomersPage checkVisibilityCustomersTable() {
        customersTable.shouldBe(visible);
        return this;
    }

    @Step("Получение списка имен клиентов")
    public List<String> getCustomersFirstNames() {
        return customersTableRows.stream()
                .map(row -> row.$("td:nth-child(1)").getText())
                .collect(Collectors.toList());
    }

    @Step("Получение списка фамилий клиентов")
    public List<String> getCustomersLastNames() {
        return customersTableRows.stream()
                .map(row -> row.$("td:nth-child(2)").getText())
                .collect(Collectors.toList());
    }

    @Step("Сортировка клиентов по имени")
    public CustomersPage clickFirstNameSortButton(boolean ascending) {
        firstNameSortButton.shouldBe(visible, Duration.ofSeconds(3));

        int maxAttempts = 3;
        int attempts = 0;

        while ((ascending ? sortAscIcon : sortDescIcon).has(cssClass("ng-hide")) && attempts < maxAttempts) {
            firstNameSortButton.click();
            attempts++;
        }

        List<String> names = getCustomersFirstNames();
        List<String> sortedNames = new ArrayList<>(names);
        sortedNames.sort(ascending ? Comparator.naturalOrder() : Comparator.reverseOrder());

        if (!names.equals(sortedNames)) {
            throw new AssertionError("Ошибка сортировки. Ожидалось: " +
                    sortedNames + ", получено: " + names);
        }

        return this;
    }

    @Step("Получение клиента для удаления")
    public CustomerForDeletion getClientForDeletion() {
        List<String> customersFirstNames = getCustomersFirstNames();
        List<String> customersLastNames = getCustomersLastNames();

        if (customersFirstNames == null || customersFirstNames.isEmpty()) {
            throw new IllegalStateException("Список клиентов пуст. Невозможно определить клиента для удаления.");
        }

        double averageLength = customersFirstNames.stream()
                .mapToInt(String::length)
                .average()
                .orElse(0.0);

        int closestFirstNameIndex = 0;
        double smallestDiff = Double.MAX_VALUE;

        for (int i = 0; i < customersFirstNames.size(); i++) {
            int currentLength = customersFirstNames.get(i).length();
            double diff = Math.abs(currentLength - averageLength);

            if (diff < smallestDiff) {
                smallestDiff = diff;
                closestFirstNameIndex = i;
            }
        }

        String selectedFirstName = customersFirstNames.get(closestFirstNameIndex);
        String selectedLastName = customersLastNames.get(closestFirstNameIndex);

        log.info("Выбран клиент для удаления: [{}] {} {}", closestFirstNameIndex, selectedFirstName, selectedLastName);
        return new CustomerForDeletion(closestFirstNameIndex, selectedFirstName, selectedLastName);
    }

    @Step("Удаление клиента")
    public CustomersPage deleteCustomer(int index) {
        customersTableRows.get(index).$("button").click();
        return this;
    }
}
