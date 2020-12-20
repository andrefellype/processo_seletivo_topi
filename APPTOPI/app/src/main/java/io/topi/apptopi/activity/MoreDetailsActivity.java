package io.topi.apptopi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.topi.apptopi.R;
import io.topi.apptopi.model.GitItem;

public class MoreDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);
        getSupportActionBar().setTitle(getString(R.string.title_repositorio));

        GitItem gitItem = (GitItem) getIntent().getExtras().getSerializable("git_bundle");

        EditText edtNameRepository = (EditText) this.findViewById(R.id.edt_name_repository);
        TextView tvForks = (TextView) this.findViewById(R.id.tv_forks);
        TextView tvStargazers = (TextView) this.findViewById(R.id.tv_stargazers);
        EditText edtDescriptionRepository = (EditText) this.findViewById(R.id.edt_description_repository);
        TextView tvUsernameUser = (TextView) this.findViewById(R.id.tv_username_user);
        Button btnBack = (Button) this.findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoreDetailsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if(gitItem != null){
            edtNameRepository.setText(gitItem.getName());
            tvForks.setText(Integer.toString(gitItem.getForks()));
            tvStargazers.setText(Integer.toString(gitItem.getStargazers_count()));
            edtDescriptionRepository.setText(gitItem.getDescription());
            tvUsernameUser.setText(gitItem.getOwner().getLogin());
        }
    }
}