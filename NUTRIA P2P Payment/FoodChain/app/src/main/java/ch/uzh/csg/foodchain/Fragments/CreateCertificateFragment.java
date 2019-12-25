package ch.uzh.csg.foodchain.Fragments;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.uzh.csg.foodchain.Fragments.ProducerFragments.NewProducerFragment;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.Configuration;
import ch.uzh.csg.foodchain.Utils.Extras;
import ch.uzh.csg.foodchain.Utils.GeneralUtils;

import static ch.uzh.csg.foodchain.Fragments.ProducerFragments.NewProducerFragment.newActions;
import static ch.uzh.csg.foodchain.Fragments.ProducerFragments.NewProducerFragment.newCertificate;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateCertificateFragment extends Fragment implements View.OnClickListener {
    /**
     * The constant strFinalData.
     */
    public static String strFinalData;
    private ImageView ivAddEditText;
    private EditText etInitialEditText;
    private LinearLayout llAddEditText;
    private FrameLayout flAddEditText;
    private EditText editText;
    private TextView tvTitle;
    private ArrayList<ImageView> ImageViewList = new ArrayList();
    private ArrayList<EditText> editTextList = new ArrayList();
    private List<String> userCertificateList = new ArrayList<>();
    //private DatabaseHelper databaseHelper;
    private String strToken;
    private static final String TAG = "CreateCertificateFrag";
    /**
     * The Btn create.
     */
    Button btnCreate;
    private View view;

    /**
     * The Action count.
     */
    int action_count= 1;
    /**
     * The Certificate count.
     */
    int certificate_count= 1;

    /**
     * Instantiates a new Create certificate fragment.
     */
    public CreateCertificateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_create_certificate, container, false);
        ivAddEditText = view.findViewById(R.id.btn_add_ingredient);
        btnCreate = view.findViewById(R.id.btnCertificate);
        btnCreate.setOnClickListener(this);
        tvTitle = view.findViewById(R.id.tvTitle);
        etInitialEditText = view.findViewById(R.id.et_add_ing_1);
        llAddEditText = view.findViewById(R.id.ll_add_ingredients);
        etInitialEditText.setHint(getArguments().getString("action"));
        tvTitle.setText(getArguments().getString("title"));
        flAddEditText = view.findViewById(R.id.fl_add_ingredient);
        editTextList.add(etInitialEditText);
        ImageViewList.add(ivAddEditText);
        ivAddEditText.setOnClickListener(this);
        strToken = GeneralUtils.getAPIToken(getActivity());

        etInitialEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < 1) {
                    ivAddEditText.setVisibility(View.INVISIBLE);
                } else {
                    ivAddEditText.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_add_ingredient:
                ivAddEditText.setVisibility(View.INVISIBLE);
                addEditTextForIngredient();
                break;

            case R.id.btnCertificate:
                takeDataFromFields();


                if (getArguments().getString("action", null).equals("Add Certificate")) {
                    //  databaseHelper = new DatabaseHelper(getActivity());
                    for (String certificate : userCertificateList) {

                        String certificate_id = "-1";
                        String certificate_name = certificate;

                        NewProducerFragment.certificates.put(certificate_id+""+certificate_count ,certificate);

                        apiCallCertificate();
                        certificate_count++;
                    }
                } else if (getArguments().getString("action", null).equals("Add Action")) {
                    //databaseHelper = new DatabaseHelper(getActivity());
                    for (String actions : userCertificateList) {

                        String action_id = "-1";
                        String action_name = actions;
                        Log.d("Counttttttttt:",action_count+"");
                        NewProducerFragment.actions.put(action_id+""+action_count,actions);

                        apiCallAction();
                        action_count++;
                    }
                }
                getActivity().onBackPressed();
                break;

        }

    }

    private void addCertificate(String certificate) {
        NewProducerFragment.certificates.put("-1", certificate);

        Log.d(TAG, "userCertificateListCertificate: "+ NewProducerFragment.certificates + " : " );
    }

    /**
     * Take data from fields.
     */
    public void takeDataFromFields() {
        strFinalData = "";
        int counter=0;
        for (EditText etIngred : editTextList) {
            if(counter==0){
                strFinalData += etIngred.getText().toString();
            }
            else {
                strFinalData = "," + etIngred.getText().toString();
            }
            Log.d(TAG, "takeDataFromFields: "+ strFinalData );
            counter++;
            userCertificateList.add(etIngred.getText().toString());

            Log.d("zma data", strFinalData);
            Log.d("zma data",userCertificateList.toString());


        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void addEditTextForIngredient() {
        ImageViewList.get(ImageViewList.size() - 1).setVisibility(View.INVISIBLE);
        final FrameLayout frameLayout = new FrameLayout(getActivity());
        frameLayout.setLayoutParams(flAddEditText.getLayoutParams());
        frameLayout.setTag(editTextList.size());
        editText = new EditText(getActivity());
        editText.setLayoutParams(etInitialEditText.getLayoutParams());
        editText.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edittext_bg));
        editText.setHint(getArguments().getString("action"));
        editText.setPadding(etInitialEditText.getPaddingLeft(), 0, 0, 0);
        frameLayout.addView(editText);
        final ImageView imageView = new ImageView(getActivity());
        imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.cross));
        imageView.setLayoutParams(ivAddEditText.getLayoutParams());
        frameLayout.addView(imageView);
        imageView.setTag(0);
        imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.cross));
        ImageViewList.add(imageView);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < 1) {
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.cross));
                    imageView.setTag(0);
                } else {
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.add));
                    imageView.setTag(1);
                }

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((int) (imageView.getTag()) == 1) {
                    addEditTextForIngredient();
                } else {
                    llAddEditText.removeView(frameLayout);
                    editTextList.remove((int) (frameLayout.getTag()));
                    ImageViewList.remove((int) (frameLayout.getTag()));
                    ImageViewList.get(ImageViewList.size() - 1).setVisibility(View.VISIBLE);

                }
            }
        });
        llAddEditText.addView(frameLayout);
        editTextList.add(editText);

    }

    /**
     * Api call certificate.
     */
//adding certificates
    public void apiCallCertificate() {
        String[] str = strFinalData.split(",");
        if(str!=null && str.length>0){
            return;
        }
        String str1 = str[0];
        JSONObject js = new JSONObject();

        for(int i=0;i<str.length;i++){
            Log.d("zma certificate",str[i]);
            try {
                js.put("certificateName", str[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, Configuration.GET_ALL_CERTIFICATES, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.getString("message");
                            if (result.contains("certificate is successfully created")) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Extras.responseErrorDialog("Inalid Json Response:",getActivity());
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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(jsonObjReq);
    }

    /**
     * Api call action.
     */
//adding action
    public void apiCallAction() {

        String[] str = strFinalData.split(",");
        if(str!=null && str.length>0){
            return;
        }
        String str1 = str[0];
        JSONObject js = new JSONObject();

        for(int i=0;i<str.length;i++){
            Log.d("zma actions",str[i]);
            try {
                js.put("actionName", str[i]);
            } catch (JSONException e) {
                Extras.responseErrorDialog("Invalid Server Response!",getActivity());
            } catch (NullPointerException e) {
                Extras.responseErrorDialog("Invalid Server Response!",getActivity());
            }
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, Configuration.GET_ALL_ACTION, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.getString("message");
                            if (result.contains("action is successfully created")) {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(jsonObjReq);
    }

}