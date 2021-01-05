package io.topi.apptopi.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.topi.apptopi.model.GitItem;
import io.topi.apptopi.service.GitService;

public class GitItemViewModel extends ViewModel {

    private MutableLiveData<List<GitItem>> ldGitItem;
    private MutableLiveData<Integer> ldRefreshList;
    private MutableLiveData<Boolean> ldNotConnection;

    public GitItemViewModel(){
        ldGitItem = new MutableLiveData<>();
        ldRefreshList = new MutableLiveData<>();
        ldNotConnection = new MutableLiveData<>();
        ldRefreshList.setValue(-1);
        getList();
    }

    public LiveData<List<GitItem>> getGitItens(){
        if(ldGitItem == null){
            ldGitItem = new MutableLiveData<>();
        }
        return ldGitItem;
    }

    public LiveData<Integer> getRefreshList(){
        if(ldRefreshList == null){
            ldRefreshList = new MutableLiveData<>();
        }
        return ldRefreshList;
    }

    public LiveData<Boolean> getNotConnection(){
        if(ldNotConnection == null){
            ldNotConnection = new MutableLiveData<>();
        }
        return ldNotConnection;
    }

    public void setLdRefreshList(Integer status){
        ldRefreshList.setValue(status);
    }

    public void setLdNotConnection(Boolean status){
        ldNotConnection.setValue(status);
    }

    public void getList(){
        GitService.getGitApi("language:Java", "stars", "1", new GitService.GitApiCallback() {
            @Override
            public void onSucess(List<GitItem> gitItems) {
                ldGitItem.setValue(gitItems);
                ldRefreshList.setValue(1);
                ldNotConnection.setValue(false);
            }

            @Override
            public void onNotConnection() {
                ldRefreshList.setValue(1);
                ldNotConnection.setValue(true);
            }
        });
    }

    public void getListSearch(String search){
        GitService.getGitApi("language:Java", "stars", "1", new GitService.GitApiCallback() {
            @Override
            public void onSucess(List<GitItem> gitItems) {
                List<GitItem> gitItemsNew = new ArrayList<>();
                for (GitItem gitItem : gitItems) {
                    int posNameRepo = gitItem.getName().indexOf(search);
                    int posNameOwner = gitItem.getOwner().getLogin().indexOf(search);
                    if (posNameRepo >= 0 || posNameOwner >= 0) {
                        gitItemsNew.add(gitItem);
                    }
                }
                ldGitItem.setValue(new ArrayList<>());
                ldGitItem.setValue(gitItemsNew);
                ldRefreshList.setValue(1);
                ldNotConnection.setValue(false);
            }

            @Override
            public void onNotConnection() {
                ldRefreshList.setValue(1);
                ldNotConnection.setValue(true);
            }
        });
    }

    public void getListSortName(){
        GitService.getGitApi("language:Java", "stars", "1", new GitService.GitApiCallback() {
            @Override
            public void onSucess(List<GitItem> gitItems) {
                ldGitItem.setValue(new ArrayList<>());
                Collections.sort(gitItems, new Comparator<GitItem>() {
                    @Override
                    public int compare(GitItem o1, GitItem o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                ldGitItem.setValue(gitItems);
                ldRefreshList.setValue(1);
                ldNotConnection.setValue(false);
            }

            @Override
            public void onNotConnection() {
                ldRefreshList.setValue(1);
                ldNotConnection.setValue(true);
            }
        });
    }

    public void getListSortStar(){
        GitService.getGitApi("language:Java", "stars", "1", new GitService.GitApiCallback() {
            @Override
            public void onSucess(List<GitItem> gitItems) {
                Collections.sort(gitItems, new Comparator<GitItem>() {
                    @Override
                    public int compare(GitItem o1, GitItem o2) {
                        if(o1.getStargazers_count() > o2.getStargazers_count()) {
                            return 1;
                        } else if( o1.getStargazers_count() < o2.getStargazers_count()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
                ldGitItem.setValue(new ArrayList<>());
                ldGitItem.setValue(gitItems);
                ldRefreshList.setValue(1);
                ldNotConnection.setValue(false);
            }

            @Override
            public void onNotConnection() {
                ldRefreshList.setValue(1);
                ldNotConnection.setValue(true);
            }
        });
    }
}
