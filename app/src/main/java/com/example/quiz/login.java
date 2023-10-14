package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class login extends AppCompatActivity {

    TextView txtviewToReg;
    private Dialog loadingDialog;
    private EditText loginemail;
    private EditText loginpass;
    FirebaseAuth mAuth;
    private Button submitLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        txtviewToReg=findViewById(R.id.txtviewToReg);

        //loading dialog
        loadingDialog = new Dialog(login.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //loading dialog
        mAuth = FirebaseAuth.getInstance();

        //login user
        submitLogin = findViewById(R.id.logsub);
        submitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount();
            }
        });
        //login user


        txtviewToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(login.this,userRegActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }



    //login user
    private void loginUserAccount()
    {



        // show the visibility of progress bar to show loading
        //progressbar.setVisibility(View.VISIBLE);
        loadingDialog.show();
        // Take the value of two edit texts in Strings
        String email, password;
        loginemail = findViewById(R.id.logemail);
        loginpass = findViewById(R.id.logpass);
        email = loginemail.getText().toString();
        password = loginpass.getText().toString();




        //validate email
        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(loginemail.getText().toString());


        if (!matcher.matches()) {
            Toast.makeText(login.this,"Invalid email",Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss();
            return;
        }
        //validate email




        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email!!",
                    Toast.LENGTH_LONG)
                    .show();
            loadingDialog.dismiss();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();
            loadingDialog.dismiss();
            return;
        }

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {
                                    loadingDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),
                                            "Login successful!!",
                                            Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    //progressBar.setVisibility(View.GONE);

                                    // if sign-in is successful
                                    // intent to home activity
                                    Intent intent = new Intent(login.this, categoryActivity.class);
                                    startActivity(intent);
                                }

                                else {

                                    // sign-in failed
                                    loadingDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Login failed!!", Toast.LENGTH_LONG).show();

                                    return;
                                }
                            }
                        });

    }
}