package io.github.wonggwan.lab7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText NewPassword;
    private EditText ConfirmPassword;
    private Button bntok;
    private Button bntclear;
    private Button bntreset;
    private SharedPreferences.Editor edit;
    private boolean is_registered = false;
    private SharedPreferences sp;

    private void bindview() {
        NewPassword = ((EditText)findViewById(R.id.edittext1));
        ConfirmPassword = ((EditText)findViewById(R.id.edittext2));
        bntok = ((Button)findViewById(R.id.okbnt));
        bntclear = ((Button)findViewById((R.id.clearbnt)));
        bntreset = ((Button)findViewById(R.id.resetbnt));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindview();

        sp = getSharedPreferences("Password",MODE_PRIVATE);
        edit = sp.edit();

        if(!TextUtils.isEmpty(sp.getString("Password",null))) {
            is_registered = true;
            NewPassword.setVisibility(View.INVISIBLE);
            ConfirmPassword.setHint("请输入登陆密码");
        }


        //额外功能，希望可以在"再次输入界面这个栏目里有更换账号密码的选项"
        bntreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPassword.setVisibility(View.VISIBLE);
                is_registered = false;
                NewPassword.setHint("请设置登陆密码");
                ConfirmPassword.setHint("请再次输入该密码");
                ConfirmPassword.setText("");
                bntreset.setVisibility(View.INVISIBLE);
            }
        });


        bntok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp1 = MainActivity.this.NewPassword.getText().toString();
                String tmp2 = MainActivity.this.ConfirmPassword.getText().toString();

                System.out.println(tmp1);
                System.out.println(tmp2);

                if(MainActivity.this.is_registered) {
                    if(tmp2.equalsIgnoreCase(MainActivity.this.sp.getString("Password",null))) {
                        Toast.makeText(MainActivity.this.getApplicationContext(), "密码正确", Toast.LENGTH_SHORT).show();
                        Intent local = new Intent(MainActivity.this, FileEdit.class);;
                        MainActivity.this.startActivity(local);
                    }
                    else {
                        Toast.makeText(MainActivity.this.getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    NewPassword.setHint("请设置登陆密码");
                    ConfirmPassword.setHint("请再次输入该密码");

                    if (TextUtils.isEmpty(tmp2)) {
                        Toast.makeText(MainActivity.this.getApplicationContext(), "请再次输入密码", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (TextUtils.isEmpty(tmp1)) {
                            Toast.makeText(MainActivity.this.getApplicationContext(), "密码不可以为空", Toast.LENGTH_SHORT).show();
                        }
                        else if (!tmp1.equalsIgnoreCase(tmp2)) {
                            Toast.makeText(MainActivity.this.getApplicationContext(), "两次密码不相同", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this.getApplicationContext(), "密码设置成功", Toast.LENGTH_SHORT).show();
                            MainActivity.this.edit.putString("Password", tmp1);
                            MainActivity.this.edit.commit();
                            Intent local1 = new Intent(MainActivity.this, FileEdit.class);
                            MainActivity.this.startActivity(local1);
                        }
                    }
                }
                }
        });
        bntclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.NewPassword.setText("");
                MainActivity.this.ConfirmPassword.setText("");
            }
        });
    }
}
