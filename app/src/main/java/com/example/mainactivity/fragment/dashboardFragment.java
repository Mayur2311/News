package com.example.mainactivity.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mainactivity.FirebaseComponents;
import com.example.mainactivity.R;
import com.example.mainactivity.firebasecallbacks.ReadCallBacks;
import com.example.mainactivity.models.Person;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;

public class dashboardFragment extends Fragment  {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseComponents firebaseComponents;
    FirebaseFirestore firestore;
    TextView name, email;

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;



    public dashboardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavigationView navigationView = getActivity().findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);

        firebaseComponents = new FirebaseComponents(firestore, firebaseUser);
        name = headerView.findViewById(R.id.userName);
        email = headerView.findViewById(R.id.email);


        firebaseComponents.readFireStore(new ReadCallBacks() {
            @Override
            public void onCallbacks(Person person) {
                name.setText(person.getFname() + person.getLname());
                email.setText(person.getEmail());
            }
        });

    }
}
