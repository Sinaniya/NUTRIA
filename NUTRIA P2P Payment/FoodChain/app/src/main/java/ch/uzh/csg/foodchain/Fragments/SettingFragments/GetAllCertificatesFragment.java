package ch.uzh.csg.foodchain.Fragments.SettingFragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

import ch.uzh.csg.foodchain.Adapters.AllCertificateAdapter;
import ch.uzh.csg.foodchain.Models.AllCertificateModel;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.AlertUtils;
import ch.uzh.csg.foodchain.Utils.Configuration;
import ch.uzh.csg.foodchain.Utils.GeneralUtils;


/**
 * The type Get all certificates fragment.
 */
public class GetAllCertificatesFragment extends Fragment {
    /**
     * The Alert dialog.
     */
    android.support.v7.app.AlertDialog alertDialog;
    // @BindView(R.id.rv_editLicenses)
    private RecyclerView rvAllCertificates;
    private ArrayList<AllCertificateModel> editLicensesModelArrayList;
    private AllCertificateAdapter editLicenseAdapter;
    private String strToken;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_get_all_certificate, container, false);
        rvAllCertificates = view.findViewById(R.id.rv_getAllCertificates);
        strToken = GeneralUtils.getAPIToken(getActivity());
        apiSetUp();
        return view;
    }

    /**
     * Api set up.
     */
    public void apiSetUp(){
        rvAllCertificates.setLayoutManager(new LinearLayoutManager(getActivity()));
        editLicensesModelArrayList = new ArrayList<>();
        apicall();
        if (alertDialog == null)
            alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        editLicenseAdapter = new AllCertificateAdapter(getActivity(), editLicensesModelArrayList);
        rvAllCertificates.setAdapter(editLicenseAdapter);

    }


    /**
     * API call getting all certificates
     */
    private void apicall() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.GET_ALL_CERTIFICATES
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                alertDialog.dismiss();
                Log.d("response",response);
                try {
                    if (alertDialog != null)
                        alertDialog.dismiss();
                    JSONArray jsonArr = new JSONArray(response);
                    for(int i=0;i<jsonArr.length();i++){
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
                    e.printStackTrace();
                    if (alertDialog != null)
                        alertDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (alertDialog != null)
                    alertDialog.dismiss();
            }
        })
        {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+strToken);
                return headers;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue( getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

}
