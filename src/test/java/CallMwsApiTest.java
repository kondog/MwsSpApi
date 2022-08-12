package test.java;

import main.java.japacomo.CallMwsApi;
import main.java.japacomo.TakeSpecifiedProperty;
import org.junit.Before;
import org.junit.jupiter.api.Test;

class CallMwsApiTest {
    CallMwsApi mwsapi;

    @Before
    public void prepare(){
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

}