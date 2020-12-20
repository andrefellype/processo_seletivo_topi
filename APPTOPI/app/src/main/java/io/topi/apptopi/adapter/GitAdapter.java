package io.topi.apptopi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import io.topi.apptopi.R;
import io.topi.apptopi.model.GitItem;

public class GitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<GitItem> lista;
    private RecyclerViewGitListener recyclerViewGitListener;

    public GitAdapter(Context context, List<GitItem> lista, RecyclerViewGitListener<GitItem> recyclerViewGitListener){
        this.context = context;
        this.lista = new ArrayList<>(lista);
        this.recyclerViewGitListener = recyclerViewGitListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderGit(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_git, parent, false));
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, final int position) {
        ViewHolderGit viewHolderGit = (ViewHolderGit) vh;

        GitItem gitItem = lista.get(position);

        viewHolderGit.tvNameRepo.setText(gitItem.getName());

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
        viewHolderGit.tvDescriptionRepo.setText(description);
        viewHolderGit.tvForks.setText(Integer.toString(gitItem.getForks()));
        viewHolderGit.tvStargazers.setText(Integer.toString(gitItem.getStargazers_count()));

        if(gitItem.getOwner() != null) {
            if(gitItem.getOwner().getAvatar_url() != null && gitItem.getOwner().getAvatar_url().length() > 0) {
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.person_circle)
                        .dontAnimate().centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(context).load(gitItem.getOwner().getAvatar_url())
                        .apply(requestOptions).into(viewHolderGit.ivUser);
            }
            viewHolderGit.tvUsernameUser.setText(gitItem.getOwner().getLogin());
        }

        viewHolderGit.tvMoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewGitListener.onItemClick(lista.get(position), position);
            }
        });
    }

    private int listPosition(int position) {
        return position - 1;
    }

    class ViewHolderGit extends RecyclerView.ViewHolder {

        public ViewHolderGit(View itemView) {
            super(itemView);
            cvGit = (MaterialCardView) itemView.findViewById(R.id.cv_git);
            tvNameRepo = (TextView) itemView.findViewById(R.id.tv_name_repo);
            tvDescriptionRepo = (TextView) itemView.findViewById(R.id.tv_description_repo);
            tvForks = (TextView) itemView.findViewById(R.id.tv_forks);
            tvStargazers = (TextView) itemView.findViewById(R.id.tv_stargazers);
            ivUser = (ImageView) itemView.findViewById(R.id.iv_user);
            tvUsernameUser = (TextView) itemView.findViewById(R.id.tv_username_user);
            tvMoreDetails = (TextView) itemView.findViewById(R.id.tv_more_details);
        }

        public MaterialCardView cvGit;
        public TextView tvNameRepo;
        public TextView tvDescriptionRepo;
        public TextView tvForks;
        public TextView tvStargazers;
        public ImageView ivUser;
        public TextView tvUsernameUser;
        public TextView tvMoreDetails;
    }
}