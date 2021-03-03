package io.topi.apptopi.main.view.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import io.topi.apptopi.R
import io.topi.apptopi.main.model.GitItem
import kotlinx.android.synthetic.main.activity_more_details.*
import kotlinx.android.synthetic.main.layout_fork_side_star.*

class MoreDetailsActivity : AppCompatActivity() {

    companion object Factory {
        fun showActivity(context: Context, gitItem: GitItem) {
            var intent = Intent(context, MoreDetailsActivity::class.java)
            intent.putExtra("git_bundle", gitItem)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_details)
        supportActionBar?.setTitle(getString(R.string.title_repositorio))

        var gitItem: GitItem = intent.extras?.getSerializable("git_bundle") as GitItem

        if(gitItem != null){
            edtNameRepository.setText(gitItem.name)
            tvForks.setText(Integer.toString(gitItem.forks))
            tvStargazers.setText(Integer.toString(gitItem.stargazers_count))
            edtDescriptionRepository.setText(gitItem.description)
            if(gitItem.owner != null) {
                if(gitItem.owner.avatar_url != null && gitItem.owner.avatar_url.isNotEmpty()) {
                    var requestOptions = RequestOptions()
                            .placeholder(R.drawable.person_circle)
                            .dontAnimate().centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    Glide.with(getBaseContext()).load(gitItem.owner.avatar_url)
                            .apply(requestOptions).into(ivUser)
                }
                tvUsernameUser.setText(gitItem.owner.login)
            }
        }

        btnBack.setOnClickListener({ onBackPressed() })
    }
}