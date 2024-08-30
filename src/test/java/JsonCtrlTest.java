package test.java;

import japacomo.JsonCtrl;
import json.GetItemOffers.GetItemOffersJson;
import json.GetItemOffers.NumberOfOffer;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JsonCtrlTest {
    @Test
    public void testJsonRead() {
        String json = takeJsonFromTestFile();
        System.out.print(json);
    }

    @Test
    public void testMakeJsonObjectAndCheckASIN() {
        String json = takeJsonFromTestFile();
        JsonCtrl jsonC = new JsonCtrl();
        GetItemOffersJson model = jsonC.makeGsonObj(json);
        assertEquals("B000ALF4F0", model.getPayload().getAsin());
    }

    @Test
    public void testMakeJsonObjectAndCheckNumOfOffer() {
        String json = takeJsonFromTestFile();
        JsonCtrl jsonC = new JsonCtrl();
        GetItemOffersJson model = jsonC.makeGsonObj(json);
        List<NumberOfOffer> offers = model.getPayload().
                getSummary().
                getNumberOfOffers();
        assertEquals(2, offers.size());
        for (int i = 0; i < offers.size(); i++) {
            String fulfillmentChannel = offers.get(i).getFulfillmentChannel();
            String condition = offers.get(i).getCondition();
            int offerCount = offers.get(i).getOfferCount();

            if (i == 0) {assertEquals("Amazon", fulfillmentChannel);
            } else if (i == 1) {assertEquals("Merchant", fulfillmentChannel);}
        }
    }

    private String takeJsonFromTestFile() {
        try {
            Path path = Paths.get("src/test/java/testresources/lowestPriceOffersForAsinResult.json");
            byte[] bytes = Files.readAllBytes(path);
            String json = new String(bytes, StandardCharsets.UTF_8);
            return json;
        } catch (Exception e) {
            System.out.println(e);
        }
        return "";
    }
}
