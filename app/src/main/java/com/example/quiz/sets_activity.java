package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.example.quiz.SplashActivity.catList;
import static com.example.quiz.SplashActivity.selected_cat_index;


public class sets_activity extends AppCompatActivity {

    private GridView sets_gridView;
    private FirebaseFirestore firestore;
    //public static int category_id;
    private Dialog loadingDialog;
    public static List<String> setsIDs = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets_activity);

        Toolbar toolbar=findViewById(R.id.set_toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setTitle(catList.get(selected_cat_index).getName());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sets_gridView = findViewById(R.id.sets_gridview);

        loadingDialog = new Dialog(sets_activity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();


        firestore = FirebaseFirestore.getInstance();

        loadSets();


    }
    public void loadSets(){

        setsIDs.clear();

        firestore.collection("quiz").document(catList.get(selected_cat_index).getId())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                long noOfSets = (long)documentSnapshot.get("SETS");

                for(int i=1; i <= noOfSets;i++){
                    setsIDs.add(documentSnapshot.getString("SET"+String.valueOf(i)+"_ID"));
                }

                SetsAdapter adapter = new SetsAdapter((int)setsIDs.size());
                sets_gridView.setAdapter(adapter);

                loadingDialog.dismiss();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(sets_activity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });




    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            sets_activity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}