package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.visible;

public class CustomersPage {
    private static final Logger LOG = LoggerFactory.getLogger(CustomersPage.class);

    @FindBy(tagName = "table")
    private SelenideElement customersTable;
    @FindBy(css = "table tbody tr")
    private List<SelenideElement> customersTableRows;
    @FindBy(css = "table tbody tr td.ng-binding")
    private List<SelenideElement> dataCells;
    @FindBy(css = "a[ng-click*='fName']")
    private SelenideElement firstNameSortButton;
    @FindBy(css = "span.fa-caret-down[ng-show*='fName'][ng-show*='!sortReverse']")
    private SelenideElement sortAscIcon;
    @FindBy(css = "span.fa-caret-up[ng-show*='fName'][ng-show*='sortReverse']")
    private SelenideElement sortDescIcon;
    private final By deleteButtonLocator = By.cssSelector("button[ng-click='deleteCust(cust)']");

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

    public List<String> extractCustomerData(String type) {
        List<String> firstNamesList = new ArrayList<>();
        List<String> lastNamesList = new ArrayList<>();
        List<String> postCodesList = new ArrayList<>();
        for (int i = 0; i < dataCells.size(); i += 3) {
            firstNamesList.add(dataCells.get(i).text());
            lastNamesList.add(dataCells.get(i + 1).text());
            postCodesList.add(dataCells.get(i + 2).text());
        }
        LOG.info("Получаем {} значения", type);
        return switch (type) {
            case "fName" -> firstNamesList;
            case "lName" -> lastNamesList;
            case "pCode" -> postCodesList;
            default -> null;
        };
    }

    @Step("Проверка отображения таблицы Customers")
    public CustomersPage checkVisibilityCustomersTable() {
        customersTable.shouldBe(visible);
        return this;
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
        return this;
    }

    @Step("Получение Customer для удаления")
    public Customer getClientForDeletion() {
        List<String> customersFirstNames = extractCustomerData("fName");
        List<String> customersLastNames = extractCustomerData("lName");
        if (customersFirstNames == null || customersFirstNames.isEmpty()) {
            throw new IllegalStateException("Список Customers пуст. Невозможно определить клиента для удаления.");
        }
        double averageLength = customersFirstNames.stream().mapToInt(String::length).average().orElse(0.0);
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
    public CustomersPage deleteCustomer(Customer customer) {
        SelenideElement customerRow = customersTableRows.get(customer.index);
        customerRow.find(deleteButtonLocator).click();
        LOG.info("Удален клиент: [{}] {} {}", customer.index, customer.firstName, customer.lastName);
        return this;
    }

    @Step("Поиск Customer")
    public boolean findCustomer(String firstName, String lastName, String postCode) {
        List<String> firstNamesList = extractCustomerData("fName");
        List<String> lastNamesList = extractCustomerData("lName");
        List<String> postCodesList = extractCustomerData("pCode");
        for (int i = 0; i < firstNamesList.size(); i++) {
            if (firstNamesList.get(i).equals(firstName) &&
                    lastNamesList.get(i).equals(lastName) &&
                    postCodesList.get(i).equals(postCode)) {
                return true;
            }
        }
        return false;
    }
}
