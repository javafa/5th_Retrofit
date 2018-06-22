package com.kodonho.android.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView imageView;
    TextView textView;
    CustomAdapter adapter;

    GitHub gitHub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setView();
        setRetrofit();
        setList();
    }
    private void setView(){
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new CustomAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private void setRetrofit(){
        // 2. 레트로핏 객체 생성
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        // 3. 서비스 생성 - 내가 만든 인터페이스에 코드를 채워서 넘겨준다
        gitHub = retrofit.create(GitHub.class);
    }
    private void setList(){
        // 4. 서비스를 통해서 원격지 호출정보 생성
        Call<List<GitUser>> remoteList = gitHub.getList();
        // 5. 원격지로 호출
        remoteList.enqueue(new Callback<List<GitUser>>() {
            @Override
            public void onResponse(Call<List<GitUser>> call, Response<List<GitUser>> response) {
                // 응답 클래스의 body 로 최종 결과값이 전달된다
                List<GitUser> list = response.body();
                adapter.setDataAndRefresh(list);
            }
            @Override
            public void onFailure(Call<List<GitUser>> call, Throwable t) {

            }
        });
    }
    public void setUser(String userid){
        Call<GitUser> remoteUser = gitHub.getUser(userid);
        remoteUser.enqueue(new Callback<GitUser>() {
            @Override
            public void onResponse(Call<GitUser> call, Response<GitUser> response) {
                GitUser user = response.body();
                textView.setText(user.getLogin());
                // 화면에 이미지 세팅하기
                Glide
                    .with(getBaseContext()) // 컨텍스트
                    .load(user.getAvatar_url()) // 이미지 주소
                    .into(imageView); // 이미지뷰 위젯
            }
            @Override
            public void onFailure(Call<GitUser> call, Throwable t) {

            }
        });
    }
}
// 1. 레트로핏 인터페이스 생성
interface GitHub {
    @GET("users")
    Call<List<GitUser>> getList();

    @GET("users/{a}") // users/michael
    Call<GitUser> getUser(@Path("a") String userid);

    @GET("users") // users?aaa=michael
    Call<GitUser> getAvatar(@Query("aaa") String userid);
}