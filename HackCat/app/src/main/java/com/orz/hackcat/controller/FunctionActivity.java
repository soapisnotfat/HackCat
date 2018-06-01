package com.orz.hackcat.controller;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.orz.hackcat.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;



public class FunctionActivity extends AppCompatActivity {

        protected static final int RESULT_SPEECH = 1;
        private static final int REQUEST_GALLERY = 0;
        private ImageView imageView;
        private TextView txtText;
        private Button btnChoose, btnUpload, voice_button, voice_clear, pdf_button;
        private static final int SELECT_PHOTO = 100;
        Uri selectedImage;
        FirebaseStorage storage;
        StorageReference storageRef, imageRef, txtRef;
        UploadTask uploadTask;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_function);

            imageView = (ImageView) findViewById(R.id.imageView);
            btnChoose = (Button) findViewById(R.id.button_choose);
            voice_button = (Button) findViewById(R.id.voice_button);
            btnUpload = (Button) findViewById(R.id.button_upload);
            voice_clear = (Button) findViewById(R.id.voice_clear);
            txtText = (TextView) findViewById(R.id.textView2);
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            pdf_button = (Button) findViewById(R.id.pdf_button);


            btnChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, REQUEST_GALLERY);
                }
            });

            voice_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backingString.setBackinng_aud("");
                    txtText.setText(" Output what you said :)");
                }
            });

            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadImage(imageView);
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
            pdf_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtRef = storageRef.child("textfile/output.txt");

                    try {
                        final File localFile = File.createTempFile("output","txt");
                        txtRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                String strLine;
                                StringBuilder text = new StringBuilder();

                                /** Reading contents of the temporary file, if already exists */
                                try {
                                    FileReader fReader = new FileReader(localFile);
                                    BufferedReader bReader = new BufferedReader(fReader);

                                    /** Reading the contents of the file , line by line */
                                    while( (strLine = bReader.readLine()) != null  ){
                                        text.append(strLine + "\n");
                                    }
                                    String t = text.toString();
                                    backingString.setBackinng_pic(t);
                                    FunctionActivity.this.startActivity(new Intent(FunctionActivity.this, ViewActivity.class));
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }catch(IOException e){
                                    e.printStackTrace();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    }catch (IOException exception){
                    }
                }
            });
        }


        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case REQUEST_GALLERY:
                    if (resultCode == RESULT_OK) {
                        selectedImage = data.getData();
                        imageView.setImageURI(data.getData());
                        break;
                    }
                    break;
                case RESULT_SPEECH: {
                    if (resultCode == RESULT_OK && null != data) {

                        ArrayList<String> text = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (text.get(0) != null) {
                            backingString.add(1, text.get(0) + ". ");
                            txtText.setText(backingString.getBackinng_aud());
//                            break;
                        }
                        break;
                    }
                }
            }
        }

        public void uploadImage(View view) {
            if (selectedImage != null) {
                imageRef = storageRef.child("images/img_1.jpg");
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
                        Toast.makeText(com.orz.hackcat.controller.FunctionActivity.this, "Error in uploading!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });
                Toast.makeText(FunctionActivity.this,
                        "successfully uploaded", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FunctionActivity.this,
                        "please select file first", Toast.LENGTH_SHORT).show();
            }
        }

        public void generatePdf() {
            String textFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/output.pdf";
            try {
                Document document = new Document();
                FileOutputStream fileoutput = new FileOutputStream(textFilePath);
                PdfWriter.getInstance(document, fileoutput);
                document.open();
                Paragraph para = new Paragraph(backingString.getBackinng_aud());
                para.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(para);
                document.close();
            } catch (Exception e) {
            }
        }

    public void getDownTxt() {
        txtRef = storageRef.child("textfile/output.txt");

        try {
            final File localFile = File.createTempFile("output","txt");
            txtRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    String strLine="";
                    StringBuilder text = new StringBuilder();

                    /** Reading contents of the temporary file, if already exists */
                    try {
                        FileReader fReader = new FileReader(localFile);
                        BufferedReader bReader = new BufferedReader(fReader);

                        /** Reading the contents of the file , line by line */
                        while( (strLine=bReader.readLine()) != null  ){
                            text.append(strLine+"\n");
                        }
                        txtText.setText(text);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }catch (IOException exception){
            Toast.makeText(this, "IOEXCEPTION", Toast.LENGTH_SHORT).show();
        }
    }
}
