package funix.prm.a2prm391x_alarmclock_letbfx08130;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AlarmDatabase extends SQLiteOpenHelper {
    //database name
    public static final String DATABASE_NAME="alarm_manager";
    //database version
    public static final int DATABASE_VERSION=1;
    public static final String TABLE_NAME="alarm_table";

    public AlarmDatabase(Context context) {
        super ( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY, Time TEXT)";
        db.execSQL ( query );
    }

    //upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL ( "DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate ( db );
    }

    //add the new alarm
    public void addAlarm(String time) {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase ( );
        ContentValues values=new ContentValues ( );
        values.put ( "Time", time );
        //inserting new row
        sqLiteDatabase.insert ( TABLE_NAME, null, values );
        //close database connection
        sqLiteDatabase.close ( );
    }

    //get the all notes
    public List<Alarm> getAllAlarm() {
        List<Alarm> arrayList=new ArrayList<> ( );
        // select all query
        String select_query="SELECT *FROM " + TABLE_NAME;

        SQLiteDatabase db=this.getWritableDatabase ( );
        Cursor cursor=db.rawQuery ( select_query, null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst ( )) {
            do {
                Alarm alarm=new Alarm ( );
                alarm.setId ( cursor.getString ( 0 ) );
                alarm.setTime ( cursor.getString ( 1 ) );
                arrayList.add ( alarm );
            } while (cursor.moveToNext ( ));
        }
        cursor.close ( );
        return arrayList;
    }

    //delete alarm
    public void delete(String id) {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase ( );
        //deleting row
        sqLiteDatabase.delete ( TABLE_NAME, "ID=" + id, null );
        sqLiteDatabase.close ( );
    }

    //update the alarm
    public void updateAlarm(String time, String id) {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase ( );
        ContentValues values=new ContentValues ( );
        values.put ( "Time", time );
        //updating row
        sqLiteDatabase.update ( TABLE_NAME, values, "ID=" + id, null );
        sqLiteDatabase.close ( );
    }
}
