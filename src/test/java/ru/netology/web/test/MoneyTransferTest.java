package ru.netology.web.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferOfMoneyPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.getFirstCardNumber;
import static ru.netology.web.data.DataHelper.getSecondCardNumber;

public class MoneyTransferTest {

    @BeforeEach
    void setUpAll() {
        Configuration.holdBrowserOpen = true; //оставлять браузер открытым
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        // 2-ю и 3-ю строки можно заменить на var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @AfterEach
    void equalizingTheBalanceOfCards() {
        var dashboardPage = new DashboardPage();
        var balansFirstCardBefore = dashboardPage.getFirstCardBalans();
        var balansSecondCardBefore = dashboardPage.getSecondCardBalans();
        int amountTransfer;
        if (balansFirstCardBefore > balansSecondCardBefore) {
            amountTransfer = (balansFirstCardBefore - balansSecondCardBefore) / 2;
            dashboardPage.transferOfMoneyToSecondCard(); //переходим на страницу перевода
            var transferOfMoneyPage = new TransferOfMoneyPage(); //новая страца переводов
            transferOfMoneyPage.transfer(amountTransfer, getFirstCardNumber());

        }
        if (balansSecondCardBefore > balansFirstCardBefore) {
            amountTransfer = (balansSecondCardBefore - balansFirstCardBefore) / 2;
            dashboardPage.transferOfMoneyToFirstCard(); //переходим на страницу перевода
            var transferOfMoneyPage = new TransferOfMoneyPage(); //новая страца переводов
            transferOfMoneyPage.transfer(amountTransfer, getSecondCardNumber()); // перевод

        }
    }

    //    пополнение первой карты со второй на сумму не превышающую баланс
    @Test
    void shouldTopUpFirstCard() {
        var dashboardPage = new DashboardPage();//новая страца баланса
        //суммы на картах до перевода
        var balansBeforeTopUpFirstCard = dashboardPage.getFirstCardBalans();
        var balansBeforeDebitingSecondCard = dashboardPage.getSecondCardBalans();
        int amountTransfer = 1; // сколько переводим
        dashboardPage.transferOfMoneyToFirstCard(); //переходим на страницу перевода
        var transferOfMoneyPage = new TransferOfMoneyPage(); //новая страца переводов
        transferOfMoneyPage.transfer(amountTransfer, getSecondCardNumber()); // перевод
        var secondDashboardPage = new DashboardPage();//новая страца баланса
        //суммы на картах после перевода
        var balansAfterTopUpFirstCard = secondDashboardPage.getFirstCardBalans();
        var balansAfterDebitingSecondCard = secondDashboardPage.getSecondCardBalans();
//        Сравнения балансов до перевода и после перевода
        assertEquals((balansBeforeTopUpFirstCard + amountTransfer), balansAfterTopUpFirstCard);
        assertEquals((balansBeforeDebitingSecondCard - amountTransfer), balansAfterDebitingSecondCard);
    }

    //    пополнение второй карты с первой на сумму не превышающую баланс
    @Test
    void shouldTopUpSecondCard() {
        var dashboardPage = new DashboardPage();//новая страца баланса
        //суммы на картах до перевода
        var balansBeforeDebitingFirstCard = dashboardPage.getFirstCardBalans();
        var balansBeforeTopUpSecondCard = dashboardPage.getSecondCardBalans();
        int amountTransfer = 10; // сколько переводим
        dashboardPage.transferOfMoneyToSecondCard(); //переходим на страницу перевода
        var transferOfMoneyPage = new TransferOfMoneyPage(); //новая страца переводов
        transferOfMoneyPage.transfer(amountTransfer, getFirstCardNumber()); // перевод
        var secondDashboardPage = new DashboardPage();//новая страца баланса
        //суммы на картах после перевода
        var balansAfterDebitingTopUpFirstCard = secondDashboardPage.getFirstCardBalans();
        var balansAfterTopUpSecondCard = secondDashboardPage.getSecondCardBalans();
        //Сравнения балансов до перевода и после перевода
        assertEquals((balansBeforeDebitingFirstCard - amountTransfer), balansAfterDebitingTopUpFirstCard);
        assertEquals((balansBeforeTopUpSecondCard + amountTransfer), balansAfterTopUpSecondCard);
    }

    //    пополнение первой карты со второй на 0

    @Test
    void shouldTopUpFirstCardOnNull() {
        var dashboardPage = new DashboardPage();//новая страца баланса
        //суммы на картах до перевода
        var balansBeforeTopUpFirstCard = dashboardPage.getFirstCardBalans();
        var balansBeforeDebitingSecondCard = dashboardPage.getSecondCardBalans();
        int amountTransfer = 0; // сколько переводим
        dashboardPage.transferOfMoneyToFirstCard(); //переходим на страницу перевода
        var transferOfMoneyPage = new TransferOfMoneyPage(); //новая страца переводов
        transferOfMoneyPage.transfer(amountTransfer, getSecondCardNumber()); // перевод
        var secondDashboardPage = new DashboardPage();//новая страца баланса
        //суммы на картах после перевода
        var balansAfterTopUpFirstCard = secondDashboardPage.getFirstCardBalans();
        var balansAfterDebitingSecondCard = secondDashboardPage.getSecondCardBalans();
//        Сравнения балансов до перевода и после перевода
        assertEquals((balansBeforeTopUpFirstCard + amountTransfer), balansAfterTopUpFirstCard);
        assertEquals((balansBeforeDebitingSecondCard - amountTransfer), balansAfterDebitingSecondCard);
    }

    //    пополнение первой карты на сумму превышающую остаток

    @Test
    void shouldTopUpFirstCardOnAmountExceedingBalance() {
        var dashboardPage = new DashboardPage();//новая страца баланса
        //суммы на картах до перевода
        var balansBeforeTopUpFirstCard = dashboardPage.getFirstCardBalans();
        var balansBeforeDebitingSecondCard = dashboardPage.getSecondCardBalans();
        int amountTransfer = 20_000; // сколько переводим
        dashboardPage.transferOfMoneyToFirstCard(); //переходим на страницу перевода
        var transferOfMoneyPage = new TransferOfMoneyPage(); //новая страца переводов
        transferOfMoneyPage.transferError(amountTransfer, getSecondCardNumber()); // перевод
        var secondDashboardPage = new DashboardPage();//новая страца баланса
        //суммы на картах после перевода
        var balansAfterTopUpFirstCard = secondDashboardPage.getFirstCardBalans();
        var balansAfterDebitingSecondCard = secondDashboardPage.getSecondCardBalans();
//        Сравнения балансов до перевода и после перевода
        assertEquals((balansBeforeTopUpFirstCard), balansAfterTopUpFirstCard);
        assertEquals((balansBeforeDebitingSecondCard), balansAfterDebitingSecondCard);
    }

}
