package io.github.wonggwan.lab3;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wonggwan on 2017/10/22.
 */

public class Products implements Parcelable{
    private Boolean isstar;
    private Boolean istocart;
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
        istocart= Boolean.FALSE;
        isstar = Boolean.FALSE;
}

    //send basic information to the adapters
    public String getName() { return name; }
    public String getFirst_letter(){ return First_letter;}
    public String getPrice() { return price; }
    public Integer getImgObject() { return image; }
    public String getSpecialInfo() { return productinfo; }


    public Boolean get_isstar() { return isstar; }
    public Boolean get_iscart() { return istocart; }

    //these two functions are for the mainactivity to update the condition in the detail screen
    public void set_cart(Boolean addtocart) { this.istocart = addtocart; }
    public void set_star(Boolean star) { this.isstar = star; }

    protected Products(Parcel in){
        name = in.readString();
        price = in.readString();
        productinfo = in.readString();
        image = in.readInt();
        isstar = in.readByte() != 0;
        istocart = in.readByte() != 0;
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
        dest.writeInt(istocart ? 1 : 0);
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