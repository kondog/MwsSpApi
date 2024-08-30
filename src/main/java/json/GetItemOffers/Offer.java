
package json.GetItemOffers;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Offer {

    @SerializedName("Shipping")
    @Expose
    private Shipping__2 shipping;
    @SerializedName("ListingPrice")
    @Expose
    private ListingPrice__2 listingPrice;
    @SerializedName("ShippingTime")
    @Expose
    private ShippingTime shippingTime;
    @SerializedName("SellerFeedbackRating")
    @Expose
    private SellerFeedbackRating sellerFeedbackRating;
    @SerializedName("PrimeInformation")
    @Expose
    private PrimeInformation primeInformation;
    @SerializedName("SubCondition")
    @Expose
    private String subCondition;
    @SerializedName("SellerId")
    @Expose
    private String sellerId;
    @SerializedName("IsFeaturedMerchant")
    @Expose
    private Boolean isFeaturedMerchant;
    @SerializedName("IsBuyBoxWinner")
    @Expose
    private Boolean isBuyBoxWinner;
    @SerializedName("IsFulfilledByAmazon")
    @Expose
    private Boolean isFulfilledByAmazon;
    @SerializedName("ShipsFrom")
    @Expose
    private ShipsFrom shipsFrom;

    public Shipping__2 getShipping() {
        return shipping;
    }

    public void setShipping(Shipping__2 shipping) {
        this.shipping = shipping;
    }

    public ListingPrice__2 getListingPrice() {
        return listingPrice;
    }

    public void setListingPrice(ListingPrice__2 listingPrice) {
        this.listingPrice = listingPrice;
    }

    public ShippingTime getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(ShippingTime shippingTime) {
        this.shippingTime = shippingTime;
    }

    public SellerFeedbackRating getSellerFeedbackRating() {
        return sellerFeedbackRating;
    }

    public void setSellerFeedbackRating(SellerFeedbackRating sellerFeedbackRating) {
        this.sellerFeedbackRating = sellerFeedbackRating;
    }

    public PrimeInformation getPrimeInformation() {
        return primeInformation;
    }

    public void setPrimeInformation(PrimeInformation primeInformation) {
        this.primeInformation = primeInformation;
    }

    public String getSubCondition() {
        return subCondition;
    }

    public void setSubCondition(String subCondition) {
        this.subCondition = subCondition;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Boolean getIsFeaturedMerchant() {
        return isFeaturedMerchant;
    }

    public void setIsFeaturedMerchant(Boolean isFeaturedMerchant) {
        this.isFeaturedMerchant = isFeaturedMerchant;
    }

    public Boolean getIsBuyBoxWinner() {
        return isBuyBoxWinner;
    }

    public void setIsBuyBoxWinner(Boolean isBuyBoxWinner) {
        this.isBuyBoxWinner = isBuyBoxWinner;
    }

    public Boolean getIsFulfilledByAmazon() {
        return isFulfilledByAmazon;
    }

    public void setIsFulfilledByAmazon(Boolean isFulfilledByAmazon) {
        this.isFulfilledByAmazon = isFulfilledByAmazon;
    }

    public ShipsFrom getShipsFrom() {
        return shipsFrom;
    }

    public void setShipsFrom(ShipsFrom shipsFrom) {
        this.shipsFrom = shipsFrom;
    }

}
