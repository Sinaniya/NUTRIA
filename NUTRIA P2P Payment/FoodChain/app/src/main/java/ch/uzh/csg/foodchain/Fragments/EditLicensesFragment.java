package ch.uzh.csg.foodchain.Fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ch.uzh.csg.foodchain.Adapters.EditLicenseAdapter;
import ch.uzh.csg.foodchain.Fragments.ProducerFragments.GenerateQRCodeFragment;
import ch.uzh.csg.foodchain.Fragments.ProducerFragments.NewProducerFragment;
import ch.uzh.csg.foodchain.Models.AllCertificateModel;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.Configuration;
import ch.uzh.csg.foodchain.Utils.Extras;
import ch.uzh.csg.foodchain.Utils.GeneralUtils;
import ch.uzh.csg.foodchain.interfaces.CertificateAdditionCallback;


/**
 * The type Edit licenses fragment.
 */
public class EditLicensesFragment extends Fragment implements CertificateAdditionCallback {
    /**
     * The constant isCertificatedItemSelected.
     */
    public static boolean isCertificatedItemSelected = false;
    private RecyclerView rvEditLicenses;
    private ArrayList<AllCertificateModel> editLicensesModelArrayList;
    private EditLicenseAdapter editLicenseAdapter;
    /**
     * The Et dialog.
     */
    EditText etDialog;
    private RelativeLayout layoutCreateLicense;
    private Unbinder unbinder;
    private Button btnAddActions;
    private TextView tvCancel;
    /**
     * The Dialog.
     */
    Dialog dialog;
    private String strToken;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_licenses, container, false);
        unbinder = ButterKnife.bind(this, view);
        rvEditLicenses = view.findViewById(R.id.rv_editLicenses);
        btnAddActions = view.findViewById(R.id.btnRegister);
        tvCancel = view.findViewById(R.id.tvCancel);
        layoutCreateLicense = view.findViewById(R.id.tvCreateLicense);
        strToken = GeneralUtils.getAPIToken(getActivity());
        apiSetUp();


        btnAddActions.setOnClickListener(v -> {



            getActivity().onBackPressed();

        });

        tvCancel.setOnClickListener(v -> {
            GeneralUtils.connectFragment(getActivity(), new NewProducerFragment());

        });

        layoutCreateLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CreateCertificateFragment();
                Bundle args = new Bundle();
                args.putString("action", "Add Certificate");
                args.putString("title", "Create License");
                fragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("abc").commit();

            }
        });

        return view;
    }


    /**
     * Api set up.
     */
    public void apiSetUp() {

        rvEditLicenses.setLayoutManager(new LinearLayoutManager(getActivity()));
        editLicensesModelArrayList = new ArrayList<>();
        apicall();
        GeneralUtils.progressDialog(getActivity(), "");
        editLicenseAdapter = new EditLicenseAdapter(getActivity(), editLicensesModelArrayList);
        rvEditLicenses.setAdapter(editLicenseAdapter);

    }

    /**
     * Api call
     */
    private void apicall() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.GET_ALL_CERTIFICATES
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    GeneralUtils.progress.dismiss();
                    JSONArray jsonArr = new JSONArray(response);
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonObject = jsonArr.getJSONObject(i);
                        AllCertificateModel model = new AllCertificateModel();
                        String certificateId = jsonObject.getString("certificateId");
                        String certificateName = jsonObject.getString("certificateName");
                        model.setCertificateId(certificateId);
                        model.setCertificateName(certificateName);
                        editLicensesModelArrayList.add(model);
                    }
                    editLicenseAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    Extras.responseErrorDialog("Invalid Server Response!", getActivity());
                } catch (NullPointerException e) {
                    Extras.responseErrorDialog("Invalid Server Response!", getActivity());
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


    @Override
    public void getUserDefinedCertificate(List<String> userCertificates) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}
