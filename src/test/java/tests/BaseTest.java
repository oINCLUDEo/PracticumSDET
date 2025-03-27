package tests;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.BasePage;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static helpers.GeneratedData.*;

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

    //TODO: Нужно добавить отправку формы и создание Last Name
    @Test
    @Feature("Клиентская форма")
    @Story("Добавление нового Customer(а)")
    @Description("Тест на заполнение формы")
    public void formSubmissionTest() {
        BasePage basePage = page(BasePage.class);
        String postCode = generatePostCode();

        basePage.clickAddCustomerButton()
                .checkVisibilityForm()
                .setValuePostCode(postCode)
                .setValueFirstName(postCode);
    }

    @Test
    @Feature("Клиентская база")
    @Story("Сортировка таблицы Customers")
    @Description("Тест сортировки клиентов по First Name")
    public void sortClientsTest() {
        BasePage basePage = page(BasePage.class);

        basePage.clickCustomers()
                .checkVisibilityCustomersTable()
                .clickFirstNameSortButton(true);
    }

    @Test
    @Feature("Клиентская база")
    @Story("Удаление Customer из таблицы Customers")
    @Description("Тест на удаление Customer из таблицы Customers на основе длины имени по отношению к ср.арифм всех имен")
    public void deleteCustomerFromTableTest() {
        BasePage basePage = page(BasePage.class);

        basePage.clickCustomers()
                .checkVisibilityCustomersTable();

        List<String> customersFirstNames = getCustomersFirstNames();
        List<String> customersLastNames = getCustomersLastNames();
        Integer customersCount = customersFirstNames.size();
        CustomerForDeletion customerForDeletion = getClientForDeletion(customersFirstNames, customersLastNames);

        basePage.deleteCustomerFromTable(customerForDeletion.index)
                .checkVisibilityDeletedCustomer(customersCount,
                        customerForDeletion.firstName,
                        customerForDeletion.lastName );
    }
}
