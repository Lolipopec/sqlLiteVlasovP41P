package com.example.sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DBHelper  extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "menuDb";
    public static final String TABLE_MENU = "menu";

    public static final String KEY_ID = "_id";
    public static final String KEY_DISH = "dish";
    public static final String KEY_PRICE = "price";
    public static final String KEY_COMPOSITION = "composition";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_MENU + "(" + KEY_ID
                + " integer primary key," + KEY_DISH + " text," + KEY_COMPOSITION  + " text," + KEY_PRICE + " text"+")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_MENU);

        onCreate(db);

    }
}