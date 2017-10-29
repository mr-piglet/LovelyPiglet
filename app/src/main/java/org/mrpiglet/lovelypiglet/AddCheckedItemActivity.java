package org.mrpiglet.lovelypiglet;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AddCheckedItemActivity extends AppCompatActivity {
    Button firstConfirmButton, secondConfirmButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_checked_item);

        firstConfirmButton = (Button) findViewById(R.id.bt_first_confirm);
        secondConfirmButton = (Button) findViewById(R.id.bt_second_confirm);

        prepareButtons(true); //set first confirm button to enabled, and second to disabled
    }

    // true - set first confirm button to enabled, and second to disabled
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

    public void onClickAddItem(View view) {
        if (view.getId() == R.id.bt_first_confirm) {
            prepareButtons(false);
            //display toast to instruct user what to do next
            Toast.makeText(getApplicationContext(), getString(R.string.confirmSnoutToastMsg), Toast.LENGTH_LONG).show();
        } else
            if (view.getId() == R.id.bt_second_confirm) {
                //instruct the user to first click on add button
                if (firstConfirmButton.isEnabled()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.userInstructionToastMsg), Toast.LENGTH_LONG).show();
                    return;
                }
                finish();
            }
    }
}
