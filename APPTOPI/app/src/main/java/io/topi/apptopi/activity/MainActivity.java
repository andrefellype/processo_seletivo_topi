package io.topi.apptopi.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.topi.apptopi.R;
import io.topi.apptopi.adapter.GitAdapter;
import io.topi.apptopi.model.GitItem;
import io.topi.apptopi.service.GitService;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout srlRefreshList;
    private RecyclerView rvListGit;
    private RecyclerView.LayoutManager layoutManager;
    private GitAdapter gitAdapter;
    private FrameLayout flLoading;
    private RelativeLayout rlLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        this.srlRefreshList = (SwipeRefreshLayout) this.findViewById(R.id.srl_refresh_list);
        this.rvListGit = (RecyclerView) this.findViewById(R.id.rv_list_git);
        this.flLoading = (FrameLayout) this.findViewById(R.id.fl_loading);
        this.rlLoading = (RelativeLayout) this.findViewById(R.id.rl_loading);

        this.rvListGit.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(this);
        this.rvListGit.setLayoutManager(this.layoutManager);
        this.gitAdapter = new GitAdapter(this, new ArrayList<>());
        this.rvListGit.setAdapter(this.gitAdapter);

        GitService.getGitApi(new GitService.GitApiCallback() {
            @Override
            public void onSucess(List<GitItem> gitItems) {
                gitAdapter = new GitAdapter(MainActivity.this, gitItems);
                rvListGit.setAdapter(gitAdapter);
                flLoading.setVisibility(View.GONE);
            }
        });

        this.srlRefreshList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlRefreshList.setRefreshing(false);
                rlLoading.setVisibility(View.VISIBLE);
                gitAdapter = new GitAdapter(MainActivity.this, new ArrayList<>());
                rvListGit.setAdapter(gitAdapter);
                GitService.getGitApi(new GitService.GitApiCallback() {
                    @Override
                    public void onSucess(List<GitItem> gitItems) {
                        gitAdapter = new GitAdapter(MainActivity.this, gitItems);
                        rvListGit.setAdapter(gitAdapter);
                        rlLoading.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}