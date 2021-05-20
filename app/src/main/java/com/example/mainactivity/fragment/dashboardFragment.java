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
import com.example.mainactivity.R;
import com.example.mainactivity.firebasecallbacks.ReadCallBacks;
import com.example.mainactivity.models.Datum;
import com.example.mainactivity.models.NewsList;
import com.example.mainactivity.models.Pagination;
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
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    List<Datum> myNewsList;

    APIinterface apiInterface;


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

    public void makeApiCall() {
        apiInterface = APIClient.getClient().create(APIinterface.class);

        Call<Datum> call = apiInterface.getListByYear("access_key");
        call.enqueue(new Callback<Datum>() {

            @Override
            public void onResponse(Call<Datum> call, Response<Datum> response) {
                Log.d("Dashboard Fragment", "" + response.body());

                NewsList newsList = response.body();
                Pagination data = newsList.pagination;
                List<Datum> movies = data.news;
                myNewsList = movies;

                Log.d("Dashboard Fragment", "Movie Size" + movies.size());
                Log.d("Dashboard Fragment", "First Movie" + movies.get(0).title);


                if (currentPage != PAGE_START) adapter.removeLoading();

                adapter.addItems(movies);
                swipeRefreshLayout.setRefreshing(false);

                if (currentPage < totalPage) {
                    adapter.addLoading();
                } else {
                    isLastPage = true;
                }
                isLoading = false;

            }


            @Override
            public void onFailure(Call<Datum> call, Throwable t) {
                call.cancel();
                Log.d("Dashboard Fragment", "" + t.getMessage());
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
    public void onNewsClick(Datum news) {
        Toast.makeText(getActivity().getApplicationContext(), news.getTitle(), Toast.LENGTH_SHORT).show();
    }
}

