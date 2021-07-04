package com.example.kltn.hospitalappointy.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kltn.hospitalappointy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Patient_EditProfileActivity extends AppCompatActivity {
    private TextView mName, mEmail, mSpecialization, mExperiance, mAge, mContact, mAddress, mEducation;
    private Toolbar mToolbar;
    private CircleImageView circleImageView,btn_up;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference storageReference;

    private String name,specialization,experiance,education,email,age,contact,address,update;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Patient_Details");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__edit_profile);

        storage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("user/"+mAuth.getCurrentUser().getUid()+"/profile.png");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(circleImageView);
            }
        });

        circleImageView = (CircleImageView)findViewById(R.id.img);
        btn_up = (CircleImageView)findViewById(R.id.btn_up);
        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.doctor_editProfile_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mName = (TextView) findViewById(R.id.edit_doctor_name);
        mExperiance = (TextView) findViewById(R.id.edit_doctor_experiance);
        mEmail = (TextView) findViewById(R.id.edit_doctor_email);
        mAge = (TextView) findViewById(R.id.edit_doctor_age);
        mContact = (TextView) findViewById(R.id.edit_doctor_contact);
        mAddress = (TextView) findViewById(R.id.edit_doctor_address);

        name = getIntent().getStringExtra("Name").toString();
        experiance = getIntent().getStringExtra("Blood_Group").toString();
        email = getIntent().getStringExtra("Email").toString();
        age = getIntent().getStringExtra("Age").toString();
        contact = getIntent().getStringExtra("Contact_N0").toString();
        address = getIntent().getStringExtra("Address").toString();

        mName.setText(name);
        mExperiance.setText(experiance);
        mEmail.setText(email);
        mAge.setText(age);
        mContact.setText(contact);
        mAddress.setText(address);

        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });
    }
    public void update(View view){

        switch (view.getId()){

            case R.id.edit_name:
                alertDialog(name,"Name");
                break;

            case R.id.edit_experiance:
                alertDialog(experiance,"Blood_Group");
                break;

            case R.id.edit_address:
                alertDialog(address,"Address");
                break;
            case R.id.edit_age:
                alertDialog(age,"Age");
                break;
            case R.id.edit_contact:
                alertDialog(contact,"Contact");
                break;

            case R.id.final_update:
                updateDoctorProfile();
                break;

            default:
                break;
        }

    }
    private void updateDoctorProfile() {

        String currentUser = mAuth.getCurrentUser().getUid().toString();

        mDatabase.child(currentUser).child("Name").setValue(name);
        mDatabase.child(currentUser).child("Blood_Group").setValue(experiance);
        mDatabase.child(currentUser).child("Address").setValue(address);
        mDatabase.child(currentUser).child("Age").setValue(age);
        mDatabase.child(currentUser).child("Contact").setValue(contact);


        startActivity(new Intent(Patient_EditProfileActivity.this, Patient_ProfileActivity.class));



    }

    private void alertDialog(String text, final String detail){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.udate_dialog, null);


        TextView textView = (TextView) view.findViewById(R.id.update_textView);
        final EditText editText = (EditText) view.findViewById(R.id.editText);

        textView.setText(detail);
        editText.setText(text, TextView.BufferType.EDITABLE);

        builder.setView(view);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                update = editText.getText().toString();

                if(detail.equals("Name")){
                    mName.setText(update);
                    name = mName.getText().toString();
                }
                else if(detail.equals("Blood_Group")){
                    mExperiance.setText(update);
                    experiance = mExperiance.getText().toString();
                }
                else if(detail.equals("Address")){
                    mAddress.setText(update);
                    address = mAddress.getText().toString();
                }
                else if(detail.equals("Age")){
                    mAge.setText(update);
                    age = mAge.getText().toString();
                }
                else if(detail.equals("Contact")){
                    mContact.setText(update);
                    contact = mContact.getText().toString();
                }
                else {

                }

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                //imgAvt.setImageURI(imageUri);

                uplooadImage(imageUri);
            }
        }
    }

    private void uplooadImage(Uri imageUri){
        final StorageReference fileRef = storageReference.child("user/"+mAuth.getCurrentUser().getUid()+"/profile.png");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(Patient_ProfileActivity.this,"Image Uploaded", Toast.LENGTH_SHORT).show();
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(circleImageView);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Patient_EditProfileActivity.this,"Failed", Toast.LENGTH_SHORT).show();
            }
        });}
}