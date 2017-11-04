package org.mrpiglet.lovelypiglet;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.mrpiglet.lovelypiglet.data.CheckedItemsDbHelper;
import org.mrpiglet.lovelypiglet.utils.DatabaseOperation;

public class AddCheckedItemActivity extends AppCompatActivity {
    private EditText descriptionEditText;
    private Button firstConfirmButton, secondConfirmButton;
    private Toast activeToast = null;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_checked_item);

        descriptionEditText = (EditText) findViewById(R.id.et_description);
        firstConfirmButton = (Button) findViewById(R.id.bt_first_confirm);
        secondConfirmButton = (Button) findViewById(R.id.bt_second_confirm);

        CheckedItemsDbHelper dbHelper = new CheckedItemsDbHelper(this);
        db = dbHelper.getWritableDatabase();

        prepareButtons(true); //set first confirm button to enabled, and second to disabled
    }

    //true - set first confirm button to enabled, and second to disabled
    //false - vice versa
    private void prepareButtons(boolean firstTime) {
        if (firstTime) {
            firstConfirmButton.setEnabled(true);
            firstConfirmButton.setBackgroundColor(Color.parseColor(getString(R.string.colorPrimaryDarkString)));
            //secondConfirmButton.setEnabled(false);
        } else {
            firstConfirmButton.setEnabled(false);
            firstConfirmButton.setBackgroundColor(Color.parseColor(getString(R.string.colorDisabledString)));
            //secondConfirmButton.setEnabled(true);
        }
    }

    private void showToast(String toastText) {
        if (activeToast != null) {
            activeToast.cancel(); //prevent queuing up toasts
        }
        //display toast to instruct user what to do next
        activeToast = Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG);
        activeToast.show();
    }

    public void onClickAddItem(View view) {
        if (view.getId() == R.id.bt_first_confirm) {
            if (descriptionEditText.getText().length() == 0) { //nothing is entered
                showToast(getString(R.string.emptyDescriptionMsg));
                return;
            }
            prepareButtons(false);
            showToast(getString(R.string.confirmSnoutToastMsg));
        } else
            if (view.getId() == R.id.bt_second_confirm) {
                //instruct the user to first click on add button
                if (firstConfirmButton.isEnabled()) {
                    showToast(getString(R.string.userInstructionToastMsg));
                    return;
                }
                //add an item to database
                String description = descriptionEditText.getText().toString();
                DatabaseOperation.addNewCheckedItem(db, description);
                //finish the activity
                finish();
            }
    }

    @Override
    public void finish() { //returning "result" just to invoke db refresh in main activity
        Intent returnIntent = new Intent();
        setResult(RESULT_OK);
        super.finish();
    }
}
