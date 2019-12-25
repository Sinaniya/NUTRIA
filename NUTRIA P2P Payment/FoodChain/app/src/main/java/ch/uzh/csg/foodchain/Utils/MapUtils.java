package ch.uzh.csg.foodchain.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ch.uzh.csg.foodchain.Models.MapDataModel;
import ch.uzh.csg.foodchain.Models.ScannedQRModel.PreviousProductTag;

import static java.lang.Integer.valueOf;

/**
 * The type Map utils.
 */
public class MapUtils {

    /**
     * The constant allMapData.
     */
    public  static ArrayList<MapDataModel> allMapData = new ArrayList<>();
    /**
     * The Product tag.
     */
    PreviousProductTag productTag;

    /**
     * This function will recursively get the response
     * @param productTags
     */
    private static void getMarkersRecursively(PreviousProductTag productTags) {

        try {
            String productTagHash = productTags.getProductTagHash();
            if (productTagHash.equals("0")) {
                return;
            }
            String producerName = productTags.getProductTagProducer().getProducerName();
            String latitude = productTags.getLatitude().toString();
            String longitude = productTags.getLongitude().toString();
            String date = productTags.getDateTime();
            String productID = productTags.getProductTagId().toString();

            String previousProductTagHash = "";
            for (int i = 0; i < productTags.getPreviousProductTags().size(); i++) {
                previousProductTagHash = previousProductTagHash + ", " + productTags.getPreviousProductTags().get(i).getProductTagHash();
            }

            String actions = "";

            //Modification example
            String action_date = "";
            for (int i = 0; i < productTags.getProductTagProducer().getProducerActions().size(); i++) {
                actions = actions + ", " + productTags.getProductTagProducer().getProducerActions().get(i).getActionName();
                //ModificationExample
                //action_date = action_date + ", " + productTags.getProductTagProducer().getProducerActions().get(i).getActiondate();
            }
            String certificates = "";
            for (int i = 0; i < productTags.getProductTagProducer().getProducerCertificates().size(); i++) {
                certificates = certificates + ", " + productTags.getProductTagProducer().getProducerCertificates().get(i).getCertificateName();
            }
            String productTagActions = "";
            for (int i = 0; i < productTags.getProductTagActions().size(); i++) {
                productTagActions = productTagActions + ", " + productTags.getProductTagActions().get(i).getActionName();
            }
            addToProductTagsArrayList(productID, date, productTagHash, previousProductTagHash, latitude, longitude, actions, certificates, productTagActions, producerName);

            for (PreviousProductTag productTag : productTags.getPreviousProductTags()) {
                getMarkersRecursively(productTag);
            }

        } catch (Exception e) {
            e.getMessage();
        }

    }

    /**
     * Add to product tags array list.
     *
     * @param productID              the product id
     * @param date                   the date
     * @param productTagHash         the product tag hash
     * @param previousProductTagHash the previous product tag hash
     * @param lat                    the lat
     * @param lon                    the lon
     * @param actions                the actions
     * @param certificates           the certificates
     * @param productTagActions      the product tag actions
     * @param producerName           the producer name
     */
    public static void addToProductTagsArrayList(String productID, String date, String productTagHash,
                                                             String previousProductTagHash, String lat, String lon, String actions,
                                                             String certificates, String productTagActions, String producerName) {

        MapDataModel mapDataModel = new MapDataModel();

        mapDataModel.setProductId(Integer.parseInt(productID));
        mapDataModel.setDateTime(date);
        mapDataModel.setCurrHash(productTagHash);
        mapDataModel.setPreHash(previousProductTagHash);
        mapDataModel.setLat(Double.parseDouble(lat));
        mapDataModel.setLng(Double.parseDouble(lon));
        mapDataModel.setActions(actions);
        mapDataModel.setCertificates(certificates);
        mapDataModel.setProductTagActions(productTagActions);
        mapDataModel.setProducerName(producerName);

        allMapData.add(mapDataModel);

    }

    /**
     * Sort the array list according to date and time
     */
    private static void sortingArrayList() {
        Collections.sort(allMapData, new Comparator<MapDataModel>() {
            public int compare(MapDataModel d1, MapDataModel d2) {
                return valueOf(d1.getDateTime().compareTo(d2.getDateTime()));
            }
        });
    }

    /**
     * Get markers data array list.
     *
     * @param productTag the product tag
     * @return the array list
     */
    public  static  ArrayList<MapDataModel> getMarkersData(PreviousProductTag productTag){
        getMarkersRecursively(productTag);
        sortingArrayList();
        return  allMapData;
    }

}
