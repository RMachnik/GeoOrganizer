package pl.rafik.geoorganizer.model.entity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskOpenHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "TASKS.db";
	private static final int DB_VERSION = 1;
	public static final String TABLE_NAME = "TASKS";
	public static final String ID = "_ID";
	public static final String TASK_NOTE = "TASK_NOTE";
	public static final String TASK_DATE = "TASK_DATE";

	public static final String TASK_LATITUDE = "TASK_LATITUDE";
	public static final String TASK_LONGITUDE = "TASK_LONGITUDE";
	public static final String TASK_ADDRESS = "TASK_ADDRESS";
	public static final String TASK_PRIORITY = "TASK_PRIORITY";
	public static final String TASK_STATUS = "TASK_STATUS";

	private static final String DB_CREATE = "CREATE TABLE " + TABLE_NAME
			+ " ( " + ID + " integer primary key autoincrement, " + TASK_NOTE
			+ " text, " + TASK_DATE + " date, " + TASK_LATITUDE + " blob, "
			+ TASK_LONGITUDE + " blob, " + TASK_ADDRESS + " text, "
			+ TASK_PRIORITY + " integer, " + TASK_STATUS + " text);";

	public TaskOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DB_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
