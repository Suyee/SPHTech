package com.suyee.mm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.suyee.mm.adapter.DataAdapter;
import com.suyee.mm.model.DataUsage;
import com.suyee.mm.viewmodel.MainViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LifecycleOwner {

    private RecyclerView recyclerView;
    private MainViewModel mainViewModel;
    private DataAdapter adapter;
    public static Context activity;
    private TextView error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        activity = getApplicationContext();

        recyclerView = findViewById(R.id.list);
        error = findViewById(R.id.error);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        Observer<ArrayList<DataUsage>> dataUsageListObserver = new Observer<ArrayList<DataUsage>>() {
            @Override
            public void onChanged(ArrayList<DataUsage> dataUsages) {
                adapter = new DataAdapter(getApplicationContext(), dataUsages);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(getApplicationContext()).getOrientation()));
            }
        };

        Observer<Boolean> errorFlagObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    recyclerView.setVisibility(View.GONE);
                    error.setVisibility(View.VISIBLE);
                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                    error.setVisibility(View.GONE);
                }
            }
        };
        mainViewModel.getLiveData().observe(this, dataUsageListObserver);
        mainViewModel.getErrorFlag().observe(this, errorFlagObserver);
    }
}
