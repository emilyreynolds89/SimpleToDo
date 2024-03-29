package com.codepath;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import static com.codepath.MainActivity.ITEM_TEXT;
import static com.codepath.MainActivity.ITEM_POSITION;

public class EditItemActivity extends AppCompatActivity {

    // text field containing updated item description
    EditText etItemText;

    // track item's position in the list
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // resolve the text field from the layout
        etItemText = (EditText) findViewById(R.id.etItemText);

        // set the text field's content from the intent
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));

        // track the position of the item in the list
        position = getIntent().getIntExtra(ITEM_POSITION, 0);

        // set the title bar to reflect the purpose of the view
        getSupportActionBar().setTitle("Edit Item");
    }

    public void onSaveItem(View v) {
        // Prepare intent to pass back to MainActivity
        Intent data = new Intent();

        // Pass updated item text and original position
        data.putExtra(ITEM_TEXT, etItemText.getText().toString());

        data.putExtra(ITEM_POSITION, position); // ints work too
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the edit activity, passes intent back to main
    }
}
