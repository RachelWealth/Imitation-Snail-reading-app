package bjfu.it.duanyingli.dreader.ui.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import bjfu.it.duanyingli.dreader.R;
import bjfu.it.duanyingli.dreader.dbHelper.DUserDatabaseHelper;

public class ForgetFragment extends Fragment {
    public static String EXTRA_PHONENUM;
    private String newPassText;
    private String phoneNum;
    public static final String ARGS_PHONE = "phone";
    private NavController controller;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_forget, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.forgetButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText newPass = getActivity().findViewById(R.id.newPass);
                EditText enNewPass = getActivity().findViewById(R.id.enNewPass);
                newPassText = newPass.getText().toString();
                String enNewPassText = enNewPass.getText().toString();
                if(newPassText.equals(enNewPassText)){
                    //两次输入一样
                    Bundle args = new Bundle();
                    args.putString(ARGS_PHONE, phoneNum);
                    new UpdateForTask().execute(phoneNum);
                    controller = Navigation.findNavController(view);
                    controller.navigate(R.id.action_forgetFragment_to_phoneFragment, args);
                }else{
                    //两次输入不一样
                    Toast toast = Toast.makeText(getContext(), "两次输入密码不匹配", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }

    private class UpdateForTask extends AsyncTask<String,Void,Boolean> {
        private ContentValues passValues; //定义为内部类，便于操作Activity实例变量
        protected void onPreExecute() {
            ContentValues passValue = new ContentValues();
            passValue.put("Password",newPassText);
            //此处无论是否加入购物车选项被选中，都要先加入容器中
            passValues = new ContentValues();
            passValues.put("PhoneNum",phoneNum);
            passValues.put("Password",newPassText);
        }//初始化任务
        protected Boolean doInBackground(String... phone) throws SQLiteException {
            String phoneN = phone[0];
            SQLiteOpenHelper dreaderDatabaseHelper = new DUserDatabaseHelper(getActivity());
            try(SQLiteDatabase db = dreaderDatabaseHelper.getWritableDatabase()){
                db.update("USER",passValues,
                        "PhoneNum = ?",new String[]{phoneN});
                return true;
            }
        }//执行费时的任务并publishProgress().
        protected void onPostExecute(Boolean success){
            if(!success){
                Toast toast = Toast.makeText(getActivity(),"请登录账户",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}