package com.codepath;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // numeric code to identify the edit activity
    public static final int EDIT_REQUEST_CODE = 20;

    // keys used for passing data between activities
    public static final String ITEM_TEXT = "itemText";
    public static final String ITEM_POSITION = "itemPosition";

    // declaring stateful objects here
    // these will be null before onCreate is called
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // obtain reference to the ListView created with the layout
        lvItems = (ListView) findViewById(R.id.lvItems);

        // initialize items list
        readItems();

        // initialize the adapter using the items list
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        // wire the adapter to the view
        lvItems.setAdapter(itemsAdapter);

        // added mock items to the list
        // items.add("First to do item");
        // items.add("Second to do item");

        // setup the listener on creation
        setupListViewListener();
    }

    private void setupListViewListener() {
        // set the ListView's itemLongClickListener
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // remove the item in the list at the index given by position
                items.remove(position);

                // notify adapter that the underlying dataset changed
                itemsAdapter.notifyDataSetChanged();

                // logging
                Log.i("MainActivity", "Removed item" + position);

                // store the updated list
                writeItems();

                // return true to tell framework the long click was consumed
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // first parameter is the context, second the class of the activity to launch
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);

                // put extras into the bundle for access in the edit activity
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);

                // brings up the edit activity with the expectation of the result
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        });
    }

    public void onAddItem(View v) {
        // obtain reference to the EditText created with the layout
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);

        // grab the EditText content as a string
        String itemText = etNewItem.getText().toString();

        // add the item to the list via the adapter
        itemsAdapter.add(itemText);

        // store the updated list
        writeItems();

        // clear the EditText by setting to empty string
        etNewItem.setText("");

        // display a notification to the user
        Toast.makeText(getApplicationContext(), "Item added to the list", Toast.LENGTH_SHORT).show();
    }

    // returns the file in which the data is stored
    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    // read the items from the file system
    private void readItems() {
        try {
            // create the array using the content in the file
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            // print error to console
            e.printStackTrace();

            // load empty list
            items = new ArrayList<>();
        }
    }

    // write the items to the filesystem
    private void writeItems() {
        try {
            // save the item list as a line delimited text file
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            // print error to console
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // EDIT_REQUEST_CODE defined with constants
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            // extract updated item value from result extras
            String updatedItem = data.getExtras().getString(ITEM_TEXT);

            // get the position of the item which was edited
            int position = data.getExtras().getInt(ITEM_POSITION, 0);

            // update the model with the new item text at the edited position
            items.set(position, updatedItem);

            // notify the adapter the model changed
            itemsAdapter.notifyDataSetChanged();

            // Store the updated items back to disk
            writeItems();

            // notify the user the operation completed OK
            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
        }
    }
}
