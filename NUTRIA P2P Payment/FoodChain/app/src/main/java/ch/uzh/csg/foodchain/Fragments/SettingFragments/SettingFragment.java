package ch.uzh.csg.foodchain.Fragments.SettingFragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ch.uzh.csg.foodchain.Activities.PrinterSettingsActivity;
import ch.uzh.csg.foodchain.Activities.MainActivity;
import ch.uzh.csg.foodchain.Fragments.ConsumerFragments.QRScaningFragment;
import ch.uzh.csg.foodchain.Fragments.ProducerFragments.GenerateQRCodeFragment;
import ch.uzh.csg.foodchain.Fragments.ProducerFragments.ProducerFragment;
import ch.uzh.csg.foodchain.Fragments.ProducerFragments.WalletFragment;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.AlertUtils;
import ch.uzh.csg.foodchain.Utils.Configuration;
import ch.uzh.csg.foodchain.Utils.Extras;
import ch.uzh.csg.foodchain.Utils.GeneralUtils;


/**
 * The type Setting fragment.
 */
public class SettingFragment extends Fragment {
    /**
     * The Alert dialog.
     */
    android.support.v7.app.AlertDialog alertDialog;
    private TextView tvAllActions, tvAllCertificates, tvSettingBack, tvNewAction, tvNewCertificate ,tvprinter;
    private BottomNavigationView bottomNavigationView;
    private TextView tvLogout;
    private String strToken;
    private Dialog dialog;
    private EditText etDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        tvAllActions = view.findViewById(R.id.allActions);
        tvAllCertificates = view.findViewById(R.id.allCertificates);
        tvNewAction = view.findViewById(R.id.createNewAction);
        tvNewCertificate = view.findViewById(R.id.createNewCertificate);
        tvLogout = view.findViewById(R.id.tvLogout);
        tvSettingBack = view.findViewById(R.id.settingBack);
        tvprinter = view.findViewById(R.id.print);

        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottomBarSetting);
        bottomNavigationView.getMenu().getItem(4).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        strToken = GeneralUtils.getAPIToken(getActivity());
        tvprinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrinterSettingsActivity.class);
                startActivity(intent);
            }
        });
        tvAllActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new GetAllActionFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("").commit();
            }
        });

        tvAllCertificates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new GetAllCertificatesFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("").commit();
            }
        });

        tvSettingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProducerFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("").commit();
            }
        });

        tvNewCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_layout);
                etDialog = dialog.findViewById(R.id.etCertificateName);
                Button button = dialog.findViewById(R.id.btnCertificate);
                TextView tvTitle = dialog.findViewById(R.id.tvTitle);
                TextView tvName = dialog.findViewById(R.id.tvName);
                tvTitle.setText(getString(R.string.create_new_certificate_text));
                tvName.setText(getString(R.string.certificate_name));
                button.setText(getString(R.string.create_certificate_settings));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strCeriticate = etDialog.getText().toString();
                        if(strCeriticate.equals(null) ||strCeriticate.isEmpty()){
                            dialog.cancel();
                            return;
                        }

                        apiCallCertificate(strCeriticate);
                        if (alertDialog == null)
                            alertDialog = AlertUtils.createProgressDialog((Activity) getActivity());
                        alertDialog.show();
                    }
                });
                dialog.show();
            }
        });

        tvNewAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_layout);
                etDialog = dialog.findViewById(R.id.etCertificateName);
                Button button = dialog.findViewById(R.id.btnCertificate);
                TextView tvTitle = dialog.findViewById(R.id.tvTitle);
                TextView tvName = dialog.findViewById(R.id.tvName);
                tvTitle.setText(getString(R.string.create_new_action_text));
                tvName.setText(getString(R.string.action_name));
                button.setText(getString(R.string.create_action_settings));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strAction = etDialog.getText().toString();
                        if(strAction.equals(null) || strAction.isEmpty()){
                            dialog.cancel();
                            return;
                        }

                        apiCallAction(strAction);
                        if (alertDialog == null)
                            alertDialog = AlertUtils.createProgressDialog((Activity) getActivity());
                        alertDialog.show();
                    }
                });
                dialog.show();
            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeneralUtils.putBooleanValueInEditor(getActivity(), "loggedIn", false).commit();
                GeneralUtils.removeStringValueInEditor(getActivity(), "producer_id").commit();
                GeneralUtils.removeStringValueInEditor(getActivity(), "api_token").commit();
                GeneralUtils.putBooleanValueInEditor(getActivity(), "loggedIn", false).commit();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });
        return view;
    }

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


    /**
     * Api call action.
     * creating new Actions
     * adding actions
     * @param strAction the str action
     *
     */
    public void apiCallAction(String strAction) {

        JSONObject js = new JSONObject();

        try {
            js.put("actionName", strAction);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, Configuration.GET_ALL_ACTION, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        alertDialog.dismiss();
                        try {
                            String result = response.getString("message");
                            if (result.contains("action is successfully created")) {
                                dialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alertDialog.dismiss();
                Extras.responseErrorDialog(error.getCause().getMessage(),getActivity());
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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(jsonObjReq);
    }

    /**
     * creating new Certificates
     * Api call certificate.
     * adding certificates
     * @param strCeriticate the str ceriticate
     */
    public void apiCallCertificate(String strCeriticate) {
        JSONObject js = new JSONObject();

        try {
            js.put("certificateName", strCeriticate);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, Configuration.GET_ALL_CERTIFICATES, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            alertDialog.dismiss();
                            String result = response.getString("message");
                            if (result.contains("certificate is successfully created")) {
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alertDialog.dismiss();
                Extras.responseErrorDialog(error.getCause().getMessage(),getActivity());
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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(jsonObjReq);
    }

}
