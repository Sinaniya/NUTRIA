package ch.uzh.csg.foodchain.Fragments.ProducerFragments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ch.uzh.csg.foodchain.Fragments.EditLicensesFragment;
import ch.uzh.csg.foodchain.Fragments.ProcessActionFragment;
import ch.uzh.csg.foodchain.Models.AllCertificateModel;
import ch.uzh.csg.foodchain.Models.ProcessActionDataModel;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.Configuration;
import ch.uzh.csg.foodchain.Utils.Extras;
import ch.uzh.csg.foodchain.Utils.GeneralUtils;
import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * The type New producer fragment.
 */
public class NewProducerFragment extends Fragment {

    // private DatabaseHelper databaseHelper;
    private ImageView ivLicencesEnterList, ivProcessActionList;
    private EditText etProducerName, etProducerCertificate, etProducerWebsite, etEthereumAccount, etProducerUsername, etProducerPassword;
    private String strProducerName, strProducerCertficate, strProducerUsername, strProducerPassword, strProducerAccount, strProducerUrl;
    private RelativeLayout layoutLicenseList, layoutActionList;
    private TextView tvCertificateList, tvProducer, tvActionsList;
    private Button btnRegister;
    private ImageView ivBack;
    private Unbinder unbinder;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    /**
     * The Dialog.
     */
    Dialog dialog;
    /**
     * The Certificates.
     */
    public static HashMap<String, String> certificates, /**
     * The Actions.
     */
    actions;
    /**
     * The constant newActions.
     */
    public static ArrayList newActions = new ArrayList();
    /**
     * The constant newCertificate.
     */
    public static ArrayList newCertificate = new ArrayList();
    //DatabaseHelper db;

    /**
     * The Str token.
     */
    String strToken;

    /**
     * The Database.
     */
    SQLiteDatabase database;

    /**
     * New instance fragment.
     *
     * @param allSelectedCertificates the all selected certificates
     * @param allSelectedActions      the all selected actions
     * @return the fragment
     */
    public static Fragment newInstance(List<AllCertificateModel> allSelectedCertificates,
                                       List<ProcessActionDataModel> allSelectedActions) {

        Fragment fragment = new NewProducerFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        certificates = new HashMap<>();
        actions = new HashMap<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_producer, container, false);
        unbinder = ButterKnife.bind(this, view);
        ivLicencesEnterList = view.findViewById(R.id.ivLicencesEnterList);
        ivProcessActionList = view.findViewById(R.id.ivProcessEnterlist);
        etProducerName = view.findViewById(R.id.etProducername);
        etProducerCertificate = view.findViewById(R.id.etProducerCertificate);
        etProducerWebsite = view.findViewById(R.id.etWebsite);
        etEthereumAccount = view.findViewById(R.id.etEthereumAccount);
        etProducerUsername = view.findViewById(R.id.etProducerUsername);
        etProducerPassword = view.findViewById(R.id.etProducerPasswd);
        btnRegister = view.findViewById(R.id.btnRegister);
        ivBack = view.findViewById(R.id.back);
        tvCertificateList = view.findViewById(R.id.tvCertificateList);
        tvActionsList = view.findViewById(R.id.tvActionsList);
        tvProducer = view.findViewById(R.id.tvProducer);
        layoutLicenseList = view.findViewById(R.id.layoutLicencesList);
        layoutActionList = view.findViewById(R.id.layoutActionlist);


        Log.d("newActions:",newActions.toString());
        Log.d("newCertificates:",newCertificate.toString());

        for(int i = 0; i < newCertificate.size(); i++ ) {
            certificates.put("-1",newCertificate.get(i).toString());
        }

        for(int i = 0; i < newActions.size(); i++ ) {
            actions.put("-1",newActions.get(i).toString());
        }

        sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String strName = sharedPreferences.getString("name", "");
        String strNumber = sharedPreferences.getString("licence_number", "");
        String strUserName = sharedPreferences.getString("username", "");
        String strPassword = sharedPreferences.getString("password", "");
        String strAccount = sharedPreferences.getString("ethereumAccount", "");
        String strUrl = sharedPreferences.getString("url", "");

        etProducerName.setText(strName);
        etProducerCertificate.setText(strNumber);
        etProducerUsername.setText(strUserName);
        etProducerPassword.setText(strPassword);
        etEthereumAccount.setText(strAccount);
        etProducerWebsite.setText(strUrl);

        strToken = GeneralUtils.getAPIToken(getActivity());

        Iterator myVeryOwnIterator = certificates.keySet().iterator();

        while (myVeryOwnIterator.hasNext()) {

            String key = (String) myVeryOwnIterator.next();
            String value = (String) certificates.get(key);
            StringBuilder builder = new StringBuilder();

            int counter=0;

            for (String name : certificates.values()) {
                if(counter==0){
                    builder.append(name);
                }
                else {
                    builder.append(", "+ name );
                }
                counter++;
                if(!(builder == null || builder.toString().equals("")))
                    tvCertificateList.setText(builder);
            }

        }

        Iterator myIterator = actions.keySet().iterator();

        while (myIterator.hasNext()) {

            String key = (String) myIterator.next();
            String value = (String) actions.get(key);
            StringBuilder builder = new StringBuilder();
            int counter=0;

            for (String name : actions.values()) {
                if(counter==0){
                    builder.append(name);
                }
                else {
                    builder.append(", "+ name );
                }
                counter++;
                if(!(builder == null || builder.toString().equals("")))
                    tvActionsList.setText(builder);
            }

        }


        layoutLicenseList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralUtils.connectFragmentWithBackStack(getActivity(), new EditLicensesFragment());
                editor.putString("name", etProducerName.getText().toString()).commit();
                editor.putString("licence_number", etProducerCertificate.getText().toString()).commit();
                editor.putString("username", etProducerUsername.getText().toString()).commit();
                editor.putString("password", etProducerPassword.getText().toString()).commit();
                editor.putString("ethereumAccount", etEthereumAccount.getText().toString()).commit();
                editor.putString("url", etProducerWebsite.getText().toString()).commit();

            }
        });

        layoutActionList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("name", etProducerName.getText().toString()).commit();
                editor.putString("licence_number", etProducerCertificate.getText().toString()).commit();
                editor.putString("username", etProducerUsername.getText().toString()).commit();
                editor.putString("password", etProducerPassword.getText().toString()).commit();
                editor.putString("ethereumAccount", etEthereumAccount.getText().toString()).commit();
                editor.putString("url", etProducerWebsite.getText().toString()).commit();


                GeneralUtils.connectFragmentWithBackStack(getActivity(), new ProcessActionFragment());

            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProducerLoginFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });

        tvProducer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProducerFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("abc").commit();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataInput();
            }
        });
        return view;
    }


    /**
     * Data input.
     * get input data from registration form
     */
    public void dataInput() {

        strProducerName = etProducerName.getText().toString().trim();
        strProducerCertficate = etProducerCertificate.getText().toString();
        strProducerUsername = etProducerUsername.getText().toString();
        strProducerPassword = etProducerPassword.getText().toString();
        strProducerAccount = etEthereumAccount.getText().toString();
        strProducerUrl = etProducerWebsite.getText().toString();

        if (strProducerName.equals("")) {
            etProducerName.setError(getString(R.string.producer_name_error));
        } else if (strProducerCertficate.equals("")) {
            etProducerCertificate.setError(getString(R.string.producer_certificate_error));
        } else if (strProducerUsername.equals("")) {
            etProducerUsername.setError(getString(R.string.producer_username_error));
        } else if (strProducerPassword.equals("")) {
            etProducerPassword.setError(getString(R.string.producer_password_error));
        } else if (strProducerAccount.equals("")) {
            etEthereumAccount.setError(getString(R.string.producer_ethereum_error));
        } else if (strProducerUrl.equals("")) {
            etProducerWebsite.setError(getString(R.string.producer_url_error));
        } else {
            GeneralUtils.progressDialog(getActivity(), "");
            apiCall();

        }


    }

    /**
     * Api call.
     */
    public void apiCall() {

        JSONObject producerJsonObject = new JSONObject();
        try {
            producerJsonObject.put("producerName", strProducerName);
            producerJsonObject.put("licenceNumber", strProducerCertficate);
            producerJsonObject.put("username", strProducerUsername);
            producerJsonObject.put("password", strProducerPassword);
            producerJsonObject.put("ethereumAccount", strProducerAccount);
            producerJsonObject.put("url", strProducerUrl);


            //add certificates to jsonArray
            JSONArray jsonCertificatesArray = new JSONArray();

            for (Map.Entry<String, String> pair : certificates.entrySet()) {
                if (pair.getKey().contains("-1")) {

                    JSONObject jsonCertificateName = new JSONObject();
                    jsonCertificateName.put("certificateName", pair.getValue());
                    Log.d("actionName", pair.getValue());
                    jsonCertificatesArray.put(jsonCertificateName);
                } else {

                    JSONObject jsonCertificateId = new JSONObject();
                    jsonCertificateId.put("certificateId", pair.getKey());
                    Log.d("certificateId", pair.getKey());
                    jsonCertificatesArray.put(jsonCertificateId);
                }

            }


            //add actions to jsonArray
            JSONArray JsonActionsArray = new JSONArray();

            for (Map.Entry<String, String> pair : actions.entrySet()) {
                if (pair.getKey().contains("-1")) {

                    JSONObject jsonActionName = new JSONObject();
                    jsonActionName.put("actionName", pair.getValue());
                    Log.d("actionName", pair.getValue());
                    JsonActionsArray.put(jsonActionName);

                } else {

                    JSONObject jsonActionID = new JSONObject();
                    jsonActionID.put("actionId", pair.getKey());
                    Log.d("actionId", pair.getKey());
                    JsonActionsArray.put(jsonActionID);
                }
            }

            producerJsonObject.put("producerCertificates", jsonCertificatesArray);
            producerJsonObject.put("producerActions", JsonActionsArray);

        } catch (JSONException e) {

            Extras.responseErrorDialog(getString(R.string.invalid_response), getActivity());

        } catch (NullPointerException e) {

            Extras.responseErrorDialog(getString(R.string.invalid_response), getActivity());

        }

        Log.d("registrationString: ", producerJsonObject.toString());
        //Api calling using volley

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, Configuration.REGISTER_PRODUCER, producerJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        GeneralUtils.progress.dismiss();

                        //clearing sharepreferences
                        editor.putString("name", "").commit();
                        editor.putString("licence_number", "").commit();
                        editor.putString("username", "").commit();
                        editor.putString("password", "").commit();
                        editor.putString("ethereumAccount", "").commit();
                        editor.putString("url", "").commit();
                        etProducerName.setText("");
                        etProducerCertificate.setText("");
                        etProducerUsername.setText("");
                        etProducerPassword.setText("");
                        etEthereumAccount.setText("");
                        etProducerWebsite.setText("");

                        //end
                        try {

                            String result = response.getString("message");
                            String token = response.getString("token");
                            GeneralUtils.putStringValueInEditor(getActivity(), "api_token", token).commit();

                            if (result.contains("producer is successfully created")) {
                                EditLicensesFragment.isCertificatedItemSelected = false;
                                ProcessActionFragment.isActionItemSelected = false;
                                GeneralUtils.connectFragment(getActivity(), new ProducerLoginFragment());

                                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success")
                                        .setContentText(getString(R.string.producer_created))
                                        .show();
                                //deleteAll();


                            } else if (result.contains("Error while creating producer")) {
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...!")
                                        .setContentText(getString(R.string.producer_creation_error))
                                        .show();
                            } else {
                                // Toast.makeText(getActivity(), "please try again", Toast.LENGTH_SHORT).show();

                                Extras.responseErrorDialog("Please Try Again!",getActivity());
                            }


                        } catch (JSONException e) {

                            Extras.responseErrorDialog(getString(R.string.invalid_response), getActivity());

                        } catch (NullPointerException e) {

                            Extras.responseErrorDialog(getString(R.string.invalid_response), getActivity());

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
                    Extras.responseErrorDialog("No response From server!", getActivity());
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


}

