package io.topi.apptopi.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.topi.apptopi.R;
import io.topi.apptopi.adapter.GitAdapter;
import io.topi.apptopi.adapter.RecyclerViewGitListener;
import io.topi.apptopi.model.GitItem;
import io.topi.apptopi.service.GitService;

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
    private ImageView ivSort;
    private View layoutSort;
    private View llSortName;
    private View llSortStar;
    private LinearLayout llMain;
    private LinearLayout llNotConnection;
    private Button btnAgainConnection;

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
        this.ivSort = (ImageView) this.findViewById(R.id.iv_sort);
        this.llMain = (LinearLayout) this.findViewById(R.id.ll_main);
        this.llNotConnection = (LinearLayout) this.findViewById(R.id.ll_not_connection);
        this.btnAgainConnection = (Button) this.findViewById(R.id.btn_again_connection);

        this.layoutSort = this.getLayoutInflater().inflate(R.layout.layout_sort_list, null);
        this.llSortName = layoutSort.findViewById(R.id.ll_sort_name);
        this.llSortStar = layoutSort.findViewById(R.id.ll_sort_star);

        this.llNotConnection.setVisibility(View.GONE);
        this.llMain.setVisibility(View.VISIBLE);

        this.edtSearch.setEnabled(false);
        this.edtSearch.clearFocus();

        this.rvListGit.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(this);
        this.rvListGit.setLayoutManager(this.layoutManager);

        this.gitItemsList = new ArrayList<>();
        this.gitAdapter = new GitAdapter(this, gitItemsList, new RecyclerViewGitListener<GitItem>() {
            @Override
            public void onItemClick(GitItem obj, int position) {
            }
        });
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
                    gitAdapter = new GitAdapter(MainActivity.this, gitItems, new RecyclerViewGitListener<GitItem>() {
                        @Override
                        public void onItemClick(GitItem obj, int position) {
                            MainActivity.this.clickItemGit(obj);
                        }
                    });
                    rvListGit.setAdapter(gitAdapter);
                    ivSearch.setVisibility(View.VISIBLE);
                    ibCancelSearch.setVisibility(View.VISIBLE);
                    pbSearchLoading.setVisibility(View.GONE);
                } else {
                    ivSearch.setVisibility(View.VISIBLE);
                    pbSearchLoading.setVisibility(View.GONE);
                    gitAdapter = new GitAdapter(MainActivity.this, gitItemsList, new RecyclerViewGitListener<GitItem>() {
                        @Override
                        public void onItemClick(GitItem obj, int position) {
                            MainActivity.this.clickItemGit(obj);
                        }
                    });
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
                gitAdapter = new GitAdapter(MainActivity.this, new ArrayList<>(), new RecyclerViewGitListener<GitItem>() {
                    @Override
                    public void onItemClick(GitItem obj, int position) {
                    }
                });
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
                gitAdapter = new GitAdapter(MainActivity.this, new ArrayList<>(), new RecyclerViewGitListener<GitItem>() {
                    @Override
                    public void onItemClick(GitItem obj, int position) {
                    }
                });
                rvListGit.setAdapter(gitAdapter);
                edtSearch.setEnabled(false);
                edtSearch.clearFocus();
                getList();
            }
        });

        final Dialog dialog = new MaterialDialog.Builder(MainActivity.this).customView(layoutSort, false).build();

        this.ivSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        this.llSortName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlLoading.setVisibility(View.VISIBLE);
                gitAdapter = new GitAdapter(MainActivity.this, new ArrayList<>(), new RecyclerViewGitListener<GitItem>() {
                    @Override
                    public void onItemClick(GitItem obj, int position) {
                    }
                });
                rvListGit.setAdapter(gitAdapter);
                edtSearch.setEnabled(false);

                Collections.sort(gitItemsList, new Comparator<GitItem>() {
                    @Override
                    public int compare(GitItem o1, GitItem o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

                gitAdapter = new GitAdapter(MainActivity.this, gitItemsList, new RecyclerViewGitListener<GitItem>() {
                    @Override
                    public void onItemClick(GitItem obj, int position) {
                        MainActivity.this.clickItemGit(obj);
                    }
                });
                rvListGit.setAdapter(gitAdapter);
                rlLoading.setVisibility(View.GONE);
                edtSearch.setEnabled(true);
                dialog.dismiss();
            }
        });

        this.llSortStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlLoading.setVisibility(View.VISIBLE);
                gitAdapter = new GitAdapter(MainActivity.this, new ArrayList<>(), new RecyclerViewGitListener<GitItem>() {
                    @Override
                    public void onItemClick(GitItem obj, int position) {
                    }
                });
                rvListGit.setAdapter(gitAdapter);
                edtSearch.setEnabled(false);

                Collections.sort(gitItemsList, new Comparator<GitItem>() {
                    @Override
                    public int compare(GitItem o1, GitItem o2) {
                        if(o1.getStargazers_count() > o2.getStargazers_count()) {
                            return 1;
                        } else if( o1.getStargazers_count() < o2.getStargazers_count()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });

                gitAdapter = new GitAdapter(MainActivity.this, gitItemsList, new RecyclerViewGitListener<GitItem>() {
                    @Override
                    public void onItemClick(GitItem obj, int position) {
                        MainActivity.this.clickItemGit(obj);
                    }
                });
                rvListGit.setAdapter(gitAdapter);
                rlLoading.setVisibility(View.GONE);
                edtSearch.setEnabled(true);
                dialog.dismiss();
            }
        });

        this.btnAgainConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llNotConnection.setVisibility(View.GONE);
                llMain.setVisibility(View.VISIBLE);
                rlLoading.setVisibility(View.GONE);
                flLoading.setVisibility(View.VISIBLE);
                getList();
            }
        });
    }

    public void getList(){
        GitService.getGitApi("language:Java", "stars", "1", new GitService.GitApiCallback() {
            @Override
            public void onSucess(List<GitItem> gitItems) {
                llNotConnection.setVisibility(View.GONE);
                llMain.setVisibility(View.VISIBLE);

                gitItemsList = new ArrayList<>();
                gitItemsList = gitItems;
                if(edtSearch.getText().toString().isEmpty()) {
                    gitAdapter = new GitAdapter(MainActivity.this, gitItems, new RecyclerViewGitListener<GitItem>() {
                        @Override
                        public void onItemClick(GitItem obj, int position) {
                            MainActivity.this.clickItemGit(obj);
                        }
                    });
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
                    gitAdapter = new GitAdapter(MainActivity.this, listNew, new RecyclerViewGitListener<GitItem>() {
                        @Override
                        public void onItemClick(GitItem obj, int position) {
                            MainActivity.this.clickItemGit(obj);
                        }
                    });
                    rvListGit.setAdapter(gitAdapter);
                    rlLoading.setVisibility(View.GONE);
                }
                edtSearch.setEnabled(true);
            }

            @Override
            public void onNotConnection() {
                llNotConnection.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                rlLoading.setVisibility(View.GONE);
                flLoading.setVisibility(View.GONE);
            }
        });
    }

    private void clickItemGit(GitItem gitItem){
        Intent intent = new Intent(MainActivity.this, MoreDetailsActivity.class);
        intent.putExtra("git_bundle", gitItem);
        startActivity(intent);
        finish();
    }
}