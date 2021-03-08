package br.com.andrefellype.appgithub.main.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.andrefellype.appgithub.main.model.GitItem
import br.com.andrefellype.appgithub.main.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepository): ViewModel() {

    var ldGitItem: MutableLiveData<List<GitItem>> = MutableLiveData()
    var ldRefreshList: MutableLiveData<Int> = MutableLiveData()
    var ldNotConnection: MutableLiveData<Boolean> = MutableLiveData()

    fun getItens(search: String = "", sort: String = "") {
        CoroutineScope(Dispatchers.Main).launch {
            mainRepository.getList(object: MainRepository.GitApiCallback{
                override fun onNotConnection() {
                    showNotConnection()
                }

                override fun onSucess(gitItems: List<GitItem>) {
                    if(search.isEmpty() && sort.isEmpty()){
                        showList(gitItems)
                    } else {
                        if(!search.isEmpty()){
                            searchList(search, sort, gitItems)
                        } else {
                            sortList(sort, gitItems)
                        }
                    }
                }
            })
        }
    }

    fun searchList(search: String, typeSort: String = "", gitItems: List<GitItem>){
        var gitItemsNew: List<GitItem> = listOf()
        for (gitItem in gitItems) {
            val posNameRepo = gitItem.name.indexOf(search)
            val posNameOwner = gitItem.owner.username.indexOf(search)
            if (posNameRepo >= 0 || posNameOwner >= 0) {
                gitItemsNew += gitItem
            }
        }
        if(typeSort.isEmpty()) {
            showList(gitItemsNew)
        } else {
            sortList(typeSort, gitItemsNew)
        }
    }

    fun sortList(typeSort: String, gitItems: List<GitItem>){
        var gitArray: ArrayList<GitItem> = arrayListOf()
        for(item in gitItems){
            gitArray.add(item)
        }
        if (typeSort == "name") {
            gitArray.sortWith(compareBy({ item -> item.name }))
        } else if(typeSort == "star") {
            gitArray.sortWith(compareBy({ item -> item.stargazers }))
        }
        var itensNew: List<GitItem> = listOf()
        for(item in gitArray){
            itensNew += item
        }
        showList(itensNew)
    }

    fun showList(gitItems: List<GitItem>){
        ldGitItem.value = listOf();
        ldGitItem.value = gitItems
        ldRefreshList.value = 1
        ldNotConnection.value = false
    }

    fun showNotConnection(){
        ldRefreshList.value = 1
        ldNotConnection.value = true
    }

    class MainViewModelFactory(private val mainRepository: MainRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(mainRepository) as T
        }
    }

}