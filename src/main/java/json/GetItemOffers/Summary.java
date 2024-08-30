
package json.GetItemOffers;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Summary {

    @SerializedName("LowestPrices")
    @Expose
    private List<LowestPrice> lowestPrices;
    @SerializedName("BuyBoxPrices")
    @Expose
    private List<BuyBoxPrice> buyBoxPrices;
    @SerializedName("NumberOfOffers")
    @Expose
    private List<NumberOfOffer> numberOfOffers;
    @SerializedName("BuyBoxEligibleOffers")
    @Expose
    private List<BuyBoxEligibleOffer> buyBoxEligibleOffers;
    @SerializedName("SalesRankings")
    @Expose
    private List<SalesRanking> salesRankings;
    @SerializedName("ListPrice")
    @Expose
    private ListPrice listPrice;
    @SerializedName("TotalOfferCount")
    @Expose
    private Integer totalOfferCount;

    public List<LowestPrice> getLowestPrices() {
        return lowestPrices;
    }

    public void setLowestPrices(List<LowestPrice> lowestPrices) {
        this.lowestPrices = lowestPrices;
    }

    public List<BuyBoxPrice> getBuyBoxPrices() {
        return buyBoxPrices;
    }

    public void setBuyBoxPrices(List<BuyBoxPrice> buyBoxPrices) {
        this.buyBoxPrices = buyBoxPrices;
    }

    public List<NumberOfOffer> getNumberOfOffers() {
        return numberOfOffers;
    }

    public void setNumberOfOffers(List<NumberOfOffer> numberOfOffers) {
        this.numberOfOffers = numberOfOffers;
    }

    public List<BuyBoxEligibleOffer> getBuyBoxEligibleOffers() {
        return buyBoxEligibleOffers;
    }

    public void setBuyBoxEligibleOffers(List<BuyBoxEligibleOffer> buyBoxEligibleOffers) {
        this.buyBoxEligibleOffers = buyBoxEligibleOffers;
    }

    public List<SalesRanking> getSalesRankings() {
        return salesRankings;
    }

    public void setSalesRankings(List<SalesRanking> salesRankings) {
        this.salesRankings = salesRankings;
    }

    public ListPrice getListPrice() {
        return listPrice;
    }

    public void setListPrice(ListPrice listPrice) {
        this.listPrice = listPrice;
    }

    public Integer getTotalOfferCount() {
        return totalOfferCount;
    }

    public void setTotalOfferCount(Integer totalOfferCount) {
        this.totalOfferCount = totalOfferCount;
    }

}
