package com.jg.scientificcalculator;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity
        implements HistoryAdapter.OnHistoryClickListener {

    RecyclerView recyclerView;
    HistoryDBHelper dbHelper;
    ArrayList<HistoryItem> historyList;
    HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        /* 🔹 Setup Toolbar with Back Arrow */
        Toolbar toolbar = findViewById(R.id.historyToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("History");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerHistory);
        dbHelper = new HistoryDBHelper(this);

        historyList = dbHelper.getAllHistory();

        adapter = new HistoryAdapter(historyList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnClearHistory).setOnClickListener(v -> {
            dbHelper.clearHistory();
            historyList.clear();
            adapter.notifyDataSetChanged();
        });
    }

    /* 🔹 When user taps a history item */
    @Override
    public void onHistoryClick(HistoryItem item) {
        Intent data = new Intent();
        data.putExtra("expression", item.expression);
        data.putExtra("result", item.result);
        setResult(RESULT_OK, data);
        finish();
    }
}
