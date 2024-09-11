package bjfu.it.duanyingli.dreader;

import android.app.Application;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import bjfu.it.duanyingli.dreader.dbHelper.DUserDatabaseHelper;

public class DReaderApp extends Application {
    private String phone;
    private int bookInCarNum;
    private int bookId;
    private Cursor cursor;
    private Cursor cursor1;

    @Override
    public void onCreate() {
        super.onCreate();
        if (phone != null) {
            SQLiteOpenHelper dreaderDatabaseHelper = new DUserDatabaseHelper(this);
            try (SQLiteDatabase db = dreaderDatabaseHelper.getReadableDatabase()) {
                Cursor cursor = db.rawQuery("select BookNum,sum(BookNum) from CAR where PhoneNum = ?", new String[]{phone});
                if (cursor.moveToFirst()) {
                    bookInCarNum = cursor.getInt(1);
                } else {
                    bookInCarNum = 0;
                }
            } catch (SQLException e) {
                Toast.makeText(this, "Database Unavailible", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBookInCarNum() {
        SQLiteOpenHelper dreaderDatabaseHelper = new DUserDatabaseHelper(this);
        try (SQLiteDatabase db = dreaderDatabaseHelper.getReadableDatabase()) {
            //找出有没有购物车选项
            if (this.phone != null) {
                cursor1 = db.rawQuery("select sum(BookNum) from CAR where PhoneNum = ? group by PhoneNum",new String[]{phone});
                if((cursor1.moveToFirst())&&(cursor1.getCount() != 0))
                    this.bookInCarNum = cursor1.getInt(0);
            }
        }
    }

    public int getBookInCarNum() {
        return this.bookInCarNum;
    }

    public void setBookId(int id) {
        this.bookId = id;
    }

    public int getBookId() {
        return this.bookId;
    }

    public void setCursor() {
        SQLiteOpenHelper dreaderDatabaseHelper = new DUserDatabaseHelper(this);
        try (SQLiteDatabase db = dreaderDatabaseHelper.getReadableDatabase()) {
            //找出有没有购物车选项
            if (this.phone != null) {
                cursor = db.rawQuery("select _id,BName,BPrice, B_Picture_ResourceId,BookNum from CAR,BOOK " +
                        "where PhoneNum = ? and CAR._bid = BOOK._id", new String[]{phone});
                if (cursor.moveToFirst()) {
                    this.cursor = cursor;
                }
            }
        }
    }

    public Cursor getCursor() {
        return cursor;
    }
}

