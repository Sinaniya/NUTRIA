package ch.uzh.csg.foodchain.Utils;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import ch.uzh.csg.foodchain.Fragments.ConsumerFragments.QRScaningFragment;
import ch.uzh.csg.foodchain.R;

/**
 * The type General utils.
 */
public class GeneralUtils {
    /**
     * The constant sharedPreferences.
     */
    public static SharedPreferences sharedPreferences;
    /**
     * The constant editor.
     */
    public static SharedPreferences.Editor editor;
    /**
     * The constant progress.
     */
    public static ACProgressFlower progress;
    private static final String TAG = "GeneralUtils";


    /**
     * Connect fragment.
     *
     * @param context  the context
     * @param fragment the fragment
     */
    public static void connectFragment(Context context, Fragment fragment) {
        ((AppCompatActivity) context).getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    /**
     * Connect fragment with back stack.
     *
     * @param context  the context
     * @param fragment the fragment
     */
//    public static Fragment connectFragmentWithBackStack(Context context, Fragment fragment) {
//        ((AppCompatActivity) context).getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("true").commit();
//        return fragment;
//    }
public static void connectFragmentWithBackStack(Context context, Fragment fragment) {

    ((AppCompatActivity) context).getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("scanning").commit(); //addToBackStack(null) any string or null can be provided. If you provide a string, any specific can be popped up using the string identifier.
    FragmentManager fragmentManager=((AppCompatActivity) context).getFragmentManager();
    final int backStackEntryCount = fragmentManager.getBackStackEntryCount();
    Log.d(TAG, "newBackStackLength : " + backStackEntryCount);
    fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            int currentBackStackCount = fragmentManager.getBackStackEntryCount();
            if (backStackEntryCount!= currentBackStackCount) {
                // we don't really care if going back or forward. we already performed the logic here.
                Log.d(TAG, "onBackStackChanged: "+ currentBackStackCount);
                fragmentManager.removeOnBackStackChangedListener(this);

                if ( backStackEntryCount> currentBackStackCount ) { // user pressed back
                    Log.d(TAG, "Back Button Pressed: "+ currentBackStackCount);
                    fragmentManager.popBackStack();
                }
            }
        }
    });

}

    /**
     * Connect fragment without back stack.
     *
     * @param context  the context
     * @param fragment the fragment
     */
    public static void connectFragmentWithoutBackStack(Context context, Fragment fragment) {

    FragmentManager fragmentManager = ((AppCompatActivity)context).getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.fragment_container, fragment);
    fragmentTransaction.remove(new QRScaningFragment());
    fragmentTransaction.commit();
    fragmentManager.popBackStack();

}

    /**
     * Put string value in editor shared preferences . editor.
     *
     * @param context the context
     * @param key     the key
     * @param value   the value
     * @return the shared preferences . editor
     */
    public static SharedPreferences.Editor putStringValueInEditor(Context context, String key, String value) {
        sharedPreferences = getSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putString(key, value).commit();
        return editor;
    }

    /**
     * Remove string value in editor shared preferences . editor.
     *
     * @param context the context
     * @param key     the key
     * @return the shared preferences . editor
     */
    public static SharedPreferences.Editor removeStringValueInEditor(Context context, String key) {
        sharedPreferences = getSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.remove(key).apply();
        return editor;
    }

    /**
     * Put integer value in editor shared preferences . editor.
     *
     * @param context the context
     * @param key     the key
     * @param value   the value
     * @return the shared preferences . editor
     */
    public static SharedPreferences.Editor putIntegerValueInEditor(Context context, String key, int value) {
        sharedPreferences = getSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putInt(key, value).commit();
        return editor;
    }

    /**
     * Put boolean value in editor shared preferences . editor.
     *
     * @param context the context
     * @param key     the key
     * @param value   the value
     * @return the shared preferences . editor
     */
    public static SharedPreferences.Editor putBooleanValueInEditor(Context context, String key, boolean value) {
        sharedPreferences = getSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putBoolean(key, value).commit();
        return editor;
    }


    /**
     * Gets shared preferences.
     *
     * @param context the context
     * @return the shared preferences
     */
    public static SharedPreferences getSharedPreferences(Context context) {

            return context.getSharedPreferences(Configuration.MY_PREF, 0);
    }

    /**
     * Gets api token.
     *
     * @param context the context
     * @return the api token
     */
//get Api Token
    public static String getAPIToken(Context context) {
        return getSharedPreferences(context).getString("api_token", "");
    }

    /**
     * Get producer id string.
     *
     * @param context the context
     * @return the string
     */
//return producerId
    public static String getProducerID(Context context){
        return getSharedPreferences(context).getString("producer_id", "");
    }

    /**
     * Is logged in boolean.
     *
     * @param context the context
     * @return the boolean
     */
    public static boolean isLoggedIn(Context context) {
        return getSharedPreferences(context).getBoolean("loggedIn", false);
    }

    /**
     * Get hash code string.
     *
     * @param context the context
     * @return the string
     */
    public static String getHashCode(Context context){
        return getSharedPreferences(context).getString("hash_code", "");
    }

    /**
     * Remove hash code.
     *
     * @param context the context
     */
    public static void removeHashCode(Context context){
        getSharedPreferences(context).edit().remove("hash_code").commit();
        Log.d("Hash","removed");
    }

    /**
     * Progress dialog ac progress flower.
     *
     * @param context the context
     * @param text    the text
     * @return the ac progress flower
     */
    public static ACProgressFlower progressDialog(Context context, String text) {
        progress =new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text(text)
                .fadeColor(Color.DKGRAY).build();
        progress.show();
        return progress;

    }


}
