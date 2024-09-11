package bjfu.it.duanyingli.dreader.ui.login;

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

/*
    验证码登陆界面操作
 */
public class LoginCodeFragment extends Fragment {
    private EditText inputCodeEdit;
    public String phoneNumber;
    public String verNum;
    private NavController controller;
    public static final String ARGS_PHONE = "phone";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phoneNumber = getArguments().getString(PhoneFragment.ARGS_PHONE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        view.findViewById(R.id.forgetpass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString(PhoneFragment.ARGS_PHONE, phoneNumber);
                controller.navigate(R.id.action_loginCodeFragment_to_forgetFragment, args);
            }
        });
        inputCodeEdit = view.findViewById(R.id.inputCodeEdit);
        view.findViewById(R.id.getCodeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verNum = String.format("%s", (int) (Math.random() * 9000) + 1000);
                Toast.makeText(getContext(), "验证码是" + verNum, Toast.LENGTH_LONG).show();
            }
        });
        view.findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = inputCodeEdit.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(getContext(), "请输入验证码", Toast.LENGTH_LONG).show();
                } else {
                    login(code);
                }
            }
        });
        view.findViewById(R.id.passlog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString(ARGS_PHONE, phoneNumber);
                controller.navigate(R.id.action_loginCodeFragment_to_loginPwdFragment, args);
            }
        });
    }

    private void login(String code) {
        if (verNum.equals(code)) {
            //验证码正确
            // 写入数据库
            ((DReaderApp) requireActivity().getApplication()).setPhone(phoneNumber);
            requireActivity().finish();
        } else {
            Toast toast = Toast.makeText(getContext(), "验证码不正确", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}