package io.github.wonggwan.lab9;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.github.wonggwan.lab9.adapter.CardAdapter;
import io.github.wonggwan.lab9.factory.ServiceFactory;
import io.github.wonggwan.lab9.model.GitHub;
import retrofit2.Retrofit;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    EditText searchbox;
    Button bnt_clear;
    Button bnt_fetch;
    ProgressBar loading;
    RecyclerView myview;
    CardAdapter myadapter;
    List<GitHub> githubuser = new ArrayList<>();
    Observer<GitHub> github_observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        exec();
    }

    private void init() {
        searchbox = (EditText)findViewById(R.id.ed_search);
        bnt_clear = (Button)findViewById(R.id.bnt_clear);
        bnt_fetch = (Button)findViewById(R.id.bnt_fetch);
        loading = (ProgressBar) findViewById(R.id.progressBar_load_github);
        myview = (RecyclerView)findViewById(R.id.rv_main_list);
        loading.setVisibility(View.INVISIBLE);
        myadapter = new CardAdapter(this);
        myadapter.create(githubuser);
        myview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        myview.setAdapter(myadapter);
    }

    private void exec() {
        bnt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchbox.setText("");
                githubuser.clear();
                myadapter.notifyDataSetChanged();
            }
        });
        bnt_fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = searchbox.getText().toString();
                if (username.isEmpty()) {
                    Toast.makeText(MainActivity.this, "用户名不可为空", Toast.LENGTH_SHORT).show();
                }
                else {
                    startService(username);
                }
            }
        });

        github_observer = new Observer<GitHub>() {
            @Override
            public void onSubscribe(Disposable d) {
                loading.setVisibility(View.VISIBLE);
                myview.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNext(GitHub gitHub) {
                myadapter.add(gitHub);
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(MainActivity.this,"用户不存在",Toast.LENGTH_SHORT).show();;
            }

            @Override
            public void onComplete() {
                loading.setVisibility(View.INVISIBLE);
                myview.setVisibility(View.VISIBLE);
            }
        };

        myadapter.setOnItemClickLitener(new CardAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent newIntent = new Intent(MainActivity.this, ReposActivity.class);
                newIntent.putExtra("username", myadapter.getpos(position).getTitle());
                startActivity(newIntent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                myadapter.delete(position);
            }
        });
    }

    private void startService(String name) {
        Retrofit retrofit = ServiceFactory.createRetrofit();
        ServiceFactory.GitHubService github =  retrofit.create(ServiceFactory.GitHubService.class);
        Observable<GitHub> observable = github.getUser(name);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(github_observer);
    }
}