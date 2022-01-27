package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataHelper;


import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferOfMoneyPage {
    private SelenideElement heading = $("[data-test-id='dashboard']");
    private SelenideElement howMuch = $("[data-test-id='amount'] input");
    private SelenideElement whereFrom = $("[data-test-id='from'] input");
    private SelenideElement topUpButton = $("[data-test-id='action-transfer']");
    private SelenideElement errorNotification = $("[data-test-id='error-notification']");


    public TransferOfMoneyPage(){
        heading.shouldBe(visible);
    }

    public DashboardPage transfer(int amountTransfer, DataHelper.CardNumber cardNumberInfo){
        howMuch.doubleClick().sendKeys(Keys.DELETE);
        howMuch.setValue(String.valueOf(amountTransfer));
        whereFrom.sendKeys(Keys.CONTROL + "a");
        whereFrom.sendKeys(Keys.DELETE);
        whereFrom.setValue(cardNumberInfo.getCardNumber());
        topUpButton.click();
        return new DashboardPage();
    }
    public DashboardPage transferError(int amountTransfer, DataHelper.CardNumber cardNumberInfo){
        howMuch.doubleClick().sendKeys(Keys.DELETE);
        howMuch.setValue(String.valueOf(amountTransfer));
        whereFrom.sendKeys(Keys.CONTROL + "a");
        whereFrom.sendKeys(Keys.DELETE);
        whereFrom.setValue(cardNumberInfo.getCardNumber());
        topUpButton.click();
        errorNotification.shouldBe(visible).shouldHave(exactText("Ошибка! Недостаточно средств для перевода"));
        return new DashboardPage();
    }

}
