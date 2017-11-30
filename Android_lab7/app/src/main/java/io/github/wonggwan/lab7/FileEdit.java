package io.github.wonggwan.lab7;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by wonggwan on 2017/11/29.
 */

public class FileEdit extends AppCompatActivity{
    private Button savebutton;
    private Button loadbutton;
    private Button clearbutton;
    private Button deletebutton;
    private EditText fname;
    private EditText fcontent;
    String str;

    public void getContent(String para) {
        try {
            FileInputStream localFile = getApplicationContext().openFileInput(para);
            System.out.println(localFile.available());
            byte[] arrayofbyte = new byte[localFile.available()];

            localFile.read(arrayofbyte);
            localFile.close();
            Toast.makeText(getApplicationContext(), "加载成功", Toast.LENGTH_SHORT).show();
            str = new String(arrayofbyte);
            return;
        }
        catch (IOException e) {
            Toast.makeText(FileEdit.this, "加载失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void findview() {
        fname = ((EditText)findViewById(R.id.filename));
        fcontent = ((EditText)findViewById(R.id.filecontent));
        savebutton = ((Button)findViewById(R.id.savebnt));
        loadbutton = ((Button)findViewById(R.id.loadbnt));
        clearbutton = ((Button)findViewById(R.id.clearbnt));
        deletebutton = ((Button)findViewById(R.id.deletebnt));
    }

    public void saveContent(String paramString1, String paramString2)
    {
        try {
            FileOutputStream localFileOutputStream = getApplicationContext().openFileOutput(paramString1, 0);
            localFileOutputStream.write(paramString2.getBytes());
            localFileOutputStream.close();
            Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
            return;
        }
        catch (IOException localIOException) {
            while (true) {
                Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
                localIOException.printStackTrace();
            }
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.file_edit);
        findview();
        savebutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View para) {
                if(!fname.getText().toString().isEmpty())
                    FileEdit.this.saveContent(FileEdit.this.fname.getText().toString(), FileEdit.this.fcontent.getText().toString());
                else {
                    Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loadbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View para) {
                getContent(FileEdit.this.fname.getText().toString());
                FileEdit.this.fcontent.setText(str);
            }
        });
        clearbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View para) {
                FileEdit.this.fcontent.setText("");
            }
        });
        deletebutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View para) {
                deleteFile(fname.getText().toString());
                //FileEdit.this.DeleteFile(FileEdit.this.fname.getText().toString());
                Toast.makeText(getApplicationContext(),"删除成功", Toast.LENGTH_SHORT).show();
                str = "";

            }
        });
    }
}
