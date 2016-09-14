package com.jmit.festmanagement.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jmit.festmanagement.R;

import java.util.List;

/**
 * Created by lively on 12/9/16.
 */
public class DrawerChildAdapter extends RecyclerView.Adapter<DrawerChildAdapter.Adapter>{

    private List<String> headerList;
    private Context context;

    public DrawerChildAdapter(List<String> headersList, Context context)
    {
        this.headerList = headersList;
        this.context = context;
    }

    @Override
    public Adapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_childrow,null);
        return new Adapter(view);
    }

    @Override
    public void onBindViewHolder(Adapter holder, int position) {
        holder.childItems.setText(headerList.get(position));
    }

    @Override
    public int getItemCount() {
        return headerList.size();
    }

    public class Adapter extends RecyclerView.ViewHolder
    {
        TextView childItems;

        public Adapter(View itemView)
        {
            super(itemView);
            childItems = (TextView)itemView.findViewById(R.id.childList);
        }
    }

}