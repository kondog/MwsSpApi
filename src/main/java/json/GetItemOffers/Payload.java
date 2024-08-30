
package json.GetItemOffers;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Payload {

    @SerializedName("ASIN")
    @Expose
    private String asin;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("ItemCondition")
    @Expose
    private String itemCondition;
    @SerializedName("Identifier")
    @Expose
    private Identifier identifier;
    @SerializedName("Summary")
    @Expose
    private Summary summary;
    @SerializedName("Offers")
    @Expose
    private List<Offer> offers;
    @SerializedName("marketplaceId")
    @Expose
    private String marketplaceId;

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public String getMarketplaceId() {
        return marketplaceId;
    }

    public void setMarketplaceId(String marketplaceId) {
        this.marketplaceId = marketplaceId;
    }

}
