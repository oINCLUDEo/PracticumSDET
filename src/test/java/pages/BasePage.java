package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.support.FindBy;
import com.codeborne.selenide.SelenideElement;

import static helpers.generateData.*;
import static com.codeborne.selenide.Condition.visible;

public class BasePage {
    @FindBy(xpath = "//button[@ng-class='btnClass1']")
    private SelenideElement addCustomerButton;
    @FindBy(name = "myForm")
    private SelenideElement addCustomerForm;
    @FindBy(css = "input[placeholder='Post Code']")
    private SelenideElement inputPostCode;
    @FindBy(css = "input[placeholder='First Name']")
    private SelenideElement inputFirstName;

    @Step("Нажать на кнопку Add Customer")
    public BasePage clickAddCustomer() {
        addCustomerButton.click();
        return this;
    }

    @Step("Проверяем отображение формы")
    public BasePage checkVisibilityForm() {
        addCustomerForm.shouldBe(visible);
        return this;
    }

    @Step("Ввод Post Code")
    public BasePage inputPostCode(String postCode) {
        inputPostCode.setValue(postCode);
        return this;
    }

    @Step("Ввод First Name на основе Post Code")
    public BasePage inputFirstName(String postCode) {
        String firstName = createFirstNameFromPostCode(postCode);

        inputFirstName.setValue(firstName);
        return this;
    }
}
