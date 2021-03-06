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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;
import io.realworld.android.api.CowinClient;
import io.realworld.android.api.models.District;
import io.realworld.android.api.models.DistrictsResponse;
import io.realworld.android.vaccineslotalert.Adapters.SelectDistrictAdapter;
import io.realworld.android.vaccineslotalert.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fetches data from govt portal.
 * Displays all districts according to the state id.
 */
public class DistrictActivity extends AppCompatActivity {

    String state;
    long statecode;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    SearchView searchView;
    TextView stateName;
    SelectDistrictAdapter selectDistrictAdapter;
    List<District> districts = new ArrayList<>();
    private final CowinClient cowinClient = new CowinClient();
    private final String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district);
        Objects.requireNonNull(getSupportActionBar()).hide();

        statecode = getIntent().getExtras().getLong("statecode");
        Log.e("this", String.valueOf(statecode));
        Init();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        selectDistrictAdapter = new SelectDistrictAdapter(districts, this, state);
        recyclerView.setAdapter(selectDistrictAdapter);

        showDistricts();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                selectDistrictAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void showDistricts() {
        Call<DistrictsResponse> call = cowinClient.api.getDistricts(statecode, user_agent);
        call.enqueue(new Callback<DistrictsResponse>() {
            @Override
            public void onResponse(@NonNull Call<DistrictsResponse> call,
                                   @NonNull Response<DistrictsResponse> response) {
                if(response.code() == 200) {
                    DistrictsResponse districtsResponse = response.body();
                    districts = Objects.requireNonNull(districtsResponse).getDistricts();
                    Log.e("testt", districts.get(0).getDistrictName());
                    selectDistrictAdapter.setDistricts(districts);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    searchView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DistrictsResponse> call, @NonNull Throwable t) {
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
        stateName = findViewById(R.id.place_name_district);
        searchView = findViewById(R.id.searchView);

        state = getIntent().getExtras().getString("state");
        stateName.setText(state);
    }

}