package ch.uzh.csg.foodchain.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import ch.uzh.csg.foodchain.R;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * The type Alert utils.
 */
public class AlertUtils {

    /**
     * Create progress dialog alert dialog.
     *
     * @param activity the activity
     * @return the alert dialog
     */
    public static AlertDialog createProgressDialog(Activity activity) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.progress_dialog
                , null);

        dialogBuilder.setView(dialogView);
        ProgressBar pd = dialogView.findViewById(R.id.indeterminateBar);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialoge_box);
        alertDialog.getWindow().setAttributes(lp);
        pd.setVisibility(View.VISIBLE);
        return alertDialog;

    }

    /**
     * Show material dialog material dialog.
     *
     * @param activity the activity
     * @return the material dialog
     */
    public static MaterialDialog showMaterialDialog(Activity activity) {
        MaterialDialog materialDialog = new MaterialDialog.Builder(activity)
                .title(R.string.mapbox)
                .content("please wait")
                .progress(true, 0)
                .show();

        return materialDialog;
    }

    /**
     * Build alert message no gps.
     *
     * @param context the context
     */
    public static void buildAlertMessageNoGps(final Context context){
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setMessage("Enable GPS to use this application")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                      //  Toast.makeText()
                    }
                });
        final android.app.AlertDialog alert = builder.create();
        alert.show();
    }

//    public static SweetAlertDialog sweatProgressBar(Activity activity) {
//        SweetAlertDialog pDialog = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading");
//        pDialog.setCancelable(false);
//        pDialog.show();
//
//        return sweatProgressBar;
//    }


}
