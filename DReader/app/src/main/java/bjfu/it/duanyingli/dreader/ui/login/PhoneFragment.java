package bjfu.it.duanyingli.dreader.ui.login;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import bjfu.it.duanyingli.dreader.R;
import bjfu.it.duanyingli.dreader.dbHelper.DUserDatabaseHelper;

public class PhoneFragment extends Fragment {
    public static final String ARGS_PHONE = "phone";
    private EditText phoneEdit;
    private NavController controller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_phone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        phoneEdit = view.findViewById(R.id.phoneNumInput);
        view.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNum = phoneEdit.getText().toString();
                if (TextUtils.isEmpty(phoneNum)) {
                    Toast.makeText(getContext(), "请输入电话号码", Toast.LENGTH_SHORT).show();
                } else {
                    Bundle args = new Bundle();
                    args.putString(ARGS_PHONE, phoneNum);
                    if (hasPhone(phoneNum)) {
                        //如果存在，进入密码登陆界面
                        controller.navigate(R.id.action_phoneFragment_to_loginPwdFragment, args);
                    } else {
                        //不存在，进入注册界面
                        controller.navigate(R.id.action_phoneFragment_to_registerFragment, args);
                    }
                }
            }
        });
    }

    private boolean hasPhone(String phoneNum) {
        Cursor cursor = null;
        SQLiteOpenHelper dreaderDatabaseHelper = new DUserDatabaseHelper(getContext());
        try (SQLiteDatabase db = dreaderDatabaseHelper.getReadableDatabase()) {
            cursor = db.query("USER",
                    new String[]{"PhoneNum"},
                    "PhoneNum = ?",
                    new String[]{phoneNum},
                    null, null, null);
            return cursor.getCount() > 0;
        } catch (SQLException e) {
            Toast toast = Toast.makeText(getContext(), "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e) {
                }
            }
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
