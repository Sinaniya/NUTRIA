package ch.uzh.csg.foodchain.Models;

import java.util.HashMap;

/**
 * The type Hash map model.
 */
public class HashMapModel {

    private HashMap<String, String> actions, certificates;

    /**
     * Instantiates a new Hash map model.
     */
    public HashMapModel() {

        actions = new HashMap<>();
        certificates = new HashMap<>();

    }

    /**
     * Sets action.
     *
     * @param id   the id
     * @param name the name
     */
    public void setAction(String id, String name) {

        actions.put(id,name);
    }

    /**
     * Sets license.
     *
     * @param id   the id
     * @param name the name
     */
    public void setLicense(String id, String name) {

        certificates.put(id, name);
    }

    /**
     * Gets license.
     *
     * @return the license
     */
    public HashMap<String, String> getLicense() {

        return certificates;
    }

    /**
     * Gets action.
     *
     * @return the action
     */
    public HashMap<String, String> getAction() {

        return actions;
    }

}
