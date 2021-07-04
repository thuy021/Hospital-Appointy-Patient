package com.example.kltn.hospitalappointy.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kltn.hospitalappointy.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Patient_ProfileActivity  extends AppCompatActivity {

    private TextView mName, mEmail, mAge, mContact, mAddress, mBlood;
    private Button mEditProfileButton;
    private ImageView imageView,imgAvt;
    private Uri filePath;
    private CircleImageView circleImageView;
    private final int PICK_IMAGE_REQUEST = 71;

    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    private Toolbar mToolbar;

    private String name, blood, email, age, contact, address, shift;

    private DatabaseReference mDoctorDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

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


        mName = (TextView) findViewById(R.id.doctor_name);
        mBlood = (TextView) findViewById(R.id.patient_blood_group);
        mEmail = (TextView) findViewById(R.id.doctor_email);
        mAge = (TextView) findViewById(R.id.doctor_age);
        mContact = (TextView) findViewById(R.id.doctor_contact);
        mAddress = (TextView) findViewById(R.id.doctor_address);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.doctor_profile_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mName = (TextView) findViewById(R.id.doctor_name);


        mEditProfileButton = (Button) findViewById(R.id.edit_profile_button);
        mEditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Doctor_ProfileActivity.this,"Edit Profile Clicked",Toast.LENGTH_SHORT).show();

                Intent editProfile_Intent = new Intent(Patient_ProfileActivity.this, Patient_EditProfileActivity.class);

                editProfile_Intent.putExtra("Name",name);
                editProfile_Intent.putExtra("Blood_Group",blood);
                editProfile_Intent.putExtra("Email",email);
                editProfile_Intent.putExtra("Age",age);
                editProfile_Intent.putExtra("Contact_N0",contact);
                editProfile_Intent.putExtra("Address",address);

                startActivity(editProfile_Intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mDoctorDatabase.child("Patient_Details").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                name = dataSnapshot.child("Name").getValue().toString();
                email = dataSnapshot.child("Email").getValue().toString();
                contact = dataSnapshot.child("Contact_N0").getValue().toString();
                blood = dataSnapshot.child("Blood_Group").getValue().toString();
                age = dataSnapshot.child("Age").getValue().toString();
                address = dataSnapshot.child("Address").getValue().toString();

                mName.setText(name);
                mBlood.setText(blood);
                mEmail.setText(email);
                mAge.setText(age);
                mContact.setText(contact);
                mAddress.setText(address);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
