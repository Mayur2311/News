package com.example.mainactivity.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainactivity.FirebaseComponents;
import com.example.mainactivity.models.Model;
import com.example.mainactivity.R;
import com.example.mainactivity.firebasecallbacks.ReadCallBacks;
import com.example.mainactivity.models.Person;
import com.example.mainactivity.utils.APIClient;
import com.example.mainactivity.utils.APIinterface;
import com.example.mainactivity.utils.PaginationListener;
import com.example.mainactivity.utils.RecyclerViewAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.example.mainactivity.utils.PaginationListener.PAGE_START;

public class dashboardFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerViewAdapter.OnRecycleClickListener {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseComponents firebaseComponents;
    FirebaseFirestore firestore;
    TextView name, email;

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerViewAdapter adapter;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 50;
    private boolean isLoading = false;
    ArrayList<Model.MData> data;

    APIinterface apiInterface;

    public static void main(String[] args) {
    }


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

        recyclerView = view.findViewById(R.id.recycleView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

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

        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        makeApiCall();

        recyclerView.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                makeApiCall();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

        public void makeApiCall()
        {
             apiInterface =  APIClient.getClient().create(APIinterface.class);
             Call<Model> call =  apiInterface.getProfileData(String.valueOf(currentPage));

                call.enqueue(new Callback<Model>() {
                @Override
                public void onResponse(Call<Model> call, Response<Model> response) {
                    Log.d("Dashboard Fragment", "" + response.code());
                    response.body().getData();

                    data = (ArrayList<Model.MData>) response.body().getData();
                    for (Model.MData data1 : data )
                    {
                        Log.e(TAG,"onResponse: emails : "+data1.getEmail());
                    }

                    if (currentPage != PAGE_START) adapter.removeLoading();

                    adapter.addItems(data);
                    swipeRefreshLayout.setRefreshing(false);

                    if (currentPage < totalPage) {
                      adapter.addLoading();
                    } else {
                        isLastPage = true;
                    }
                    isLoading = false;
                }

                @Override
                public void onFailure(Call<Model> call, Throwable t) {
                    Log.e(TAG,"onResponse: emails : "+t.getMessage());
                }
            });

        }

    @Override
    public void onRefresh() {


        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();
        makeApiCall();

    }

    @Override
    public void onNewsClick(Model.MData position) {

        Toast.makeText(getContext().getApplicationContext(), position.getId() , Toast.LENGTH_LONG).show();

    }


}
