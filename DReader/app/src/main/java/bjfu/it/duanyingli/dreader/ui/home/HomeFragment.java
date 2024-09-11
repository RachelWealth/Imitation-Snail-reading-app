package bjfu.it.duanyingli.dreader.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import bjfu.it.duanyingli.dreader.DReaderApp;
import bjfu.it.duanyingli.dreader.MainActivity;
import bjfu.it.duanyingli.dreader.R;
import bjfu.it.duanyingli.dreader.dbHelper.DUserDatabaseHelper;

/*书的分类列表
 */
public class HomeFragment extends Fragment {
    private Cursor cursor;
    private Cursor cursor1;
    private ListView listTypes;
    public String phoneNum;

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            cursor.close();
            cursor1.close();
        }catch(Exception e){
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        phoneNum = ((DReaderApp) requireActivity().getApplication()).getPhone();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);//引入布局文件
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listTypes = getActivity().findViewById(R.id.BookType);
        SQLiteOpenHelper dreaderDatabaseHelper = new DUserDatabaseHelper(getActivity());
        try(SQLiteDatabase db = dreaderDatabaseHelper.getReadableDatabase()){
            cursor = db.query("TYPE",
                    new String[]{"_id","TName","T_Picture_ResourceId"},
                    null,null,null,null,null);//得到类别表的信息
            String[] from = new String[]{"T_Picture_ResourceId","TName"};
            int[] to = new int[]{R.id.TypeImage,R.id.TypeName};
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(getActivity(),
                    R.layout.singlelist,
                    cursor,
                    from,
                    to,
                    0);
            listTypes.setAdapter(listAdapter);
        }catch (SQLiteException e){
            Toast toast = Toast.makeText(getActivity(),"Database unavailable",Toast.LENGTH_SHORT);
            toast.show();
        }
        OnItemClickListener itemClickListener =
                new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listTypes, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),BookCatActivity.class);
                //传递给下以及目录点击的类型名字
                SQLiteOpenHelper dreaderDatabaseHelper = new DUserDatabaseHelper(getActivity());
                String[] str = new String[]{String.format("%s",position + 1)};
                try(SQLiteDatabase db = dreaderDatabaseHelper.getReadableDatabase()){
                    //得到类别表的信息
                    cursor1 = db.rawQuery("select * from TYPE where _id = ?",str);
                    if(cursor1.moveToFirst()){
                        String typeName = cursor1.getString(1);
                        intent.putExtra(BookCatActivity.EXTRA_BOOKTYPENAME,typeName);
                        intent.putExtra(BookCatActivity.EXTRA_NUMBER,phoneNum);
                        startActivity(intent);
                    }
                }catch (Exception e){
                    Toast.makeText(getActivity(),"没有此类图书",Toast.LENGTH_SHORT).show();
                }
            }
        };
        listTypes.setOnItemClickListener(itemClickListener);
    }
}