package io.topi.apptopi.service;

import android.app.Service;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.topi.apptopi.model.GitItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GitService {

    public interface GitApiCallback {
        void onSucess(List<GitItem> gitItems);
    }

    public static void getGitApi(final GitApiCallback gitApiCallback) {
        ServiceConfig.getRetrofit().create(GitServiceRetrofit.class).getList()
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Type typeList = new TypeToken<List<GitItem>>() {}.getType();
                        Gson gson = new Gson();
                        List<GitItem> gitItems = (List<GitItem>) gson.fromJson(response.body().getAsJsonArray("items").toString(), typeList);
                        gitApiCallback.onSucess(gitItems);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e("FAIL_APP", "FAIL");
                        Log.e("FAIL_APP", t.getMessage());
                    }
                });
    }

}
