package ch.uzh.csg.foodchain.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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

import ch.uzh.csg.foodchain.Adapters.NewProductTagAdapter;
import ch.uzh.csg.foodchain.Adapters.ProcessAndActionAdapter;
import ch.uzh.csg.foodchain.Fragments.ProducerFragments.NewProducerFragment;
import ch.uzh.csg.foodchain.Fragments.ProducerFragments.ProducerFragment;
import ch.uzh.csg.foodchain.Models.EditLicensesModel;
import ch.uzh.csg.foodchain.Models.ProcessActionDataModel;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.AlertUtils;
import ch.uzh.csg.foodchain.Utils.Configuration;
import ch.uzh.csg.foodchain.Utils.Extras;


/**
 * The type New product fragment.
 */
public class NewProductFragment extends Fragment {
    /**
     * The Alert dialog.
     */
    android.support.v7.app.AlertDialog alertDialog;
    private RecyclerView rvNewProductTag;
    private ArrayList<ProcessActionDataModel> processActionArrayList;
    private ProcessAndActionAdapter processAndActionAdapter;
    private TextView tvProcessDone;
    private ImageView ivBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_product, container, false);
        rvNewProductTag = view.findViewById(R.id.rv_newProductTag);
        tvProcessDone = view.findViewById(R.id.btnProcessDone);
        ivBack = view.findViewById(R.id.ivProcessBack);
        return view;
    }

    /**
     * The M on navigation item selected listener.
     */
    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_product:

                    return true;
                case R.id.navigation_producer:
                    Fragment fragment2 = new ProducerFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment2).addToBackStack("abc").commit();
                    return true;
                case R.id.navigation_scanner:

                    return true;
                case R.id.navigation_setting:

                    return true;
            }
            return false;
        }
    };

    /**
     * Api setup.
     */
    public void apiSetup() {
        rvNewProductTag.setLayoutManager(new LinearLayoutManager(getActivity()));
        processActionArrayList = new ArrayList<>();
        apicall();
        if (alertDialog == null)
            alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        processAndActionAdapter = new ProcessAndActionAdapter(getActivity(), processActionArrayList, null);
        rvNewProductTag.setAdapter(processAndActionAdapter);
    }

    /**
     * Api call
     */
    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.GET_ALL_ACTION
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    if (alertDialog != null)
                        alertDialog.dismiss();
                    JSONArray jsonArr = new JSONArray(response);
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonObject = jsonArr.getJSONObject(i);
                        ProcessActionDataModel model = new ProcessActionDataModel();
                        String actionId = jsonObject.getString("actionId");
                        String actionName = jsonObject.getString("actionName");

                        model.setActionId(actionId);
                        model.setActionName(actionName);

                        processActionArrayList.add(model);
                    }
                    processAndActionAdapter.notifyDataSetChanged();

                } catch (JSONException e) {

                } catch (NullPointerException e){
                    Extras.responseErrorDialog("Invalid Server Response!", getActivity());
                    if (alertDialog != null)
                        alertDialog.dismiss();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (alertDialog != null)
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
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

    }

}
