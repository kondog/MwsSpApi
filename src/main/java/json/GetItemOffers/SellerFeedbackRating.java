
package json.GetItemOffers;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class SellerFeedbackRating {

    @SerializedName("FeedbackCount")
    @Expose
    private Integer feedbackCount;
    @SerializedName("SellerPositiveFeedbackRating")
    @Expose
    private Double sellerPositiveFeedbackRating;

    public Integer getFeedbackCount() {
        return feedbackCount;
    }

    public void setFeedbackCount(Integer feedbackCount) {
        this.feedbackCount = feedbackCount;
    }

    public Double getSellerPositiveFeedbackRating() {
        return sellerPositiveFeedbackRating;
    }

    public void setSellerPositiveFeedbackRating(Double sellerPositiveFeedbackRating) {
        this.sellerPositiveFeedbackRating = sellerPositiveFeedbackRating;
    }

}
