package ch.uzh.csg.foodchain.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * The type Database helper.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * The constant DATABASE_NAME.
     */
    public static final String DATABASE_NAME = "foodchain.db";
    /**
     * The constant ACTION_TABLE.
     */
    public static final String ACTION_TABLE = "actions";
    /**
     * The constant CERTIFICATE_TABLE.
     */
    public static final String CERTIFICATE_TABLE = "certificate";
    /**
     * The constant QR_ACTION.
     */
    public static final String QR_ACTION = "qr_actions";

    /**
     * Instantiates a new Database helper.
     *
     * @param context the context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

        SQLiteDatabase database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CERTIFICATE_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,CERTIFICATE_ID TEXT,CERTIFICATE_NAME TEXT)");
        db.execSQL("CREATE TABLE " + ACTION_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,ACTION_ID TEXT,ACTION_NAME TEXT)");
        db.execSQL("CREATE TABLE " + QR_ACTION + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,ACTION_ID TEXT,ACTION_NAME TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CERTIFICATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ACTION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + QR_ACTION);
        onCreate(db);
    }

    /**
     * Insert data boolean.
     *
     * @param certificate_id   the certificate id
     * @param certificate_name the certificate name
     * @return the boolean
     */
//inserting certificates
    public boolean insertData(String certificate_id, String certificate_name) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CERTIFICATE_ID", certificate_id);
        contentValues.put("CERTIFICATE_NAME", certificate_name);
        long insert = database.insert(CERTIFICATE_TABLE, null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Insert data actions boolean.
     *
     * @param action_id   the action id
     * @param action_name the action name
     * @return the boolean
     */
    public boolean insertDataActions(String action_id, String action_name) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ACTION_ID", action_id);
        contentValues.put("ACTION_NAME", action_name);
        long insert = database.insert(ACTION_TABLE, null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Insert qr actions boolean.
     *
     * @param action_id   the action id
     * @param action_name the action name
     * @return the boolean
     */
    public boolean insertQRActions(String action_id,String action_name) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ACTION_ID", action_id);
        contentValues.put("ACTION_NAME", action_name);
        long insert = database.insert(QR_ACTION, null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Gets all certificates.
     *
     * @return the all certificates
     */
    public Cursor getAllCertificates() {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + CERTIFICATE_TABLE;
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }

    /**
     * Gets all action.
     *
     * @return the all action
     */
    public Cursor getAllAction() {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + ACTION_TABLE;
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }

    /**
     * Gets all qr actions.
     *
     * @return the all qr actions
     */
    public Cursor getAllQrActions() {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + QR_ACTION;
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }


    /**
     * Delete certificate integer.
     *
     * @param id the id
     * @return the integer
     */
    public Integer deleteCertificate(String id) {
        int a = Integer.parseInt(id);
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "DELETE FROM certficate WHERE BusNum = " + id;
        return database.delete(CERTIFICATE_TABLE, "CERTIFICATE_ID=?", new String[]{Integer.toString(a)});
    }

    /**
     * Delete action integer.
     *
     * @param id the id
     * @return the integer
     */
    public Integer deleteAction(String id) {
        int a = Integer.parseInt(id);
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "DELETE FROM actions WHERE BusNum = " + id;
        return database.delete(ACTION_TABLE, "ACTION_ID=?", new String[]{Integer.toString(a)});
    }

    /**
     * Delete qr action integer.
     *
     * @param id the id
     * @return the integer
     */
    public Integer deleteQrAction(String id) {
        int a = Integer.parseInt(id);
        Log.d("id",String.valueOf(a));
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(QR_ACTION, "ACTION_ID=?", new String[]{Integer.toString(a)});
    }


    /**
     * Clear database.
     */
    public void clearDatabase() {
        SQLiteDatabase database = this.getWritableDatabase();
        String clearDBQuery = "DELETE  FROM " + CERTIFICATE_TABLE;
        String clearDBAction = "DELETE  FROM " + ACTION_TABLE;
        database.execSQL(clearDBQuery);
        database.execSQL(clearDBAction);
    }

    /**
     * Clear qr action table.
     */
    public void clearQrActionTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearQrAction = "DELETE  FROM " + QR_ACTION;
        db.execSQL(clearQrAction);
    }
}
