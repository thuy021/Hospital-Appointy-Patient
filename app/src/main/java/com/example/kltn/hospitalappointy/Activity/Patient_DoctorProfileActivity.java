package com.example.kltn.hospitalappointy.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.kltn.hospitalappointy.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class Patient_DoctorProfileActivity extends AppCompatActivity {

    private TextView mName, mEducation, mSpecialization, mExperience, mContactNo, mShift,mDegree,mFee;
    private String shift;
    private CircleImageView circleImageView;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Button mBookAppointmentBtn;
    private Toolbar mToolbar;

    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private DatabaseReference mDoctorDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__doctor_profile);
        String id = getIntent().getStringExtra("UserId").toString();
        storage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("user/"+id+"/profile.png");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(circleImageView);
            }
        });


        circleImageView = (CircleImageView)findViewById(R.id.img);
        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.patient_doctorProfile_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Doctor Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mName = (TextView) findViewById(R.id.patient_doctorProfile_name);
        mSpecialization = (TextView) findViewById(R.id.patient_doctorProfile_specialization);
        mEducation = (TextView) findViewById(R.id.patient_doctorProfile_education);
        mExperience = (TextView) findViewById(R.id.patient_doctorProfile_experiance);
        mContactNo = (TextView) findViewById(R.id.patient_doctorProfile_contact);
        mShift = (TextView) findViewById(R.id.patient_doctorProfile_shift);
        mDegree = (TextView)findViewById(R.id.patient_doctorProfile_Degree);
        mFee = (TextView)findViewById(R.id.patient_doctorProfile_Fee);

        mBookAppointmentBtn = (Button) findViewById(R.id.book_appointment_button);
        mBookAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = getIntent().getStringExtra("Email").toString();

                calendar = Calendar.getInstance();
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(Patient_DoctorProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String userId = getIntent().getStringExtra("UserId").toString();

                        String date = dayOfMonth +"-"+ (month+1) +"-"+ year;
//                        Toast.makeText(Patient_DoctorProfileActivity.this, date , Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Patient_DoctorProfileActivity.this, Patient_BookAppointmentActivity.class);
                        intent.putExtra("Date",date);
                        intent.putExtra("DoctorUserId",userId);
                        intent.putExtra("Shift",shift);
                        intent.putExtra("year",year);
                        intent.putExtra("month",month);
                        intent.putExtra("day",dayOfMonth);
                        intent.putExtra("Email",email);
                        startActivity(intent);
                    }
                },day,month,year);
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + (3 * 60 * 60 * 1000));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + (15 * 24 * 60 * 60 * 1000));
                datePickerDialog.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String id = getIntent().getStringExtra("UserId").toString();
        String name = getIntent().getStringExtra("Name").toString();
        String education = getIntent().getStringExtra("Education").toString();
        String specialization = getIntent().getStringExtra("Specialization").toString();
        String experience = getIntent().getStringExtra("Experiance").toString();
        String degree = getIntent().getStringExtra("Degree").toString();
        String fee = getIntent().getStringExtra("Fee").toString();
        String contact = getIntent().getStringExtra("Contact").toString();
        shift = getIntent().getStringExtra("Shift").toString();


        mName.setText(name);
        mEducation.setText(education);
        mSpecialization.setText(specialization);
        mExperience.setText(experience);
        mContactNo.setText(contact);
        mShift.setText(shift);
        mDegree.setText(degree);
        mFee.setText(fee);
    }
}
