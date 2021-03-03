package io.topi.apptopi.main.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import io.topi.apptopi.R
import io.topi.apptopi.main.model.GitItem
import io.topi.apptopi.main.view.activity.MoreDetailsActivity
import kotlinx.android.synthetic.main.cell_git.view.*
import kotlinx.android.synthetic.main.layout_fork_side_star.view.*

class GitItemAdapter: RecyclerView.Adapter<GitItemAdapter.ViewHolder>() {

    private var itens: ArrayList<GitItem> = arrayListOf()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gitItem = itens.get(position)
        holder.setIsRecyclable(false)
        holder.tvNameRepo.text = gitItem.name
        holder.tvDescriptionRepo.text = if(gitItem.description != null)
            "${if(gitItem.description.length > 70) gitItem.description.substring(0, 70) else gitItem.description }" +
                    "${if (gitItem.description.length > 70)"..." else ""}" else ""
        holder.tvForks.text = gitItem.forks.toString()
        holder.tvStargazers.text = gitItem.stargazers_count.toString()

        if(gitItem.owner.avatar_url.isNotEmpty()) {
            val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.person_circle)
                    .dontAnimate().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
            Glide.with(holder.itemView.context).load(gitItem.owner.avatar_url)
                    .apply(requestOptions).into(holder.ivUser)
        }
        holder.tvUsernameUser.text = gitItem.owner.login

        holder.tvMoreDetails.setOnClickListener {view ->
            MoreDetailsActivity.showActivity(holder.itemView.context, gitItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_git, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itens.size
    }

    fun updateAdapter(gitItems: ArrayList<GitItem>){
        this.itens = gitItems;
        notifyDataSetChanged();
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvNameRepo = itemView.tvNameRepo
        val tvDescriptionRepo = itemView.tvDescriptionRepo
        val tvForks = itemView.tvForks
        val tvStargazers = itemView.tvStargazers
        val ivUser = itemView.ivUser
        val tvUsernameUser = itemView.tvUsernameUser
        val tvMoreDetails = itemView.tvMoreDetails
    }
}