package com.iamsalih.uq_app;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by muhammedsalihguler on 15/03/2017.
 */

public class LandingActivity extends Activity implements View.OnClickListener{

    public static final String EXTRA_USERNAME = "username";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_landing_view);
        Button submitButton = (Button)findViewById(R.id.to_name_section);
        submitButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.to_name_section: {
                createUsernameDialog();
                break;
            }
        }
    }

    private void createUsernameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View rootView = getLayoutInflater().inflate(R.layout.alert_dialog_username,null);
        final EditText usernameInput = (EditText)rootView.findViewById(R.id.username_text_input);
        builder.setView(rootView);
        builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       String username = usernameInput.getText().toString();
                       if(TextUtils.isEmpty(username))
                           Toast.makeText(LandingActivity.this,getString(R.string.fill_field_message),Toast.LENGTH_SHORT).show();
                       else {
                           Intent intent = new Intent(LandingActivity.this, MainActivity.class);
                           intent.putExtra(EXTRA_USERNAME,username);
                           dialog.dismiss();
                           startActivity(intent);
                           finish();
                       }
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // Send the negative button event back to the host activity
                       dialog.dismiss();
                   }
               });
        Dialog dialog = builder.create();
        dialog.show();
    }
}
