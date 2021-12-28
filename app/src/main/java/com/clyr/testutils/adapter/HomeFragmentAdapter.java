package com.clyr.testutils.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clyr.base.interfaces.OnItemClickListener;
import com.clyr.testutils.R;

import java.util.List;

/**
 * Created by 11635 of clyr on 2021/11/29.
 */

public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.HomeViewHolder> {
    private final List<String> mList;
    private final OnItemClickListener mCon;

    public HomeFragmentAdapter(List<String> mList, OnItemClickListener mCon) {
        this.mList = mList;
        this.mCon = mCon;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recyclerview_item, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        holder.bindData(mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }

    class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview);
        }

        public void bindData(String name, int position) {
            textView.setText(name);
            textView.setOnClickListener(v -> mCon.onClick(position, name));
        }
    }
}
