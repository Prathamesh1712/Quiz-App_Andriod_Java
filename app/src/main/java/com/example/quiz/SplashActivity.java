package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class SplashActivity extends AppCompatActivity {

    private TextView appName;

    public static List<CategoryModel> catList = new ArrayList<>();
    public static int  selected_cat_index = 0 ;

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appName=findViewById(R.id.appName);
        Typeface typeface = ResourcesCompat.getFont(this,R.font.blacklist);
        appName.setTypeface(typeface);

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.myanim);
        appName.setAnimation(anim);

        firestore = FirebaseFirestore.getInstance();


        new Thread(new Runnable() {
            @Override
            public void run() {
                    //sleep(3000);
                    loadData();
            }
        }).start();
    }
    private void loadData(){
        catList.clear();
        firestore.collection("quiz").document("Categories")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    DocumentSnapshot doc = task.getResult();

                    if(doc.exists()){
                        long count = (long)doc.get("COUNT");
                        for(int i=1; i<=count;i++){
                            String catName = doc.getString("CAT" + String.valueOf(i)+"_NAME");
                            String catID = doc.getString("CAT" + String.valueOf(i)+"_ID");
                            catList.add(new CategoryModel(catID,catName));
                        }

                       Intent intent=new Intent(SplashActivity.this,login.class);
                        startActivity(intent);
                        SplashActivity.this.finish();



                        /*remove start button
                        if(firebaseAuth.getCurrentUser()!=null){
                            Intent intent = new Intent(SplashActivity.this,categoryActivity.class);
                            startActivity(intent);
                            SplashActivity.this.finish();
                        }
                        else {
                            //if already logged in user
                            Intent intent = new Intent(SplashActivity.this, login.class);
                            startActivity(intent);
                            SplashActivity.this.finish();
                        }
                        *///remove start button ends here


                    }
                    else{
                        Toast.makeText(SplashActivity.this,"No Catagory document Exists!",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else{
                    Toast.makeText(SplashActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}