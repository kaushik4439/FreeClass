package com.example.lokeshkaushik.freeclass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CourseContent extends AppCompatActivity {

    private Button chooseButton;
    private Button sendButton;
    private TextView fileName;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mref;
    private static final int FILE_SELECT_CODE = 0;
    private String courseKey;
    private Uri uri;
    private String senderName;
    private StorageReference storageReference;
    private Uri downloadUrl = Uri.EMPTY;
    private EditText subject;
    private EditText message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_content);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("New Post");
        chooseButton = (Button) findViewById(R.id.chooseButton);
        sendButton = (Button) findViewById(R.id.uploadButton);
        fileName = (TextView) findViewById(R.id.textView);
        subject = (EditText) findViewById(R.id.subject);
        message = (EditText) findViewById(R.id.message);
        courseKey = getIntent().getStringExtra("courseId");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mref = firebaseDatabase.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(subject.toString())){
                    subject.setError("First enter Subject");
                    return;


                }
                senderName();
                uploadFile();

            }
        });


    }
    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {

            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                if (requestCode== FILE_SELECT_CODE && resultCode == RESULT_OK && data!=null && data.getData()!=null) {
                    uri = data.getData();
                    fileName.setText(uri.toString());
                }



    }
    public void uploadFile(){
        if(uri!=null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading File...");
            progressDialog.show();
            storageReference.child(courseKey).child(uri.getLastPathSegment()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"File Successfully Uploaded", Toast.LENGTH_LONG).show();
                    downloadUrl = taskSnapshot.getDownloadUrl();
                    sendMessage();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();


                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    Double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage(progress.intValue()+"% Uploaded...");
                }
            });
        }
        else{

            sendMessage();
        }

    }

    public void sendMessage(){

        NewPost post = new NewPost(subject.getText().toString(),message.getText().toString(),downloadUrl.toString(),senderName);

        mref.child("posts").child(courseKey).push().setValue(post);
        subject.setText(null);
        message.setText(null);
        fileName.setText(null);
        Toast.makeText(getApplicationContext(),"Successfully posted ", Toast.LENGTH_LONG).show();


    }
    public void senderName(){
        mref.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                senderName = user.getUserName();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, FacultyCourseContent.class);
        intent.putExtra("courseId",courseKey);
        intent.putExtra("courseName",getIntent().getStringExtra("courseName"));
        startActivity(intent);
        finish();
    }

}
