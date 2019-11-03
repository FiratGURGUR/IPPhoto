package com.developer.ipphoto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText edtUserName;
    SharedPreferences sharedPref;
    Button getData;
    ImageView userProfileImageView;
    TextView fullName;
    TextView followerscount;
    TextView followingcount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref= getPreferences(Context.MODE_PRIVATE);
        edtUserName = findViewById(R.id.editText);
        getData = findViewById(R.id.button);
        userProfileImageView = findViewById(R.id.imageView);
        fullName = findViewById(R.id.txtfullName);
        followerscount = findViewById(R.id.txtfollowers);
        followingcount = findViewById(R.id.txtfollowing);

        Glide
                .with(MainActivity.this)
                .load(R.drawable.profileplaceholder)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.giphy)
                .into(userProfileImageView);


        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = edtUserName.getText().toString().trim();

                if (userName.length() <= 4){
                    edtUserName.setError("username is too short");
                }else{
                    getUserData(userName);
                }
            }
        });


        userProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = edtUserName.getText().toString();
                String pctr = sharedPref.getString("picture_hd","xx");
                Intent i = new Intent(getApplicationContext(),FullScreenImage.class);
                i.putExtra("picture_url", pctr);
                i.putExtra("username", user);
                startActivity(i);
            }
        });


    }





    public void getUserData(String username){
        AndroidNetworking.get("https://www.instagram.com/"+ username +"/?__a=1")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String graphql = response.getString("graphql");
                            JSONObject jsongraphql = new JSONObject(graphql);
                            String user = jsongraphql.getString("user");
                            JSONObject jsonuser = new JSONObject(user);
                            String profile_pic_url = jsonuser.getString("profile_pic_url_hd");
                            String fullNameData = jsonuser.getString("full_name");

                            String folleewrsData = jsonuser.getString("edge_followed_by");
                            JSONObject followers = new JSONObject(folleewrsData);
                            String followerscountData = String.valueOf(followers.getInt("count"));

                            String followingData = jsonuser.getString("edge_follow");
                            JSONObject following = new JSONObject(followingData);
                            String followingcountData = String.valueOf(following.getInt("count"));


                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("picture_hd",profile_pic_url); //string değer ekleniyor
                            editor.commit(); //Kayıt


                            fullName.setText(fullNameData);
                            followerscount.setText(followerscountData);
                            followingcount.setText(followingcountData);

                            Glide
                                    .with(MainActivity.this)
                                    .load(profile_pic_url)
                                    .apply(RequestOptions.circleCropTransform())
                                    .placeholder(R.drawable.giphy)
                                    .into(userProfileImageView);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }



}
