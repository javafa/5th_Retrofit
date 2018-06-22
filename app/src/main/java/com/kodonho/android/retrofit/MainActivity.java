package com.kodonho.android.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 2. 레트로핏 객체 생성
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        // 3. 서비스 생성 - 내가 만든 인터페이스에 코드를 채워서 넘겨준다
        GitHub gitHub = retrofit.create(GitHub.class);

        // 4. 서비스를 통해서 원격지 호출정보 생성
        Call<List<GitUser>> remote = gitHub.getList();

        // 5. 원격지로 호출
        remote.enqueue(new Callback<List<GitUser>>() {
            /*
            서브 스레드를 통해 원격지에서 데이터를 가져온 후에 OnResponse로 전달해 준다
             */
            @Override
            public void onResponse(Call<List<GitUser>> call, Response<List<GitUser>> response) {
                // 응답 클래스의 body 로 최종 결과값이 전달된다
                List<GitUser> list = response.body();
                for(GitUser user : list){
                    Log.d("Retrofit", "user="+user.getLogin());
                }
            }

            @Override
            public void onFailure(Call<List<GitUser>> call, Throwable t) {

            }
        });

    }
}
// 1. 레트로핏 인터페이스 생성
interface GitHub {
    @GET("users")
    Call<List<GitUser>> getList();

    @GET("users")
    Call<GitUser> getUser();
}