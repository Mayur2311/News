package com.example.mainactivity.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainactivity.FirebaseComponents;
import com.example.mainactivity.R;
import com.example.mainactivity.firebasecallbacks.RegisterCallBacks;
import com.example.mainactivity.models.Person;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class signup extends Fragment {

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private NavController navController;

    TextView txt_existing_user;
    EditText edt_email, edt_password, edt_cpassword, edt_fname, edt_lname, edt_phone, edt_location;
    Button btn_register;

    public signup() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edt_email = view.findViewById(R.id.edt_register_email);
        edt_password = view.findViewById(R.id.edt_register_password);
        edt_cpassword = view.findViewById(R.id.edt_register_confirm_password);
        edt_fname = view.findViewById(R.id.edt_register_first_name);
        edt_lname = view.findViewById(R.id.edt_register_last_name);
        edt_phone = view.findViewById(R.id.edt_register_phone);
        edt_location = view.findViewById(R.id.edt_register_location);
        btn_register = view.findViewById(R.id.btn_register);
        txt_existing_user= view.findViewById(R.id.txt_existing_user);

        navController = Navigation.findNavController(getActivity(), R.id.auth_host_fragment);

        txt_existing_user.setOnClickListener(v -> {
            navController.navigate(R.id.logIn);
        });

        btn_register.setOnClickListener(v -> {

            if (!checkEmptyFields())
            {
                if(edt_password.getText().length()<6)
                {
                    edt_password.setError("Password must have 6 characters!");
                    edt_password.requestFocus();
                }else
                {
                    if(!edt_password.getText().toString().equals(edt_cpassword.getText().toString()))
                    {
                        edt_cpassword.setError("Password not match!");
                        edt_cpassword.requestFocus();
                    }else {
                        String email =edt_email.getText().toString();
                        String password =edt_password.getText().toString();
                        String fname =edt_fname.getText().toString();
                        String lname =edt_lname.getText().toString();
                        String phone =edt_phone.getText().toString();
                        String location =edt_location.getText().toString();

                        Person person = new Person(email, password, fname, lname, phone, location);

                        FirebaseComponents firebaseComponents = new FirebaseComponents(firestore, firebaseAuth, getActivity());

                        firebaseComponents.createUser(person, new RegisterCallBacks() {
                            @Override
                            public void registerSuccess(Boolean success) {
                                if (success)
                                {
                                    Toast.makeText(getActivity().getApplicationContext(), "Registration Success!", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                    navController.navigate(R.id.logIn);
                                }else{
                                    Toast.makeText(getActivity().getApplicationContext(), "Registration Error!!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }
            }

        });
    }

    public boolean checkEmptyFields()
    {
        if(TextUtils.isEmpty(edt_email.getText().toString()))
        {
            edt_email.setError("Email cannot be empty!");
            edt_email.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_password.getText().toString()))
        {
            edt_password.setError("Password cannot be empty!");
            edt_password.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_cpassword.getText().toString()))
        {
            edt_cpassword.setError("Confirm Password cannot be empty!");
            edt_cpassword.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_fname.getText().toString()))
        {
            edt_fname.setError("First name cannot be empty!");
            edt_fname.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_lname.getText().toString()))
        {
            edt_lname.setError("Last name cannot be empty!");
            edt_lname.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_phone.getText().toString()))
        {
            edt_phone.setError("Phone number cannot be empty!");
            edt_phone.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_location.getText().toString()))
        {
            edt_location.setError("Location cannot be empty!");
            edt_location.requestFocus();
            return true;
        }
        return false;
    }

}