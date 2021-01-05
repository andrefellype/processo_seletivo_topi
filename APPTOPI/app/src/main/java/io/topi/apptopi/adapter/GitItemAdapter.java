package io.topi.apptopi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import io.topi.apptopi.R;
import io.topi.apptopi.activity.MainActivity;
import io.topi.apptopi.activity.MoreDetailsActivity;
import io.topi.apptopi.activity.MoreDetailsActivity_;
import io.topi.apptopi.model.GitItem;

@EViewGroup(R.layout.cell_git)
public class GitItemAdapter extends RelativeLayout {

    @ViewById
    protected TextView tvNameRepo;
    @ViewById
    protected TextView tvDescriptionRepo;
    @ViewById
    protected TextView tvForks;
    @ViewById
    protected TextView tvStargazers;
    @ViewById
    protected ImageView ivUser;
    @ViewById
    protected TextView tvUsernameUser;
    @ViewById
    protected TextView tvMoreDetails;

    public GitItemAdapter(Context context){
        super(context);
    }

    public void bind(GitItem gitItem){
        tvNameRepo.setText(gitItem.getName());
        int limitDescription = 70;
        String description = "";
        for(int d=0; d<gitItem.getDescription().length(); d++){
            if(d<limitDescription){
                description += (gitItem.getDescription().charAt(d) + "");
            }
        }
        if(gitItem.getDescription().length() > description.length()){
            description += "...";
        }
        tvDescriptionRepo.setText(description);
        tvForks.setText(Integer.toString(gitItem.getForks()));
        tvStargazers.setText(Integer.toString(gitItem.getStargazers_count()));

        if(gitItem.getOwner() != null) {
            if(gitItem.getOwner().getAvatar_url() != null && gitItem.getOwner().getAvatar_url().length() > 0) {
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.person_circle)
                        .dontAnimate().centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(getContext()).load(gitItem.getOwner().getAvatar_url())
                        .apply(requestOptions).into(ivUser);
            }
            tvUsernameUser.setText(gitItem.getOwner().getLogin());
        }
        tvMoreDetails.setOnClickListener(v -> {
            MoreDetailsActivity_.intent(getContext()).extra("git_bundle", gitItem).start();
        });
    }
}
