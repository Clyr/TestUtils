package com.clyr.utils.bottomsmall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clyr.utils.R;

import java.util.List;

/**
 * Created by lzy of clyr on 2022/05/20.
 */

public class PhotoRvAdapter extends RecyclerView.Adapter<PhotoRvAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mList;

    public PhotoRvAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.active_rv_item_new, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_thumb);
        }

        public void bind(String img) {
            Glide.with(mContext).load(img).placeholder(R.drawable.slider_placeholder).into(imageView);
        }
    }
}
