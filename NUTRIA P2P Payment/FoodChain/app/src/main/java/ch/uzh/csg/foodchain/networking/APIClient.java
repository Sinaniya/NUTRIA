package ch.uzh.csg.foodchain.networking;

import android.app.Activity;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * The type Api client.
 */
public class APIClient {

    /**
     * The constant BASE_URL.
     */
    // Local
    //public static final String BASE_URL = "http://195.201.58.108:8090/";
    // UZH Server
    public static final String BASE_URL = "https://foodchain.ddns.net/api/v1/";
    // AWS Server
    //public static final String BASE_URL = "https://www.foodchain-csg.ch/api/v1/";

    /**
     * The Retrofit
     */
    private static Retrofit retrofit = null;

    /**
     * Gets api client.
     *
     * @param activity the activity
     * @return the api client
     */
    public static Retrofit getApiClient(Activity activity) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();



        return retrofit;

    }



}
