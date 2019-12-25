package ch.uzh.csg.foodchain.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * The type Extras.
 */
public class Extras {

    /**
     * The constant userType.
     */
    public static String userType = "";
    /**
     * The constant scan_type.
     */
    public static String scan_type = "";
    /**
     * The constant hash_value_for_scan.
     */
    public static String hash_value_for_scan = "";
    /**
     * The constant qr_hashes_multi.
     */
    public static List<String> qr_hashes_multi = new ArrayList<>();

    /**
     * Create drawable from view bitmap.
     *
     * @param context the context
     * @param view    the view
     * @return the bitmap
     */
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    /**
     * Response error dialog.
     *
     * @param errorMessage the error message
     * @param context      the context
     */
    public static void responseErrorDialog(String errorMessage, Context context) {
        if(GeneralUtils.progress != null)
        GeneralUtils.progress.dismiss();

        try {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Error:")
                    .setContentText(errorMessage)
                    .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    })

                    .show();
        } catch (NullPointerException e) {

        }
    }
}
