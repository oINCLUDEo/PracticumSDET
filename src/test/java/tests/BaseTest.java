package tests;

import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.BasePage;


import static com.codeborne.selenide.Selenide.*;

public class BaseTest {
    @BeforeClass
    public void setUp() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "maximized";
        open("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager");
    }

    @Test(description = "Тест на заполнение формы")
    public void testFormSubmission() {
        BasePage basePage = page(BasePage.class);

        basePage.clickAddCustomer()
                .checkVisibilityForm()
                .inputPostCode();
    }
}
