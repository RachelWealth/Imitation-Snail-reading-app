package bjfu.it.duanyingli.dreader.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import bjfu.it.duanyingli.dreader.DReaderApp;
import bjfu.it.duanyingli.dreader.MainActivity;
import bjfu.it.duanyingli.dreader.R;
import bjfu.it.duanyingli.dreader.dbHelper.DUserDatabaseHelper;
import bjfu.it.duanyingli.dreader.myweidgets.NumImageView;

/*每一个种类的书的列表 */
public class BookCatActivity extends AppCompatActivity {
    public static final String EXTRA_BOOKTYPENAME = "BookTypeId";
    public static String EXTRA_NUMBER; //当前登录用户号码
    private Cursor cursor;
    public ListView listBooks;
    public String phoneNum;
    public String typeId = "DReader";
    private NavController controller;
    public static final String ARGS_PHONE = "phone";
    private NumImageView actionbar_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_book_cat);
        NumImageView toolbarCar = findViewById(R.id.carNum);
        setCarNum(toolbarCar);
        this.getSupportActionBar().hide();
        //提取一级目录信使中的消息
        typeId = getIntent().getStringExtra(EXTRA_BOOKTYPENAME);
        SQLiteOpenHelper dreaderDatabaseHelper = new DUserDatabaseHelper(this);
        listBooks = findViewById(R.id.BookList);
        try(SQLiteDatabase db = dreaderDatabaseHelper.getReadableDatabase()){
            cursor = db.query("BOOK",new String[]{"_id","BName","Description","BPrice","B_Picture_ResourceId","Type"},
                    "Type = ?",new String[]{typeId},
                    null,null,null);//选出类型名遇上一个页面相同的图书
            String[] from = new String[]{"BName","Description","BPrice","B_Picture_ResourceId"};
            int[] to = new int[]{R.id.BookCatName,R.id.BookCatDes,R.id.BookPrice,R.id.BookCatImage};
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,R.layout.singlebooklist,cursor,from,to,0);
            listBooks.setAdapter(listAdapter);
            TextView title = findViewById(R.id.textView20);
            title.setText(typeId);
            AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> listBooks, View view, int position, long id) {
                    Intent intent = new Intent(BookCatActivity.this,BookActivity.class);
                    int _id = cursor.getInt(0);
                    phoneNum = getIntent().getStringExtra(EXTRA_NUMBER);
                    intent.putExtra(BookActivity.EXTRA_BOOKID,(int)_id);
                    intent.putExtra(BookActivity.EXTRA_NUMBER,phoneNum);
                    startActivity(intent);
                }
            };
            listBooks.setOnItemClickListener(itemClickListener);

        }catch(SQLException e){
            Toast toast = Toast.makeText(this,"Database unavailable",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void setCarNum(NumImageView view){
        ((DReaderApp)getApplication()).setBookInCarNum();
        int num = ((DReaderApp)getApplication()).getBookInCarNum();
        if(num != 0)
            view.setNum(num);
    }

    public void onToCarClick(View view) {
        Intent intent = new Intent(BookCatActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("SWITCH","1");
        intent.putExtras(bundle);
        intent.putExtra(MainActivity.EXTRA_PHONENUM,phoneNum);
        startActivity(intent);
    }

}