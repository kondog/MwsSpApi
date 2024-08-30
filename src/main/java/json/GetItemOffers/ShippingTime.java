
package json.GetItemOffers;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ShippingTime {

    @SerializedName("maximumHours")
    @Expose
    private Integer maximumHours;
    @SerializedName("minimumHours")
    @Expose
    private Integer minimumHours;
    @SerializedName("availabilityType")
    @Expose
    private String availabilityType;

    public Integer getMaximumHours() {
        return maximumHours;
    }

    public void setMaximumHours(Integer maximumHours) {
        this.maximumHours = maximumHours;
    }

    public Integer getMinimumHours() {
        return minimumHours;
    }

    public void setMinimumHours(Integer minimumHours) {
        this.minimumHours = minimumHours;
    }

    public String getAvailabilityType() {
        return availabilityType;
    }

    public void setAvailabilityType(String availabilityType) {
        this.availabilityType = availabilityType;
    }

}
