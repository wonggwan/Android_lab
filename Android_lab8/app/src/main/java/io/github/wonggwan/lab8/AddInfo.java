package io.github.wonggwan.lab8;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by davidwong on 2017/12/5.
 */

public class AddInfo  extends AppCompatActivity{
    private Button addnew;
    private EditText textname;
    private EditText textbirth;
    private EditText textgift;

    private static final String TABLE_NAME = "Info";

    public void init() {
        addnew = (Button)findViewById(R.id.BtnAdd);
        textname = (EditText) findViewById(R.id.nameET);
        textbirth = (EditText) findViewById(R.id.birthET);
        textgift = (EditText) findViewById(R.id.giftET);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createitem);

        init();

        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(textname.getText())) {
                    Toast.makeText(AddInfo.this,"名字为空，请完善",Toast.LENGTH_SHORT).show();
                }
                else {
                    String Nname = textname.getText().toString();
                    String Nbirth = textbirth.getText().toString();
                    String Ngift = textgift.getText().toString();

                    MyDB db = new MyDB(getBaseContext());
                    SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                    Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME + " where name like ?", new String[]{Nname});
                    if(cursor.moveToFirst()) {
                        Toast.makeText(AddInfo.this,"有重复的名字",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("name", Nname);
                        contentValues.put("birth", Nbirth);
                        contentValues.put("gift", Ngift);
                        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
                        sqLiteDatabase.close();
                        setResult(998,new Intent());
                        finish();
                    }
                }
            }
        });
    }
}
