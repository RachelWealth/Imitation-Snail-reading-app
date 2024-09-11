package bjfu.it.duanyingli.dreader.ui.mine;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.Objects;

import bjfu.it.duanyingli.dreader.DReaderApp;
import bjfu.it.duanyingli.dreader.R;
import bjfu.it.duanyingli.dreader.dbHelper.DUserDatabaseHelper;
import bjfu.it.duanyingli.dreader.ui.home.BookActivity;
import bjfu.it.duanyingli.dreader.ui.home.BookCatActivity;


public class MineFragment extends Fragment {
    private ListView listCars;
    public String phoneNum;
    private Cursor cursor;
    private Cursor cursor1;
    private Cursor cursor2;
    private ImageView toLogB;
    private boolean flag = false;//false则进入输入手机号码

    private NavController controller;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mine, container, false);
        toLogB = (ImageView) root.findViewById(R.id.imageView2);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);
        toLogB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((DReaderApp) getActivity().getApplication()).getPhone() == null) {
                    controller.navigate(R.id.action_navigation_mine_to_loginActivity);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        String phone = ((DReaderApp) requireActivity().getApplication()).getPhone();
        if (!Objects.equals(phone, phoneNum)) {
            phoneNum = phone;
            updateUserInfo();
        }
    }

    private void updateUserInfo() {
        SQLiteOpenHelper dreaderDatabaseHelper = new DUserDatabaseHelper(getActivity());
        try (SQLiteDatabase db = dreaderDatabaseHelper.getReadableDatabase()) {
            //判断字段是否为空
            if (phoneNum != null) {
                cursor = db.query("USER", new String[]{"UName"},
                        "PhoneNum = ?", new String[]{phoneNum},
                        null, null, null);
                if (cursor.moveToFirst()) {

                    TextView name = getActivity().findViewById(R.id.userName);
                    TextView num = getActivity().findViewById(R.id.userNum);
                    if (cursor.moveToFirst()) {
                        name.setText(cursor.getString(0));
                        String str = "账号：" + phoneNum;
                        num.setText(str);
                    } else {
                        Toast toast = Toast.makeText(getActivity(), "游标不正确", Toast.LENGTH_LONG);
                        toast.show();
                    }
                } else {
                    TextView name = getActivity().findViewById(R.id.userName);
                    TextView num = getActivity().findViewById(R.id.userNum);
                    name.setText("小段");
                    num.setText("点击头像登录账号");
                }
                addFav();

                toLogB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (((DReaderApp) getActivity().getApplication()).getPhone() == null) {
                            controller.navigate(R.id.action_navigation_mine_to_loginActivity);
                        }
                    }
                });
            }
        } catch (SQLException e) {
            Toast toast = Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void addFav() {
        listCars = getActivity().findViewById(R.id.FavList);
        SQLiteOpenHelper dreaderDatabaseHelper = new DUserDatabaseHelper(getActivity());
        try (SQLiteDatabase db = dreaderDatabaseHelper.getReadableDatabase()) {
            if (phoneNum != null) {
                cursor1 = db.rawQuery("SELECT _id,B_Picture_ResourceId,BName,BPrice " +
                        "FROM FAVOURATE,BOOK " +
                        "WHERE FAVOURATE.phoneNum = ?" +
                        "and FAVOURATE._bid = BOOK._id", new String[]{phoneNum});
                if (cursor1.moveToFirst()) {
                    //有收藏，显示收藏界面
                    //放置
                    String[] from = new String[]{"BName", "BPrice", "B_Picture_ResourceId"};

                    int[] to = new int[]{R.id.CBName, R.id.CBPrice, R.id.CBPic};
                    SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(getActivity(),
                            R.layout.single_fav_list,
                            cursor1,
                            from,
                            to,
                            0);
                    listCars.setAdapter(listAdapter);

                    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> listCars, View view, int i, long id) {
                            Intent intent = new Intent(getActivity(), BookActivity.class);
                            //传递给下以及目录点击的类型名字
                            SQLiteOpenHelper dreaderDatabaseHelper = new DUserDatabaseHelper(getActivity());
                            try (SQLiteDatabase db = dreaderDatabaseHelper.getReadableDatabase()) {
                                cursor2 = db.rawQuery("select _id, BName, B_Picture_ResourceId " +
                                                "from BOOK where _id = ?",
                                        new String[]{String.format("%s", id)});
                                if (cursor2.moveToFirst()) {
                                    int bookId = cursor2.getInt(0);
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
        } catch (SQLException e) {
            Toast toast = Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}