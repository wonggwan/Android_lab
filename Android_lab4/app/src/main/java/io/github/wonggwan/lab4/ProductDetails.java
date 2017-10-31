package io.github.wonggwan.lab4;

/**
 * Created by wonggwan on 2017/10/28.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

public class ProductDetails extends AppCompatActivity {

    private static final String DYNAMICACTION = "DynamicFilter";
    ImageButton clickstar;
    ImageButton addtocart;
    ImageButton click_gobacktomain;
    TextView Name;
    TextView Price;
    TextView Info;
    ImageView Img;
    ListView moreopt;
    private Products current;
    private Integer currentpos;
    private Boolean isfavor;
    private Integer total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitydetail);
        init();
        getdata();
        if(isfavor == true){ clickstar.setImageResource(R.drawable.pinkstar); }
        else{ clickstar.setImageResource(R.drawable.iconfont_xingxing);}
        setreturnbuton();
    }

    private void init(){
        clickstar = (ImageButton) findViewById(R.id.star);
        addtocart= (ImageButton) findViewById(R.id.addShoppingListBnt);
        click_gobacktomain = (ImageButton) findViewById(R.id.returnBnt);
        Name = (TextView) findViewById(R.id.nameofproduct);
        Info = (TextView) findViewById(R.id.productWeight);
        Price = (TextView) findViewById(R.id.productPrice);
        Img = (ImageView) findViewById(R.id.productView);
        moreopt = (ListView) findViewById(R.id.moreOption);
        moreopt.setAdapter(
                new ArrayAdapter<String>(
                        ProductDetails.this,
                        R.layout.optitem,
                        new String[]{"一键下单","分享商品","不感兴趣","查看更多商品促销信息"}
                ));
        addtocart.setImageResource(R.drawable.addincar);
        click_gobacktomain.setImageResource(R.drawable.goback);
    }

    private void getdata(){
        current = (Products) this.getIntent().getParcelableExtra("detail"); //get the signal sent from the Mainactivity
        currentpos = this.getIntent().getIntExtra("position",-1);
        Name.setText(current.getName());
        Price.setText(current.getPrice());
        Info.setText(current.getSpecialInfo());
        Img.setImageResource(current.getImgObject());
        isfavor = current.get_isstar();
        total = current.how_many_in_the_cart();
    }

    private void setreturnbuton(){
        click_gobacktomain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        addtocart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addtocart.setImageResource(R.drawable.addincar);
                Toast.makeText(ProductDetails.this, "商品已添加到购物车",Toast.LENGTH_SHORT).show();
                Intent i  = new Intent(DYNAMICACTION);
                i.putExtra("detail", current);
                i.putExtra("position",currentpos);
                sendBroadcast(i);
                current.set_cart(total+1);
                EventBus.getDefault().post(new NoticeMessage(current,currentpos));
            }
        });
        clickstar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                isfavor = !isfavor;
                if(isfavor){
                    clickstar.setImageResource(R.drawable.pinkstar);
                    Toast.makeText(ProductDetails.this, "商品已收藏", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ProductDetails.this, "已取消收藏该商品", Toast.LENGTH_SHORT).show();
                    clickstar.setImageResource(R.drawable.iconfont_xingxing);
                }
                EventBus.getDefault().post(new NoticeMessage(current, currentpos));
            }
        });
    }
}
