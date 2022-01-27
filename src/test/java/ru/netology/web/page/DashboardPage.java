package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id='dashboard']");
    private SelenideElement buttonTopUpFirstCard = $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0'] button");
    private SelenideElement buttonTopUpSecondCard = $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d'] button");
    private ElementsCollection cards = $$(".list__item");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";


    public DashboardPage(){
    heading.shouldBe(visible);
    }

    public TransferOfMoneyPage transferOfMoneyToFirstCard() {
        buttonTopUpFirstCard.click();
        return new TransferOfMoneyPage();
    }

    public TransferOfMoneyPage transferOfMoneyToSecondCard() {
        buttonTopUpSecondCard.click();
        return new TransferOfMoneyPage();
    }

    public int getFirstCardBalans(){
        val text =cards.first().text();
        return extractBalance(text);
    }

    public int getSecondCardBalans(){
        val text =cards.last().text();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

}
