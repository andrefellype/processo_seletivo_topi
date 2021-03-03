package io.topi.apptopi.main.view.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.MaterialDialog
import io.topi.apptopi.R
import io.topi.apptopi.main.model.GitItem
import io.topi.apptopi.main.view.adapter.GitItemAdapter
import io.topi.apptopi.main.viewmodel.GitItemViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_main.*
import kotlinx.android.synthetic.main.layout_not_connection.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var gitItemViewModel: GitItemViewModel
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var gitAdapter: GitItemAdapter
    private lateinit var dialog: Dialog
    private lateinit var customAlertDialogView : View
    private lateinit var ivSortNameSelect: ImageView
    private lateinit var ivSortStarSelect: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.apply {
            setCustomView(R.layout.actionbar_layout)
            displayOptions = DISPLAY_SHOW_CUSTOM
        }

        viewNotConnect.visibility = View.GONE
        viewMain.visibility = View.VISIBLE

        edtSearch.isEnabled = false
        edtSearch.clearFocus()

        this.gitAdapter = GitItemAdapter()
        this.layoutManager = LinearLayoutManager(this)
        rvListGit.setHasFixedSize(false)
        rvListGit.layoutManager = this.layoutManager
        rvListGit.adapter = gitAdapter

        this.gitItemViewModel = ViewModelProvider(this).get(GitItemViewModel::class.java)
        this.gitItemViewModel.getList()

        gitItemViewModel.ldGitItem.observe(this, Observer<ArrayList<GitItem>> {item -> this.updateAdapter(item) })
        gitItemViewModel.ldRefreshList.observe(this, Observer { item -> this.refreshList(item) })
        gitItemViewModel.ldNotConnection.observe(this, Observer { item -> this.notConnection(item) })

        this.edtSearch.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                gitItemViewModel.getList(p0.toString())
                ibCancelSearch.visibility = if (!p0.toString().isEmpty()) View.VISIBLE else View.GONE
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        })

        this.srlRefreshList.setOnRefreshListener { object: SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                gitItemViewModel.ldRefreshList.value = 0
                gitItemViewModel.getList()
            }

        } }

        this.customAlertDialogView = LayoutInflater.from(this).inflate(R.layout.layout_sort_list, null, false)
        dialog = MaterialDialog.Builder(this).customView(customAlertDialogView, false).build()

        this.btnAgainConnection.setOnClickListener(this)
        this.ivSort.setOnClickListener(this)
        this.ibCancelSearch.setOnClickListener(this)

        showAlert()
    }

    override fun onClick(p0: View?) {
        if(p0?.id == R.id.btnAgainConnection) {
            gitItemViewModel.ldNotConnection.value = false
            gitItemViewModel.ldRefreshList.value = 0
            gitItemViewModel.getList()
        } else if(p0?.id == R.id.ivSort){
            dialog.show()
        } else if(p0?.id == R.id.ibCancelSearch){
            edtSearch.setText("")
            ibCancelSearch.visibility = View.GONE
            edtSearch.clearFocus()
            gitItemViewModel.ldRefreshList.value = 0
            gitItemViewModel.getList()
        }
    }

    private fun showAlert() {
        this.ivSortNameSelect = this.customAlertDialogView.findViewById(R.id.ivSortNameSelect)
        this.ivSortNameSelect.setOnClickListener({view ->
            dialog.dismiss()
            gitItemViewModel.ldRefreshList.value = 0
            gitItemViewModel.getList("", "name")
        })
        this.ivSortStarSelect = this.customAlertDialogView.findViewById(R.id.ivSortStarSelect)
        this.ivSortStarSelect.setOnClickListener({view ->
            dialog.dismiss()
            gitItemViewModel.ldRefreshList.value = 0
            gitItemViewModel.getList("", "star")
        })
    }

    fun refreshList(status: Int){
        if(status == 0) {
            if(srlRefreshList.isRefreshing) {
                srlRefreshList.isRefreshing = false
            }
            if (this.dialog.isShowing) {
                this.dialog.dismiss()
            }
            if (viewNotConnect.visibility == View.VISIBLE) {
                viewNotConnect.visibility = View.GONE
            }
            if (pbLoading.visibility == View.GONE) {
                pbLoading.visibility = View.VISIBLE
            }
            if (viewRlLoading.visibility == View.GONE) {
                viewRlLoading.visibility = View.VISIBLE
            }
            if(rvListGit.visibility == View.VISIBLE){
                rvListGit.visibility = View.GONE
            }
            edtSearch.isEnabled = false
        } else if(status == 1) {
            if (this.dialog.isShowing) {
                this.dialog.dismiss()
            }
            if (viewMain.visibility == View.GONE) {
                viewMain.visibility = View.VISIBLE
            }
            if (pbLoading.visibility == View.VISIBLE) {
                pbLoading.visibility = View.GONE
            }
            if (viewRlLoading.visibility == View.VISIBLE) {
                viewRlLoading.visibility = View.GONE
            }
            if (rvListGit.visibility == View.GONE) {
                rvListGit.visibility = View.VISIBLE
            }
            edtSearch.isEnabled = true
        }
    }

    fun notConnection(status: Boolean)  {
        if(!status) {
            if (viewMain.visibility == View.GONE) {
                viewMain.visibility = View.VISIBLE
            }
            if (pbLoading.visibility == View.VISIBLE) {
                pbLoading.visibility = View.GONE
            }
            if (viewRlLoading.visibility == View.VISIBLE) {
                viewRlLoading.visibility = View.GONE
            }
            if (rvListGit.visibility == View.GONE) {
                rvListGit.visibility = View.VISIBLE
            }
            edtSearch.isEnabled = true
        } else {
            if (viewMain.visibility == View.VISIBLE) {
                viewMain.visibility = View.GONE
            }
            if (viewNotConnect.visibility == View.GONE) {
                viewNotConnect.visibility = View.VISIBLE
            }
            if (pbLoading.visibility == View.VISIBLE) {
                pbLoading.visibility = View.GONE
            }
            if (viewRlLoading.visibility == View.VISIBLE) {
                viewRlLoading.visibility = View.GONE
            }
            if (rvListGit.visibility == View.GONE) {
                rvListGit.visibility = View.VISIBLE
            }
            edtSearch.isEnabled = true
        }
    }

    fun updateAdapter(gitItems: ArrayList<GitItem>){
        this.gitAdapter.updateAdapter(gitItems)
    }
}