package ch.uzh.csg.foodchain.Fragments.ProducerFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.uzh.csg.foodchain.Adapters.ProducersAdapter;
import ch.uzh.csg.foodchain.Fragments.ConsumerFragments.QRScaningFragment;
import ch.uzh.csg.foodchain.Fragments.SettingFragments.SettingFragment;
import ch.uzh.csg.foodchain.Models.ProductDataModel;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.AlertUtils;
import ch.uzh.csg.foodchain.Utils.Configuration;
import ch.uzh.csg.foodchain.Utils.Extras;
import ch.uzh.csg.foodchain.Utils.GeneralUtils;


/**
 * The type Producer fragment.
 */
public class ProducerFragment extends Fragment {
    /**
     * The Alert dialog.
     */
    android.support.v7.app.AlertDialog alertDialog;
    private RecyclerView rvProducer;
    private ArrayList<ProductDataModel> allProducerArrayList;
    private ProducersAdapter producersAdapter;
    private BottomNavigationView bottomNavigationView;
    private String strToken, strProducerId;
    private TextView tvNew;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_producer, container, false);
        tvNew = view.findViewById(R.id.tvNew);
        rvProducer = view.findViewById(R.id.rv_producer);
        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.producerBottomBar);
        bottomNavigationView.performClick();
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        rvProducer.setLayoutManager(new LinearLayoutManager(getActivity()));

        strToken = GeneralUtils.getAPIToken(getActivity());
        strProducerId = GeneralUtils.getProducerID(getActivity());
        apiSetUp();

        tvNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeneralUtils.connectFragment(getActivity(), new GenerateQRCodeFragment());
            }
        });

        return view;
    }

    /**
     * Api set up.
     */
    public void apiSetUp() {
        rvProducer.setLayoutManager(new LinearLayoutManager(getActivity()));
        allProducerArrayList = new ArrayList<>();
        apiCall();
        if (alertDialog == null)
            alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        producersAdapter = new ProducersAdapter(getActivity(), allProducerArrayList);
        rvProducer.setAdapter(producersAdapter);

    }

    /**
     * API call
     */
    private void apiCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.PRODUCT + strProducerId
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    Log.d("response", response);
                    Log.d("strProducerId", strProducerId);

                    alertDialog.dismiss();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ProductDataModel model = new ProductDataModel();
                        String productTagId = jsonObject.getString("productTagId");
                        String productTagHash = jsonObject.getString("productTagHash");
                        String date = jsonObject.getString("dateTime");

                        model.setProductTagId(productTagId);
                        model.setProductTagHash(productTagHash);
                        model.setDate(date);
                        allProducerArrayList.add(model);

                    }

                    producersAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    alertDialog.dismiss();
                    Extras.responseErrorDialog("Invalid Json Response!", getActivity());

                } catch (NullPointerException e) {
                    alertDialog.dismiss();
                    Extras.responseErrorDialog("Invalid! Server null Response.", getActivity());

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                alertDialog.dismiss();
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
}

