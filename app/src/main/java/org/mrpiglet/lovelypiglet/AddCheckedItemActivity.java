package org.mrpiglet.lovelypiglet;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.mrpiglet.lovelypiglet.data.CheckedItemsDbHelper;
import org.mrpiglet.lovelypiglet.utils.AppLocalization;
import org.mrpiglet.lovelypiglet.utils.DatabaseOperation;
import org.mrpiglet.lovelypiglet.utils.SysUtils;

public class AddCheckedItemActivity extends AppCompatActivity {
    private EditText descriptionEditText;
    private Button firstConfirmButton;
    private ImageView secondConfirmImageView;
    private Toast activeToast = null;
    private SQLiteDatabase db;

    @Override
    protected void onResume() {
        super.onResume();
        //set currently selected language
        //AppLocalization.setLanguageFromPreferences(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set currently selected language before drawing xml layout
        AppLocalization.setLanguageFromPreferences(this);

        setContentView(R.layout.activity_add_checked_item);

        descriptionEditText = (EditText) findViewById(R.id.et_description);
        firstConfirmButton = (Button) findViewById(R.id.bt_first_confirm);
        secondConfirmImageView = (ImageView) findViewById(R.id.bt_second_confirm);

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
            //secondConfirmImageView.setEnabled(false);
        } else {
            firstConfirmButton.setEnabled(false);
            firstConfirmButton.setBackgroundColor(Color.parseColor(getString(R.string.colorDisabledString)));
            secondConfirmImageView.setEnabled(true);
        }
    }

    private void showToast(String toastText, int duration) {
        if (activeToast != null) {
            activeToast.cancel(); //prevent queuing up toasts
        }
        //display toast to instruct user what to do next
        activeToast = Toast.makeText(getApplicationContext(), toastText, duration);
        activeToast.show();
    }

    public void onClickAddItem(View view) {
        if (view.getId() == R.id.bt_first_confirm) {
            if (descriptionEditText.getText().length() == 0) { //nothing is entered
                showToast(getString(R.string.emptyDescriptionMsg), Toast.LENGTH_LONG);
                return;
            }
            prepareButtons(false);
            showToast(getString(R.string.confirmSnoutToastMsg), Toast.LENGTH_SHORT);

            SysUtils.hideKeyboard(this);

            secondConfirmImageView.requestFocus();
        } else
            if (view.getId() == R.id.bt_second_confirm) {
                //instruct the user to first click on add button
                if (firstConfirmButton.isEnabled()) {
                    showToast(getString(R.string.userInstructionToastMsg), Toast.LENGTH_LONG);
                    return;
                }
                //add an item to database
                String description = descriptionEditText.getText().toString();
                DatabaseOperation.addNewCheckedItem(db, description);
                //finish the activity
                showToast(getString(R.string.addSuccessMsg), Toast.LENGTH_SHORT);
                finish();
            }
    }
}
