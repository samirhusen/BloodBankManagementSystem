package com.example.bloodbankmanagementsystem.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bloodbankmanagementsystem.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.search_button) {
                    //open search
                    startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                }
//                if (item.getItemId() == R.id.about_us){
//                    //open search
//                    //startActivity(new Intent(getApplicationContext(),LoginActivity.class));;
//                }
                if (item.getItemId() == R.id.contact_us){
                    //open contact us page
                    startActivity(new Intent(getApplicationContext(),ContactUsActivity.class));
                }
                return false;
            }
        });
    }

}
