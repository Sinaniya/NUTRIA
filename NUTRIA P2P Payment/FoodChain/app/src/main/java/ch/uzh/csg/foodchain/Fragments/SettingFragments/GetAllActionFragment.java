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

import ch.uzh.csg.foodchain.Adapters.AllActionAdapter;
import ch.uzh.csg.foodchain.Models.ProcessActionDataModel;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.AlertUtils;
import ch.uzh.csg.foodchain.Utils.Configuration;
import ch.uzh.csg.foodchain.Utils.GeneralUtils;


/**
 * The type Get all action fragment.
 */
public class GetAllActionFragment extends Fragment {
    /**
     * The Alert dialog.
     */
    android.support.v7.app.AlertDialog alertDialog;
    private ArrayList<ProcessActionDataModel> processActionArrayList;
    private AllActionAdapter processAndActionAdapter;
    private RecyclerView rvAllActions;
    private String strToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_get_all_action, container, false);
        rvAllActions = view.findViewById(R.id.rv_getAllAction);
        strToken = GeneralUtils.getAPIToken(getActivity());
        apiSetUp();
        return view;
    }

    /**
     * Api set up.
     */
    public void apiSetUp(){
        rvAllActions.setLayoutManager(new LinearLayoutManager(getActivity()));
        processActionArrayList = new ArrayList<>();
        apicall();
        if (alertDialog == null)
            alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        processAndActionAdapter = new AllActionAdapter(getActivity(), processActionArrayList);
        rvAllActions.setAdapter(processAndActionAdapter);

    }


    /**
     * API call getting all certificates
     */
    private void apicall() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.GET_ALL_ACTION
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
                        ProcessActionDataModel model = new ProcessActionDataModel();
                        String actionId = jsonObject.getString("actionId");
                        String actionName = jsonObject.getString("actionName");

                        model.setActionId(actionId);
                        model.setActionName(actionName);

                        processActionArrayList.add(model);
                    }
                    processAndActionAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (alertDialog != null)
                        alertDialog.dismiss();
                }

            }

        },  new Response.ErrorListener() {
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
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

    }
    //end
}
