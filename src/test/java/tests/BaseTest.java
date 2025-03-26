package tests;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.BasePage;

import static com.codeborne.selenide.Selenide.*;
import static helpers.generateData.generatePostCode;

@Epic("Управление клиентами")
public class BaseTest {
    @BeforeClass
    public void setUp() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "maximized";
        Configuration.downloadsFolder = "target/downloads";
        open("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager");
    }

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
}
