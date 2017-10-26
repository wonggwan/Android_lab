package io.github.wonggwan.lab3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wonggwan on 2017/10/22.
 */

public class ListViewAdapter extends BaseAdapter{
    private Context my;
    private List<Products> list = new ArrayList<>();

    public ListViewAdapter(Context context) {
        list = new ArrayList<Products>();
        my = context;
    }

    //initilize the listener
    interface OnItemClickLitener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    private OnItemClickLitener mOnItemClickLitener;
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    //set up the required viewholder
    private static class ViewHolder {
        TextView myproductinfo;
        TextView myfirstletter;
        TextView myprice;
    }
    public void removeData(int position) {
        list.remove(position);
        this.notifyDataSetChanged();
    }
    public void addData(Products product) {
        list.add(product);
        this.notifyDataSetChanged();
    }
    public void clearData() {
        list.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }
    //count the number of the items in the list

    @Override
    public Products getItem(int position) {
        return list.get(position);
    }
    //send back the position of the product that was chosen

    @Override
    public long getItemId(int position) {
        return position;
    }
    //send back the position information of the chosen product

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(my);
        ViewHolder holder = null;  //initialize viewholder
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listviewitem, null);
            /*
            * Viewholder could hold the information you want to put in a certain area
            * in listview, we have the first letter; complete name and the price as the required information
            */
            holder = new ViewHolder();
            holder.myfirstletter = (TextView) convertView.findViewById(R.id.id_Firstletter);
            holder.myproductinfo = (TextView) convertView.findViewById(R.id.id_completename);
            holder.myprice = (TextView) convertView.findViewById(R.id.id_ItemPrice);
            convertView.setTag(holder);
        }
        else { holder = (ViewHolder) convertView.getTag(); }

        if (mOnItemClickLitener != null) {
            final View finalConvertView = convertView;
            //short click listener
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(finalConvertView,position);
                }
            });

            //long click listener
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickLitener.onItemLongClick(finalConvertView,position);
                    return false;
                }
            });
        }

        //put the corresponding information to the correct widget
        holder.myprice.setText( list.get(position).getPrice() );
        holder.myproductinfo.setText( list.get(position).getName() );
        holder.myfirstletter.setText(list.get(position).getFirst_letter());
        return convertView;
    }

}
