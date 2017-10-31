package io.github.wonggwan.lab5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;




public class MainActivity extends AppCompatActivity {

    private static final String STATICACTION = "StaticFilter";
    private static final String DYNAMICACTION = "DynamicFilter";

    ListView cart;
    ListViewAdapter cartadapter = new ListViewAdapter(MainActivity.this);

    List<Products> pl = new ArrayList<>();

    RecyclerView mainscreen;
    RecyclerViewAdapter mainscreenadapter = new RecyclerViewAdapter(MainActivity.this,pl);

    FloatingActionButton floatab;

    NoticeInfo mynotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        initial();
        buttoninformation();
        setrecyclerview();
        setlistview();
    }

    public void add_data_to_product_list(){
        pl.add(new Products("Enchated Forest","E","¥ 5.00","作者 Johanna Basford",R.drawable.enchatedforest));
        pl.add(new Products("Arla Milk","A","¥ 59.00","产地 德国",R.drawable.arla));
        pl.add(new Products("Devondale Milk","D","¥ 79.00","产地 澳大利亚",R.drawable.devondale));
        pl.add(new Products("Kindle Oasis","K","¥ 2399.00","版本 8GB",R.drawable.kindle));
        pl.add(new Products("Waitrose 早餐麦片","W","¥ 179.00","重量 2Kg",R.drawable.waitrose));
        pl.add(new Products("Mcvitie's 饼干","M","¥ 14.90","产地 英国",R.drawable.mcvitie));
        pl.add(new Products("Ferrero Rocher","F","¥ 132.59","重量 300g",R.drawable.ferrero));
        pl.add(new Products("Maltesers","M","¥ 141.43","重量 118g",R.drawable.maltesers));
        pl.add(new Products("Lindt","L","¥ 139.43","重量 240g",R.drawable.lindt));
        pl.add(new Products("Borggreve","B","¥ 28.90","重量 640g",R.drawable.borggreve));
    }

    private void initial(){

        mynotice = new NoticeInfo();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DYNAMICACTION);
        registerReceiver(mynotice, filter);

        cart = (ListView) findViewById(R.id.id_listview);
        mainscreen = (RecyclerView) findViewById(R.id.id_recyclerview);
        floatab = (FloatingActionButton) findViewById(R.id.FloatingActionButton);

        cart.setVisibility(View.INVISIBLE);
        mainscreen.setVisibility(View.VISIBLE);
        // The cart is not shown in the first place


        floatab.setImageResource(R.drawable.emptycart);
        // let the icon of the cart shown in the first screen

        add_data_to_product_list();

        Integer randomID = new Random().nextInt(pl.size());
        Intent i =  new Intent(STATICACTION);
        i.putExtra("position", randomID);
        i.putExtra("detail",pl.get(randomID));
        sendBroadcast(i);

    }

    public void setrecyclerview(){
        mainscreen.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //the type that the data in the RecycleView would be shown, in this case, VERTICAL

        mainscreenadapter = new RecyclerViewAdapter(this,pl);//create the RecyclerView Adapter based on the data list
        mainscreenadapter.setOnItemClickLitener(
                new RecyclerViewAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent my = new Intent().setClass(MainActivity.this,ProductDetails.class);
                        my.putExtra("detail",mainscreenadapter.getItems(position));
                        my.putExtra("position", position);
                        startActivityForResult(my,1); //the number of intent can be set as you want
                    }
                    @Override
                    public void onItemLongClick(View view, final int position) {
                        AlertDialog.Builder mybuild = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("移除商品")
                                .setMessage("从清单中删除"+mainscreenadapter.getItems(position).getName()+"?")
                                .setNegativeButton("取消",null)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int w) {
                                        Toast.makeText(MainActivity.this,"已移除第"+(position+1)+"个商品："+mainscreenadapter.getItems(position).getName(),Toast.LENGTH_LONG).show();
                                        mainscreenadapter.removeData(position);
                                    }
                                });
                        mybuild.create().show();
                    }
                }
        );
        mainscreen.setAdapter(mainscreenadapter);
    }

    public void setlistview(){
        cartadapter = new ListViewAdapter(MainActivity.this);
        cartadapter.setOnItemClickLitener(
                new ListViewAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent newInten = new Intent().setClass(MainActivity.this,ProductDetails.class);
                        newInten.putExtra("detail",cartadapter.getItem(position));
                        newInten.putExtra("position",whereisproduct(position));
                        //startActivityForResult(newInten,1);
                        startActivity(newInten);
                    }
                    @Override
                    public void onItemLongClick(View view, final int position) {
                        AlertDialog.Builder Abuilder = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("移除商品")
                                .setMessage("从购物车中移除"+cartadapter.getItem(position).getName()+"?")
                                .setNegativeButton("取消",null)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mainscreenadapter.getItems(whereisproduct(position))
                                                .set_cart(0);
                                        cartadapter.removeData(position);
                                    }
                                });
                        Abuilder.create().show();
                    }
                });
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headView = inflater.inflate(R.layout.shoppinglisthead, null);
        //a headview is to set the content at the top of the cart seperately so that the data would not be cleaned each time you refresh the cart
        cart.addHeaderView(headView);
        cart.setAdapter(cartadapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NoticeMessage event) {
        mainscreenadapter.getItems(event.productpos).set_cart(event.number_in_cart);
        mainscreenadapter.getItems(event.productpos).set_star(event.isstar);
        update();
    }

    private void buttoninformation(){
        floatab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mainscreen.getVisibility() == View.INVISIBLE){
                    floatab.setImageResource(R.drawable.emptycart);
                    mainscreen.setVisibility(View.VISIBLE);
                    cart.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "当前位置：首页",Toast.LENGTH_SHORT).show();
                }
                else{
                    update();
                    floatab.setImageResource(R.drawable.mainpage);
                    mainscreen.setVisibility(View.INVISIBLE);
                    cart.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "当前位置：购物车",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void update(){
        cartadapter.clearData();
        for (int i = 0 ;i < mainscreenadapter.getItemCount(); i++) {
            if (mainscreenadapter.getItems(i).get_iscart()) {
                cartadapter.addData(mainscreenadapter.getItems(i));
            }
        }
    }

    private Integer whereisproduct(Integer pos_in_ShopList) {
        int i = 0;
        for ( ;i < mainscreenadapter.getItemCount();i++) {
            String s1 = mainscreenadapter.getItems(i).getName();
            String s2 = cartadapter.getItem(pos_in_ShopList).getName();
            if ( s1.equals(s2)) { break; }
        }
        return i;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        boolean check = intent.getBooleanExtra("added",false);
        if(check){
            mainscreen.setVisibility(View.INVISIBLE);
            cart.setVisibility(View.VISIBLE);
            floatab.setImageResource(R.drawable.mainpage);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mynotice != null){
            unregisterReceiver(mynotice);
            mynotice = null;
        }
        EventBus.getDefault().unregister(this);
    }
}