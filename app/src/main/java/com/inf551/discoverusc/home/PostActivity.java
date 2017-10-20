package com.inf551.discoverusc.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inf551.discoverusc.R;

public class PostActivity extends AppCompatActivity {

    private ImageButton mimageSelect;
    private static final int GALLERY_REQUEST = 1;
    private Uri mImageUri = null;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private Button mSubitBtn;

    private StorageReference mStorage;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mStorage = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);

        mPostTitle = (EditText) findViewById(R.id.titleField);
        mPostDesc = (EditText) findViewById(R.id.descField);
        mSubitBtn = (Button) findViewById(R.id.submitBtn);
        mSubitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });


        mimageSelect = (ImageButton) findViewById(R.id.imageSelect);
        mimageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });



    }

    private void startPosting() {

        mProgress.setMessage("Uploading to diSCover...");

        String title_val = mPostTitle.getText().toString().trim();
        String desc_val = mPostDesc.getText().toString().trim();

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImageUri != null){
            mProgress.show();
            // Upload the file and metadata
            StorageReference filepath = mStorage.child("Post_Images").child(mImageUri.getLastPathSegment());//return name of image;if post many images, their name should not be overwritted.
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    @SuppressWarnings("VisibleForTests")Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    mProgress.dismiss();
                }
            });

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mImageUri = data.getData();
            mimageSelect.setImageURI(mImageUri);
        }
    }
}
