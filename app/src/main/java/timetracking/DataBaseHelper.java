package timetracking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {


    private static final String TAG = "Databasehelper";

    private static final String TABLE_NAME = "testing";
    private static final String COL1 = "ID";
    private static final String COL2 = "name";

    public DataBaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME +  " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COL2 +" TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);

    }

    public boolean addData(String item) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);
   //   contentValues.put(COL1, item);

        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //falls daten falsch eingegeben wurden return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //return all data which is in the database
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
   //     query = String.format("SELECT * FROM {0} WHERE a={1}", TABLE_NAME, "asdasd");
        Cursor data = db.rawQuery(query, null);

        return data;
    }

  /*  public Cursor getDatumAnfang(String datum){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + datum + "'";
        Cursor data = db.rawQuery(query, null);
        return data;


    }
*/
    public void updateName(String NameNeu, int id, String NameAlt){

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + NameNeu + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + NameAlt + "'";

        Log.d(TAG, "deltename: query " +query);
        Log.d(TAG, "updatename: Setting name to" +NameNeu);

        db.execSQL(query);

    }

    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + name + "'";

        Log.d(TAG, "deleteName: query " +query);
        Log.d(TAG, "deleteName: Deleting" + name + "from Database");

        db.execSQL(query);
    }

    public Cursor getItemId(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME + " WHERE " + COL2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

}
