package com.cs616.trysqlite;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v4.app.NavUtils;


public class MainActivity extends AppCompatActivity {

    private int currentMode;
    private long id;
    private Spinner categorySpinner;
    private EditText titleEditText;
    private EditText bodyEditText;
    private DatabaseHandler dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbh = new DatabaseHandler(this);
        try {
            dbh.getCategoryTable().createCategory(new Category("Work"));
            dbh.getCategoryTable().createCategory(new Category("School"));
        }
        catch(SQLException e) {
            Log.w("comments.db", e.getMessage());
        }

        //Log.d("SQLITE", String.valueOf(dbh.getCategoryTable().getCategoryCount()) );

        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        refreshSpinner();

        titleEditText = (EditText) findViewById(R.id.titleEditText_Main);
        bodyEditText = (EditText) findViewById(R.id.bodyEditText_Main);
        Intent i = getIntent();
        String title = i.getStringExtra("title");
        String body = i.getStringExtra("body");
        long category = i.getLongExtra("category", 1);


        //Toast.makeText(this, Long.toString(category), Toast.LENGTH_SHORT).show();

        int k = Integer.parseInt(Long.toString(category));
        titleEditText.setText(title);
        bodyEditText.setText(body);
    }

    public void refreshSpinner() {
        categorySpinner.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_row_main, dbh.getCategoryTable().getAllCategories()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action_save) {
            Note note = new Note();
            note.setTitle(titleEditText.getText().toString());
            note.setBody(bodyEditText.getText().toString());

            Category category = dbh.getCategoryTable().readCategoryByName(categorySpinner.getSelectedItem().toString());
            note.setCategoryId(category.getId());
            dbh.getNoteTable().createNote(note);

            Toast toast = Toast.makeText(this, "Note saved.", Toast.LENGTH_SHORT);
            toast.show();

            finish();
            return true; // TODO
        }
        /*else if(id == R.id.action_debug) {

            Log.d("SQLITE", "=== Start of CategoryTable ===");
            for(Category c : dbh.getCategoryTable().getAllCategories())
                Log.d("SQLITE", "  " + c.toString());
            Log.d("SQLITE", "=== End of CategoryTable ===");
            Log.d("SQLITE", "=== Start of NoteTable ===");
            for(Note note : dbh.getNoteTable().getAllNotes())
                Log.d("SQLITE", "  " + note.toString());
            Log.d("SQLITE", "=== End of CategoryTable ===");

            return true;
        }*/
        else if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onClick(View view) {

        // source: http://stackoverflow.com/questions/10903754/input-text-dialog-android
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Category");


        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addCategory(input.getText().toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void addCategory(String categoryName) {
        dbh.getCategoryTable().createCategory(new Category(categoryName));
        refreshSpinner();
    }



}
