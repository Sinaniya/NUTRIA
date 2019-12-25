package ch.uzh.csg.foodchain.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import ch.uzh.csg.foodchain.Adapters.ActorListAdapter;
import ch.uzh.csg.foodchain.Adapters.ProducersAdapter;
import ch.uzh.csg.foodchain.Fragments.ConsumerFragments.QRScaningFragment;
import ch.uzh.csg.foodchain.Fragments.ProducerFragments.GenerateQRCodeFragment;
import ch.uzh.csg.foodchain.Models.ScannedQRModel.PreviousProductTag;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.Configuration;
import ch.uzh.csg.foodchain.Utils.Extras;
import ch.uzh.csg.foodchain.Utils.GeneralUtils;
import ch.uzh.csg.foodchain.networking.APIClient;
import ch.uzh.csg.foodchain.networking.APIServices;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * The type Actor list fragment.
 */
public class ActorListFragment extends Fragment {
    private static Retrofit retrofit = null;
    private Bundle bundle;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private RecyclerView rvActorList;
    private ArrayList<PreviousProductTag> actorListModelArrayList;
    private ActorListAdapter actorListAdapter;
    private String strHashCode;
    /**
     * The M context.
     */
    Context mContext;
    private TextView tvUpdateProduct;
    private double strLat1, strLat2, strLat3, strLat4, strLat5, strLng1, strLng2, strLng3, strLng4, strLng5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_actor_list, container, false);
        tvUpdateProduct = view.findViewById(R.id.updateProduct);
        rvActorList = view.findViewById(R.id.rv_actorlist);
        apiSetUp();

        sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if(Extras.userType.equals("consumer")){
            tvUpdateProduct.setText(getString(R.string.scan_new_qr));
            tvUpdateProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Extras.scan_type = "";
                    GeneralUtils.connectFragmentWithBackStack(getActivity(), new QRScaningFragment());
                }
            });
        } else if(Extras.scan_type.equals("producer")) {
            tvUpdateProduct.setText("Back");
            tvUpdateProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Extras.scan_type = "";
                    GeneralUtils.connectFragmentWithBackStack(getActivity(), new GenerateQRCodeFragment());
                }
            });

        } else {

            tvUpdateProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GeneralUtils.connectFragmentWithBackStack(getActivity(), new GenerateQRCodeFragment());
                }
            });
        }

        return view;
    }

    /**
     * Api set up.
     */
    public void apiSetUp() {
        rvActorList.setLayoutManager(new LinearLayoutManager(getActivity()));
        actorListModelArrayList = new ArrayList<>();
        GeneralUtils.progressDialog(getActivity(), "");
        retrofitAPICall();
        actorListAdapter = new ActorListAdapter(getActivity(), actorListModelArrayList);
        rvActorList.setAdapter(actorListAdapter);

    }


    /**
     * Api call
     */
    private void retrofitAPICall() {

        APIServices services = getClient().create(APIServices.class);

        if(QRScaningFragment.strBarCodeValue == null || QRScaningFragment.strBarCodeValue.equals("") ) {
            QRScaningFragment.strBarCodeValue = ProducersAdapter.Qrcode;
        }

        Call<PreviousProductTag> call = services.getScanningQR(QRScaningFragment.strBarCodeValue);
        QRScaningFragment.strBarCodeValue = "";
        call.enqueue(new Callback<PreviousProductTag>() {
            @Override
            public void onResponse(Call<PreviousProductTag> call, retrofit2.Response<PreviousProductTag> response) {

                String response_string = new Gson().toJson(response.body());
                Log.d("RESPONSE:", response_string);
                try {
                    if (response.isSuccessful()) {
                        actorListModelArrayList.add(response.body());
                        actorListAdapter.notifyDataSetChanged();
                    }
                }catch (NullPointerException e){
                    Extras.responseErrorDialog("Information provided to the server was invalid!", getActivity());
                }
            }

            @Override
            public void onFailure(Call<PreviousProductTag> call, Throwable t) {
                Log.d("zma actor list frag", t.getMessage());
                Extras.responseErrorDialog(t.getMessage(), getActivity());
            }
        });

    }

    /**
     * Gets client.
     *
     * @return the client
     */
    public Retrofit getClient() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS);
        try {

            String apiToken = GeneralUtils.getAPIToken(getActivity());
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    okhttp3.Request original = chain.request();

                    // Customize the request

                    okhttp3.Request request = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Content-Type", "form-data")
                            .header("Authorization", apiToken)
                            .method(original.method(), original.body())
                            .build();

                    okhttp3.Response response = chain.proceed(request);
                    GeneralUtils.progress.dismiss();

                    // Customize or return the response
                    return response;
                }
            });
        } catch (Exception ex) {
            Log.d("mehr", String.valueOf(ex));
            Extras.responseErrorDialog("Invalid Server Response!", getActivity());
        }

        OkHttpClient OkHttpClient = httpClient.build();


        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(APIClient.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpClient)
                    .build();
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
