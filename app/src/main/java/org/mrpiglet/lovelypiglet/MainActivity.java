package org.mrpiglet.lovelypiglet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    //delete after connecting with database
    private static final int NUM_LIST_ITEMS = 100;

    private CheckedItemsAdapter checkedItemsAdapter;
    private RecyclerView dataRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting up the recycler view
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        dataRecyclerView = (RecyclerView) findViewById(R.id.rv_checked_items);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        dataRecyclerView.setLayoutManager(layoutManager);

        dataRecyclerView.setHasFixedSize(true);

        checkedItemsAdapter = new CheckedItemsAdapter(NUM_LIST_ITEMS);

        dataRecyclerView.setAdapter(checkedItemsAdapter);
    }
}
