package io.topi.apptopi.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import io.topi.apptopi.R;
import io.topi.apptopi.adapter.GitAdapter;
import io.topi.apptopi.model.GitItem;
import io.topi.apptopi.viewmodel.GitItemViewModel;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewById
    protected SwipeRefreshLayout srlRefreshList;
    @ViewById
    protected RecyclerView rvListGit;
    @ViewById
    protected ProgressBar pbLoading;
    @ViewById
    protected View viewRlLoading;
    @ViewById
    protected EditText edtSearch;
    @ViewById
    protected ImageView ivSearch;
    @ViewById
    protected ImageButton ibCancelSearch;
    @ViewById
    protected ProgressBar pbSearchLoading;
    @ViewById
    protected View viewMain;
    @ViewById
    protected View viewNotConnect;

    private RecyclerView.LayoutManager layoutManager;
    private Dialog dialog;

    @Bean
    protected GitAdapter gitAdapter;

    private GitItemViewModel gitItemViewModel;

    @AfterViews
    void initMain() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        this.viewNotConnect.setVisibility(View.GONE);
        this.viewMain.setVisibility(View.VISIBLE);

        this.edtSearch.setEnabled(false);
        this.edtSearch.clearFocus();

        this.rvListGit.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(this);
        this.rvListGit.setLayoutManager(this.layoutManager);
        this.rvListGit.setAdapter(gitAdapter);

        gitItemViewModel = new ViewModelProvider(this).get(GitItemViewModel.class);
        gitItemViewModel.getGitItens().observe(this, this::updateAdapter);
        gitItemViewModel.getRefreshList().observe(this, this::refreshList);
        gitItemViewModel.getNotConnection().observe(this, this::notConnection);

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
                    gitItemViewModel.getListSearch(s.toString());
                    ibCancelSearch.setVisibility(View.VISIBLE);
                } else {
                    gitItemViewModel.getList();
                    ibCancelSearch.setVisibility(View.GONE);
                }
            }
        });

        this.srlRefreshList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gitItemViewModel.setLdRefreshList(0);
                gitItemViewModel.getList();
            }
        });

        LayoutSort layoutSort = LayoutSort_.build(this);
        this.dialog = new MaterialDialog.Builder(MainActivity.this).customView(layoutSort, false).build();
    }

    void refreshList(Integer status){
        if(status == 0) {
            if (srlRefreshList.isRefreshing()) {
                srlRefreshList.setRefreshing(false);
            }
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (viewNotConnect.getVisibility() == View.VISIBLE) {
                viewNotConnect.setVisibility(View.GONE);
            }
            if (pbLoading.getVisibility() == View.GONE) {
                pbLoading.setVisibility(View.VISIBLE);
            }
            if (viewRlLoading.getVisibility() == View.GONE) {
                viewRlLoading.setVisibility(View.VISIBLE);
            }
            if(rvListGit.getVisibility() == View.VISIBLE){
                rvListGit.setVisibility(View.GONE);
            }
            edtSearch.setEnabled(false);
        } else if(status == 1) {
            if(dialog.isShowing()){
                dialog.dismiss();
            }
            if (viewMain.getVisibility() == View.GONE) {
                viewMain.setVisibility(View.VISIBLE);
            }
            if (pbLoading.getVisibility() == View.VISIBLE) {
                pbLoading.setVisibility(View.GONE);
            }
            if (viewRlLoading.getVisibility() == View.VISIBLE) {
                viewRlLoading.setVisibility(View.GONE);
            }
            if (rvListGit.getVisibility() == View.GONE) {
                rvListGit.setVisibility(View.VISIBLE);
            }
            edtSearch.setEnabled(true);
        }
    }

    void notConnection(Boolean status)  {
        if(!status) {
            if (viewMain.getVisibility() == View.GONE) {
                viewMain.setVisibility(View.VISIBLE);
            }
            if (pbLoading.getVisibility() == View.VISIBLE) {
                pbLoading.setVisibility(View.GONE);
            }
            if (viewRlLoading.getVisibility() == View.VISIBLE) {
                viewRlLoading.setVisibility(View.GONE);
            }
            if (rvListGit.getVisibility() == View.GONE) {
                rvListGit.setVisibility(View.VISIBLE);
            }
            edtSearch.setEnabled(true);
        } else {
            if (viewMain.getVisibility() == View.VISIBLE) {
                viewMain.setVisibility(View.GONE);
            }
            if (viewNotConnect.getVisibility() == View.GONE) {
                viewNotConnect.setVisibility(View.VISIBLE);
            }
            if (pbLoading.getVisibility() == View.VISIBLE) {
                pbLoading.setVisibility(View.GONE);
            }
            if (viewRlLoading.getVisibility() == View.VISIBLE) {
                viewRlLoading.setVisibility(View.GONE);
            }
            if (rvListGit.getVisibility() == View.GONE) {
                rvListGit.setVisibility(View.VISIBLE);
            }
            edtSearch.setEnabled(true);
        }
    }

    void updateAdapter(List<GitItem> gitItems){
        gitAdapter.updateAdapter(gitItems);
    }

    @Click(R.id.ivSort)
    void clickSort(){
        dialog.show();
    }

    @Click(R.id.btnAgainConnection)
    void clickAgainConnection(){
        gitItemViewModel.setLdNotConnection(false);
        gitItemViewModel.setLdRefreshList(0);
        gitItemViewModel.getList();
    }

    @Click(R.id.ibCancelSearch)
    void clickIbCancelSearch(){
        edtSearch.setText("");
        ibCancelSearch.setVisibility(View.GONE);
        edtSearch.clearFocus();
        gitItemViewModel.setLdRefreshList(0);
        gitItemViewModel.getList();
    }
}