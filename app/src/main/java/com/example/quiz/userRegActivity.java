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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class userRegActivity extends AppCompatActivity {

    EditText emailaddress,password1, confirmpassword1;
    TextView loginpage;
    private FirebaseAuth mAuth;
    private Dialog loadingDialog;
    private Button registerUs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg);

        //take text from design
        emailaddress=findViewById(R.id.regemailaddress1);
        password1=findViewById(R.id.regpassword1);
        confirmpassword1=findViewById(R.id.regcpassword1);
        registerUs = findViewById(R.id.buttonreg);
        //take text from design

        loginpage = findViewById(R.id.teew5);


        //loading dialog
        loadingDialog = new Dialog(userRegActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //loading dialog

        mAuth = FirebaseAuth.getInstance();

        loginpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(userRegActivity.this,login.class);
                startActivity(intent);
                finish();
            }
        });


        //register button onclick
        registerUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


        //check for empty email or password
        if(emailaddress.getText().toString().isEmpty()){
            emailaddress.setError("Enter email");
            return;
        }


                //validate email
                String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(emailaddress.getText().toString());


                if (!matcher.matches()) {
                    Toast.makeText(userRegActivity.this,"Invalid email",Toast.LENGTH_SHORT).show();
                    return;
                }
                //validate email


                if(password1.getText().toString().isEmpty()){
            password1.setError("Enter Password");
            return;
        }
        if(confirmpassword1.getText().toString().isEmpty()){
            confirmpassword1.setError("Enter Password Again");
            return;
        }
        //check for empty email or password



        //login
        if(password1.getText().toString().equals(confirmpassword1.getText().toString())){
            registerNewUser();
        }
        else{
            // password and confirm password do not match
            confirmpassword1.setError("Passwords do not match...");
            return;
        }
            }
        });


    }









    //create user
    private void registerNewUser()
    {

        // show the visibility of progress bar to show loading
       loadingDialog.show();

        // Take the value of two edit texts in Strings

        // Validations for input email and password

        emailaddress=findViewById(R.id.regemailaddress1);
        password1=findViewById(R.id.regpassword1);

        String email, password;
        email = emailaddress.getText().toString();
        password = password1.getText().toString();


        // create new user or register new user
        mAuth
                .createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            loadingDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(userRegActivity.this, login.class);
                            startActivity(intent);
                            finish();
                        }
                        else {

                            loadingDialog.dismiss();
                            // Registration failed
                            Toast.makeText(getApplicationContext(), "Registration failed!!" + " Please try again ", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                });
       //loadingDialog.dismiss();
    }
    //create user


}