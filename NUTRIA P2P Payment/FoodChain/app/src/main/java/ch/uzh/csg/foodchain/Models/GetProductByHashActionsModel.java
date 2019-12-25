package ch.uzh.csg.foodchain.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * The type Get product by hash actions model.
 */
public class GetProductByHashActionsModel implements Serializable {


    @SerializedName("actionId")
    @Expose
    private Integer actionId;
    @SerializedName("actionName")
    @Expose
    private String actionName;

    /**
     * Gets action id.
     *
     * @return the action id
     */
    public Integer getActionId() {
        return actionId;
    }

    /**
     * Sets action id.
     *
     * @param actionId the action id
     */
    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    /**
     * Gets action name.
     *
     * @return the action name
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * Sets action name.
     *
     * @param actionName the action name
     */
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

}
