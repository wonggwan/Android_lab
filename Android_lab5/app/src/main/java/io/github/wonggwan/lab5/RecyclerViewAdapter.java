package io.github.wonggwan.lab5;

/**
 * Created by wonggwan on 2017/10/28.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHder> {
    private Context my;
    private List<Products> recyclerlist;

    public RecyclerViewAdapter(Context context,List<Products> lista) {
        recyclerlist = lista;
        my = context;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }
    private OnItemClickLitener mOnItemClickLitener;
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public static class ViewHder extends RecyclerView.ViewHolder {
        TextView completename;
        TextView firstletterofname;
        public ViewHder(View itemView) {
            super(itemView);
            firstletterofname= (TextView)itemView.findViewById(R.id.id_Firstletter);
            completename = (TextView)itemView.findViewById(R.id.id_completename);
        }
    }

    public void removeData(int position) {
        recyclerlist.remove(position);
        notifyItemRemoved(position);
    }
    public Products getItems(int position) {
        return recyclerlist.get(position);
    }

    @Override
    public void onBindViewHolder(final ViewHder holder, int position) {
        holder.completename.setText( recyclerlist.get(position).getName() );
        holder.firstletterofname.setText(recyclerlist.get(position).getFirst_letter());
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
    public ViewHder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHder hold = new ViewHder(LayoutInflater
                .from(my)
                .inflate(R.layout.recyclerviewitem, parent,false));
        return hold;
    }

    @Override
    public int getItemCount() {
        return recyclerlist == null?0:recyclerlist.size();
    }
}