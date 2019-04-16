package de.sesam.app.APi;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SesamApi {
    // @FormUrlEncoded
    // @Headers({"Content-Type: application/json", "Accept: application/json", "Accept-Language: de"})
    // @GET("oauth/token")
    // Call<List<Authentication>> getAuthentication(
    //         @Query("grant_type") String grant_type,
    //         @Query("email") String email,
    //         @Query("password") String password
    // );
    //
    // @Headers({"Content-Type: application/json", "Accept: application/json", "Accept-Language: de"})
    // @GET("oauth/token")
    // Call<List<Authentication>> getAuthentications(
    //         @QueryMap Map<String, String> parameters
    // );

    // @Headers({"Content-Type: application/json", "Accept: application/json", "Accept-Language: de"})

    @FormUrlEncoded
    @POST("oauth/token")
    Call<Authentication> getAuthentication(
            @Field("grant_type") String grant_type,
            @Field("email") String email,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("oauth/token")
    Call<Authentication> getAuthentication(@FieldMap Map<String, String> fields);
}
