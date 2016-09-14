package com.jmit.festmanagement.adapters;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jmit.festmanagement.data.DrawerItem;
import com.jmit.festmanagement.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lively on 12/9/16.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MyViewHolder>
{

    private List<DrawerItem> headerList;
    private DrawerChildAdapter listAdapter;
    private Context context;

    public DrawerAdapter(List<DrawerItem> headersList, Context context)
    {
        this.context=context;
        this.headerList = headersList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        holder.headerText.setText(headerList.get(position).getTitle());
        holder.row_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.childListrecview);
                RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(manager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                listAdapter = new DrawerChildAdapter(Arrays.asList(headerList.get(position).getChild()),context);

                recyclerView.setAdapter(listAdapter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return headerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView headerText;
        private View row_view;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            row_view=itemView;
            headerText = (TextView)itemView.findViewById(R.id.headerItems);
        }
    }
}