package ch.uzh.csg.foodchain.networking;


import com.google.gson.JsonObject;

import ch.uzh.csg.foodchain.Models.CreateProductTag.ProductTag;
import ch.uzh.csg.foodchain.Models.ProducerLoginModel.ProducerLogin;
import ch.uzh.csg.foodchain.Models.ScannedQRModel.PreviousProductTag;
import ch.uzh.csg.foodchain.Models.ScannedQRModel.ScanningQR;
import ch.uzh.csg.foodchain.Models.VerifyProductHashModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * The interface Api services.
 */
public interface APIServices {

    /**
     * Verify hash call.
     *
     * @param hash the hash
     * @return the call
     */
    @GET("productTags/hash/{hash}")
    Call<VerifyProductHashModel> verifyHash(@Path("hash") String hash);

    /**
     * Gets scanning qr.
     * new QR Scanning API
     * @param hash the hash
     * @return the scanning qr
     */
    @GET("productTags/hash/{hash}")
    Call<PreviousProductTag> getScanningQR(@Path("hash") String hash);

    /**
     * Login producer call.
     *
     * @param user_name the user name
     * @param password  the password
     * @return the call
     */
    @POST("producers/login")
    @FormUrlEncoded
    Call<ProducerLogin> loginProducer(@Field("username") String user_name,
                                             @Field("password") String password);

    /**
     * Create product tag call.
     *
     * @param token the token
     * @param raw   the raw
     * @return the call
     */
    @POST("productTags")
    @Headers({"Content-Type: application/json"})
    Call<ProductTag> createProductTag(@Header("Authorization") String token, @Body JsonObject raw);

}
