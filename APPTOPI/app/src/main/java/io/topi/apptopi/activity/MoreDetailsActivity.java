package io.topi.apptopi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import io.topi.apptopi.R;
import io.topi.apptopi.model.GitItem;

@EActivity(R.layout.activity_more_details)
public class MoreDetailsActivity extends AppCompatActivity {

    @ViewById
    protected EditText edtNameRepository;
    @ViewById
    protected TextView tvForks;
    @ViewById
    protected TextView tvStargazers;
    @ViewById
    protected EditText edtDescriptionRepository;
    @ViewById
    protected TextView tvUsernameUser;

    @AfterViews
    protected void initMain() {
        getSupportActionBar().setTitle(getString(R.string.title_repositorio));

        GitItem gitItem = (GitItem) getIntent().getExtras().getSerializable("git_bundle");

        if(gitItem != null){
            edtNameRepository.setText(gitItem.getName());
            tvForks.setText(Integer.toString(gitItem.getForks()));
            tvStargazers.setText(Integer.toString(gitItem.getStargazers_count()));
            edtDescriptionRepository.setText(gitItem.getDescription());
            tvUsernameUser.setText(gitItem.getOwner().getLogin());
        }
    }

    @Click(R.id.btnBack)
    void clickBack(){
        onBackPressed();
    }
}