
package co.ilumi.ilumiexample.model.data;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Entities {

    @SerializedName("sentiment")
    @Expose
    private List<Sentiment> sentiment = new ArrayList<Sentiment>();

    @SerializedName("color")
    @Expose
    private List<Color> color = new ArrayList<Color>();


    /**
     * 
     * @return
     *     The sentiment
     */
    public List<Sentiment> getSentiment() {
        return sentiment;
    }

    /**
     * 
     * @param sentiment
     *     The sentiment
     */
    public void setSentiment(List<Sentiment> sentiment) {
        this.sentiment = sentiment;
    }


    /**
     *
     * @return
     *     The color
     */
    public List<Color> getColor() {
        return color;
    }

    /**
     *
     * @param color
     *     The color
     */
    public void setColor(List<Color> color) {
        this.color = color;
    }


}
