package bjfu.it.duanyingli.dreader.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import bjfu.it.duanyingli.dreader.DReaderApp;
import bjfu.it.duanyingli.dreader.MainActivity;
import bjfu.it.duanyingli.dreader.R;
import bjfu.it.duanyingli.dreader.dbHelper.DUserDatabaseHelper;
import bjfu.it.duanyingli.dreader.myweidgets.NumImageView;


/*
每本书的详细信息
 */
public class BookActivity extends AppCompatActivity {
    public static String EXTRA_BOOKID = "bookId";
    public static String EXTRA_NUMBER; //当前登录用户号码
    private Cursor cursor;
    private Cursor cursor1;
    private Cursor cursor2;
    private Cursor cursor3;
    public int bookId;
    private String nowPhoneNum;
    public String nameText = "DReader";
    private NumImageView actionbar_btn;

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            cursor.close();
            cursor1.close();
            cursor2.close();
            cursor3.close();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        this.getSupportActionBar().hide();
        nowPhoneNum = getIntent().getStringExtra(EXTRA_NUMBER);
        if(nowPhoneNum == null)
            nowPhoneNum = ((DReaderApp)getApplication()).getPhone();
        bookId = getIntent().getIntExtra(EXTRA_BOOKID, -1);
        if(bookId == -1)
            bookId = ((DReaderApp)getApplication()).getBookId();
        SQLiteOpenHelper dreaderDatabaseHelper = new DUserDatabaseHelper(this);
        try (SQLiteDatabase db = dreaderDatabaseHelper.getReadableDatabase()) {
            cursor = db.rawQuery("select * from BOOK where _id = ?", new String[]{Integer.toString(bookId)});
            if (cursor.moveToFirst()) {
                nameText = cursor.getString(1);
                String authurName = cursor.getString(2);
                int price = cursor.getInt(4);
                int photoId = cursor.getInt(6);
                String detail = cursor.getString(7);

                TextView name = findViewById(R.id.BName);
                name.setText(nameText);

                TextView authur = findViewById(R.id.Authur);
                authur.setText(authurName);

                TextView bprice = findViewById(R.id.BPrice);
                bprice.setText(String.format("%s", price));

                ImageView photo = findViewById(R.id.BPic);
                photo.setImageResource(photoId);

                TextView det = findViewById(R.id.BDes);
                det.setText(detail);

                if (nowPhoneNum != null) {
                    cursor1 = db.rawQuery("select * from FAVOURATE where PhoneNum = ? and _bid = ? ", new String[]{nowPhoneNum, String.format("%s", bookId)});
                    if (cursor1.moveToFirst()) {
                        //如果存在这个选项则说明有购物车关系
                        CheckBox car = findViewById(R.id.carBox);
                        car.setChecked(true);
                    }
                }
            }
            actionbar_btn = findViewById(R.id.carNum1);
            setCarNum(actionbar_btn);
        } catch (SQLException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        findViewById(R.id.carBox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UpdateAddtoFavTask().execute(bookId);
            }
        });
    }

    public void onAddToCarclick(View view) {
        actionbar_btn = findViewById(R.id.carNum1);
        setCarNum(actionbar_btn);
        new UpdateAddtoCarTask().execute(bookId);
    }

    private class UpdateAddtoFavTask extends AsyncTask<Integer, Void, Boolean> {
        private ContentValues bookValues; //定义为内部类，便于操作Activity实例变量

        protected void onPreExecute() {
            String nowPhoneNum = getIntent().getStringExtra(EXTRA_NUMBER);
            //此处无论是否加入购物车选项被选中，都要先加入容器中
            bookValues = new ContentValues();
            bookValues.put("PhoneNum", nowPhoneNum);
            bookValues.put("_bid", bookId);
        }//初始化任务

        protected Boolean doInBackground(Integer... books) {
            CheckBox car = (CheckBox) findViewById(R.id.carBox);
            Integer bookId = books[0];
            SQLiteOpenHelper dreaderDatabaseHelper = new DUserDatabaseHelper(BookActivity.this);
            try (SQLiteDatabase db = dreaderDatabaseHelper.getWritableDatabase()) {
                //先查找是否有该条记录
                String nowPhoneNum = getIntent().getStringExtra(EXTRA_NUMBER);
                if (nowPhoneNum == null) {
                    return false;
                } else {
                    cursor2 = db.rawQuery("select * from FAVOURATE where PhoneNum = ? and _bid = ?", new String[]{nowPhoneNum, String.format("%s", bookId)});
                    //如果有，判断当前用户是要加入购物车还是取消加
                    cursor2.moveToNext();
                    if (cursor2.getCount() != 0) {
                        //有。
                        //加入购物车，忽略
                        //不加入购物车，删除
                        if (!car.isChecked()) {
                            //db.delete("FAVOURATE", "PhoneNum = ? and _bid = ?", new String[]{nowPhoneNum, String.format("%s", bookId)});
                            db.execSQL("delete from FAVOURATE where PhoneNum = ? and _bid = ?", new String[]{nowPhoneNum, String.format("%s", bookId)});
                        }
                    } else
                        //没有，还是判断是要加入购物车还是取消加入
                        //加入购物车，插入
                        //不加入购物车，忽略
                        if(car.isChecked()) {
                        db.insert("FAVOURATE", null, bookValues);
                    }
                    return true;
                }
            } catch (SQLiteException e) {
                return false;
            }
        }//执行费时的任务并publishProgress().

        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(BookActivity.this, "请登录账户", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private class UpdateAddtoCarTask extends AsyncTask<Integer, Void, Boolean> {
        private ContentValues carValues;
        protected void onPreExecute() {
            actionbar_btn.setNum(actionbar_btn.getNum()+1);
            carValues = new ContentValues();
            carValues.put("PhoneNum", nowPhoneNum);
        }
        @Override
        protected Boolean doInBackground(Integer... books) {
            Log.e("$$$odIn","$$$");
            int bookId = books[0];
            SQLiteOpenHelper dreaderDatabaseHelper = new DUserDatabaseHelper(BookActivity.this);
            try (SQLiteDatabase db = dreaderDatabaseHelper.getReadableDatabase()) {
                //查找原来有没有
                //有就+1
                //没有就新建
                if(nowPhoneNum == null){
                    return false;
                }else{
                    cursor3 = db.rawQuery("select * from CAR " +
                            "where PhoneNum = ? and _bid = ?", new String[]{nowPhoneNum, String.format("%s",bookId)});
                    if ((cursor3.getCount() != 0)&&(cursor3.moveToFirst())) {
                        //有，所以+1

                        int id = cursor3.getInt(1);
                        int num = cursor3.getInt(2);
                        carValues.put("_bid", id);
                        carValues.put("BookNum", num + 1);
                        db.update("CAR",carValues,"PhoneNum = ? and _bid = ?",new String[]{nowPhoneNum, String.format("%s",bookId)});
                    } else {
                        Log.e("（（（（（（（",nowPhoneNum);
                        carValues.put("_bid", bookId);
                        carValues.put("BookNum", 1);
                        db.insert("CAR",null,carValues);
                    }
                }
                return true;
            }
        }
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(BookActivity.this, "请登录账户", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void onToCarClick(View view) {
        Intent intent = new Intent(BookActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("SWITCH","1");
        intent.putExtras(bundle);
        intent.putExtra(MainActivity.EXTRA_PHONENUM,nowPhoneNum);
        startActivity(intent);
    }

    public void setCarNum(NumImageView view){
        ((DReaderApp)getApplication()).setBookInCarNum();
        int num = ((DReaderApp)getApplication()).getBookInCarNum();
        if(num != 0)
            view.setNum(num);
    }
}