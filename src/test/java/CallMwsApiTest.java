package test.java;

import japacomo.CallMwsApi;
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
    public void testUnzipFile(){
        TakeSpecifiedProperty prop = new TakeSpecifiedProperty("src/main/resources/conf/us.config.properties");
        mwsapi = new CallMwsApi(prop);
        mwsapi.unzipFile("test", "test");
        mwsapi.unzipFile("src/test/java/testresources/text.type.test.txt",
                "src/test/java/testresources/test.txt");
        mwsapi.unzipFile("src/test/java/testresources/test.tar.gz",
                "src/test/java/testresources/test.txt");
    }

    @Test
    public void testCallLowestAPI() {
        TakeSpecifiedProperty prop = new TakeSpecifiedProperty(
                "src/main/resources/conf/us.config.properties");
        mwsapi = new CallMwsApi(prop);
        System.out.println(mwsapi.takeLowestPricedOffersForASIN("B009WK6ZIU", "New"));
    }

}