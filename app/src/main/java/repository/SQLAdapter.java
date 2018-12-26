package repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.provider.BaseColumns;
import android.util.Log;

import entity.Product;

import static repository.SQLAdapter.Products.COLUMN_NAME_BOUGHT;
import static repository.SQLAdapter.Products.COLUMN_NAME_DESCRIPTION;
import static repository.SQLAdapter.Products.COLUMN_NAME_ISCHECKED;
import static repository.SQLAdapter.Products.COLUMN_NAME_LIKED;
import static repository.SQLAdapter.Products.COLUMN_NAME_NAME;
import static repository.SQLAdapter.Products.COLUMN_NAME_PHOTO;
import static repository.SQLAdapter.Products.COLUMN_NAME_PRICE;
import static repository.SQLAdapter.Products.TABLE_NAME;

public class SQLAdapter {
    String TAG = "SQLadapter";
    private final Context mCtx;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private String orderBy = null;
    private  static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Product.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    Products._ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_NAME + " TEXT," +
                    COLUMN_NAME_DESCRIPTION + " TEXT," +
                    COLUMN_NAME_PRICE + " TEXT," +
                    COLUMN_NAME_LIKED + " TEXT,"+
                    COLUMN_NAME_BOUGHT + " TEXT,"+
                    COLUMN_NAME_PHOTO + " TEXT,"+
                    COLUMN_NAME_ISCHECKED + " TEXT)"
            ;
    public static class Products implements BaseColumns {
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_LIKED = "liked";
        public static final String COLUMN_NAME_BOUGHT = "bought";
        public static final String COLUMN_NAME_PHOTO = "photo";
        public static final String COLUMN_NAME_ISCHECKED = "isChecked";

    }

    public class DatabaseHelper extends SQLiteOpenHelper {
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        private DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    public SQLAdapter (Context mCtx){
        this.mCtx = mCtx;
    }
    public SQLAdapter open() throws SQLException {
        mDbHelper = new SQLAdapter.DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
//"nothing wrong with leaving the database connection open"(Dianne Hackborn) =)
    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }
    public long createProduct(String name, String description, String price, boolean liked, boolean bought, byte[] photo, boolean isChecked) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_NAME_NAME, name);
        initialValues.put(COLUMN_NAME_DESCRIPTION, description);
        initialValues.put(COLUMN_NAME_PRICE, price);
        initialValues.put(COLUMN_NAME_LIKED, liked);
        initialValues.put(COLUMN_NAME_BOUGHT, bought);
        initialValues.put(COLUMN_NAME_PHOTO, photo);
        initialValues.put(COLUMN_NAME_ISCHECKED, isChecked);

        return mDb.insert(TABLE_NAME, null, initialValues);
    }
    public long changeBuyStatus (boolean bought, long id) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_NAME_BOUGHT, bought);

        return mDb.update(TABLE_NAME,  initialValues,"_id = ?", new String[] { String.valueOf(id) });
    }

    public Cursor fetchAllProducts() {
        Cursor mCursor = mDb.query(Products.TABLE_NAME, new String[]{Products._ID, COLUMN_NAME_NAME,
                Products.COLUMN_NAME_DESCRIPTION, Products.COLUMN_NAME_PRICE,Products.COLUMN_NAME_LIKED, Products.COLUMN_NAME_BOUGHT,Products.COLUMN_NAME_PHOTO, Products.COLUMN_NAME_ISCHECKED}, null, null, null, null, orderBy, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }
    public boolean deleteAllProducts() {
        int doneDelete = 0;
        doneDelete = mDb.delete(Products.TABLE_NAME, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }
}
