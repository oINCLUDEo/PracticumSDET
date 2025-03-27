package tests;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.AddCustomerPage;
import pages.BankManagerPage;
import pages.CustomersPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static helpers.GeneratedData.*;
import static org.testng.Assert.*;

@Epic("Управление клиентами")
public class BaseTest {
    private boolean isAscendingSort = true;

    @BeforeClass
    public void setUp() {
        Configuration.baseUrl = "https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager";
        Configuration.browser = "chrome";
        Configuration.browserSize = "maximized";
        Configuration.downloadsFolder = "target/downloads";
        Configuration.headless = true;
        open(Configuration.baseUrl);
    }

    @AfterClass
    public void tearDown() {
        closeWebDriver();
    }

    @Test
    @Feature("Клиентская форма")
    @Story("Добавление нового Customer(а)")
    @Description("Тест на заполнение формы Customer")
    public void formSubmissionTest() {
        BankManagerPage bankManager = page(BankManagerPage.class);
        AddCustomerPage addCustomer = bankManager.openAddCustomerPage();

        String postCode = generatePostCode();
        String firstName = createFirstNameFromPostCode(postCode);
        String lastName = generateLastName();

        addCustomer
                .checkVisibilityForm()
                .setValuePostCode(postCode)
                .setValueFirstName(firstName)
                .setValueLastName(lastName)
                .submitAddCustomer();

        CustomersPage customers = bankManager.openCustomersPage();
        customers.checkVisibilityCustomersTable();

        assertTrue(customers.findCustomer(firstName, lastName, postCode),
                "Клиент с данными: FirstName=" + firstName +
                        ", LastName=" + lastName +
                        ", PostCode=" + postCode +
                        " не найден в таблице!");
    }

    //TODO:Возможно стоит проверить была ли сортировка сделана в других столбцах правильно
    @Test
    @Feature("Клиентская база")
    @Story("Сортировка таблицы Customers")
    @Description("Тест сортировки клиентов по First Name")
    public void sortClientsTest() {
        BankManagerPage bankManager = page(BankManagerPage.class);
        CustomersPage customers = bankManager.openCustomersPage();

        customers
                .checkVisibilityCustomersTable()
                .clickFirstNameSortButton(isAscendingSort);

        List<String> sortedNames = customers.getCustomersFirstNames();
        List<String> expectedSortedNames = new ArrayList<>(sortedNames);
        expectedSortedNames.sort(String::compareTo);

        if (!isAscendingSort) {
            Collections.reverse(expectedSortedNames);
        }
        assertEquals(sortedNames, expectedSortedNames, "Сортировка First Name не работает!");
    }

    @Test
    @Feature("Клиентская база")
    @Story("Удаление Customer из таблицы Customers")
    @Description("Тест на удаление Customer из таблицы Customers. " +
            "Выбор Customer основан на выборе имени по ближайшей его длине к ср.арифм из всех имен в таблице")
    public void deleteCustomerFromTableTest() {
        BankManagerPage bankManager = page(BankManagerPage.class);
        CustomersPage customers = bankManager.openCustomersPage();

        customers.checkVisibilityCustomersTable();

        CustomersPage.Customer customer = customers.getClientForDeletion();

        customers.deleteCustomer(customer.index)
                .checkVisibilityCustomersTable();

        assertFalse(customers.getCustomersFirstNames().contains(customer.firstName),
                "Клиент всё ещё присутствует в списке, хотя должен был быть удалён!");
    }
}
