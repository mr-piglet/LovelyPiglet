package org.mrpiglet.lovelypiglet;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.mrpiglet.lovelypiglet.data.CheckedItemsDbHelper;
import org.mrpiglet.lovelypiglet.utils.AppLocalization;
import org.mrpiglet.lovelypiglet.utils.DatabaseOperation;


public class MainActivity extends AppCompatActivity
                          implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int ITEMS_LOADER_ID = 0;

    private CheckedItemsAdapter checkedItemsAdapter;
    private RecyclerView dataRecyclerView;
    private SQLiteDatabase db;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //gets called when menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) { //user chose to open settings activity on main menu
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set currently selected language before drawing xml layout
        AppLocalization.setLanguageFromPreferences(this);

        setContentView(R.layout.activity_main);

        CheckedItemsDbHelper dbHelper = new CheckedItemsDbHelper(this);

        db = dbHelper.getWritableDatabase();

        //inserting fake data for test purposes
        //DatabaseOperation.insertFakeData(db);

        Cursor cursor = DatabaseOperation.getAllItems(db); //get all items to display

        //setting up the recycler view
        setupRecyclerView(cursor);

        addItemTouchHelper(); //adds item touch helper for deleting entries

        FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an AddTaskActivity
                Intent addTaskIntent = new Intent(MainActivity.this, AddCheckedItemActivity.class);
                startActivity(addTaskIntent);
            }
        });

        /*
         Ensure a loader is initialized and active. If the loader doesn't already exist, one is
         created, otherwise the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(ITEMS_LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //set currently selected language
        //AppLocalization.setLanguageFromPreferences(this);

        // re-queries for all tasks, refresher recyclerview
        getSupportLoaderManager().restartLoader(ITEMS_LOADER_ID, null, this);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mTaskData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                try {
                    Cursor cursor = DatabaseOperation.getAllItems(db);
                    return cursor;
                } catch (Exception e) {
                    Log.e("ASYNC_LOADER", "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };
    }

    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        checkedItemsAdapter.swapCursor(data);
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.
     * onLoaderReset removes any references this activity had to the loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        checkedItemsAdapter.swapCursor(null);
    }


}
