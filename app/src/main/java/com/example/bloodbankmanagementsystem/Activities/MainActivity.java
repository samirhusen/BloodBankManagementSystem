package com.example.bloodbankmanagementsystem.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbankmanagementsystem.Adapters.RequestAdapter;
import com.example.bloodbankmanagementsystem.HelperClass.UploadHelperClass;
import com.example.bloodbankmanagementsystem.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements RequestAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private List<UploadHelperClass> uploadHelperClasses;
    private RequestAdapter requestAdapter;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private FirebaseStorage mStorage;

    Button mlogoutbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView make_request_button = findViewById(R.id.make_request_button);
        make_request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MakeRequestActivity.class));
            }
        });
//        if (FirebaseAuth.getInstance().getCurrentUser() == null ) {
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//            finish();
//        }
        mlogoutbutton = findViewById(R.id.logout_button);
        mlogoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        uploadHelperClasses = new ArrayList<>();

        requestAdapter = new RequestAdapter(uploadHelperClasses, MainActivity.this);
        recyclerView.setAdapter(requestAdapter);

        //for the delete item click to delete the post
        requestAdapter.setOnItemClickListener(MainActivity.this);

        mStorage = FirebaseStorage.getInstance();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");



        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                uploadHelperClasses.clear();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    UploadHelperClass uploadHelperClass = postSnapshot.getValue(UploadHelperClass.class);
                    uploadHelperClass.setKey(postSnapshot.getKey());
                    uploadHelperClasses.add(uploadHelperClass);
                }

                requestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.search_button) {
                    //open search
                    startActivity(new Intent(MainActivity.this, SearchActivity.class));
                }
                if (item.getItemId() == R.id.about_us){
                    //open about us
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                }
                if (item.getItemId() == R.id.contact_us){
                    //open contact us
                    startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
                }
                return false;
            }
        });
//        requestAdapter = new RequestAdapter(uploadHelperClasses, this);
//        recyclerView.setAdapter(requestAdapter);
//        populateHomePage();
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Long press on any post to delete it permanently", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteCLick(int position) {

        UploadHelperClass selecteditem = uploadHelperClasses.get(position);
        final String selectedKey = selecteditem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selecteditem.getImageUri());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(MainActivity.this, "Post has been deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}

//    private void populateHomePage(){
//        UploadHelperClass uploadHelperClass = new UploadHelperClass("Please help me ","https://images.unsplash.com/photo-1494548162494-384bba4ab999?ixlib=rb-1.2.1&w=1000&q=80");
//        uploadHelperClasses.add(uploadHelperClass);
//        requestAdapter.notifyDataSetChanged();
//    }
//}

//logout button code
//    public void logout(View view) {
//        FirebaseAuth.getInstance().signOut();
//        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
//        finish();
//    }