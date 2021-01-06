package io.topi.apptopi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

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
    @ViewById
    protected ImageView ivUser;

    @AfterViews
    protected void initMain() {
        getSupportActionBar().setTitle(getString(R.string.title_repositorio));

        GitItem gitItem = (GitItem) getIntent().getExtras().getSerializable("git_bundle");

        if(gitItem != null){
            edtNameRepository.setText(gitItem.getName());
            tvForks.setText(Integer.toString(gitItem.getForks()));
            tvStargazers.setText(Integer.toString(gitItem.getStargazers_count()));
            edtDescriptionRepository.setText(gitItem.getDescription());
            if(gitItem.getOwner() != null) {
                if(gitItem.getOwner().getAvatar_url() != null && gitItem.getOwner().getAvatar_url().length() > 0) {
                    RequestOptions requestOptions = new RequestOptions()
                            .placeholder(R.drawable.person_circle)
                            .dontAnimate().centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL);
                    Glide.with(getBaseContext()).load(gitItem.getOwner().getAvatar_url())
                            .apply(requestOptions).into(ivUser);
                }
                tvUsernameUser.setText(gitItem.getOwner().getLogin());
            }
        }
    }

    @Click(R.id.btnBack)
    void clickBack(){
        onBackPressed();
    }
}