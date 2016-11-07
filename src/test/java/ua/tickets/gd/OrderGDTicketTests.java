package ua.tickets.gd;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.Before;
import org.junit.Test;
import ua.tickets.components.DatePicker;
import ua.tickets.components.Payment;
import ua.tickets.components.SearchForm;
import ua.tickets.components.User;
import ua.tickets.pages.gd.GDHomePage;
import ua.tickets.pages.gd.SearchResult;
import ua.tickets.utils.BaseTest;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ua.tickets.utils.Random.generateRandomEmail;
import static ua.tickets.utils.Random.generateRandomString;

public class OrderGDTicketTests extends BaseTest {

    @Before
    public void before(){
        Configuration.baseUrl = "https://gd.tickets.ua/en";
        Configuration.holdBrowserOpen = true;
    }

    @Test
    public void orderTicketWithSearchTest(){
        String testValue = generateRandomString();
        GDHomePage.openGDHomePage();
        new SearchForm().oneSideShouldBeChecked().setRoundTrip().
                chooseDeparture("Dnipro").fillArrival("Lviv");
        new DatePicker().setDateAfterCurrent(3).setDateAfterCurrent(6);
        SearchForm.submitSearch().
                selectThirdClassTicket().chooseFirstAvailableSit();
        User user = new User(".passenger-form");
        user.fillLastName(testValue).fillFirstName(testValue).
        fillEmail(generateRandomEmail()).fillPhoneNumber("112223333").
                acceptOfferta().submit().
                preloaderShouldBeVisible();

        new SearchResult().selectThirdClassTicket().chooseFirstAvailableSit();
        user.submit().
                preloaderShouldBeVisible();
        new Payment().fillCreditCartWithTestData().submit().cartDataShouldHaveError();
    }

    @Test
    public void orderTicketWithoutSearchTest(){
        open("/preloader/~2210707~2218999~15.11.2016~2~ukraine~0~18.11.2016~~~0");
        new SearchResult().selectThirdClassTicket().chooseFirstAvailableSit();
        $(".one_offer.active").shouldBe(Condition.visible);
    }
}
