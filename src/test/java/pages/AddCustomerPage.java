package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Condition.visible;
import static helpers.GeneratedData.createFirstNameFromPostCode;

public class AddCustomerPage {
    @FindBy(name = "myForm")
    private SelenideElement addCustomerForm;

    @FindBy(css = "input[placeholder='Post Code']")
    private SelenideElement postCodeInput;

    @FindBy(css = "input[placeholder='First Name']")
    private SelenideElement firstNameInput;

    @Step("Проверка отображения формы")
    public AddCustomerPage checkVisibilityForm() {
        addCustomerForm.shouldBe(visible);
        return this;
    }

    @Step("Ввод Post Code")
    public AddCustomerPage setValuePostCode(String postCode) {
        postCodeInput.setValue(postCode);
        return this;
    }

    @Step("Ввод First Name на основе Post Code")
    public AddCustomerPage setValueFirstName(String postCode) {
        String firstName = createFirstNameFromPostCode(postCode);
        firstNameInput.setValue(firstName);
        return this;
    }
}

