package bjfu.it.duanyingli.dreader.ui.cart;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import bjfu.it.duanyingli.dreader.DReaderApp;
import bjfu.it.duanyingli.dreader.R;
import bjfu.it.duanyingli.dreader.dbHelper.DUserDatabaseHelper;
import bjfu.it.duanyingli.dreader.ui.home.BookActivity;

public class CartFragment extends Fragment {

    private Cursor cursor;
    private Cursor cursor1;
    private Cursor cursor2;
    private String phoneNum;
    private NavController navController;
    private int count;
    private int sum;

    @Override
    public void onResume() {
        super.onResume();
        phoneNum = ((DReaderApp) requireActivity().getApplication()).getPhone();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            cursor.close();
            cursor1.close();
            cursor2.close();
        } catch (Exception e) {
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        phoneNum = ((DReaderApp) requireActivity().getApplication()).getPhone();
        ((DReaderApp) requireActivity().getApplication()).setCursor();
        cursor = ((DReaderApp) requireActivity().getApplication()).getCursor();
        if ((phoneNum == null) || (cursor.getCount() == 0)) {
            return inflater.inflate(R.layout.fragment_cart, container, false);
        } else {
            return inflater.inflate(R.layout.fragment_dashboard, container, false);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {//响应动作，映入每本书的详情页
        super.onActivityCreated(savedInstanceState);
        if ((phoneNum != null) && (cursor.getCount() != 0)) {
            if (cursor.moveToFirst()) {
                String[] from = new String[]{"BName", "BPrice", "B_Picture_ResourceId", "BookNum"};
                int[] to = new int[]{R.id.carBName, R.id.carBPrice, R.id.carBPic, R.id.carBNum};
                SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(getActivity(),
                        R.layout.singlecarlist,
                        cursor,
                        from,
                        to,
                        0);
                ListView listCars = getActivity().findViewById(R.id.carList);
                listCars.setAdapter(listAdapter);
                getAll();
                TextView bookAllNum = getActivity().findViewById(R.id.bookAllNum);
                bookAllNum.setText(String.format("%s",count));
                TextView bookAllPrice = getActivity().findViewById(R.id.bookAllPrice);
                bookAllPrice.setText(String.format("%s",sum));
                AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> listCars, View view, int i, long id) {
                        Intent intent = new Intent(getActivity(), BookActivity.class);
                        //传递给下以及目录点击的类型名字
                        SQLiteOpenHelper dreaderDatabaseHelper = new DUserDatabaseHelper(getActivity());
                        try (SQLiteDatabase db = dreaderDatabaseHelper.getReadableDatabase()) {
                            cursor1 = db.rawQuery("select _id, BName, B_Picture_ResourceId " +
                                            "from BOOK where _id = ?",
                                    new String[]{String.format("%s", id)});
                            if(cursor1.moveToFirst()) {
                                int bookId = cursor1.getInt(0);
                                ((DReaderApp) requireActivity().getApplication()).setBookId(bookId);
                                startActivity(intent);
                            }
                        } catch (SQLiteException e) {
                            Toast toast = Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                };
                listCars.setOnItemClickListener(itemClickListener);
            }
        }
    }

    public void getAll(){
        SQLiteOpenHelper dreaderDatabaseHelper = new DUserDatabaseHelper(getActivity());
        try (SQLiteDatabase db = dreaderDatabaseHelper.getReadableDatabase()) {
            cursor2 = db.rawQuery("select sum(BookNum),sum(BookNum*BPrice) from BOOK, CAR where BOOK._id = CAR._bid group by PhoneNum having PhoneNum = ?",new String[]{phoneNum});
            if(cursor2.moveToFirst()){
                count = cursor2.getInt(0);
                sum = cursor2.getInt(1);
            }
        }catch (SQLException e) {
            Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT).show();
        }

    }
}