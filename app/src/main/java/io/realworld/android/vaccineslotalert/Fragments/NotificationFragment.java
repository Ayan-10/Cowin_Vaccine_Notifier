package io.realworld.android.vaccineslotalert.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.realworld.android.vaccineslotalert.Activies.PinActivity;
import io.realworld.android.vaccineslotalert.Activies.StateActivity;
import io.realworld.android.vaccineslotalert.Adapters.NotificationAdapter;
import io.realworld.android.vaccineslotalert.Data.Alert;
import io.realworld.android.vaccineslotalert.R;

/**
 * For showing detailed information about vaccine slots in respected area
 */
public class NotificationFragment extends Fragment {

    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    TextView textView;
    private FloatingActionButton fab;
    private FloatingActionButton fab_delete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_notification, container, false);
        Init(root);

        fab.setOnClickListener(view -> showDialog(getActivity()));

        List<Alert> alerts = Paper.book().read("test11", new ArrayList<>());

        if( alerts.size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            notificationAdapter = new NotificationAdapter(getContext(), alerts, getActivity());
            recyclerView.setAdapter(notificationAdapter);
        }

        fab_delete.setOnClickListener(view -> {

            if( alerts.size() > 0) {

                AlertDialog mDialog = new AlertDialog.Builder(requireContext())
                        .setTitle("Delete All")
                        .setMessage("Are you sure! You want to delete all of this notifications?")
                        .setCancelable(true)
                        .setPositiveButton("Delete", (dialog, which) -> {
                            Paper.book().delete("test11");
                            recyclerView.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).show();
            } else {
                Toast.makeText(requireContext(), "There is no notifications in alert box", Toast.LENGTH_LONG).show();
            }
        });
        return root;
    }

    /**
     * For initialize UI elements
     */
    private void Init(View view) {
        Paper.init(view.getContext());
        recyclerView = view.findViewById(R.id.alert_recycler_view);
        textView = view.findViewById(R.id.no_alerts);
        fab = view.findViewById(R.id.fab_alert);
        fab_delete = view.findViewById(R.id.delete_all);
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