package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.page;

public class BankManagerPage {
    @FindBy(xpath = "//button[@ng-class='btnClass1']")
    private SelenideElement addCustomerButton;

    @FindBy(xpath = "//button[@ng-class='btnClass3']")
    private SelenideElement customersButton;

    @Step("Открытие страницы добавления клиента")
    public AddCustomerPage openAddCustomerPage() {
        addCustomerButton.click();
        return page(AddCustomerPage.class);
    }

    @Step("Открытие страницы списка клиентов")
    public CustomersPage openCustomersPage() {
        customersButton.click();
        return page(CustomersPage.class);
    }

}
