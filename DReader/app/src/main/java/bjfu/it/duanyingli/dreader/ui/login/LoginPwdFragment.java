package bjfu.it.duanyingli.dreader.ui.login;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
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

import bjfu.it.duanyingli.dreader.DReaderApp;
import bjfu.it.duanyingli.dreader.R;
import bjfu.it.duanyingli.dreader.dbHelper.DUserDatabaseHelper;

/*
    密码登陆界面操作
 */
public class LoginPwdFragment extends Fragment {
    private String phoneNum;
    private NavController controller;
    private EditText inputPwdEdit;
    public static final String ARGS_PHONE = "phone";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phoneNum = getArguments().getString(PhoneFragment.ARGS_PHONE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_pwd, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        view.findViewById(R.id.forgetpass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString(PhoneFragment.ARGS_PHONE, phoneNum);
                controller.navigate(R.id.action_loginPwdFragment_to_forgetFragment, args);
            }
        });
        inputPwdEdit = view.findViewById(R.id.inputPwdEdit);
        view.findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pwd = inputPwdEdit.getText().toString();
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(getContext(), "请输入密码", Toast.LENGTH_LONG).show();
                } else {
                    login(pwd);
                }
            }
        });
        view.findViewById(R.id.verlog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString(ARGS_PHONE, phoneNum);
                controller.navigate(R.id.action_loginPwdFragment_to_loginCodeFragment, args);
            }
        });
    }

    private void login(String passWord) {
        //提取出数据库中号码，匹配密码
        SQLiteOpenHelper dreaderDatabaseHelper = new DUserDatabaseHelper(getContext());
        Cursor cursor = null;
        try (SQLiteDatabase db = dreaderDatabaseHelper.getReadableDatabase()) {
            cursor = db.query("USER", new String[]{"Password"},
                    "PhoneNum = ?", new String[]{phoneNum}, null, null, null);
            if (cursor.moveToFirst()) {
                String rightPassword = cursor.getString(0);
                if (rightPassword.equals(passWord)) {
                    //密码正确，登陆成功，进入个人中心
                    //传给个人中心界面电话号码
                    ((DReaderApp) requireActivity().getApplication()).setPhone(phoneNum);
                    requireActivity().finish();
                } else {
                    Toast.makeText(getContext(), "密码不正确", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "无此电话号码", Toast.LENGTH_SHORT).show();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}