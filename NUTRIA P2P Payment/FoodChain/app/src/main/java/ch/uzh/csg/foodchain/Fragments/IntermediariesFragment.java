package ch.uzh.csg.foodchain.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.uzh.csg.foodchain.Models.MapDataModel;
import ch.uzh.csg.foodchain.Models.ScannedQRModel.PreviousProductTag;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.Extras;
import ch.uzh.csg.foodchain.Utils.MapUtils;

import static java.lang.Integer.valueOf;


/**
 * The type Intermediaries fragment.
 */
public class IntermediariesFragment extends Fragment {
    /**
     * The Map view.
     */
    MapView mapView;
    /**
     * The Google map.
     */
    GoogleMap googleMap;
    /**
     * The Shared preferences.
     */
    SharedPreferences sharedPreferences;
    /**
     * The Open street map view.
     */
    org.osmdroid.views.MapView openStreetMapView;
    /**
     * The Mc.
     */
    MapController mc;
    /**
     * The Num txt.
     */
    TextView numTxt;
    /**
     * The Custom marker view.
     */
    View customMarkerView;
    /**
     * The Markers counter.
     */
    int markersCounter;
    /**
     * The All map data.
     */
    ArrayList<MapDataModel> allMapData = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        markersCounter = 1;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intermediaries, container, false);
        Bundle args = getArguments();
        customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        numTxt = (TextView) customMarkerView.findViewById(R.id.num_txt);
        openStreetMapView = view.findViewById(R.id.mapview);
        openStreetMapView.setTileSource(TileSourceFactory.MAPNIK);
        openStreetMapView.setBuiltInZoomControls(true);
        openStreetMapView.setMultiTouchControls(true);
        mc = (MapController) openStreetMapView.getController();
        openStreetMapView.getOverlays().clear();

        assert args != null;

        ArrayList<PreviousProductTag> list = (ArrayList<PreviousProductTag>) args.getSerializable("coordinates");
        allMapData = MapUtils.getMarkersData(list.get(0)); // Get all markers recursively
        for (MapDataModel product : allMapData) {
            Log.d("AFTERSORTARRAY:", product.getDateTime());
            addMarkersToMap(product.getProductId() + "", product.getDateTime(), product.getCurrHash(), product.getPreHash(), product.getLat().toString(), product.getLng().toString(), product.getActions(), product.getCertificates(), product.getProductTagActions(), product.getProducerName());
        }

        mc.setZoom(9);
        openStreetMapView.invalidate();
        return view;
    }

    private void addMarkersToMap(String productID, String date, String productTagHash,
                                 String previousProductTagHash, String lat, String lon, String actions,
                                 String certificates, String productTagActions, String producerName) {

        numTxt.setText(markersCounter + "");

        String display_string = "Product ID: " + productID + "\n Producer Name: " + producerName + "\n Date: " + date + "\n Product Tag Hash: " + productTagHash +
                "\n Latitude: " + lat + "\n Longitude: " + lon + "\nPrevious Product Tag(s): " + previousProductTagHash.replaceFirst(",", "") + "\nCertificates: " + certificates.replaceFirst(",", "") +
                "\n Actions: " + productTagActions.replaceFirst(",", "");

        if (!lat.isEmpty() || lat != null) {

            Drawable drawable = new BitmapDrawable(getResources(), Extras.createDrawableFromView(getActivity(), customMarkerView));

            Marker marker = null;
            GeoPoint geoPoint = new GeoPoint(Double.parseDouble(lat), Double.parseDouble(lon));
            mc.animateTo(geoPoint);
            marker = new Marker(openStreetMapView);
            marker.setPosition(geoPoint);
            marker.setTitle(display_string);
            marker.setEnableTextLabelsWhenNoImage(true);
            marker.setIcon(drawable);

            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

            openStreetMapView.getOverlays().add(marker);

            //clear once all load to map
            if (markersCounter == allMapData.size()) {
                markersCounter = 0;
                allMapData.clear();
            } else {
                markersCounter++;
            }

        }
    }
}
