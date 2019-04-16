package de.sesam.app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.sesam.app.APi.Authentication;
import de.sesam.app.APi.SesamApi;
import de.sesam.app.R;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private final String TAGNAME = "Testlog";
    // private JsonPlaceholderApi jsonPlaceholderApi;
    private SesamApi sesamApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button subscribeButton = findViewById(R.id.subscribeButton);
        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAGNAME,"Button clicked....");

                Gson gson = new GsonBuilder().serializeNulls().create();

                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                Request originalRequest = chain.request();
                                Request newRequest = originalRequest.newBuilder()
                                        .build();

                                return chain.proceed(newRequest);
                            }
                        })
                        .addInterceptor(loggingInterceptor)
                        .build();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://staging.my.sesam-homebox.de/api/m1/") // slash "/" am Ende sehr wichtig
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(okHttpClient)
                        .build();

                sesamApi = retrofit.create(SesamApi.class);
                // jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi.class);

                getAuthentication();
                // getPosts();
                // getComments();
                // createPosts();
                // updatePost();
                // deletePost();

            }
        });

    }

    private void getAuthentication() {

        EditText username = findViewById(R.id.emailEditText);
        EditText password = findViewById(R.id.passwordEditText);


        Map<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "password");
        parameters.put("email", username.getText().toString().trim());
        parameters.put("password", password.getText().toString().trim());


        Call<Authentication> call = sesamApi.getAuthentication(parameters);
        call.enqueue(new Callback<Authentication>() {
            @Override
            public void onResponse(Call<Authentication> call, Response<Authentication> response) {

                if(!response.isSuccessful()){
                    Log.i(TAGNAME, "Code: " + response.code());
                    if (response.code() == 401) {
                        Log.i(TAGNAME, "Nicht autoriesiert");
                        Toast.makeText(getApplicationContext(),
                                "Benutzer konnte nicht Authorisiert werden!",
                                Toast.LENGTH_LONG).show();
                    }
                    return;
                }

                Toast.makeText(getApplicationContext(),
                        "Benutzer erfolgreich Authorisiert!",
                        Toast.LENGTH_LONG).show();


                Authentication authResponse = response.body();

                String content = "";
                content += "----\n";
                content += "Code: " + response.code() + "\n";
                assert authResponse != null;
                content += "Access_token: " + authResponse.getAccess_token() + "\n";
                content += "Expires_in: " + authResponse.getExpires_in() + "\n";
                content += "Created_at: " + authResponse.getCreated_at() + "\n";
                content += "Token_type: " + authResponse.getToken_type() + "\n\n";
                content += "Scope: " + authResponse.getScope() + "\n\n";
                content += "Refresh_token: " + authResponse.getRefresh_token() + "\n\n";

                Toast.makeText(getApplicationContext(),
                        content,
                        Toast.LENGTH_LONG).show();


                Intent deliviriesIntent = new Intent(LoginActivity.this, DeliveriesActivity.class);
                deliviriesIntent.putExtra("access_token", authResponse.getAccess_token());
                LoginActivity.this.startActivity(deliviriesIntent);
                finish();

                Log.i(TAGNAME, content);
            }

            @Override
            public void onFailure(Call<Authentication> call, Throwable t) {
                Log.i(TAGNAME, t.getMessage());
            }
        });

        // call.enqueue(new Callback<List<Authentication>>() {
        //     @Override
        //     public void onResponse(Call<List<Authentication>> call, Response<List<Authentication>> response) {
        //         if(!response.isSuccessful()){
        //             Log.i(TAGNAME, "Code: " + response.code());
        //             return;
        //         }
        //
        //         List<Authentication> authentications = response.body();
        //
        //         for (Authentication authentication : authentications) {
        //             String content = "";
        //             content += "getAccess_token: " + authentication.getAccess_token() + "\n";
        //             content += "getRefresh_token: " + authentication.getRefresh_token() + "\n";
        //             content += "getScope: " + authentication.getScope() + "\n";
        //             content += "getToken_type: " + authentication.getToken_type() + "\n\n";
        //             content += "getCreated_at: " + authentication.getCreated_at() + "\n\n";
        //             content += "getExpires_in: " + authentication.getExpires_in() + "\n\n";
        //
        //             Log.i(TAGNAME, content);
        //         }
        //     }
        //
        //     @Override
        //     public void onFailure(Call<List<Authentication>> call, Throwable t) {
        //         Log.i(TAGNAME, t.getMessage());
        //     }
        // });
    }

//     private void getPosts() {
//         Map<String, String> parameters = new HashMap<>();
//         parameters.put("email", "developer@sesam-global.com");
//         parameters.put("password", "Sesam17!");
//
// //        Call<List<Post>> call = jsonPlaceholderApi.getPosts(new Integer[]{2,3,6}, null,null);
//         Call<List<Post>> call = jsonPlaceholderApi.getPosts(parameters);
//         call.enqueue(new Callback<List<Post>>() {
//             @Override
//             public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
//                 if(!response.isSuccessful()){
//                     Log.i(TAGNAME, "Code: " + response.code());
//                     return;
//                 }
//
//                 List<Post> posts = response.body();
//
//                 for (Post post : posts) {
//                     String content = "";
//                     content += "ID: " + post.getId() + "\n";
//                     content += "User ID: " + post.getUserId() + "\n";
//                     content += "Title: " + post.getTitle() + "\n";
//                     content += "Text: " + post.getText() + "\n\n";
//
//                     Log.i(TAGNAME, content);
//                 }
//             }
//
//             @Override
//             public void onFailure(Call<List<Post>> call, Throwable t) {
//                 Log.i(TAGNAME, t.getMessage());
//             }
//         });
//     }

//     private void getComments() {
// //        Call<List<Comment>> call = jsonPlaceholderApi.getComments(2);
//         Call<List<Comment>> call = jsonPlaceholderApi.getComments("posts/3/comments");
//         call.enqueue(new Callback<List<Comment>>() {
//             @Override
//             public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
//                 if(!response.isSuccessful()){
//                     Log.i(TAGNAME, "Code: " + response.code());
//                     return;
//                 }
//
//                 List<Comment> comments = response.body();
//                 for(Comment comment : comments) {
//                     String content = "";
//                     content += "ID: " + comment.getId() + "\n";
//                     content += "Post ID: " + comment.getPostId() + "\n";
//                     content += "Name: " + comment.getName() + "\n";
//                     content += "Email: " + comment.getEmail() + "\n\n";
//                     content += "Text: " + comment.getText() + "\n\n";
//
//                     Log.i(TAGNAME, content);
//                 }
//             }
//
//             @Override
//             public void onFailure(Call<List<Comment>> call, Throwable t) {
//                 Log.i(TAGNAME, t.getMessage());
//             }
//         });
//     }
//
//     private void createPosts() {
//         Post post = new Post(23, "New Title", "New Text");
//
//         Map<String, String> fields = new HashMap<>();
//         fields.put("userId", "23");
//         fields.put("title", "New Title");
//         fields.put("body", "New Text");
//
//         // Call<Post> call = jsonPlaceholderApi.createPost(post);
//         // Call<Post> call = jsonPlaceholderApi.createPost(25, "NewTitle", "New text"); //FormUrlEncoded
//         Call<Post> call = jsonPlaceholderApi.createPost(fields); //Map
//
//         call.enqueue(new Callback<Post>() {
//             @Override
//             public void onResponse(Call<Post> call, Response<Post> response) {
//                 if(!response.isSuccessful()){
//                     Log.i(TAGNAME, "Code: " + response.code());
//                     return;
//                 }
//
//                 Post postResponse = response.body();
//                 String content = "";
//                 content += "----\n";
//                 content += "Code: " + response.code() + "\n";
//                 content += "ID: " + postResponse.getId() + "\n";
//                 content += "User ID: " + postResponse.getUserId() + "\n";
//                 content += "Title: " + postResponse.getTitle() + "\n";
//                 content += "Text: " + postResponse.getText() + "\n\n";
//
//                 Log.i(TAGNAME, content);
//             }
//
//             @Override
//             public void onFailure(Call<Post> call, Throwable t) {
//                 Log.i(TAGNAME, t.getMessage());
//             }
//         });
//     }
//
//     private void updatePost() {
//         Post post = new Post(2, null, null);
//         Map<String, String> headers = new HashMap<>();
//         headers.put("Map-Header1", "def");
//         headers.put("Map-Header2", "ghi");
//
//         // PUT POST
//         // Call<Post> call = jsonPlaceholderApi.putPost(5,post);
//         // Call<Post> call = jsonPlaceholderApi.putPost("abc", 5,post);
//
//         // PATCH POST
//         // Call<Post> call = jsonPlaceholderApi.patchPost(5,post);
//         Call<Post> call = jsonPlaceholderApi.patchPost(headers, 5,post);
//
//
//         call.enqueue(new Callback<Post>() {
//             @Override
//             public void onResponse(Call<Post> call, Response<Post> response) {
//                 if(!response.isSuccessful()){
//                     Log.i(TAGNAME, "Code: " + response.code());
//                     return;
//                 }
//
//                 Post postResponse = response.body();
//                 String content = "";
//                 content += "----\n";
//                 content += "Code: " + response.code() + "\n";
//                 content += "ID: " + postResponse.getId() + "\n";
//                 content += "User ID: " + postResponse.getUserId() + "\n";
//                 content += "Title: " + postResponse.getTitle() + "\n";
//                 content += "Text: " + postResponse.getText() + "\n\n";
//
//                 Log.i(TAGNAME, content);
//             }
//
//             @Override
//             public void onFailure(Call<Post> call, Throwable t) {
//                 Log.i(TAGNAME, t.getMessage());
//             }
//         });
//     }
//
//     private void deletePost() {
//         Call<Void> call = jsonPlaceholderApi.deletePost(5);
//
//         call.enqueue(new Callback<Void>() {
//             @Override
//             public void onResponse(Call<Void> call, Response<Void> response) {
//                 Log.i(TAGNAME, ""+response.code());
//             }
//
//             @Override
//             public void onFailure(Call<Void> call, Throwable t) {
//                 Log.i(TAGNAME, t.getMessage());
//             }
//         });
//     }
}
