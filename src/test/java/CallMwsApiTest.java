package test.java;

import japacomo.CallMwsApi;
import japacomo.FileCtrlSet;
import japacomo.TakeSpecifiedProperty;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CallMwsApiTest {
    CallMwsApi mwsapi;

    @BeforeEach
    public void setUp(){
        TakeSpecifiedProperty prop = new TakeSpecifiedProperty("src/main/resources/conf/us.config.properties");
        mwsapi = new CallMwsApi(prop);
    }

    @Test
    public void testCallLowestAPI() {
        TakeSpecifiedProperty prop = new TakeSpecifiedProperty(
                "src/main/resources/conf/us.config.properties");
        mwsapi = new CallMwsApi(prop);
        System.out.println(mwsapi.takeLowestPricedOffersForASIN("B00AMD7Y68", "New"));
    }

}