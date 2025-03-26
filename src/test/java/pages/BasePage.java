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
    @Step("Ввод Пост Кода")
    public BasePage inputPostCode() {
        inputPostCode.setValue(generatePostCode());
        return this;
    }
}
