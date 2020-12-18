package io.topi.apptopi.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    private EditText edtSearch;
    private List<GitItem> gitItemsList;
    private ImageView ivSearch;
    private ImageButton ibCancelSearch;
    private ProgressBar pbSearchLoading;

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
        this.edtSearch = (EditText) this.findViewById(R.id.edt_search);
        this.ivSearch = (ImageView) this.findViewById(R.id.iv_search);
        this.ibCancelSearch = (ImageButton) this.findViewById(R.id.ib_cancel_search);
        this.pbSearchLoading = (ProgressBar) this.findViewById(R.id.pb_search_loading);

        this.edtSearch.setEnabled(false);
        this.edtSearch.clearFocus();

        this.rvListGit.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(this);
        this.rvListGit.setLayoutManager(this.layoutManager);

        this.gitItemsList = new ArrayList<>();
        this.gitAdapter = new GitAdapter(this, gitItemsList);
        this.rvListGit.setAdapter(this.gitAdapter);

        this.getList();

        this.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()) {
                    ivSearch.setVisibility(View.GONE);
                    pbSearchLoading.setVisibility(View.VISIBLE);
                    List<GitItem> gitItems = new ArrayList<>();
                    for (GitItem gitItem : gitItemsList) {
                        int posNameRepo = gitItem.getName().indexOf(s.toString());
                        int posNameOwner = gitItem.getOwner().getLogin().indexOf(s.toString());
                        if (posNameRepo >= 0 || posNameOwner >= 0) {
                            gitItems.add(gitItem);
                        }
                    }
                    gitAdapter = new GitAdapter(MainActivity.this, gitItems);
                    rvListGit.setAdapter(gitAdapter);
                    ivSearch.setVisibility(View.VISIBLE);
                    ibCancelSearch.setVisibility(View.VISIBLE);
                    pbSearchLoading.setVisibility(View.GONE);
                } else {
                    ivSearch.setVisibility(View.VISIBLE);
                    pbSearchLoading.setVisibility(View.GONE);
                    gitAdapter = new GitAdapter(MainActivity.this, gitItemsList);
                    rvListGit.setAdapter(gitAdapter);
                    ibCancelSearch.setVisibility(View.GONE);
                }
            }
        });

        this.ibCancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
                ibCancelSearch.setVisibility(View.GONE);
                srlRefreshList.setRefreshing(false);
                rlLoading.setVisibility(View.VISIBLE);
                gitAdapter = new GitAdapter(MainActivity.this, new ArrayList<>());
                rvListGit.setAdapter(gitAdapter);
                edtSearch.setEnabled(false);
                edtSearch.clearFocus();
                getList();
            }
        });

        this.srlRefreshList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlRefreshList.setRefreshing(false);
                rlLoading.setVisibility(View.VISIBLE);
                gitAdapter = new GitAdapter(MainActivity.this, new ArrayList<>());
                rvListGit.setAdapter(gitAdapter);
                edtSearch.setEnabled(false);
                edtSearch.clearFocus();
                getList();
            }
        });
    }

    private void getList(){
        GitService.getGitApi(new GitService.GitApiCallback() {
            @Override
            public void onSucess(List<GitItem> gitItems) {
                gitItemsList = new ArrayList<>();
                gitItemsList = gitItems;
                if(edtSearch.getText().toString().isEmpty()) {
                    gitAdapter = new GitAdapter(MainActivity.this, gitItems);
                    rvListGit.setAdapter(gitAdapter);
                    flLoading.setVisibility(View.GONE);
                    rlLoading.setVisibility(View.GONE);
                } else {
                    List<GitItem> listNew = new ArrayList<>();
                    String search = edtSearch.getText().toString();
                    for (GitItem gitItem : gitItems) {
                        int posNameRepo = gitItem.getName().indexOf(search.toString());
                        int posNameOwner = gitItem.getOwner().getLogin().indexOf(search.toString());
                        if (posNameRepo >= 0 || posNameOwner >= 0) {
                            listNew.add(gitItem);
                        }
                    }
                    gitAdapter = new GitAdapter(MainActivity.this, listNew);
                    rvListGit.setAdapter(gitAdapter);
                    rlLoading.setVisibility(View.GONE);
                }
                edtSearch.setEnabled(true);
            }
        });
    }
}