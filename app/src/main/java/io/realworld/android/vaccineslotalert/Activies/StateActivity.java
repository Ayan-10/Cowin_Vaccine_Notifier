package io.realworld.android.vaccineslotalert.Activies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;
import io.realworld.android.api.CowinClient;
import io.realworld.android.api.models.State;
import io.realworld.android.api.models.StatesResponse;
import io.realworld.android.vaccineslotalert.Adapters.SelectStateAdapter;
import io.realworld.android.vaccineslotalert.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fetches data from govt portal.
 * Displays all states
 */
public class StateActivity extends AppCompatActivity {

    ProgressBar progressBar;
    RecyclerView recyclerView;
    SearchView searchView;
    SelectStateAdapter selectStateAdapter;
    List<State> states = new ArrayList<>();
    private final CowinClient cowinClient = new CowinClient();
    private final String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Init();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        selectStateAdapter = new SelectStateAdapter(states, this);
        recyclerView.setAdapter(selectStateAdapter);

        showStates();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                selectStateAdapter.getFilter().filter(newText);
                return false;
            }
        });


    }

    private void showStates() {
        Call<StatesResponse> call = cowinClient.api.getStates(user_agent);
        call.enqueue(new Callback<StatesResponse>() {
            @Override
            public void onResponse(@NonNull Call<StatesResponse> call,
                                   @NonNull Response<StatesResponse> response) {
                if(response.code() == 200){
                    StatesResponse statesResponse = response.body();
                    states = Objects.requireNonNull(statesResponse).getStates();
                    Log.d("testt",states.get(0).getStateName());
                    selectStateAdapter.setStates(states);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.VISIBLE);
                }else{

                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    searchView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<StatesResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network Failure! Please check your internet connection and retry", Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * For initialize UI elements
     */
    private void Init() {
        Paper.init(this);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);

    }
}