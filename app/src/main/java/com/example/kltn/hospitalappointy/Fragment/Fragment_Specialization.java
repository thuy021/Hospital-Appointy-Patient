package com.example.kltn.hospitalappointy.Fragment;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kltn.hospitalappointy.DataRetrievalClass.BookedAppointmentList;
import com.example.kltn.hospitalappointy.Activity.Patient_DoctorProfileActivity;
import com.example.kltn.hospitalappointy.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class Fragment_Specialization extends Fragment {

    private TextInputLayout mSearch;
    private EditText searchtext;

    private RecyclerView mRecylerView;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public Fragment_Specialization(){
        //Required Empty public constructor otherwise app will crash
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_specialization,container,false);

        mSearch = (TextInputLayout) rootView.findViewById(R.id.search_by_specialization);
        searchtext = (EditText) rootView.findViewById(R.id.special_searchtxt);

        searchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onStart();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onStart();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                onStart();
            }
        });

        mRecylerView = (RecyclerView) rootView.findViewById(R.id.specialization_recyclerView);
        mRecylerView.setHasFixedSize(true);
        mRecylerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();


        String search = mSearch.getEditText().getText().toString();


        Query query = mDatabase.child("Specialization").orderByKey().startAt(search).endAt(search +"\uf8ff");

        final Query q = FirebaseDatabase.getInstance().getReference().child("Specialization");

        FirebaseRecyclerOptions<BookedAppointmentList> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<BookedAppointmentList>()
                .setQuery(query, BookedAppointmentList.class)
                .build();

        FirebaseRecyclerOptions<BookedAppointmentList> firebaseRecyclerOptions1 = new FirebaseRecyclerOptions.Builder<BookedAppointmentList>()
                .setQuery(q, BookedAppointmentList.class)
                .build();
        if(searchtext.getText().toString().trim().length() == 0){
            FirebaseRecyclerAdapter<BookedAppointmentList,SpecializationViewHolder> firebaseRecyclerAdapter =
                    new FirebaseRecyclerAdapter<BookedAppointmentList, SpecializationViewHolder>(firebaseRecyclerOptions1) {
                        @Override
                        protected void onBindViewHolder(@NonNull final SpecializationViewHolder holder, final int position, @NonNull final BookedAppointmentList model) {

//                        final String doctorID = model.getDoctor_ID().toString();
                            final String Special = getRef(position).getKey().toString();

                            holder.setSpecialization(Special);

                            if(position%2 == 0) {
                                holder.setImage(1);

                            } else{
                                holder.setImage(2);

                            }

                            holder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

//                                Toast.makeText(getContext(), position+" = "+Special+" DoctorID = ", Toast.LENGTH_SHORT).show();

                                    alertDialog(Special);

                                }
                            });

                        }

                        @Override
                        public SpecializationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.singel_specialization_list, parent, false);

                            return new SpecializationViewHolder(view);
                        }
                    };
            mRecylerView.setAdapter(firebaseRecyclerAdapter);
            firebaseRecyclerAdapter.startListening();

        }else{
            FirebaseRecyclerAdapter<BookedAppointmentList,SpecializationViewHolder> firebaseRecyclerAdapter =
                    new FirebaseRecyclerAdapter<BookedAppointmentList, SpecializationViewHolder>(firebaseRecyclerOptions) {
                        @Override
                        protected void onBindViewHolder(@NonNull final SpecializationViewHolder holder, final int position, @NonNull final BookedAppointmentList model) {

//                        final String doctorID = model.getDoctor_ID().toString();
                            final String Special = getRef(position).getKey().toString();

                            holder.setSpecialization(Special);

                            if(position%2 == 0) {
                                holder.setImage(1);

                            } else{
                                holder.setImage(2);

                            }

                            holder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

//                                Toast.makeText(getContext(), position+" = "+Special+" DoctorID = ", Toast.LENGTH_SHORT).show();

                                    alertDialog(Special);

                                }
                            });

                        }

                        @Override
                        public SpecializationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.singel_specialization_list, parent, false);

                            return new SpecializationViewHolder(view);
                        }
                    };
            mRecylerView.setAdapter(firebaseRecyclerAdapter);
            firebaseRecyclerAdapter.startListening();

        }

    }


    private void alertDialog(String special) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.doctor_list_dialog, null);

        RecyclerView alertRecyclerView = view.findViewById(R.id.doctor_dialog);
        alertRecyclerView.setHasFixedSize(true);
        alertRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        builder.setView(view);

        Query query = mDatabase.child("Specialization").child(special);

        FirebaseRecyclerOptions<BookedAppointmentList> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<BookedAppointmentList>()
                .setQuery(query, BookedAppointmentList.class)
                .build();

        FirebaseRecyclerAdapter<BookedAppointmentList, SpecializationVH> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<BookedAppointmentList, SpecializationVH>(firebaseRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull final SpecializationVH holder, int position, @NonNull final BookedAppointmentList model) {

                        final String doctorID = model.getDoctor_ID().toString();

                        mDatabase.child("Doctor_Details").child(doctorID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final String doctorName = dataSnapshot.child("Name").getValue().toString();
                                final String specialization = dataSnapshot.child("Specialization").getValue().toString();
                                final String contact = dataSnapshot.child("Contact").getValue().toString();
                                final String experience = dataSnapshot.child("Experiance").getValue().toString();
                                final String degree = dataSnapshot.child("Degree").getValue().toString();
                                final String fee = dataSnapshot.child("Fee").getValue().toString();
                                final String education = dataSnapshot.child("Education").getValue().toString();
                                final String shift = dataSnapshot.child("Shift").getValue().toString();
                                final String email = dataSnapshot.child("Email").getValue().toString();

                                holder.setDoctorName(doctorName);
                                holder.setSpecialization(specialization);
                                holder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getContext(), Patient_DoctorProfileActivity.class);
                                        intent.putExtra("Name",doctorName);
                                        intent.putExtra("Specialization",specialization);
                                        intent.putExtra("Contact",contact);
                                        intent.putExtra("Experiance",experience);
                                        intent.putExtra("Degree",degree);
                                        intent.putExtra("Fee",fee);
                                        intent.putExtra("Education",education);
                                        intent.putExtra("Shift",shift);
                                        intent.putExtra("UserId",doctorID);
                                        intent.putExtra("Email",email);
                                        startActivity(intent);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public SpecializationVH onCreateViewHolder(ViewGroup parent, int viewType) {

                        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_doctor_list, null);
                        return new SpecializationVH(mView);
                    }
                };
        alertRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

//        Window window = dialog.getWindow();
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public class SpecializationVH extends RecyclerView.ViewHolder {

        View mView;

        public SpecializationVH(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setDoctorName(String doctorName) {
            TextView userName = (TextView) mView.findViewById(R.id.name_id_single_user);
            userName.setText(doctorName);
        }

        public void setSpecialization(String specialization) {
            TextView userName = (TextView) mView.findViewById(R.id.special_id_single_user);
            userName.setText(specialization);
        }
    }


    public class SpecializationViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public SpecializationViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setSpecialization(String special) {
            TextView userName = (TextView) mView.findViewById(R.id.special_id_single_user);
            userName.setText(special);
        }

        public void setImage(int i) {

            CircleImageView cr1 = (CircleImageView) mView.findViewById(R.id.profile_id_single_user);

            if(i == 1){
                cr1.setImageDrawable(getResources().getDrawable(R.mipmap.stethoscope));

            }else {
                cr1.setImageDrawable(getResources().getDrawable(R.mipmap.injection));
            }

        }
    }
}
