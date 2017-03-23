package com.iamsalih.uq_app;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_CATEGORY = "category";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView categoryMessage = (TextView)findViewById(R.id.category_title);
        String username = "";
        if(getIntent().getExtras() != null) {
            username = getIntent().getExtras().getString(LandingActivity.EXTRA_USERNAME);
        }
        getSupportActionBar().setTitle(getString(R.string.categories));
        categoryMessage.setText(getString(R.string.pick_category_message,username));
        CardView sportButton = (CardView)findViewById(R.id.category_sport);
        sportButton.setOnClickListener(this);
        CardView artButton = (CardView)findViewById(R.id.category_art);
        artButton.setOnClickListener(this);
        CardView historyButton = (CardView)findViewById(R.id.category_history);
        historyButton.setOnClickListener(this);
        CardView scienceButton = (CardView)findViewById(R.id.category_Science);
        scienceButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String category = "";
        switch (view.getId()) {
            case R.id.category_sport:
                category = getString(R.string.sport_category_name);
                break;
            case R.id.category_art:
                category =getString(R.string.art_category_name);
                break;
            case R.id.category_history:
                category = getString(R.string.history_category_name);
                break;
            case R.id.category_Science:
                category = getString(R.string.science_category_name);
                break;

        }
        Intent intent = new Intent(MainActivity.this,QuizActivity.class);
        intent.putExtra(EXTRA_CATEGORY, category);
        startActivity(intent);
    }
}
