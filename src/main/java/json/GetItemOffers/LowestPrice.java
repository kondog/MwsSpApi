
package json.GetItemOffers;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class LowestPrice {

    @SerializedName("condition")
    @Expose
    private String condition;
    @SerializedName("fulfillmentChannel")
    @Expose
    private String fulfillmentChannel;
    @SerializedName("LandedPrice")
    @Expose
    private LandedPrice landedPrice;
    @SerializedName("ListingPrice")
    @Expose
    private ListingPrice listingPrice;
    @SerializedName("Shipping")
    @Expose
    private Shipping shipping;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getFulfillmentChannel() {
        return fulfillmentChannel;
    }

    public void setFulfillmentChannel(String fulfillmentChannel) {
        this.fulfillmentChannel = fulfillmentChannel;
    }

    public LandedPrice getLandedPrice() {
        return landedPrice;
    }

    public void setLandedPrice(LandedPrice landedPrice) {
        this.landedPrice = landedPrice;
    }

    public ListingPrice getListingPrice() {
        return listingPrice;
    }

    public void setListingPrice(ListingPrice listingPrice) {
        this.listingPrice = listingPrice;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

}
