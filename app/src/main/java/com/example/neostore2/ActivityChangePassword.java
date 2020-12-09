package com.example.neostore2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityChangePassword extends AppCompatActivity {
    private RetroViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        model = new ViewModelProvider(this).get(RetroViewModel.class);


        Button submit = findViewById(R.id.btSubmit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText oldpasswordbox = findViewById(R.id.etOldPassword);
                EditText passwordbox = findViewById(R.id.etPassword);
                EditText confirmbox = findViewById(R.id.etPasswordConfirm);

                String token = HelperShared.Helper.getInstance(getApplicationContext()).fetchUser().getToken();
                String old = oldpasswordbox.getText().toString().trim();
                String password = passwordbox.getText().toString().trim();
                String confirm = confirmbox.getText().toString().trim();

                model.getChangePassword(token, old, password, confirm).observe(ActivityChangePassword.this, new Observer<ResponseChangePassword>() {
                    @Override
                    public void onChanged(ResponseChangePassword responseChangePassword) {
                        Toast.makeText(ActivityChangePassword.this,responseChangePassword.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });
    }
}