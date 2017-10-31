package io.github.wonggwan.lab4;

/**
 * Created by wonggwan on 2017/10/28.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class Products implements Parcelable{
    private Boolean isstar;
    private Integer numberincart;
    private String  name;
    private String  price;
    private Integer image;
    private String  productinfo;
    private String  First_letter;

    Products(String itemname, String fletter, String Price, String info, Integer img) {
        name = itemname;
        First_letter = fletter;
        price = Price;
        productinfo = info;
        image = img;
        numberincart = 0;
        isstar = Boolean.FALSE;
    }

    //send basic information to the adapters
    public String getName() { return name; }
    public String getFirst_letter(){ return First_letter;}
    public String getPrice() { return price; }
    public Integer getImgObject() { return image; }
    public String getSpecialInfo() { return productinfo; }


    public Boolean get_isstar() { return isstar; }
    public Boolean get_iscart() { return numberincart>0; }
    public Integer how_many_in_the_cart() { return numberincart;}

    //these two functions are for the mainactivity to update the condition in the detail screen
    public void set_cart(Integer addtocart) { this.numberincart = addtocart; }
    public void set_star(Boolean star) { this.isstar = star; }

    protected Products(Parcel in){
        name = in.readString();
        price = in.readString();
        productinfo = in.readString();
        image = in.readInt();
        isstar = in.readByte() != 0;
        numberincart = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(productinfo);
        dest.writeInt(image);
        dest.writeInt(isstar ? 1 : 0);
        dest.writeInt(numberincart);
    }

    public static final Parcelable.Creator<Products> CREATOR
            = new Parcelable.Creator<Products>(){
        @Override
        public Products createFromParcel(Parcel source) {
            return new Products(source);
        }
        @Override
        public Products[] newArray(int size) {
            return new Products[size];
        }
    };
}
