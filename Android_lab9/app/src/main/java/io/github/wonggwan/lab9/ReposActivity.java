package io.github.wonggwan.lab9;

/**
 * Created by wonggwan on 2017/12/18.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.github.wonggwan.lab9.factory.ServiceFactory;
import io.github.wonggwan.lab9.model.Repos;
import retrofit2.Retrofit;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ReposActivity extends Activity {

    ListView mylistview;
    Observer<List<Repos>>  repos_obs;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repos);
        init();
        Intent intent = getIntent();
        String s = intent.getStringExtra("username");
        getrepos(s);
    }

    private void init() {
        mylistview = (ListView)findViewById(R.id.lv_repos_list);
        loading = (ProgressBar)findViewById(R.id.progressBar_load_repos);
        loading.setVisibility(View.INVISIBLE);

        repos_obs = new Observer<List<Repos>>() {
            @Override
            public void onSubscribe(Disposable s) {
                loading.setVisibility(View.VISIBLE);
                mylistview.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNext(List<Repos> repos) {
                List<HashMap<String,String>> data = new ArrayList<>(repos.size());
                for (Repos my: repos) {
                    HashMap<String,String> m = new HashMap<>();
                    m.put("title",""+my.getTitle());
                    m.put("sub1",""+my.getSub1());
                    m.put("sub2",""+my.getSub2());
                    data.add(m);
                }
                SimpleAdapter sm= new SimpleAdapter(ReposActivity.this,
                        data,
                        R.layout.cardview,
                        new String[]{"title","sub1","sub2"},
                        new int[]{R.id.tv_title,R.id.tv_sub_1,R.id.tv_sub_2}
                );
                mylistview.setAdapter(sm);
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(ReposActivity.this,"网络连接超时",Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onComplete() {
                loading.setVisibility(View.INVISIBLE);
                mylistview.setVisibility(View.VISIBLE);
            }
        };
    }

    private void  getrepos(String name) {
        Retrofit retrofit = ServiceFactory.createRetrofit();
        ServiceFactory.GitHubService github =  retrofit.create(ServiceFactory.GitHubService.class);
        Observable<List<Repos>> observable = github.getUserRepos(name);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(repos_obs);
    }
}
