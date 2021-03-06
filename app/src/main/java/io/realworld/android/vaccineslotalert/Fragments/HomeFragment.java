package io.realworld.android.vaccineslotalert.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.realworld.android.vaccineslotalert.Activies.AboutActivity;
import io.realworld.android.vaccineslotalert.Activies.PinActivity;
import io.realworld.android.vaccineslotalert.Activies.StateActivity;
import io.realworld.android.vaccineslotalert.Adapters.HomeAdapter;
import io.realworld.android.vaccineslotalert.Data.Subscription;
import io.realworld.android.vaccineslotalert.R;

/**
 * For showing area subscriptions
 */
public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    HomeAdapter homeAdapter;
    TextView textView;
    FloatingActionButton fab;
    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Init(view);
        List<Subscription> subscriptions = Paper.book().read("subscription", new ArrayList<>());

        if( subscriptions.size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            homeAdapter = new HomeAdapter(getContext(), subscriptions, getActivity());
            recyclerView.setAdapter(homeAdapter);
        }

        fab.setOnClickListener(v -> showDialog(getActivity()));

        imageView.setOnClickListener(v -> {

            Intent intent = new Intent(requireContext(), AboutActivity.class);
            startActivity(intent);

        });
    }

    /**
     * For initialize UI elements
     */
    private void Init(View view) {
        Paper.init(view.getContext());
        recyclerView = view.findViewById(R.id.subscription_recycler_view);
        textView = view.findViewById(R.id.no_subs);
        fab = view.findViewById(R.id.fab);
        imageView = view.findViewById(R.id.info);
    }

    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogButton = dialog.findViewById(R.id.search_by_pin);
        dialogButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), PinActivity.class);
            startActivity(intent);
            dialog.dismiss();
        });

        Button disButton = dialog.findViewById(R.id.search_by_district);
        disButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), StateActivity.class);
            startActivity(intent);
            dialog.dismiss();
        });

        dialog.show();
    }
}