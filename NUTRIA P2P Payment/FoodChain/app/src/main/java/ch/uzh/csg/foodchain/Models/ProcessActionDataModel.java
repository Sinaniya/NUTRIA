package ch.uzh.csg.foodchain.Models;

/**
 * The type Process action data model.
 */
public class ProcessActionDataModel {
    /**
     * The Action id.
     */
    String actionId;
    /**
     * The Action name.
     */
    String actionName;
    private boolean isChecked;
    private int position;
    /**
     * The constant on.
     */
    public static boolean on;


    /**
     * Gets action id.
     *
     * @return the action id
     */
    public String getActionId() {
        return actionId;
    }

    /**
     * Sets action id.
     *
     * @param actionId the action id
     */
    public void setActionId(String actionId) {
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

    /**
     * Gets checked.
     *
     * @return the checked
     */
    public boolean getChecked() {
        return isChecked;
    }

    /**
     * Sets checked.
     *
     * @param checked the checked
     */
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets position.
     *
     * @param position the position
     */
    public void setPosition(int position) {
        this.position = position;
    }

}
