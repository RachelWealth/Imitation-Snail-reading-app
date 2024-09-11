package bjfu.it.duanyingli.dreader.ui.login;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
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

import bjfu.it.duanyingli.dreader.DReaderApp;
import bjfu.it.duanyingli.dreader.MainActivity;
import bjfu.it.duanyingli.dreader.R;
import bjfu.it.duanyingli.dreader.dbHelper.DUserDatabaseHelper;

/*
    注册界面操作
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    public String phoneNumber;
    public String verNum;
    private EditText inputCodeEdit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phoneNumber = getArguments().getString(PhoneFragment.ARGS_PHONE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.getCodeBtn).setOnClickListener(this);
        view.findViewById(R.id.registerButton).setOnClickListener(this);
        inputCodeEdit = view.findViewById(R.id.inputCodeEdit);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.getCodeBtn) {
            verNum = String.format("%s", (int) (Math.random() * 9000) + 1000);
            Toast.makeText(getContext(), "验证码是" + verNum, Toast.LENGTH_LONG).show();
        } else if (view.getId() == R.id.registerButton) {
            String code = inputCodeEdit.getText().toString();
            if (TextUtils.isEmpty(code)) {
                Toast.makeText(getContext(), "验证码为空", Toast.LENGTH_LONG).show();
            } else if (verNum.equals(code)) {
                //加入数据库
                SQLiteOpenHelper dreaderDatabaseHelper = new DUserDatabaseHelper(getContext());
                try (SQLiteDatabase db = dreaderDatabaseHelper.getWritableDatabase()) {
                    ContentValues userValues = new ContentValues();
                    userValues.put("PhoneNum", phoneNumber);
                    userValues.put("UName", phoneNumber);
                    userValues.put("Password", phoneNumber);
                    db.insert("USER", null, userValues);
                    //进入个人中心
                    ((DReaderApp) requireActivity().getApplication()).setPhone(phoneNumber);
                    requireActivity().finish();
                } catch (SQLException e) {
                    Toast toast = Toast.makeText(getContext(), "Database unavailable", Toast.LENGTH_LONG);
                    toast.show();
                }
            } else {
                //提示错误
                Toast toast = Toast.makeText(getContext(), "验证码输入错误", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}