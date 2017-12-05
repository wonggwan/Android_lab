package io.github.wonggwan.lab8;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    public Button addbnt;
    public ListView myview;
    public SimpleAdapter adapter;
    public TextView setname;
    public EditText setbirth;
    public EditText setgift;
    public TextView phonenum;

    private static final String TABLE_NAME = "Info";
    public List<Map<String, String>> datas = new ArrayList<Map<String, String>>();

    private void databaseUpdate() {
        MyDB db = new MyDB(getBaseContext());
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+TABLE_NAME, null);
        datas = new ArrayList<Map<String,String>>();
        if(cursor != null) {
            while(cursor.moveToNext()) {
                String curname = cursor.getString(0);
                String curbirth = cursor.getString(1);
                String curgift = cursor.getString(2);
                Map<String, String> map = new HashMap<String, String>();
                map.put("name" ,curname);
                map.put("birth",curbirth);
                map.put("gift",curgift);
                datas.add(map);
            }
            adapter = new SimpleAdapter(MainActivity.this, datas, R.layout.item,new String[]{"name", "birth", "gift"},new int[]{R.id.nameLV, R.id.birthLV, R.id.giftLV});
            myview.setAdapter(adapter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myview = ((ListView)findViewById(R.id.Start));
        addbnt = (Button) findViewById(R.id.add);

        addbnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddInfo.class);
                startActivityForResult(i,666);
            }
        });

        databaseUpdate();

        editcontacts();

        deletecontacts();
    }

    public void editcontacts() {
        myview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                LayoutInflater diy = LayoutInflater.from(MainActivity.this);
                View newView = diy.inflate(R.layout.toastmessage,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS},0);
                }

                setname = (TextView) newView.findViewById(R.id.nameXG1);
                setbirth = (EditText) newView.findViewById(R.id.birthXG1);
                setgift = (EditText) newView.findViewById(R.id.giftXG1);
                phonenum = (TextView) newView.findViewById(R.id.phone);

                setname.setText(datas.get(position).get("name"));
                setbirth.setText(datas.get(position).get("birth"));
                setgift.setText(datas.get(position).get("gift"));

                String tmp = "";
                Cursor c1 = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
                while(c1.moveToNext()) {
                    String tmp2 = c1.getString(c1.getColumnIndex("_id"));
                    if(c1.getString(c1.getColumnIndex("display_name")).equals(setname.getText().toString())){
                        if(Integer.parseInt(c1.getString(c1.getColumnIndex("has_phone_number")))>0) {
                            Cursor c2 = getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "contact_id = " + tmp2, null, null);
                            while(c2.moveToNext()) {
                                tmp = tmp + c2.getString(c2.getColumnIndex("data1")) + "\n";
                            }
                            c2.close();
                        }
                    }
                }
                c1.close();
                if(tmp.isEmpty()) tmp = "none";
                phonenum.setText(tmp);

                builder.setView(newView);
                builder.setTitle("修改联系人信息");
                builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(setbirth.length() != 0) {
                            MyDB db = new MyDB(getBaseContext());
                            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                            sqLiteDatabase.execSQL("update " + TABLE_NAME + " set birth = ? where name = ?",new Object[]{ setbirth.getText().toString(), setname.getText().toString()});
                            sqLiteDatabase.close();
                        }
                        if(setgift.length() != 0) {
                            MyDB db = new MyDB(getBaseContext());
                            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                            sqLiteDatabase.execSQL("update " + TABLE_NAME + " set gift = ? where name = ?", new Object[]{ setgift.getText().toString(), setname.getText().toString()});
                            sqLiteDatabase.close();
                        }
                        Toast.makeText(MainActivity.this,"信息修改完成",Toast.LENGTH_SHORT).show();
                        databaseUpdate();
                    }
                });
                builder.setNegativeButton("back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
                builder.show();
            }
        });
    }

    public void deletecontacts() {
        myview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder message = new AlertDialog.Builder(MainActivity.this);
                message.setTitle("删除联系人");
                message.setMessage("请确认是否删除该联系人的信息");
                message.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyDB db = new MyDB(getBaseContext());
                        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                        sqLiteDatabase.execSQL("delete from " + TABLE_NAME  + " where name = ?", new String[]{datas.get(position).get("name")});
                        sqLiteDatabase.close();
                        datas.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                    }
                });
                message.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
                message.create().show();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode, data);
        if(requestCode == 666 && resultCode == 998) {
            databaseUpdate();
        }
    }
}
