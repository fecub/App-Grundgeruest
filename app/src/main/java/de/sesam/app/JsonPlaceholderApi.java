package de.sesam.app;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface JsonPlaceholderApi {
    /*
     *
     * Es gibt verschiedene möglichkeiten die
     * API anzufragen, sind unten aufgelistet
     *
     */

    // region -- GET Methods

/*    QUERY: Möglichkeit 1   */
//    @GET("posts")
//    Call<List<Post>> getPosts();

/*    QUERY: Möglichkeit 2   */
//    @GET("posts")
//    Call<List<Post>> getPosts(@Query("userId") int userId);

/*    QUERY: Möglichkeit 3   */
    @GET("posts")
    Call<List<Post>> getPosts(
            @Query("userId") Integer[] userId,
//            @Query("userId") Integer userId,  //int not nullable, but Integer is nullable
//            @Query("userId") Integer userId2,
            @Query("_sort") String sort,
            @Query("_order") String order
    );

    @GET("posts")
    Call<List<Post>> getPosts(
            @QueryMap Map<String, String> parameters
    );

/*    PATH: Möglichkeit 1   */
    @GET("posts/{id}/comments")
    Call<List<Comment>> getComments(@Path("id") int postId);

/*    PATH: Möglichkeit 2   */
//    @GET("posts/{id}/comments/{postId}")
//    Call<List<Comment>> getComments(
//            @Path("id") int id,
//            @Path("postId") int postId
//    );

//    @GET("comments")
//    Call<List<Comment>> getComments(@Query("postId") int postId);


    /*    PATH: Möglichkeit 3  (nur zur info, nicht gut) */
    @GET
    Call<List<Comment>> getComments(@Url String url);

    // endregion

    // region -- POST Methods

    @POST("posts")
    Call<Post> createPost(@Body Post post);

    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(
            @Field("userId") int userId,
            @Field("title") String title,
            @Field("body") String text
    );

    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(@FieldMap Map<String, String> fields);

    // endregion

    // region -- PUT Methods


    // @Headers("Static-Header: 123")
    @Headers({"Static-Header: 123", "Static-Header2: 456"})
    @PUT("posts/{id}")
    Call<Post> putPost(@Header ("Dynamic-Header") String header,
                       @Path("id") int id,
                       @Body Post post);

    // @PUT("posts/{id}")  // will completly replace the existing ressource
    //                     // when not existing, possible it creates new, depends on APi
    Call<Post> putPost(@Path("id") int id, @Body Post post);
    // endregion

    // region -- PATCH Methods

    @PATCH("posts/{id}") // will only change the fields that we send
    Call<Post> patchPost(@Path("id") int id, @Body Post post);


    @PATCH("posts/{id}") // will only change the fields that we send
    Call<Post> patchPost(@HeaderMap Map<String, String> headers,
                         @Path("id") int id,
                         @Body Post post);

    // endregion

    // region -- DELETE Methods

    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int id);

    // endregion

}

