package tests;

import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.BasePage;

import static com.codeborne.selenide.Selenide.*;
import static helpers.generateData.generatePostCode;

public class BaseTest {
    @BeforeClass
    public void setUp() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "maximized";
        Configuration.downloadsFolder = "target/downloads";
        open("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager");
    }

    @Test(description = "Тест на заполнение формы")
    public void formSubmissionTest() {
        BasePage basePage = page(BasePage.class);
        String postCode = generatePostCode();

        basePage.clickAddCustomerButton()
                .checkVisibilityForm()
                .setValuePostCode(postCode)
                .setValueFirstName(postCode);
    }

    @Test(description = "Тест сортировки клиентов")
    public void sortClientsTest() {
        BasePage basePage = page(BasePage.class);

        basePage.clickCustomers()
                .checkVisibilityCustomersTable()
                .clickFirstNameSortButton(true);
    }
}
