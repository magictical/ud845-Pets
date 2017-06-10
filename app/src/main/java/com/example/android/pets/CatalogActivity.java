/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.pets.data.PetDbHelper;
import com.example.android.pets.data.PetContract.PetEntry;

import static android.R.attr.max;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

    public static final String LOG_TAG = CatalogActivity.class.getName();

    private PetDbHelper mPetDbHelper;

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        mPetDbHelper = new PetDbHelper(this);
        /*SQLiteDatabase db = mPetDbHelper.getReadableDatabase();*/
    }

    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mPetDbHelper = new PetDbHelper(this);

        // Create and/or open a database to read from it
        SQLiteDatabase db = mPetDbHelper.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        //read data from the table by query

        //커서에서 사용될 테이블이 여기서 정의 되니 가능하면 모든 테이블의 컬럼을 선택하는것이 편하다
        String[] projection = { PetEntry._ID, PetEntry.COLUMN_PET_NAME, PetEntry.COLUMN_PET_BREED,
        PetEntry.COLUMN_PET_GENDER, PetEntry.COLUMN_PET_WEIGHT};
        String selection = PetEntry._ID;
        String [] selectionArgs = { "max" };


        //이제 테이블에 사용될 커서가 정의됨 위에서 테이블의 범위를 정의한것에 주의하자
        Cursor cursor = db.query(
                PetEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
                );
        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.text_view_pet);
            displayView.setText("The "+ PetEntry.TABLE_NAME +" table contains "+ cursor.getCount() + "\n");
            displayView.append(PetEntry._ID + "-" + PetEntry.COLUMN_PET_NAME + "-" +
            PetEntry.COLUMN_PET_BREED + " - " + PetEntry.COLUMN_PET_GENDER + " - " + PetEntry.COLUMN_PET_WEIGHT + "\n");

            //index of each column
            int idColumn = cursor.getColumnIndex(PetEntry._ID);
            int nameColumn = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
            int breedColumn = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
            int genderColumn = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
            int weightColumn = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);


            //use while clause to get a all data from pets table
            while(cursor.moveToNext()) {
                int _id = cursor.getInt(idColumn);
                String name = cursor.getString(nameColumn);
                String breed = cursor.getString(breedColumn);
                int gender = cursor.getInt(genderColumn);
                int weight = cursor.getInt(weightColumn);

                displayView.append("\n"+ _id + " - " + name + " - " + breed + " - " + gender + " - " +
                        weight + "\n");
            }



        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }


    private void insertPet() {
        mPetDbHelper = new PetDbHelper(this);
        SQLiteDatabase db = mPetDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetEntry.COLUMN_PET_GENDER, 1);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 7);
        //insert the new row, get the primary key of the new row
        long newRowId = db.insert(PetEntry.TABLE_NAME, null, values);
        Log.v(LOG_TAG, "the row number is "  + newRowId);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                //insert dummy data
                insertPet();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
