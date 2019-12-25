package ch.uzh.csg.foodchain.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.uzh.csg.foodchain.Fragments.SettingFragments.GetAllActionFragment;
import ch.uzh.csg.foodchain.Models.ProcessActionDataModel;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.AlertUtils;
import ch.uzh.csg.foodchain.Utils.Configuration;
import ch.uzh.csg.foodchain.Utils.Extras;
import ch.uzh.csg.foodchain.Utils.GeneralUtils;

/**
 * The type All action adapter.
 */
public class AllActionAdapter extends RecyclerView.Adapter<AllActionAdapter.MyViewHolder> {
    /**
     * The Alert dialog.
     */
    android.support.v7.app.AlertDialog alertDialog;
    private String strActionId;
    private Dialog dialog;
    private EditText etDialog;
    private ArrayList<ProcessActionDataModel> editLicensesModelArrayList;
    private Context context;

    /**
     * Instantiates a new All action adapter.
     *
     * @param context                    the context
     * @param editLicensesModelArrayList the edit licenses model array list
     */
    public AllActionAdapter(Context context, ArrayList<ProcessActionDataModel> editLicensesModelArrayList) {
        this.context = context;
        this.editLicensesModelArrayList = editLicensesModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_action_layout, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ProcessActionDataModel editLicensesModel = editLicensesModelArrayList.get(position);
        holder.allAction.setText(editLicensesModel.getActionName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strActionId = editLicensesModel.getActionId();
             cameraBuilder(strActionId);
            }
        });

    }
    @Override
    public int getItemCount() {
        return editLicensesModelArrayList.size();
    }

    /**
     * The type My view holder.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        /**
         * The All action.
         */
        TextView allAction;
        /**
         * The Card view.
         */
        CardView cardView;
        /**
         * The Shared preferences.
         */
        SharedPreferences sharedPreferences;
        /**
         * The Editor.
         */
        SharedPreferences.Editor editor;

        /**
         * Instantiates a new My view holder.
         *
         * @param itemView the item view
         */
        public MyViewHolder(View itemView) {
            super(itemView);
            allAction = itemView.findViewById(R.id.allAction);
            cardView = itemView.findViewById(R.id.cvAction);
            sharedPreferences = context.getSharedPreferences(Configuration.MY_PREF,Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
    }

    /**
     * Api call update action.
     *
     * @param strToken        the str token
     * @param id              the id
     * @param strUpdateAction the str update action
     */
//apiCall updating actions
    public void apiCallUpdateAction(String strToken, String id, String strUpdateAction) {
        JSONObject js = new JSONObject();
        try {
            js.put("actionName",strUpdateAction);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT, Configuration.UPDATE_ACTION+id, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        alertDialog.dismiss();
                        try {
                            if (alertDialog != null)
                                alertDialog.dismiss();
                            String result = response.getString("message");
                            if(result.contains("action is successfully updated")){
                                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Fragment fragment = new GetAllActionFragment();
                                ((AppCompatActivity)context).getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack("").commit();

                            }
                        } catch (JSONException e) {
                            if (alertDialog != null)
                                alertDialog.dismiss();
                            Extras.responseErrorDialog("Invalid server Response!", context);

                        } catch (NullPointerException e){
                            if (alertDialog != null)
                                alertDialog.dismiss();
                            Extras.responseErrorDialog("Invalid server Response!", context);

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
                    Extras.responseErrorDialog("Can't get server!", context);
                } else {

                    errorMessage.replaceAll("\\{", "");
                    errorMessage.replaceAll("\\}", "");

                    Extras.responseErrorDialog(errorMessage, context);
                }
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+strToken);
                return headers;
            }

        };

        // Adding request to request queue
        Volley.newRequestQueue(context).add(jsonObjReq);

    }

    //api call delete actions
    private void apicallDelete(String strToken, String strActionID) {
        Log.d("action id",strActionID);
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, Configuration.UPDATE_ACTION+strActionID
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                alertDialog.dismiss();
                try {
                    if (alertDialog != null)
                        alertDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    String strRes = jsonObject.getString("message");
                    if (strRes.contains("certificate is successfully deleted")) {
                        Toast.makeText(context, strRes, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Fragment fragment = new GetAllActionFragment();
                        ((AppCompatActivity) context).getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("").commit();

                    }
                } catch (JSONException e) {
                    if (alertDialog != null)
                        alertDialog.dismiss();
                    Extras.responseErrorDialog("Invalid Server Response!", context);

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
                    Extras.responseErrorDialog("Can't get server!", context);
                } else {

                    errorMessage.replaceAll("\\{", "");
                    errorMessage.replaceAll("\\}", "");

                    Extras.responseErrorDialog(errorMessage, context);
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
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    /**
     * Camera builder.
     *
     * @param actionId the action id
     */
    public void cameraBuilder(String actionId) {
        String strActionID = actionId;
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(context);
        pictureDialog.setTitle("Open");
        String[] pictureDialogItems = {
                "\tUpdate",
                "\tDelete"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                               updateCertificate(strActionID);
                                break;
                            case 1:
                               deleteAction(strActionID);
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    /**
     * Update certificate.
     *
     * @param strActionID the str action id
     */
    public void updateCertificate(String strActionID){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_layout);
        etDialog = dialog.findViewById(R.id.etCertificateName);
        Button button = dialog.findViewById(R.id.btnCertificate);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        TextView tvName = dialog.findViewById(R.id.tvName);
        tvTitle.setText("Update Action");
        tvName.setText("Action Name");
        button.setText("Update");
        String strToken = GeneralUtils.getAPIToken(context);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strAction = etDialog.getText().toString();
                apiCallUpdateAction(strToken,strActionID,strAction);
                if (alertDialog == null)
                    alertDialog = AlertUtils.createProgressDialog((Activity) context);
                alertDialog.show();
            }
        });

        dialog.show();

    }

    /**
     * Delete action.
     *
     * @param strActionID the str action id
     */
    public void deleteAction(String strActionID){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.delete_confirmation_dialog_layout);
        TextView title = dialog.findViewById(R.id.tvDeleteTitle);
        Button delete = dialog.findViewById(R.id.btnDelete);
        Button cancel = dialog.findViewById(R.id.btnCancel);
        title.setText("Delete Action!");
        String strToken = GeneralUtils.getAPIToken(context);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apicallDelete(strToken,strActionID);
                if (alertDialog == null)
                    alertDialog = AlertUtils.createProgressDialog((Activity) context);
                alertDialog.show();
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
}

