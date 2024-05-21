package test.java;

import japacomo.CallMwsApi;
import japacomo.TakeSpecifiedProperty;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.Assert.*;

public class JapacomoCtrlTest {
    @BeforeEach
    public void setUp(){
    }
    @Test
    public void testReadConfigFile(){
        TakeSpecifiedProperty prop = new TakeSpecifiedProperty("src/main/resources/conf/us.config.properties");
        String propertyMaketID = prop.getProperty("marketID");
        Assertions.assertEquals("ATVPDKIKX0DER",propertyMaketID);

        String propertyReportTypes[] = prop.getPropertyAsArray("downloadReportTypes", ",");
        String types[] = {
                "GET_AMAZON_FULFILLED_SHIPMENTS_DATA_GENERAL",
                "GET_FLAT_FILE_OPEN_LISTINGS_DATA",
                "GET_RESERVED_INVENTORY_DATA",
                "GET_FBA_ESTIMATED_FBA_FEES_TXT_DATA",
                "GET_FBA_MYI_UNSUPPRESSED_INVENTORY_DATA",
                "GET_RESERVED_INVENTORY_DATA",
        };

        Assertions.assertArrayEquals(propertyReportTypes, types);
    }
}
