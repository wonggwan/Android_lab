package io.github.wonggwan.lab9.adapter;

/**
 * Created by wonggwan on 2017/12/18.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import io.github.wonggwan.lab9.R;
import io.github.wonggwan.lab9.model.GitHub;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.myviewholder>{

    Context mContext;
    private List<GitHub> mylist;
    public CardAdapter(Context context){
        this.mContext=context;
        this.mylist = null;
    }

    private OnItemClickLitener mOnItemClickLitener;
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    public void add(GitHub h) {
        mylist.add(h);
        notifyDataSetChanged();
    }
    public void delete(int posititon) {
        mylist.remove(posititon);
        notifyDataSetChanged();
    }
    public void create(List<GitHub> newlist) {
        mylist = newlist;
        notifyDataSetChanged();
    }

    public GitHub getpos(int position) {
        return mylist.get(position);
    }

    @Override
    public myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        myviewholder holder = new myviewholder(LayoutInflater
                .from(mContext)
                .inflate(R.layout.cardview, parent,false));
        return holder;
    }

    public static class myviewholder extends RecyclerView.ViewHolder {
        TextView title;
        TextView sub1;
        TextView sub2;
        public myviewholder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            sub1 = (TextView) itemView.findViewById(R.id.tv_sub_1);
            sub2 = (TextView) itemView.findViewById(R.id.tv_sub_2);
        }
    }

    @Override
    public void onBindViewHolder(final myviewholder holder, final int position) {
        final GitHub g = ((GitHub)mylist.get(position));
        holder.title.setText(g.getTitle());
        holder.sub1.setText("ID: "+g.getSub1());
        holder.sub2.setText("Blog: "+g.getSub2());
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }});

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }});
        }
    }

    @Override
    public int getItemCount() {
        return mylist!=null? mylist.size():0;
    }
}
