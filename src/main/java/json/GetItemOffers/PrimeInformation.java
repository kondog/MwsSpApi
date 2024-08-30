
package json.GetItemOffers;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class PrimeInformation {

    @SerializedName("IsPrime")
    @Expose
    private Boolean isPrime;
    @SerializedName("IsNationalPrime")
    @Expose
    private Boolean isNationalPrime;

    public Boolean getIsPrime() {
        return isPrime;
    }

    public void setIsPrime(Boolean isPrime) {
        this.isPrime = isPrime;
    }

    public Boolean getIsNationalPrime() {
        return isNationalPrime;
    }

    public void setIsNationalPrime(Boolean isNationalPrime) {
        this.isNationalPrime = isNationalPrime;
    }

}
