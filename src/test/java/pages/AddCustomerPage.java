package pages;

import com.codeborne.selenide.SelenideElement;
import helpers.GeneratedData;
import io.qameta.allure.Step;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Condition.visible;
import static helpers.GeneratedData.createFirstNameFromPostCode;

public class AddCustomerPage {
    @FindBy(name = "myForm")
    private SelenideElement addCustomerForm;
    @FindBy(css = "input[placeholder='Post Code']")
    private SelenideElement postCodeInput;
    @FindBy(css = "input[placeholder='First Name']")
    private SelenideElement firstNameInput;
    @FindBy(css = "input[placeholder='Last Name']")
    private SelenideElement lastNameInput;
    @FindBy(xpath = "//button[text()='Add Customer']")
    private SelenideElement addCustomerButton;

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
    public AddCustomerPage setValueFirstName(String firstName) {
        firstNameInput.setValue(firstName);
        return this;
    }

    @Step("Ввод Last Name")
    public AddCustomerPage setValueLastName(String lastName) {
        lastNameInput.setValue(lastName);
        return this;
    }

    @Step("Отправка формы Add Customer")
    public AddCustomerPage submitAddCustomer() {
        addCustomerButton.click();
        return this;
    }
}

