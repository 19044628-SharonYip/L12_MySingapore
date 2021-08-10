package sg.edu.rp.c346.id19044628.ndpsongsdialog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "islands.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_ISLAND = "Island";
	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_DESCRIPTION = "description";
	private static final String COLUMN_AREA = "area";
	private static final String COLUMN_STARS = "stars";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// CREATE TABLE Song
		// (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT,
		// singers TEXT, stars INTEGER, year INTEGER );
		String createSongTableSql = "CREATE TABLE " + TABLE_ISLAND + "("
				+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ COLUMN_NAME + " TEXT, "
				+ COLUMN_DESCRIPTION + " TEXT, "
				+ COLUMN_AREA + " INTEGER, "
                + COLUMN_STARS + " INTEGER )";
		db.execSQL(createSongTableSql);
		Log.i("info", createSongTableSql + "\ncreated tables");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ISLAND);
		onCreate(db);
	}

	public long insertSong(String title, String singers, int year, int stars) {
		// Get an instance of the database for writing
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, title);
		values.put(COLUMN_DESCRIPTION, singers);
        values.put(COLUMN_AREA, year);
		values.put(COLUMN_STARS, stars);
		// Insert the row into the TABLE_SONG
		long result = db.insert(TABLE_ISLAND, null, values);
		// Close the database connection
		db.close();
        Log.d("SQL Insert","" + result);
        return result;
	}

	public ArrayList<Island> getAllIslands() {
		ArrayList<Island> islandslist = new ArrayList<Island>();
		String selectQuery = "SELECT " + COLUMN_ID + ","
				+ COLUMN_NAME + "," + COLUMN_DESCRIPTION + ","
				+ COLUMN_AREA + ","
				+ COLUMN_STARS + " FROM " + TABLE_ISLAND;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(0);
				String name = cursor.getString(1);
				String desc = cursor.getString(2);
				int area = cursor.getInt(3);
                int stars = cursor.getInt(4);

				Island newIsland = new Island(id, name, desc, area, stars);
				islandslist.add(newIsland);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return islandslist;
	}

	public ArrayList<Island> getAllislandsByStars(int starsFilter) {
		ArrayList<Island> islandslist = new ArrayList<Island>();

		SQLiteDatabase db = this.getReadableDatabase();
        String[] columns= {COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_AREA, COLUMN_STARS};
        String condition = COLUMN_STARS + ">= ?";
        String[] args = {String.valueOf(starsFilter)};

        //String selectQuery = "SELECT " + COLUMN_ID + ","
        //            + COLUMN_TITLE + ","
        //            + COLUMN_SINGERS + ","
        //            + COLUMN_YEAR + ","
        //            + COLUMN_STARS
        //            + " FROM " + TABLE_SONG;

        Cursor cursor;
        cursor = db.query(TABLE_ISLAND, columns, condition, args, null, null, null, null);

        // Loop through all rows and add to ArrayList
		if (cursor.moveToFirst()) {
			do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String desc = cursor.getString(2);
                int area = cursor.getInt(3);
                int stars = cursor.getInt(4);

                Island newIsland = new Island(id, name, desc, area, stars);
				islandslist.add(newIsland);
			} while (cursor.moveToNext());
		}
		// Close connection
		cursor.close();
		db.close();
		return islandslist;
	}

	public int updateIsland(Island data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, data.getName());
        values.put(COLUMN_DESCRIPTION, data.getDescription());
        values.put(COLUMN_AREA, data.getArea());
        values.put(COLUMN_STARS, data.getStars());
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(data.getId())};
        int result = db.update(TABLE_ISLAND, values, condition, args);
        db.close();
        return result;
    }


    public int deleteSong(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(id)};
        int result = db.delete(TABLE_ISLAND, condition, args);
        db.close();
        return result;
    }



}
