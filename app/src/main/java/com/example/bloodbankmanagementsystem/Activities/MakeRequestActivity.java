package com.example.bloodbankmanagementsystem.Activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bloodbankmanagementsystem.HelperClass.UploadHelperClass;
import com.example.bloodbankmanagementsystem.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class MakeRequestActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    EditText messageText;
    TextView chooseImageText;//choose image as link but works as an button
    ImageView postImage;
    Button submit_button;
    ProgressBar progressBar;

    Uri imageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_request);

       // AndroidNetworking.initialize(getApplicationContext());
        messageText = findViewById(R.id.message);
        chooseImageText = findViewById(R.id.choose_text);//link to choose the image
        postImage = findViewById(R.id.post_image);
        submit_button = findViewById(R.id.submit_button);
        progressBar = findViewById(R.id.progressBar);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        //to choose image
        chooseImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFileChooser();

            }
        });

        //to post or upload the image
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(MakeRequestActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {

                    uploadFile();

                }
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.search_button) {
                    //open search
                    startActivity(new Intent(MakeRequestActivity.this, SearchActivity.class));
                }
                if (item.getItemId() == R.id.about_us){
                    //open about us activity
                    startActivity(new Intent(MakeRequestActivity.this, AboutActivity.class));
                }
                if (item.getItemId() == R.id.contact_us){
                    //open contact us activity
                    startActivity(new Intent(MakeRequestActivity.this, ContactUsActivity.class));
                }
                return false;
            }
        });

    }

    //get the extension of the image
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (imageUri != null){

            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
            + "." + getFileExtension(imageUri));

             mUploadTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            },1000);

                            Toast.makeText(MakeRequestActivity.this, "Upload Successful", Toast.LENGTH_LONG).show();
                            /*UploadHelperClass uploadHelperClasses = new UploadHelperClass(messageText.getText().toString().trim(),
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());

                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(uploadHelperClasses);*/
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();

                            while (!urlTask.isSuccessful());

                            Uri downloadUrl = urlTask.getResult();

                            UploadHelperClass uploadHelperClass = new UploadHelperClass(messageText.getText().toString().trim(),downloadUrl.toString());

                            String uploadId = mDatabaseRef.push().getKey();

                            mDatabaseRef.child(uploadId).setValue(uploadHelperClass);

                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(MakeRequestActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });

                    }else {
                        Toast.makeText(this,"No Image Selected", Toast.LENGTH_SHORT).show();
                    }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            imageUri = data.getData();

            Picasso.with(this).load(imageUri).into(postImage);
        }
    }
}
