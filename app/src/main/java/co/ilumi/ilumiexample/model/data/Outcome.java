
package co.ilumi.ilumiexample.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import co.ilumi.ilumiexample.model.data.Entities;

public class Outcome {

    @SerializedName("_text")
    @Expose
    private String Text;
    @SerializedName("confidence")
    @Expose
    private Object confidence;
    @SerializedName("intent")
    @Expose
    private String intent;
    @SerializedName("entities")
    @Expose
    private Entities entities;

    /**
     * 
     * @return
     *     The Text
     */
    public String getText() {
        return Text;
    }

    /**
     * 
     * @param Text
     *     The _text
     */
    public void setText(String Text) {
        this.Text = Text;
    }

    /**
     * 
     * @return
     *     The confidence
     */
    public Object getConfidence() {
        return confidence;
    }

    /**
     * 
     * @param confidence
     *     The confidence
     */
    public void setConfidence(Object confidence) {
        this.confidence = confidence;
    }

    /**
     * 
     * @return
     *     The intent
     */
    public String getIntent() {
        return intent;
    }

    /**
     * 
     * @param intent
     *     The intent
     */
    public void setIntent(String intent) {
        this.intent = intent;
    }

    /**
     * 
     * @return
     *     The entities
     */
    public Entities getEntities() {
        return entities;
    }

    /**
     * 
     * @param entities
     *     The entities
     */
    public void setEntities(Entities entities) {
        this.entities = entities;
    }

}
