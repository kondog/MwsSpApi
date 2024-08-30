
package json.GetItemOffers;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class BuyBoxPrice {

    @SerializedName("condition")
    @Expose
    private String condition;
    @SerializedName("LandedPrice")
    @Expose
    private LandedPrice__1 landedPrice;
    @SerializedName("ListingPrice")
    @Expose
    private ListingPrice__1 listingPrice;
    @SerializedName("Shipping")
    @Expose
    private Shipping__1 shipping;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public LandedPrice__1 getLandedPrice() {
        return landedPrice;
    }

    public void setLandedPrice(LandedPrice__1 landedPrice) {
        this.landedPrice = landedPrice;
    }

    public ListingPrice__1 getListingPrice() {
        return listingPrice;
    }

    public void setListingPrice(ListingPrice__1 listingPrice) {
        this.listingPrice = listingPrice;
    }

    public Shipping__1 getShipping() {
        return shipping;
    }

    public void setShipping(Shipping__1 shipping) {
        this.shipping = shipping;
    }

}
