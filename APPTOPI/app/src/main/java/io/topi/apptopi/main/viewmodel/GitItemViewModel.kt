package io.topi.apptopi.main.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.topi.apptopi.main.model.GitItem
import io.topi.apptopi.main.repositorio.GitService
import kotlin.collections.ArrayList

class GitItemViewModel : ViewModel() {

    var ldGitItem: MutableLiveData<ArrayList<GitItem>> = MutableLiveData()
    var ldRefreshList: MutableLiveData<Int> = MutableLiveData()
    var ldNotConnection: MutableLiveData<Boolean> = MutableLiveData()

    fun getList(search: String = "", typeSort: String = ""){
        GitService.getGitApi("language:Java", "stars", "1", object: GitService.GitApiCallback{
            override fun onSucess(gitItems: ArrayList<GitItem>) {
                if(search.length == 0 && typeSort.length == 0) {
                    showList(gitItems)
                } else {
                    if(search.length > 0) {
                        searchList(search, typeSort, gitItems)
                    } else if(typeSort.length > 0) {
                        sortList(typeSort, gitItems)
                    }
                }
            }

            override fun onNotConnection() {
                showNotConnection()
            }
        })
    }

    fun searchList(search: String, typeSort: String = "", gitItems: ArrayList<GitItem>){
        var gitItemsNew: ArrayList<GitItem> = arrayListOf()
        for (gitItem in gitItems) {
            val posNameRepo = gitItem.name.indexOf(search)
            val posNameOwner = gitItem.owner.login.indexOf(search)
            if (posNameRepo >= 0 || posNameOwner >= 0) {
                gitItemsNew.add(gitItem)
            }
        }
        if(typeSort.length == 0) {
            showList(gitItemsNew)
        } else {
            sortList(typeSort, gitItemsNew)
        }
    }

    fun sortList(typeSort: String, gitItems: ArrayList<GitItem>){
        if (typeSort.equals("name")) {
            gitItems.sortWith(compareBy({ item -> item.name }))
        } else if(typeSort.equals("star")) {
            gitItems.sortWith(compareBy({ item -> item.stargazers_count }))
        }
        showList(gitItems)
    }

    fun showList(gitItems: ArrayList<GitItem>){
        ldGitItem.value = arrayListOf<GitItem>();
        ldGitItem.value = gitItems
        ldRefreshList.value = 1
        ldNotConnection.value = false
    }

    fun showNotConnection(){
        ldRefreshList.value = 1
        ldNotConnection.value = true
    }
}