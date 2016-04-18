
package co.ilumi.ilumiexample.model.data;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IlumiNLPResponseJSON {

    @SerializedName("msg_id")
    @Expose
    private String msgId;
    @SerializedName("_text")
    @Expose
    private String Text;
    @SerializedName("outcomes")
    @Expose
    private List<Outcome> outcomes = new ArrayList<Outcome>();

    /**
     * 
     * @return
     *     The msgId
     */
    public String getMsgId() {
        return msgId;
    }

    /**
     * 
     * @param msgId
     *     The msg_id
     */
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

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
     *     The outcomes
     */
    public List<Outcome> getOutcomes() {
        return outcomes;
    }

    /**
     * 
     * @param outcomes
     *     The outcomes
     */
    public void setOutcomes(List<Outcome> outcomes) {
        this.outcomes = outcomes;
    }

}
