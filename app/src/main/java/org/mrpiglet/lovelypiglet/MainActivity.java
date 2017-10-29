package org.mrpiglet.lovelypiglet;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import org.mrpiglet.lovelypiglet.data.CheckedItemsDbHelper;
import org.mrpiglet.lovelypiglet.utils.DatabaseOperation;

public class MainActivity extends AppCompatActivity {
    private CheckedItemsAdapter checkedItemsAdapter;
    private RecyclerView dataRecyclerView;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckedItemsDbHelper dbHelper = new CheckedItemsDbHelper(this);

        db = dbHelper.getWritableDatabase();

        Cursor cursor = DatabaseOperation.getAllItems(db); //get all items to display

        //setting up the recycler view
        setupRecyclerView(cursor);

        addItemTouchHelper(); //adds item touch helper for deleting entries
    }

    private void addItemTouchHelper() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //do nothing, we only care about swiping
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //get the id of the item being swiped
                long id = (long) viewHolder.itemView.getTag();
                //remove from DB
                DatabaseOperation.removeCheckedItem(db, id);
                // COMPLETED (10) call swapCursor on mAdapter passing in getAllGuests() as the argument
                //update the list
                checkedItemsAdapter.swapCursor(DatabaseOperation.getAllItems(db));
            }
        }).attachToRecyclerView(dataRecyclerView);
    }


    private void setupRecyclerView(Cursor cursor) {
        dataRecyclerView = (RecyclerView) findViewById(R.id.rv_checked_items);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        dataRecyclerView.setLayoutManager(layoutManager);

        dataRecyclerView.setHasFixedSize(true);

        checkedItemsAdapter = new CheckedItemsAdapter(this, cursor);

        dataRecyclerView.setAdapter(checkedItemsAdapter);
    }
}
