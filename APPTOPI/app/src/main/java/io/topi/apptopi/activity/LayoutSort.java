package io.topi.apptopi.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import io.topi.apptopi.R;
import io.topi.apptopi.viewmodel.GitItemViewModel;

@EViewGroup(R.layout.layout_sort_list)
public class LayoutSort extends LinearLayout {

    @ViewById
    protected LinearLayout llSortName;

    @ViewById
    protected LinearLayout llSortStar;

    private GitItemViewModel gitItemViewModel;

    public LayoutSort(Context context) {
        super(context);
        gitItemViewModel = new ViewModelProvider((FragmentActivity) context).get(GitItemViewModel.class);
    }

    @Click(R.id.llSortName)
    void clickSortName(){
        gitItemViewModel.setLdRefreshList(0);
        gitItemViewModel.getListSortName();
    }

    @Click(R.id.llSortStar)
    void clickSortStar(){
        gitItemViewModel.setLdRefreshList(0);
        gitItemViewModel.getListSortStar();
    }
}
