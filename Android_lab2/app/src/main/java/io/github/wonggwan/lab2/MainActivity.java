package io.github.wonggwan.lab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.ImageView;
import android.view.View;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.Snackbar;
import android.widget.RadioGroup;
import android.widget.Button;
import android.widget.EditText;
import android.support.design.widget.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    private ImageView mypic;
    private RadioGroup identity;
    private Boolean isastudent;
    private Button login;
    private Button register;
    private TextInputLayout myid;
    private TextInputLayout mypw;
    private EditText sid;
    private EditText spw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initial();
        function();
    }

    private void initial() {
        mypic = (ImageView) findViewById(R.id.myimage);
        identity = (RadioGroup) findViewById(R.id.radioGroup);
        isastudent = true;
        login = (Button) findViewById(R.id.button);
        myid = (TextInputLayout) findViewById(R.id.mystudentnumber);
        mypw = (TextInputLayout) findViewById(R.id.mystudentpassword);
        sid = myid.getEditText();
        spw = mypw.getEditText();
        register = (Button) findViewById(R.id.button2);
    }

    private void function() {
        final String content[] = new String[]{"拍摄", "从相册选择"};

        mypic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("上传头像");
                        builder.setItems(content, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int choice) {
                                String showStr = "您选择了[" + content[choice] + "]";
                                Toast.makeText(getApplicationContext(), showStr, Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "您选择了[取消]", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.create().show();
            }
        });

        identity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedbox) {
                if(checkedbox == R.id.radioButton1){ isastudent = true; }
                if(checkedbox == R.id.radioButton2){ isastudent = false; }
                String showmessage;
                if(isastudent == true) { showmessage = "您选择了学生"; }
                else{ showmessage = "您选择了教职工"; }

                Snackbar my = Snackbar.make(group.getRootView(), showmessage, Snackbar.LENGTH_SHORT);
                my.setAction("确定", new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Toast.makeText(getApplicationContext(), "Snackbar 的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                    }
                });
                my.show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String studentnumber = sid.getText().toString();
                String studentpassword = spw.getText().toString();
                if(studentnumber.length() == 0){
                    myid.setError("学号不能为空");
                    myid.setErrorEnabled(true);
                }
                else if(studentpassword.length() == 0) {
                    mypw.setError("密码不能为空");
                    mypw.setErrorEnabled(true);
                }
                else if(studentnumber.compareTo("123456") == 0){
                    if(studentpassword.compareTo("6666") == 0){
                        myid.setErrorEnabled(false);
                        mypw.setErrorEnabled(false);
                        Snackbar my1 = Snackbar.make(v.getRootView(), "登陆成功", Snackbar.LENGTH_SHORT);
                        my1.setAction("确定", new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                Toast.makeText(getApplicationContext(), "Snackbar 的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                            }
                        });
                        my1.show();
                    }
                }
                else{
                    myid.setErrorEnabled(false);
                    mypw.setErrorEnabled(false);
                    Snackbar my2 = Snackbar.make(v.getRootView(), "登陆失败", Snackbar.LENGTH_SHORT);
                    my2.setAction("确定", new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            Toast.makeText(getApplicationContext(), "Snackbar 的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                        }
                    });
                    my2.show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isastudent == true) {
                    Snackbar my3 = Snackbar.make(v.getRootView(), "学生注册功能尚未启用", Snackbar.LENGTH_SHORT);
                    my3.setAction("确定", new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            Toast.makeText(getApplicationContext(), "Snackbar 的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                        }
                    });
                    my3.show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "教职工注册功能尚未启用", Toast.LENGTH_SHORT).show();}
                }
            });
        }
    }