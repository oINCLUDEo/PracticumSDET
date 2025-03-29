package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.page;

public class BankManagerPage {
    private static final Logger LOG = LoggerFactory.getLogger(BankManagerPage.class);

    @FindBy(xpath = "//button[@ng-class='btnClass1']")
    private SelenideElement addCustomerButton;
    @FindBy(xpath = "//button[@ng-class='btnClass3']")
    private SelenideElement customersButton;

    @Step("Открытие страницы добавления Customer")
    public AddCustomerPage openAddCustomerPage() {
        addCustomerButton.shouldBe(visible).click();

        LOG.info("Открытие формы Add Customer");
        return page(AddCustomerPage.class);
    }

    @Step("Открытие страницы списка Customers")
    public CustomersPage openCustomersPage() {
        customersButton.shouldBe(visible).click();

        LOG.info("Открытие Customers таблицы");
        return page(CustomersPage.class);
    }

}
