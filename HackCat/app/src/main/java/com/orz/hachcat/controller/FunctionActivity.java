package com.orz.hachcat.controller;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.orz.hachcat.R;

import java.util.ArrayList;


    public class FunctionActivity extends AppCompatActivity {

        protected static final int RESULT_SPEECH = 1;
        private ImageView imageView;
        private TextView txtText;
        private Button btnChoose, btnUpload, voice_button;
        private static final int SELECT_PHOTO = 100;
        Uri selectedImage;
        FirebaseStorage storage;
        StorageReference storageRef, imageRef;
        UploadTask uploadTask;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_function);

            imageView = (ImageView) findViewById(R.id.imageView);
            btnChoose = (Button) findViewById(R.id.button_choose);
            btnUpload = (Button) findViewById(R.id.button_upload);
            voice_button = (Button) findViewById(R.id.voice_button);
            txtText = (TextView) findViewById(R.id.textView2);
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();


            btnChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageBrowse(imageView);
                }
            });

            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imageView != null) {
                        uploadImage(imageView);
                    }
                }
            });
            voice_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(
                            RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                    try {
                        startActivityForResult(intent, RESULT_SPEECH);
                        txtText.setText("");
                    } catch (ActivityNotFoundException a) {
                        Toast t = Toast.makeText(getApplicationContext(),
                                "Opps! Your device doesn't support Speech to Text",
                                Toast.LENGTH_SHORT);
                        t.show();
                    }
                }
            });
        }

        public void imageBrowse(View view) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case SELECT_PHOTO:
                    if (resultCode == RESULT_OK) {
                        selectedImage = data.getData();
                        imageView.setImageURI(selectedImage);
                        break;
                    }
                case RESULT_SPEECH: {
                    if (resultCode == RESULT_OK && null != data) {

                        ArrayList<String> text = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                        txtText.setText(text.get(0));
                        break;
                    }
                }
            }
        }

        public void uploadImage(View view) {
            if (selectedImage != null) {
                imageRef = storageRef.child("images/" + selectedImage.getLastPathSegment());
                Log.d("first select", "first select");
                uploadTask = imageRef.putFile(selectedImage);
                Log.d("second upload", "second upload");
                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    }
                });
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(com.orz.hachcat.controller.FunctionActivity.this, "Error in uploading!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });
            } else {
                Toast.makeText(FunctionActivity.this,
                        "please select file first", Toast.LENGTH_SHORT).show();
            }
        }



    }
