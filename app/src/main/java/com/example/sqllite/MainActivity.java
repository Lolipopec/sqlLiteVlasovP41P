package com.example.sqllite;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnAdd, btnClear;
    EditText etDish, etPrice, etComposition;
    SQLiteDatabase database;
    ContentValues contentValues;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etDish = (EditText) findViewById(R.id.etDish);
        etPrice = (EditText) findViewById(R.id.etPrice);
        etComposition = (EditText) findViewById(R.id.etComposition);
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        UpdateTable();
    }
public void UpdateTable ()
{
    Cursor cursor = database.query(DBHelper.TABLE_MENU, null, null, null, null, null, null);

    if (cursor.moveToFirst()) {
        int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
        int dishIndex = cursor.getColumnIndex(DBHelper.KEY_DISH);
        int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
        int compositionIndex = cursor.getColumnIndex(DBHelper.KEY_COMPOSITION);
        TableLayout dpOutput = findViewById(R.id.dpOutput);
        dpOutput.removeAllViews();
        do {
            TableRow dpOutputRow = new TableRow(this);
            dpOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            LinearLayout.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

            TextView outputDish = new TextView(this);
            params.weight = 3.0f;
            outputDish.setLayoutParams(params);
            outputDish.setText(cursor.getString(dishIndex));
            dpOutputRow.addView(outputDish);

            TextView outputPrice = new TextView(this);
            params.weight = 3.0f;
            outputPrice.setLayoutParams(params);
            outputPrice.setText(cursor.getString(priceIndex));
            dpOutputRow.addView(outputPrice);

            TextView outputComposition = new TextView(this);
            params.weight = 3.0f;
            outputComposition.setLayoutParams(params);
            outputComposition.setText(cursor.getString(compositionIndex));
            dpOutputRow.addView(outputComposition);

            Button deleteBtn = new Button(this);
            deleteBtn.setOnClickListener(this);
            params.weight =1.0f;
            deleteBtn.setLayoutParams(params);
            deleteBtn.setText("Удалить");
            deleteBtn.setId(cursor.getInt(idIndex));
            dpOutputRow.addView(deleteBtn);

            dpOutput.addView(dpOutputRow);

        } while (cursor.moveToNext());
        cursor.close();
    }
}
    @Override
    public void onClick(View v) {


        dbHelper = new DBHelper(this);
        switch (v.getId()) {

            case R.id.btnAdd:
                String dish = etDish.getText().toString();
                String price = etPrice.getText().toString();
                String composition = etComposition.getText().toString();
                contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_COMPOSITION, composition);
                contentValues.put(DBHelper.KEY_PRICE, price);
                contentValues.put(DBHelper.KEY_DISH, dish);
                etDish.setText("");
                etPrice.setText("");
                etComposition.setText("");

                database.insert(DBHelper.TABLE_MENU, null, contentValues);
                UpdateTable();

                break;

            case R.id.btnClear:
                TableLayout dpOutput = findViewById(R.id.dpOutput);
                dpOutput.removeAllViews();
                database.delete(DBHelper.TABLE_MENU, null, null);
                UpdateTable();
                break;
            default:
                View outputDBRow = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                outputDB.removeView(outputDBRow);
                outputDB.invalidate();

                database.delete(DBHelper.TABLE_MENU,dbHelper.KEY_ID+" = ?",new String[]{ String.valueOf(v.getId())});
                contentValues = new ContentValues();
                Cursor cursorUpdater = database.query(DBHelper.TABLE_MENU, null, null, null, null, null, null);
                if (cursorUpdater.moveToFirst()) {
                    int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID);
                    int dishIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_DISH);
                    int priceIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PRICE);
                    int compositionIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_COMPOSITION);
                    int realID = 1;
                    do {
                        if (cursorUpdater.getInt(idIndex) > realID) {
                            contentValues.put(DBHelper.KEY_ID, realID);
                            contentValues.put(DBHelper.KEY_DISH, cursorUpdater.getString(dishIndex));
                            contentValues.put(DBHelper.KEY_PRICE, cursorUpdater.getString(priceIndex));
                            contentValues.put(DBHelper.KEY_COMPOSITION, cursorUpdater.getString(compositionIndex));
                            database.replace(DBHelper.TABLE_MENU, null, contentValues);
                        }
                        realID++;
                    } while (cursorUpdater.moveToNext());
                    if  (cursorUpdater.moveToLast() && v.getId()!=realID){
                        database.delete(dbHelper.TABLE_MENU, DBHelper.KEY_ID + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                    }
                    UpdateTable();
                }
                break;
        }
        dbHelper.close();
    }
}