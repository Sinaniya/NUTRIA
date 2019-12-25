package ch.uzh.csg.foodchain.Fragments.ProducerFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ch.uzh.csg.foodchain.Fragments.CheckUserFragment;
import ch.uzh.csg.foodchain.Models.ProducerLoginModel.ProducerLogin;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.AlertUtils;
import ch.uzh.csg.foodchain.Utils.Configuration;
import ch.uzh.csg.foodchain.Utils.Extras;
import ch.uzh.csg.foodchain.Utils.GeneralUtils;
import ch.uzh.csg.foodchain.networking.APIClient;
import ch.uzh.csg.foodchain.networking.APIServices;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * The type Producer login fragment.
 */
public class ProducerLoginFragment extends Fragment {
    private AlertDialog alertDialog;
    private Button btnProducerSignIn;
    private TextView tvRegisterProducer;
    private LinearLayout linearBack;
    private EditText etUsername, etPassword;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String strUsername, strPassword;
    /**
     * The Tag.
     */
    String TAG="ProducerLoginFragment";
    private APIServices apiServices;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_producer_login, container, false);
        btnProducerSignIn = view.findViewById(R.id.btnProducerSignIn);
        tvRegisterProducer = view.findViewById(R.id.tvRegisterProducer);
        linearBack = view.findViewById(R.id.backLayout);
        etUsername = view.findViewById(R.id.userName);
        etPassword = view.findViewById(R.id.password);
        sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        apiServices = APIClient.getApiClient(getActivity()).create(APIServices.class);

        btnProducerSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataInput();
            }
        });

        tvRegisterProducer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.connectFragmentWithBackStack(getActivity(), new NewProducerFragment());

            }
        });

        linearBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CheckUserFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("").commit();
            }
        });


        return view;
    }

    /**
     * Data input.
     */
    public void dataInput() {
        strUsername = etUsername.getText().toString().trim();
        strPassword = etPassword.getText().toString().trim();

        if (strUsername.equals("")) {
            etUsername.setError("please enter your username");
        } else if (strPassword.equals("")) {
            etPassword.setError("please enter your password");
        } else {
            if (alertDialog == null)
                alertDialog = AlertUtils.createProgressDialog(getActivity());
            alertDialog.show();
         //   apiCall();
            loginApiCall();
        }
    }


    /**
     * login Producer Api call retrofit
     */
    private void loginApiCall() {

        Call<ProducerLogin> loginCall = apiServices.loginProducer(strUsername, strPassword);

        loginCall.enqueue(new Callback<ProducerLogin>() {
            @Override
            public void onResponse(Call<ProducerLogin> call, retrofit2.Response<ProducerLogin> response) {
                
                ProducerLogin producerLogin = response.body();



                    if(response.isSuccessful()) {
                        try {

                            if (producerLogin.getHttpStatus() == 200) {

                                if (alertDialog != null)
                                    alertDialog.dismiss();

                                String message = producerLogin.getMessage();
                                String token = producerLogin.getToken();
                                Log.d("APITOKEN:",token);
                                String producerId = producerLogin.getResourceId().toString();

                                GeneralUtils.putStringValueInEditor(getActivity(), "api_token", token).commit();
                                GeneralUtils.putStringValueInEditor(getActivity(), "producer_id", producerId).commit();

                                if (message.equals("producer is successfully logged in")) {
                                    GeneralUtils.connectFragmentWithBackStack(getActivity(), new GenerateQRCodeFragment());
                                    etUsername.setText("");
                                    etPassword.setText("");
                                    GeneralUtils.putBooleanValueInEditor(getActivity(), "loggedIn", true).commit();
                                } else {
                                    Toast.makeText(getActivity(), "you got some error!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        } catch (NullPointerException ex) {
                            if (alertDialog != null)
                                alertDialog.dismiss();
                            Extras.responseErrorDialog("Invalid Server Response!", getActivity());
                        }
                    } else {
                        if (alertDialog != null)
                            alertDialog.dismiss();
                        Extras.responseErrorDialog("Invalid Credential's!", getActivity());
                    }

            }

            @Override
            public void onFailure(Call<ProducerLogin> call, Throwable t) {
                if (alertDialog != null)
                    alertDialog.dismiss();
                Extras.responseErrorDialog(t.getMessage(), getActivity());
            }
        });
    }


}
