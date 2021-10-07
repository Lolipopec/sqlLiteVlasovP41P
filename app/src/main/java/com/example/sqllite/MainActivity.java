package com.example.sqllite;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnAdd, btnRead, btnClear;
    EditText etName, etEmail;
    TextView textView;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);

        textView = (TextView) findViewById(R.id.TextBox);

        dbHelper = new com.example.sqllite.DBHelper(this);
    }

    @Override
    public void onClick(View v) {

        String name = etName.getText().toString();
        String email = etEmail.getText().toString();

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();


        switch (v.getId()) {

            case R.id.btnAdd:
                contentValues.put(com.example.sqllite.DBHelper.KEY_NAME, name);
                contentValues.put(com.example.sqllite.DBHelper.KEY_MAIL, email);

                database.insert(com.example.sqllite.DBHelper.TABLE_CONTACTS, null, contentValues);
                break;

            case R.id.btnRead:
                Cursor cursor = database.query(com.example.sqllite.DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(com.example.sqllite.DBHelper.KEY_ID);
                    int nameIndex = cursor.getColumnIndex(com.example.sqllite.DBHelper.KEY_NAME);
                    int emailIndex = cursor.getColumnIndex(com.example.sqllite.DBHelper.KEY_MAIL);
                    String s="";
                    do {
                        Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                                ", name = " + cursor.getString(nameIndex) +
                                ", email = " + cursor.getString(emailIndex));
                        s +="\nname = " + cursor.getString(nameIndex) +
                                ", email = " + cursor.getString(emailIndex);
                    } while (cursor.moveToNext());
                    textView.setText(s);
                } else
                    Log.d("mLog","0 rows");

                cursor.close();
                break;

            case R.id.btnClear:
                database.delete(com.example.sqllite.DBHelper.TABLE_CONTACTS, null, null);
                break;
        }
        dbHelper.close();
    }
}