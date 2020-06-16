package com.example.bloodbankmanagementsystem.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbankmanagementsystem.Adapters.SearchAdapter;
import com.example.bloodbankmanagementsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity {

    EditText city_name;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    ArrayList<String> namelist;
    ArrayList<String> citylist;
    ArrayList<String> bloodgrouplist;
    ArrayList<String> phonenumberlist;
    SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        city_name = findViewById(R.id.city_name);
        recyclerView = findViewById(R.id.recyclerView);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                if (item.getItemId() == R.id.search_button) {
//                    //open search
//                    startActivity(new Intent(SearchActivity.this, SearchActivity.class));
//                }
                if (item.getItemId() == R.id.about_us){
                    //open about us
                    startActivity(new Intent(SearchActivity.this, AboutActivity.class));
                }
                if (item.getItemId() == R.id.contact_us){
                    //open contact us
                    startActivity(new Intent(SearchActivity.this, ContactUsActivity.class));
                }
                return false;
            }
        });

        /*
         * Create a array list for each node you want to use
         * */
        namelist = new ArrayList<>();
        citylist = new ArrayList<>();
        bloodgrouplist = new ArrayList<>();
        phonenumberlist = new ArrayList<>();

        city_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    setAdapter(s.toString());
                } else {
                    /*
                     * Clear the list when editText is empty
                     * */
                    namelist.clear();
                    citylist.clear();
                    bloodgrouplist.clear();
                    phonenumberlist.clear();
                    recyclerView.removeAllViews();
                }
            }

        });
    }

    private void setAdapter(final String searchedString) {
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*
                 * Clear the list for every new search
                 * */
                namelist.clear();
                citylist.clear();
                bloodgrouplist.clear();
                phonenumberlist.clear();
                recyclerView.removeAllViews();

                int counter = 0;

                /*
                 * Search all users for matching searched string
                 * */
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey();
                    String name = snapshot.child("name").getValue(String.class);
                    String city = snapshot.child("city").getValue(String.class);
                    String blood_group = snapshot.child("blood_group").getValue(String.class);
                    String number = snapshot.child("number").getValue(String.class);

                    //used try catch for null pointer exception to catch error and ignore it
                    try {
                        if (city.toLowerCase().contains(searchedString.toLowerCase())) {
                            namelist.add(name);
                            citylist.add(city);
                            bloodgrouplist.add(blood_group);
                            phonenumberlist.add(number);
                            counter++;
                        } else if (blood_group.toLowerCase().contains(searchedString.toLowerCase())) {
                            namelist.add(name);
                            citylist.add(city);
                            bloodgrouplist.add(blood_group);
                            phonenumberlist.add(number);
                            counter++;
                        }
                    }catch (NullPointerException ignored) {

                    }
                    /*
                     * Get maximum of 20 searched results only
                     * */
                    if (counter == 20)
                        break;
                }

                searchAdapter = new SearchAdapter(SearchActivity.this, namelist, citylist, bloodgrouplist, phonenumberlist);
                recyclerView.setAdapter(searchAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}



