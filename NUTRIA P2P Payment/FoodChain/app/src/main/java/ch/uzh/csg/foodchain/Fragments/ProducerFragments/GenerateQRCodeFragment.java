package ch.uzh.csg.foodchain.Fragments.ProducerFragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.uzh.csg.foodchain.Activities.PrintImageActivity;
import ch.uzh.csg.foodchain.Adapters.ActionAdapter;
import ch.uzh.csg.foodchain.Fragments.ConsumerFragments.QRScaningFragment;
import ch.uzh.csg.foodchain.Fragments.SettingFragments.SettingFragment;
import ch.uzh.csg.foodchain.Models.CreateProductTag.ProductTag;
import ch.uzh.csg.foodchain.Models.ProcessActionDataModel;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.AlertUtils;
import ch.uzh.csg.foodchain.Utils.Configuration;
import ch.uzh.csg.foodchain.Utils.Extras;
import ch.uzh.csg.foodchain.Utils.GeneralUtils;
import ch.uzh.csg.foodchain.networking.APIClient;
import ch.uzh.csg.foodchain.networking.APIServices;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import retrofit2.Call;
import retrofit2.Callback;

import static com.android.volley.VolleyLog.TAG;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;


/**
 * The type Generate qr code fragment.
 */
public class GenerateQRCodeFragment extends Fragment {

    /**
     * The constant QRcodeWidth.
     */
    public final static int QRcodeWidth = 500;
    private ImageView genrateQrImage, scanQrImage;
    private Dialog dialog;
    private Button btnQrGenetate, btnQrScan, btnQrPrint;
    private TextView btnremove;
    private String strToken, strLatitude, strLongitude, strProducerId;
    private String strHashCode;
    private TextView tvProductTag;
    private Bitmap bitmap, bm;
    private boolean check = false;
    private boolean recent = false;
    private RecyclerView rvActionList;

    /**
     * The Product tag raw.
     */
    String productTagRaw;

    private APIServices apiServices;

    /**
     * The Process action array list.
     */
    ArrayList<ProcessActionDataModel> processActionArrayList;
    /**
     * The Process and action adapter.
     */
    ActionAdapter processAndActionAdapter;
    /**
     * The Bottom navigation view.
     */
    BottomNavigationView bottomNavigationView;
    /**
     * The Qr actions.
     */
    public static HashMap<String, String> qrActions;
    /**
     * The String builder.
     */
    StringBuilder stringBuilder = null;
    private FusedLocationProviderClient mFusedLocationClient;
    /**
     * The M location request.
     */
    LocationRequest mLocationRequest  = LocationRequest.create();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qrActions = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_generate_qrcode, container, false);

        genrateQrImage = view.findViewById(R.id.genrateQrCodeImage);
        btnremove = view.findViewById(R.id.removeShared);
        btnQrGenetate = view.findViewById(R.id.btnQrGenrate);
        tvProductTag = view.findViewById(R.id.tvProductTag);
        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.producerBottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.getMenu().getItem(2).setChecked(true);
        Log.d("zma token", GeneralUtils.getAPIToken(getActivity()));
        btnQrPrint = view.findViewById(R.id.btnPrintQrCode);
        btnQrScan = view.findViewById(R.id.btnQrScan);
        rvActionList = view.findViewById(R.id.rvActionList);

        apiServices = APIClient.getApiClient(getActivity()).create(APIServices.class);

        strToken = GeneralUtils.getAPIToken(getActivity());
        strHashCode = GeneralUtils.getHashCode(getActivity());
        if (strHashCode.equals("")) {
            strHashCode = "0";
        } else {
            btnremove.setVisibility(View.VISIBLE);
            strHashCode = GeneralUtils.getHashCode(getActivity());
        }

        strProducerId = GeneralUtils.getProducerID(getActivity());
        boolean isgps = SmartLocation.with(getActivity()).location().state().isGpsAvailable();
        if (!isgps) {
            displayLocationSettingsRequest(getActivity());
        }

        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }

        }).check();

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        startLocationListener();
        apiSetup();

        mFusedLocationClient  = LocationServices.getFusedLocationProviderClient(getActivity());
        buildGoogleApiClient();
        createLocationRequest();
        Loc_Update();
        //create new product
        btnQrGenetate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                getQrAction();

                //Generate QR Code at first
                if (qrActions.size() <= 0 || stringBuilder == null) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.select_actions_msg))
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialog,
                                                            final int which) {


                                        }
                                    })
                            .create();
                    alertDialog.show();
                } else {

                    Log.d("STRHAHCODE: ", strHashCode);

                    if(!strHashCode.equals("0")) {

                        GeneralUtils.progressDialog(getActivity(), "checking");
                        createProductTagApiCall();
                        recent = true;
                    } else {
                        createfirstQRMessage();
                    }
                }

            }


        });


        btnQrScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.connectFragmentWithBackStack(getActivity(), new QRScaningFragment());
            }
        });
        btnremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSharedprefQR();


            }
        });
        btnQrPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   databaseHelper.clearQrActionTable();
                qrActions.clear();

                if (recent == false) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(getString(R.string.generate_qr_message))
                            .show();
                }
                else {

                    Intent intent = new Intent(getActivity(), PrintImageActivity.class);
                    intent.putExtra("filepath", Environment.getExternalStorageDirectory()
                            + "/FoodChain/"
                            + "/Files/QRCodeImage.png");
                    startActivity(intent);
                }
            }
        });

        tvProductTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeneralUtils.connectFragment(getActivity(), new ProducerFragment());
            }
        });


        return view;
    }


    /**
     * Delete QR from shared pref
     */
    public void deleteSharedprefQR() {

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.discard_qr_dialog_layout);
        Button delete = dialog.findViewById(R.id.btnDelete);
        Button cancel = dialog.findViewById(R.id.btnCancel);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralUtils.removeHashCode(getActivity());
                strHashCode = GeneralUtils.getHashCode(getActivity());
                dialog.dismiss();
                Fragment fragment = new GenerateQRCodeFragment();
                ((AppCompatActivity) getActivity()).getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("").commit();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    /**
     * Show Alert for GPS
     */
    private void buildAlertMessageNoGps() {
        try {
            AlertUtils.buildAlertMessageNoGps(getActivity());
        } catch (Exception ex) {
            Log.d("Exception", String.valueOf(ex));
        }
    }

    private void startLocationListener() {

        long mLocTrackingInterval = 1000 * 1; // 1 sec
        float trackingDistance = 0;
        LocationAccuracy trackingAccuracy = LocationAccuracy.HIGH;

        LocationParams.Builder builder = new LocationParams.Builder()
                .setAccuracy(trackingAccuracy)
                .setDistance(trackingDistance)
                .setInterval(500);

        SmartLocation.with(getActivity())
                .location()
                .continuous()
                .config(builder.build())
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        strLatitude = String.valueOf(location.getLatitude());
                        strLongitude = String.valueOf(location.getLongitude());
                    }
                });
    }

    /**
     * Text to image encode bitmap.
     *
     * @param Value the value
     * @return the bitmap
     * @throws WriterException the writer exception
     */
    Bitmap TextToImageEncode(String Value) throws WriterException {

        Log.d("HashValue: ", Value);
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    /**
     * Api setup.
     */
    public void apiSetup() {
        rvActionList.setLayoutManager(new LinearLayoutManager(getActivity()));
        processActionArrayList = new ArrayList<>();
        GeneralUtils.progressDialog(getActivity(), "");
        apicall();
        processAndActionAdapter = new ActionAdapter(getActivity(), processActionArrayList, null);
        rvActionList.setAdapter(processAndActionAdapter);
        processAndActionAdapter.loadItems(processActionArrayList);

    }

    /**
     * The M on navigation item selected listener.
     */
    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selected_fragment = null;
            switch (item.getItemId()) {

                case R.id.navigation_product:
                    selected_fragment = new ProducerFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).addToBackStack("abc").commit();
                    return true;

                case R.id.navigation_scanner:
                    selected_fragment = new QRScaningFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).addToBackStack("abc").commit();
                    return true;

                case R.id.navigation_home:
                    selected_fragment = new GenerateQRCodeFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).addToBackStack("abc").commit();
                    return true;

                case R.id.navigation_setting:
                    selected_fragment = new SettingFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).addToBackStack("abc").commit();
                    return true;

                case R.id.navigation_wallet:
                    selected_fragment = new WalletFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).addToBackStack("abc").commit();
                    return true;
            }

            return false;

        }

    };

    /**
     * loading Actions
     */
    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.GET_ALL_ACTION
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArr = new JSONArray(response);
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonObject = jsonArr.getJSONObject(i);
                        ProcessActionDataModel model = new ProcessActionDataModel();
                        String actionId = jsonObject.getString("actionId");
                        String actionName = jsonObject.getString("actionName");

                        GeneralUtils.progress.dismiss();
                        model.setActionId(actionId);
                        model.setActionName(actionName);

                        processActionArrayList.add(model);

                    }
                    processAndActionAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    Extras.responseErrorDialog(e.getCause().getMessage(), getActivity());

                } catch (NullPointerException e) {
                    Extras.responseErrorDialog(e.getCause().getMessage(),getActivity());
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String errorMessage = "";

                String jsonError = "";
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    jsonError = new String(networkResponse.data);
                    // Print Error!
                }

                try {
                    JSONObject jsonObject = new JSONObject(jsonError);
                    errorMessage = jsonObject.getString("errors");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(errorMessage.equals("")) {
                    Extras.responseErrorDialog("Can't get server!", getActivity());
                } else {

                    errorMessage.replaceAll("\\{", "");
                    errorMessage.replaceAll("\\}", "");

                    Extras.responseErrorDialog(errorMessage, getActivity());
                }

            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + strToken);
                return headers;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

    }

    /**
     * Send mail.
     */
    public void sendMail() {
        String path = Environment.getExternalStorageDirectory()
                + "/FoodChain/"
                + "/Files/QRCodeImage.png";
        Log.d("path", path);
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, "softwrengr@gmail.com");
        intent.setType("application/image");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        intent.putExtra(Intent.EXTRA_TEXT, "Enter your message");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + path));
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));

    }

    /**
     * Get hash byte [ ].
     * Generating SHA 256 key
     * @param password the password
     * @return the byte [ ]
     */

    public byte[] getHash(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        digest.reset();
        return digest.digest(password.getBytes());
    }
    //end
    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {

            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    /**
     * get the output of media file
     * @return File
     */
    private File getOutputMediaFile() {
        File mediaStorageDir = new
                File(Environment.getExternalStorageDirectory()
                + "/FoodChain/"
                + "/Files");


        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "QRCodeImage.png");
        return mediaFile;
    }

    private boolean deleoutput() {
        File mediaStorageDir = new
                File(Environment.getExternalStorageDirectory()
                + "/FoodChain/"
                + "/Files");


        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return false;
            }
        }
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "QRCodeImage.png");
        if (mediaFile.exists())
            mediaFile.delete();
        return true;
    }

    /**
     * Create product tag api call.
     */
    public void createProductTagApiCall() {
        if (strLatitude == null && strLongitude == null) {
            startLocationListener();
            buildGoogleApiClient();
            createLocationRequest();
            Loc_Update();
        }

        Log.d("latitudeLogitude: ",strLongitude+ "   "+ strLatitude);

        getQrAction();
        JsonObject js = new JsonObject();

        try {

            if (strLatitude != null && strLongitude != null) {

                js.addProperty("longitude", Double.parseDouble(strLongitude));
                js.addProperty("latitude", Double.parseDouble(strLatitude));

                JsonArray hashCode = new JsonArray();

                Extras.qr_hashes_multi = Arrays.asList(strHashCode.split(","));

                for(int i = 0; i < Extras.qr_hashes_multi.size(); i++) {
                    hashCode.add(Extras.qr_hashes_multi.get(i));
                    Log.d("strHashCodennnnnnnn: ", Extras.qr_hashes_multi.get(i));
                }

                js.add("previousProductTagHashes",hashCode);

                JsonArray productTagActionsJsonArray = new JsonArray();

                for (String key : qrActions.keySet()) {

                    Log.d("key", key);
                    JsonObject action = new JsonObject();
                    action.addProperty("actionId", key);
                    productTagActionsJsonArray.add(action);

                }

                JsonObject productTagProducerJsonObject = new JsonObject();
                productTagProducerJsonObject.addProperty("producerId", strProducerId);

                js.add("productTagActions", productTagActionsJsonArray);
                js.add("productTagProducer", productTagProducerJsonObject);
            }

        } catch (JsonParseException e) {
            Extras.responseErrorDialog("Invalid Input!", getActivity());
        } catch (NullPointerException e){
            Extras.responseErrorDialog("Invalid Information, Make sure your Location is Enabled and try again!", getActivity());

        }


        Log.d("productTagRaw: ", js.toString());


        Call<ProductTag> createProduct = apiServices.createProductTag("Bearer "+strToken,js);
        createProduct.enqueue(new Callback<ProductTag>() {

            @Override
            public void onResponse(Call<ProductTag> call, retrofit2.Response<ProductTag> response) {


                String response_string = new Gson().toJson(response.body());

                Log.d("RESPONSE: ",response_string);
                ProductTag productTag = response.body();


                try {

                    String productTagHash = productTag.getProductTagHash();

                    GeneralUtils.putStringValueInEditor(getActivity(), "hash_code", "").commit();

                    try {

                        bitmap = TextToImageEncode(productTagHash);
                        genrateQrImage.setImageBitmap(bitmap);
                        BitmapDrawable drawable = (BitmapDrawable) genrateQrImage.getDrawable();
                        bm = drawable.getBitmap();
                        storeImage(bm);
                        GeneralUtils.progress.dismiss();
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText(getString(R.string.product_created))
                                .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                    }
                                })

                                .show();

                    } catch (WriterException e) {
                        Extras.responseErrorDialog(getString(R.string.qr_creation_error), getActivity());

                    }
                } catch (JsonParseException e) {
                    Extras.responseErrorDialog(getString(R.string.invalid_response), getActivity());

                }
                catch (NullPointerException e) {

                    Extras.responseErrorDialog(getString(R.string.try_again), getActivity());

                }

            }

            @Override
            public void onFailure(Call<ProductTag> call, Throwable t) {

                Extras.responseErrorDialog(t.getMessage(), getActivity());

            }
        });

    }



    /**
     * Gets qr action.
     */
    public void getQrAction() {

        stringBuilder = new StringBuilder();
        for (String name : qrActions.values()) {

            stringBuilder.append(name + "");
        }
    }

    private void displayLocationSettingsRequest(Context context) {

        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(10000 / 2);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            Log.i(TAG, "All location settings are satisfied.");
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the result
                                // in onActivityResult().
                                status.startResolutionForResult(getActivity(), 1);
                            } catch (IntentSender.SendIntentException e) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                            break;
                    }
                }
            });
        }

    }


    /**
     * Createfirst qr message.
     * dialog box to display on first qr code generate
     */
    public void createfirstQRMessage() {

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.first_scan_message);
        Button yes_btn = dialog.findViewById(R.id.btnYes);
        Button no_btn = dialog.findViewById(R.id.btnNo);
        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                GeneralUtils.progressDialog(getActivity(), "checking");
                createProductTagApiCall();
                recent = true;

            }
        });
        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onNO();
            }
        });
        dialog.show();

    }

    private void onNO() {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.scan_message))
                .setCancelable(false)
                .setPositiveButton(R.string.button_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog,
                                                final int which) {

                                GeneralUtils.connectFragmentWithBackStack(getActivity(), new QRScaningFragment());
                            }
                        }).setNegativeButton(R.string.button_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog,
                                                final int which) {
                            }
                        })
                .create();
        alertDialog.show();
    }

    /**
     * Build google api client.
     */
    protected synchronized void buildGoogleApiClient() {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location!=null){
                                        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                                        strLatitude = String.valueOf(location.getLatitude());
                                        strLongitude = String.valueOf(location.getLongitude());
                                        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        //LOG
                    }
                })
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Create location request.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(30000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void Loc_Update() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(getActivity()).checkLocationSettings(builder.build());
        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                for (Location location : locationResult.getLocations()) {
                                    //Do what you want with location
                                    //like update camera
                                    strLatitude = String.valueOf(location.getLatitude());
                                    strLongitude = String.valueOf(location.getLongitude());
                                    //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16f));
                                }

                            }
                        },null);

                    }
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(getActivity(), 2001);
                                break;
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.

                            break;
                    }
                }}
        });

    }

}

/*
    public void printQRCode() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Print or Share");
        builder.setMessage("Share this QR code or print it right there.");
        builder.setPositiveButton("Print", (dialog, which) -> {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                PrintHelper printHelper = new PrintHelper(getActivity());
                printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
                printHelper.printBitmap("QRCode " + new Date().toString(), bitmap);
            } else {
                Toast.makeText(getActivity(), "Your device doesn't support printer, you can share the code instead", Toast.LENGTH_SHORT).show();
                sendMail();
            }
        });
        builder.setNegativeButton("Share", (dialog, which) -> {
            sendMail();
        });
        builder.setNeutralButton("Cancel", null);
        builder.show();
    }*/
