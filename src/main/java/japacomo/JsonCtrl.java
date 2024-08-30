package japacomo;

import com.google.gson.Gson;
import json.GetItemOffers.GetItemOffersJson;

public class JsonCtrl {
    //TODO:this class may be bettter to move to json package not here.
    public GetItemOffersJson makeGsonObj(String jsonStr){
        Gson gson = new Gson();
        GetItemOffersJson model = gson.fromJson(jsonStr, GetItemOffersJson.class);
        return model;
    }
}
