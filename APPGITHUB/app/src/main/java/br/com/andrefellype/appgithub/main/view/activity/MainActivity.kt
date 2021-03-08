package br.com.andrefellype.appgithub.main.view.activity

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
import br.com.andrefellype.appgithub.R
import br.com.andrefellype.appgithub.main.model.GitItem
import br.com.andrefellype.appgithub.main.repository.MainRepository
import br.com.andrefellype.appgithub.main.view.adapter.GitAdapter
import br.com.andrefellype.appgithub.main.viewmodel.MainViewModel
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_main.*
import kotlinx.android.synthetic.main.layout_not_connection.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var gitItemViewModel: MainViewModel
    val gitAdapter: GitAdapter = GitAdapter()
    val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
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

        edtSearch.isEnabled = false
        edtSearch.clearFocus()

        rvListGit.setHasFixedSize(false)
        rvListGit.layoutManager = this.layoutManager
        rvListGit.adapter = gitAdapter

        this.gitItemViewModel = ViewModelProvider(this, MainViewModel.MainViewModelFactory(MainRepository()))
                .get(MainViewModel::class.java)

        this.gitItemViewModel.ldGitItem.observe(this, Observer { itens -> this.updateAdapter(itens) } )
        this.gitItemViewModel.ldRefreshList.observe(this, Observer { status -> if(status == 0) this.refreshList() })
        this.gitItemViewModel.ldNotConnection.observe(this, Observer {status -> if(status) notConnection() })

        this.gitItemViewModel.getItens()

        edtSearch.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                gitItemViewModel.getItens(s.toString())
                ibCancelSearch.visibility = if(!s.toString().isEmpty()) View.VISIBLE else View.GONE
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        this.customAlertDialogView = LayoutInflater.from(this).inflate(R.layout.layout_sort_list, null, false)
        dialog = MaterialDialog.Builder(this).customView(customAlertDialogView, false).build()

        showAlert()

        btnAgainConnection.setOnClickListener(this)
        ivSort.setOnClickListener(this)
        ibCancelSearch.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if(p0?.id == R.id.btnAgainConnection) {
            gitItemViewModel.ldNotConnection.value = false
            gitItemViewModel.ldRefreshList.value = 0
            gitItemViewModel.getItens()
        } else if(p0?.id == R.id.ivSort){
            dialog.show()
        } else if(p0?.id == R.id.ibCancelSearch){
            edtSearch.setText("")
            ibCancelSearch.visibility = View.GONE
            edtSearch.clearFocus()
            gitItemViewModel.ldRefreshList.value = 0
            gitItemViewModel.getItens()
        }
    }

    private fun refreshList(){
        if(dialog.isShowing){
            dialog.dismiss()
        }
        viewNotConnect.visibility = View.GONE
        rvListGit.visibility = View.GONE
        pbLoading.visibility = View.VISIBLE
        edtSearch.isEnabled = false
    }

    private fun notConnection(){
        if(dialog.isShowing){
            dialog.dismiss()
        }
        viewMain.visibility = View.GONE
        viewNotConnect.visibility = View.VISIBLE
        pbLoading.visibility = View.GONE
        rvListGit.visibility = View.GONE
        edtSearch.isEnabled = true
    }

    private fun updateAdapter(gitItems: List<GitItem>){
        if(dialog.isShowing){
            dialog.dismiss()
        }
        viewMain.visibility = View.VISIBLE
        rvListGit.visibility = View.VISIBLE
        viewNotConnect.visibility = View.GONE
        pbLoading.visibility = View.GONE
        edtSearch.isEnabled = true
        this.gitAdapter.updateAdapter(gitItems)
    }

    private fun showAlert() {
        this.ivSortNameSelect = this.customAlertDialogView.findViewById(R.id.ivSortNameSelect)
        this.ivSortNameSelect.setOnClickListener { view ->
            dialog.dismiss()
            gitItemViewModel.ldRefreshList.value = 0
            gitItemViewModel.getItens(sort = "name")
        }
        this.ivSortStarSelect = this.customAlertDialogView.findViewById(R.id.ivSortStarSelect)
        this.ivSortStarSelect.setOnClickListener { view ->
            dialog.dismiss()
            gitItemViewModel.ldRefreshList.value = 0
            gitItemViewModel.getItens(sort = "star")
        }
    }
}