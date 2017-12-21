package io.github.wonggwan.lab9.factory;

import java.util.List;
import java.util.concurrent.TimeUnit;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import io.reactivex.Observable;
import io.github.wonggwan.lab9.BuildConfig;
import io.github.wonggwan.lab9.model.GitHub;
import io.github.wonggwan.lab9.model.Repos;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by wonggwan on 2017/12/18.
 */

public class ServiceFactory {

    //获取GitHub的API
    public static final String API_URL = "https://api.github.com";
    public interface GitHubService{
        @GET("/users/{user}")
        Observable<GitHub> getUser(@Path("user") String user);

        @GET("users/{user}/repos")
        Observable< List<Repos> > getUserRepos(@Path("user") String user);
    }

    public static Retrofit createRetrofit() {
        //请求网络访问
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(9, TimeUnit.SECONDS);
        builder.connectTimeout(8, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        return new Retrofit.Builder().baseUrl(API_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
