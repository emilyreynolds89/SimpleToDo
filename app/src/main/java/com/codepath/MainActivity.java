package com.codepath;

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
}
