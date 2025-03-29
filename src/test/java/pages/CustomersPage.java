package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.visible;
import static org.testng.Assert.assertTrue;

public class CustomersPage {
    private static final Logger LOG = LoggerFactory.getLogger(CustomersPage.class);

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

    public static class Customer {
        public final Integer index;
        public final String firstName;
        public final String lastName;
        public final String postCode;

        public Customer(int index, String firstName, String lastName) {
            this.index = index;
            this.firstName = firstName;
            this.lastName = lastName;
            this.postCode = null;
        }
        public Customer(String firstName, String lastName, String postCode) {
            this.index = null;
            this.firstName = firstName;
            this.lastName = lastName;
            this.postCode = postCode;
        }
    }

    @Step("Проверка отображения таблицы Customers")
    public CustomersPage checkVisibilityCustomersTable() {
        customersTable.shouldBe(visible);
        return this;
    }

    @Step("Получение списка имен Customers")
    public List<String> getCustomersFirstNames() {
        List<String> firstNames = customersTableRows.stream()
                .map(row -> row.$("td:nth-child(1)").getText())
                .collect(Collectors.toList());

        return firstNames;
    }

    @Step("Получение списка фамилий Customers")
    public List<String> getCustomersLastNames() {
        List<String> lastNames = customersTableRows.stream()
                .map(row -> row.$("td:nth-child(2)").getText())
                .collect(Collectors.toList());

        LOG.info("Получили список Last Names Customers: {}", lastNames);
        return lastNames;
    }

    @Step("Сортировка таблицы Customers по First Name")
    public CustomersPage clickFirstNameSortButton(boolean ascending) {
        firstNameSortButton.shouldBe(visible, Duration.ofSeconds(3));

        int maxAttempts = 3;
        int attempts = 0;

        while ((ascending ? sortAscIcon : sortDescIcon).has(cssClass("ng-hide")) && attempts < maxAttempts) {
            firstNameSortButton.click();
            attempts++;
        }

        LOG.info("Сортировка таблицы Customer по First Name");
        List<String> firstNames = getCustomersFirstNames();
        LOG.info("Полученный список после сортировки: {}", firstNames);
        return this;
    }

    @Step("Получение Customer для удаления")
    public Customer getClientForDeletion() {
        List<String> customersFirstNames = getCustomersFirstNames();
        List<String> customersLastNames = getCustomersLastNames();

        if (customersFirstNames == null || customersFirstNames.isEmpty()) {
            throw new IllegalStateException("Список Customers пуст. Невозможно определить клиента для удаления.");
        }

        double averageLength = customersFirstNames.stream()
                .mapToInt(String::length)
                .average()
                .orElse(0.0);

        List<Integer> closestIndices = new ArrayList<>();
        double smallestDiff = Double.MAX_VALUE;

        for (int i = 0; i < customersFirstNames.size(); i++) {
            int currentLength = customersFirstNames.get(i).length();
            double diff = Math.abs(currentLength - averageLength);

            if (diff < smallestDiff) {
                smallestDiff = diff;
                closestIndices.clear();
                closestIndices.add(i);
            } else if (diff == smallestDiff) {
                closestIndices.add(i);
            }
        }

        if (closestIndices.size() > 1) {
            LOG.warn("Найдено {} Customers с одинаковым отклонением от среднего:", closestIndices.size());
            for (int index : closestIndices) {
                LOG.warn(" - [{}] {} {}", index, customersFirstNames.get(index), customersLastNames.get(index));
            }
        }

        int selectedIndex = closestIndices.get(0);
        String selectedFirstName = customersFirstNames.get(selectedIndex);
        String selectedLastName = customersLastNames.get(selectedIndex);

        LOG.info("Выбран Customer для удаления: [{}] {} {}", selectedIndex, selectedFirstName, selectedLastName);
        return new Customer(selectedIndex, selectedFirstName, selectedLastName);
    }

    @Step("Удаление Customer")
    public CustomersPage deleteCustomer(int index) {
        customersTableRows.get(index).$("button").click();

        LOG.info("Удален Customer с индексом: [{}]", index);
        return this;
    }

    @Step("Поиск нужного Customer")
    public boolean findCustomer(String firstName, String lastName, String postCode) {
        return customersTableRows.stream()
                .anyMatch(row -> row.$("td:nth-child(1)").getText().equals(firstName) &&
                        row.$("td:nth-child(2)").getText().equals(lastName) &&
                        row.$("td:nth-child(3)").getText().equals(postCode));
    }
}
