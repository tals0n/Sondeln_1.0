package de.tali.sondeln20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tali on 01.06.2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper

{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "findings";
    private static final String TABLE_FINDING = "finding";



    /// Column names

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_PIC = "picture";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FINDING_TABLE = "CREATE TABLE " + TABLE_FINDING + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                +KEY_NAME + " TEXT," + KEY_LATITUDE +" REAL,"+
                KEY_LONGITUDE + " REAL," + KEY_PIC + " Text" + ")";
        db.execSQL(CREATE_FINDING_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FINDING);

        onCreate(db);
    }

    public void addFinding(Finding finding)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(KEY_NAME,finding.getName());
        values.put(KEY_LATITUDE, finding.getLatitude());
        values.put(KEY_LONGITUDE, finding.getLongitude());
        values.put(KEY_PIC, finding.getPicture());

        db.insert(TABLE_FINDING,null,values);
        db.close();
    }


    public Finding getFinding(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FINDING, new String[]
                {KEY_ID,KEY_NAME,KEY_LATITUDE,KEY_LONGITUDE,KEY_PIC}, KEY_ID + "=?",
                new String[] {String.valueOf(id)}, null,null,null,null);
        if(cursor != null)
            cursor.moveToFirst();

        Finding finding = new Finding(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getDouble(2),cursor.getDouble(3),cursor.getString(4));


        return finding;

    }


    public ArrayList<Finding> getAllFinding()
    {
        ArrayList<Finding> findinglist = new ArrayList<Finding>();

        String selectQuery = "SELECT * FROM " + TABLE_FINDING;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst())
        {
            do
            {
                Finding finding = new Finding();
                finding.set_id(Integer.parseInt(cursor.getString(0)));
                finding.setName(cursor.getString(1));
                finding.setLatitude(cursor.getDouble(2));
                finding.setLongitude(cursor.getDouble(3));
                findinglist.add(finding);

            }while (cursor.moveToNext());

        }


        return findinglist;
    }

    public int getFindingsCount()
    {
        String countQuery = "SELECT * FROM " + TABLE_FINDING;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);
        return cursor.getCount();
    }




    public int updateFinding(Finding finding)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, finding.getName());
        values.put(KEY_LATITUDE, finding.getLatitude());
        values.put(KEY_LONGITUDE, finding.getLongitude());
        values.put(KEY_PIC, finding.getPicture());

        return db.update(TABLE_FINDING, values, KEY_ID + " = ?",
                new String[]{String.valueOf(finding.getid())});
    }



    public void deleteFinding(Finding finding)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FINDING, KEY_ID + " =? ",
                new String[]{String.valueOf(finding.getid())});
        db.close();
    }
}
