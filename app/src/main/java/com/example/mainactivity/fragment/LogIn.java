package com.example.mainactivity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainactivity.FirebaseComponents;
import com.example.mainactivity.R;
import com.example.mainactivity.dashActivity;
import com.example.mainactivity.firebasecallbacks.LoginCallBacks;
import com.example.mainactivity.utils.NetworkCheck;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class LogIn extends Fragment {

    NavController navController;
    Button login_btn;
    EditText emailTxtView, passwordTxtView;
    TextView createAcc_txt;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseComponents firebaseComponents;


    public LogIn() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null)
        {
            updateUI();
            Toast.makeText(getActivity().getApplicationContext(), "User Already Signin.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(getActivity(), R.id.auth_host_fragment);
        createAcc_txt = view.findViewById(R.id.txt_register_user);
        login_btn = view.findViewById(R.id.btn_login);
        emailTxtView = view.findViewById(R.id.txt_email_login);
        passwordTxtView = view.findViewById(R.id.txt_password_login);
        firebaseComponents = new FirebaseComponents(firebaseAuth, getActivity());

        createAcc_txt.setOnClickListener( v -> {

           navController.navigate(R.id.signup);
        });


        login_btn.setOnClickListener(v -> {

            NetworkCheck networkCheck =  new NetworkCheck();

            isFieldEmpty();


            if(networkCheck.checkNetwork(getActivity())) {
                firebaseComponents.loginUser(emailTxtView.getText().toString(), passwordTxtView.getText().toString(), new LoginCallBacks() {
                    @Override
                    public void loginCallBack(boolean loginSuccess) {

                        if (loginSuccess == true) {
                            firebaseUser = firebaseAuth.getCurrentUser();
                            updateUI();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else
            {
                Snackbar snackbar = Snackbar.make(view, "No Internet!!!", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }
        });
    }

    private void updateUI() {
        Toast.makeText(getActivity().getApplicationContext(), "LogInSucess", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), dashActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public boolean isFieldEmpty() {
        if (TextUtils.isEmpty(emailTxtView.getText().toString())) {
            emailTxtView.setError("Email cannot be empty!");
            emailTxtView.requestFocus();
            return true;
        } else if (TextUtils.isEmpty(passwordTxtView.getText().toString())) {
            passwordTxtView.setError("Password cannot be empty!");
            passwordTxtView.requestFocus();
            return true;
        }

        return false;
    }

}