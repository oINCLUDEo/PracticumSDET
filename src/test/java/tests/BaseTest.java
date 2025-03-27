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

import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static helpers.GeneratedData.*;
import static org.testng.Assert.assertFalse;

@Epic("Управление клиентами")
public class BaseTest {
    @BeforeClass
    public void setUp() {
        Configuration.baseUrl = "https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager";
        Configuration.browser = "chrome";
        Configuration.browserSize = "maximized";
        Configuration.downloadsFolder = "target/downloads";
        open(Configuration.baseUrl);
    }

    @AfterClass
    public void tearDown() {
        closeWebDriver();
    }

    //TODO: Нужно добавить отправку формы и создание Last Name
    @Test
    @Feature("Клиентская форма")
    @Story("Добавление нового Customer(а)")
    @Description("Тест на заполнение формы")
    public void formSubmissionTest() {
        BankManagerPage bankManager = page(BankManagerPage.class);
        AddCustomerPage addCustomer = bankManager.openAddCustomerPage();

        String postCode = generatePostCode();

        addCustomer
                .checkVisibilityForm()
                .setValuePostCode(postCode)
                .setValueFirstName(postCode);
    }

    @Test
    @Feature("Клиентская база")
    @Story("Сортировка таблицы Customers")
    @Description("Тест сортировки клиентов по First Name")
    public void sortClientsTest() {
        BankManagerPage bankManager = page(BankManagerPage.class);
        CustomersPage customers = bankManager.openCustomersPage();

        customers
                .checkVisibilityCustomersTable()
                .clickFirstNameSortButton(true);
    }

    @Test
    @Feature("Клиентская база")
    @Story("Удаление Customer из таблицы Customers")
    @Description("Тест на удаление Customer из таблицы Customers на основе длины имени по отношению к ср.арифм всех имен")
    public void deleteCustomerFromTableTest() {
        BankManagerPage bankManager = page(BankManagerPage.class);
        CustomersPage customers = bankManager.openCustomersPage();

        customers.checkVisibilityCustomersTable();

        List<String> firstNames = customers.getCustomersFirstNames();

        CustomersPage.CustomerForDeletion customer = customers.getClientForDeletion();

        customers.deleteCustomer(customer.index)
                .checkVisibilityCustomersTable();

        assertFalse(customers.getCustomersFirstNames().contains(customer.firstName),
                "Клиент всё ещё присутствует в списке, хотя должен был быть удалён!");
    }
}
