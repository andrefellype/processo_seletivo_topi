package io.topi.apptopi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import io.topi.apptopi.R;
import io.topi.apptopi.model.GitItem;

@EBean
public class GitAdapter extends RecyclerViewAdapterBase<GitItem, GitItemAdapter> {

    @RootContext
    Context context;

    @AfterInject
    void initAdapter(){
        itens = new ArrayList<GitItem>();
    }

    @Override
    protected GitItemAdapter onCreateItemView(ViewGroup parent, int viewType) {
        return GitItemAdapter_.build(context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewWrapper<GitItemAdapter> holder, int position) {
        GitItemAdapter view = holder.getView();
        GitItem gitItem = itens.get(position);
        view.bind(gitItem);
    }

    public void updateAdapter(List<GitItem> gitItems){
        this.itens = gitItems;
        notifyDataSetChanged();
    }
}