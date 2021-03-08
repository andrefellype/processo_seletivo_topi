package br.com.andrefellype.appgithub.main.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.andrefellype.appgithub.R
import br.com.andrefellype.appgithub.main.model.GitItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.cell_git.view.*
import kotlinx.android.synthetic.main.layout_fork_side_star.view.*

class GitAdapter: RecyclerView.Adapter<GitAdapter.ViewHolder>() {

    private var itens: List<GitItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_git, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gitItem = itens.get(position)
        holder.setIsRecyclable(false)
        holder.setIsRecyclable(false)
        holder.tvNameRepo.text = gitItem.name
        holder.tvDescriptionRepo.text = "${if(gitItem.description?.length >= 70) gitItem.description?.substring(0, 70) else gitItem.description}" + "${if (gitItem.description.length > 70)"..." else ""}"
        holder.tvForks.text = gitItem.forks.toString()
        holder.tvStargazers.text = gitItem.stargazers.toString()
        if(gitItem.owner.photo?.isNotEmpty()) {
            val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.ic_baseline_person_24)
                .dontAnimate().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
            Glide.with(holder.itemView.context).load(gitItem.owner.photo).apply(requestOptions).into(holder.ivUser)
        }
        holder.tvUsernameUser.text = gitItem.owner.username
    }

    fun updateAdapter(itens: List<GitItem>){
        this.itens = itens
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itens.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvNameRepo = itemView.tvNameRepo
        val tvDescriptionRepo = itemView.tvDescriptionRepo
        val tvForks = itemView.tvForks
        val tvStargazers = itemView.tvStargazers
        val ivUser = itemView.ivUser
        val tvUsernameUser = itemView.tvUsernameUser
    }
}