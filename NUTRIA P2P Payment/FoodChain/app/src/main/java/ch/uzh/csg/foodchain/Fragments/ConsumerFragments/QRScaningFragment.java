package ch.uzh.csg.foodchain.Fragments.ConsumerFragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import ch.uzh.csg.foodchain.Adapters.ActorListAdapter;
import ch.uzh.csg.foodchain.Adapters.ProducersAdapter;
import ch.uzh.csg.foodchain.Models.ProductDataModel;
import cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ch.uzh.csg.foodchain.Activities.MainActivity;
import ch.uzh.csg.foodchain.Fragments.ActorListFragment;
import ch.uzh.csg.foodchain.Fragments.ProducerFragments.GenerateQRCodeFragment;
import ch.uzh.csg.foodchain.Models.ScannedQRModel.PreviousProductTag;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.Extras;
import ch.uzh.csg.foodchain.Utils.GeneralUtils;
import ch.uzh.csg.foodchain.networking.APIClient;
import ch.uzh.csg.foodchain.networking.APIServices;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The type Qr scaning fragment.
 */
public class QRScaningFragment extends Fragment {
    private static Retrofit retrofit = null;
    /**
     * The constant strBarCodeValue.
     */
    public static String strBarCodeValue;
    /**
     * The M context.
     */
    Context mContext;

    /**
     * The Count.
     */
    int count = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qr, container, false);

        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }

        }).check();

        IntentIntegrator.forFragment(this).initiateScan();

        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
        intentIntegrator.setPrompt("Scan");
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setBarcodeImageEnabled(false);


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {

            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "did not scan anything", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            } else if (result != null) {

                if (result.getContents().length() < 64) {

                    Toast.makeText(getActivity(), "Product is not valid " + result.getContents(), Toast.LENGTH_LONG).show();

                    if (Extras.userType.equals("consumer")) {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    } else {

                        GeneralUtils.connectFragmentWithBackStack(getActivity(), new GenerateQRCodeFragment());
                    }
                } else {
                    strBarCodeValue = result.getContents();

                    ProducersAdapter.Qrcode = strBarCodeValue;
                    verifyHashKey(strBarCodeValue);

                }

            } else {
                super.onActivityResult(requestCode, resultCode, data);

            }
        } catch (Exception e) {

        }

    }

    private void verifyHashKey(String hash) {

        try {
            APIServices services = getClient().create(APIServices.class);
            Call<PreviousProductTag> call = services.getScanningQR(hash);
            ArrayList<String> hashcodes = new ArrayList<>();

            call.enqueue(new Callback<PreviousProductTag>() {
                @Override
                public void onResponse(Call<PreviousProductTag> call, retrofit2.Response<PreviousProductTag> response) {

                    try {
                        List<PreviousProductTag> scanning_qr_list = new ArrayList<>();

                        scanning_qr_list.add(response.body());


                        Log.d("RESPZZZZOxe: ", new Gson().toJson(response));

                        //sort list
                        Collections.sort(scanning_qr_list, new Comparator<PreviousProductTag>() {
                            public int compare(PreviousProductTag d1, PreviousProductTag d2) {
                                return Long.valueOf(d1.getProductTagId()).compareTo(d2.getProductTagId().longValue());
                            }
                        });


                        System.out.println("After Assending sort");
                        for (PreviousProductTag person : scanning_qr_list) {

                            count++;

                            Log.d("AFTERSORTARRAY:", person.getDateTime() + " " + person.getProductTagId());

                        }

                        if (scanning_qr_list.size() > 1) {

                            for (int i = 0; i < scanning_qr_list.size(); i++) {

                                hashcodes.add(response.body().getProductTagHash());

                            }

                            if (hashcodes.contains(hash)) {

                                for (int i = 0; i < scanning_qr_list.size(); i++) {

                                    String productHash = response.body().getProductTagHash();

                                    if (productHash.equals(hash)) {

                                        Log.d("HashCodeAAA:",strBarCodeValue);
                                        Log.d("HashCodeBBB:",hash);

                                        ProducersAdapter.Qrcode = strBarCodeValue;

                                        scanning_qr_list.clear();
                                        scanning_qr_list.add(response.body());

                                        String hashesValue = GeneralUtils.getHashCode(getActivity());

                                        if(hashesValue == "") {
                                            hashesValue = strBarCodeValue;
                                        } else {

                                            hashesValue = hashesValue +","+ strBarCodeValue;
                                        }

                                        Log.d("HashesVALUEAFTERCONCAT:", hashesValue);

                                        GeneralUtils.putStringValueInEditor(getActivity(), "hash_code", hashesValue).commit();
                                        GeneralUtils.connectFragmentWithBackStack(getActivity(), new ActorListFragment());

                                    }

                                }

                            } else {

                                Toast.makeText(getActivity(), "Invalid product", Toast.LENGTH_SHORT).show();
                                GeneralUtils.connectFragment(getActivity(), new GenerateQRCodeFragment());
                            }
                        } else {

                            String productHash = response.body().getProductTagHash();
                            if (productHash.equals(hash)) {

                                scanning_qr_list.clear();
                                scanning_qr_list.add(response.body());

                                String hashesValue = GeneralUtils.getHashCode(getActivity());
                                ProducersAdapter.Qrcode = strBarCodeValue;
                                if(hashesValue == "") {
                                    hashesValue = strBarCodeValue;
                                } else {

                                    hashesValue = hashesValue +","+ strBarCodeValue;
                                }


                                GeneralUtils.putStringValueInEditor(getActivity(), "hash_code", hashesValue).commit();
                                GeneralUtils.connectFragmentWithBackStack(getActivity(), new ActorListFragment());

                            } else {
                                Toast.makeText(getActivity(), "Invalid product", Toast.LENGTH_SHORT).show();
                                GeneralUtils.connectFragment(getActivity(), new GenerateQRCodeFragment());
                            }
                        }

                    } catch (NullPointerException e) {

                        Extras.responseErrorDialog(response.message(), getActivity());

                    }
                }

                @Override
                public void onFailure(Call<PreviousProductTag> call, Throwable t) {

                    Extras.responseErrorDialog(t.getMessage(), getActivity());

                }
            });
        } catch (Exception e) {

        }
    }

    /**
     * Gets client.
     *
     * @return the client
     */
    public Retrofit getClient() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS);

        httpClient.retryOnConnectionFailure(true);
        mContext=getActivity();
        try {
            Log.d("Context", String.valueOf(mContext));
            String apiToken = GeneralUtils.getAPIToken(getActivity());
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {

                    // try {
                    Request original = chain.request();

                    // Customize the request
                    Request request = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Content-Type", "form-data")
                            .header("Authorization", apiToken)
                            .method(original.method(), original.body())
                            .build();
                    Log.d("apiToken", String.valueOf(apiToken));

                    Response response = chain.proceed(request);

                    // Customize or return the response
                    return response;
                }
            });


            OkHttpClient OkHttpClient = httpClient.build();


            if (retrofit == null) {

                retrofit = new Retrofit.Builder()
                        .baseUrl(APIClient.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(OkHttpClient)
                        .build();
            }

        } catch ( Exception e){

        }
        return retrofit;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

}
