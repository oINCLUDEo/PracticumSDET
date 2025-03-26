package pages;

import helpers.generateData;
import io.qameta.allure.Step;
import org.openqa.selenium.support.FindBy;
import com.codeborne.selenide.SelenideElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.sleep;
import static helpers.generateData.*;
import static com.codeborne.selenide.Condition.visible;

public class BasePage {
    private static Logger log = LoggerFactory.getLogger(generateData.class);

    @FindBy(xpath = "//button[@ng-class='btnClass1']")
    private SelenideElement addCustomerButton;
    @FindBy(xpath = "//button[@ng-class='btnClass3']")
    private SelenideElement customersButton;
    @FindBy(name = "myForm")
    private SelenideElement addCustomerForm;
    @FindBy(css = "input[placeholder='Post Code']")
    private SelenideElement postCodeInput;

    @FindBy(css = "input[placeholder='First Name']")
    private SelenideElement firstNameInput;
    @FindBy(tagName = "table")
    private SelenideElement customersTable;
    @FindBy(css = "a[ng-click*='fName']")
    private SelenideElement firstNameSortButton;
    @FindBy(css = "span.fa-caret-down[ng-show*='fName'][ng-show*='!sortReverse']")
    private SelenideElement sortAscIcon;
    @FindBy(css = "span.fa-caret-up[ng-show*='fName'][ng-show*='sortReverse']")
    private SelenideElement sortDescIcon;

    @Step("Клик по кнопке Add Customer")
    public BasePage clickAddCustomerButton() {
        addCustomerButton.click();
        return this;
    }

    @Step("Проверка отображения формы")
    public BasePage checkVisibilityForm() {
        addCustomerForm.shouldBe(visible);
        return this;
    }

    @Step("Ввод Post Code")
    public BasePage setValuePostCode(String postCode) {
        postCodeInput.setValue(postCode);
        return this;
    }

    @Step("Ввод First Name на основе Post Code")
    public BasePage setValueFirstName(String postCode) {
        String firstName = createFirstNameFromPostCode(postCode);

        firstNameInput.setValue(firstName);
        return this;
    }

    @Step("Клик по кнопке Customers")
    public BasePage clickCustomers() {
        customersButton.click();
        return this;
    }

    @Step("Проверка отображения Customers таблицы")
    public BasePage checkVisibilityCustomersTable() {
        customersTable.shouldBe(visible);
        return this;
    }

    @Step("Сортировка таблицы Customers по First Name")
    public BasePage clickFirstNameSortButton(boolean ascending) {
        firstNameSortButton.shouldBe(visible, Duration.ofSeconds(3));

        if (ascending) {
            while (sortAscIcon.has(cssClass("ng-hide"))){
                firstNameSortButton.click();
            }
        } else {
            while (sortDescIcon.has(cssClass("ng-hide"))){
                firstNameSortButton.click();
            }
        }

        List<String> names = $$("tbody tr td:nth-child(1)").texts();
        List<String> sortedNames = new ArrayList<>(names);
        sortedNames.sort(ascending ? Comparator.naturalOrder() : Comparator.reverseOrder());

        if (!names.equals(sortedNames)) {
            throw new AssertionError("Ошибка сортировки. Ожидалось: " +
                    sortedNames + ", получено: " + names);
        }

        return this;
    }
}
