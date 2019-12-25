package ch.uzh.csg.foodchain.Fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.uzh.csg.foodchain.Adapters.ProcessAndActionAdapter;
import ch.uzh.csg.foodchain.Fragments.ProducerFragments.NewProducerFragment;
import ch.uzh.csg.foodchain.Models.AllCertificateModel;
import ch.uzh.csg.foodchain.Models.ProcessActionDataModel;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.AlertUtils;
import ch.uzh.csg.foodchain.Utils.Configuration;
import ch.uzh.csg.foodchain.Utils.Extras;
import ch.uzh.csg.foodchain.Utils.GeneralUtils;
import ch.uzh.csg.foodchain.Utils.OnActionItemClickListener;
import ch.uzh.csg.foodchain.interfaces.CertificateAdditionCallback;


/**
 * The type Process action fragment.
 */
public class ProcessActionFragment extends Fragment implements OnActionItemClickListener, CertificateAdditionCallback {
    /**
     * The constant isActionItemSelected.
     */
    public static boolean isActionItemSelected = false;
    /**
     * The Alert dialog.
     */
    android.support.v7.app.AlertDialog alertDialog;
    private RecyclerView rvNewProductTag;
    private ArrayList<ProcessActionDataModel> processActionArrayList;
    private ProcessAndActionAdapter processAndActionAdapter;
    private Button btnProcessDone;
    private ImageView ivBack;
    private RelativeLayout layoutCreateAction;
    private Dialog dialog;
    private EditText etDialog;
    private String strToken;

    /**
     * The All selected actions.
     */
    List<ProcessActionDataModel> allSelectedActions = new ArrayList<>();

    /**
     * New instance fragment.
     *
     * @param allSelectedCertificates the all selected certificates
     * @return the fragment
     */
    public static Fragment newInstance(List<AllCertificateModel> allSelectedCertificates) {
        Fragment fragment = new NewProducerFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onItemClick(ProcessActionDataModel item) {
        boolean isItemRemoved = false;
        for (int i = 0; i < allSelectedActions.size(); i++) {
            if (allSelectedActions.get(i).getActionId().equals(item.getActionId())) {
                allSelectedActions.remove(i);
                isItemRemoved = true;

            }
        }
        if (!isItemRemoved) {
            allSelectedActions.add(item);
            isActionItemSelected = true;

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_product, container, false);
        rvNewProductTag = view.findViewById(R.id.rv_newProductTag);

        btnProcessDone = view.findViewById(R.id.btnProcessDone);
        ivBack = view.findViewById(R.id.ivProcessBack);
        layoutCreateAction = view.findViewById(R.id.tvCreateAction);
        strToken = GeneralUtils.getAPIToken(getActivity());
        apiSetup();

        layoutCreateAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CreateCertificateFragment();
                Bundle args = new Bundle();

                args.putString("action", "Add Action");
                args.putString("title", "Create Action");
                fragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("abc").commit();

            }
        });
        

        btnProcessDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new NewProducerFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("").commit();
            }
        });

        return view;
    }

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
        processAndActionAdapter = new ProcessAndActionAdapter(getActivity(), processActionArrayList, this::onItemClick);
        rvNewProductTag.setAdapter(processAndActionAdapter);
    }

    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.GET_ALL_ACTION
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                alertDialog.dismiss();

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


                    Extras.responseErrorDialog("Invalid Server Response!", getActivity());
                } catch (NullPointerException e){
                    if (alertDialog != null)
                        alertDialog.dismiss();
                    Extras.responseErrorDialog("Invalid Server Response!", getActivity());
                }

            }

        },  new Response.ErrorListener() {
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

    /**
     * Api call action.
     */
    public void apiCallAction() {

        String strAction = etDialog.getText().toString();

        JSONObject js = new JSONObject();
        try {
            js.put("actionName", strAction);
        } catch (JSONException e) {
            Extras.responseErrorDialog("Invalid Server Response!", getActivity());
        } catch (NullPointerException e){
            Extras.responseErrorDialog("Invalid Server Response!", getActivity());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, Configuration.GET_ALL_ACTION, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.getString("message");
                            if (result.contains("action is successfully created")) {
                                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }

                        } catch (JSONException e) {
                            if (alertDialog != null)
                                alertDialog.dismiss();
                            Extras.responseErrorDialog("Failed to connect to server!", getActivity());

                        } catch (NullPointerException e){
                            if (alertDialog != null)
                                alertDialog.dismiss();
                            Extras.responseErrorDialog("Failed to connect to server!", getActivity());

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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };
        // Adding request to request queue
        Volley.newRequestQueue(getActivity()).add(jsonObjReq);
    }


    @Override
    public void getUserDefinedCertificate(List<String> userCertificates) {

    }
}
