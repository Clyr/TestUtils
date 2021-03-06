package com.clyr.testutils.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.clyr.base.bean.DateInfo;
import com.clyr.base.bean.OrderInfo;
import com.clyr.base.bean.RoomInfo;
import com.clyr.testutils.R;
import com.clyr.view.scrollablepanel.PanelAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kelin on 16-11-18.
 */

public class ScrollablePanelAdapter extends PanelAdapter {
    private static final int TITLE_TYPE = 4;
    private static final int VERT_TYPE = 0;
    private static final int HORI_TYPE = 1;
    private static final int ORDER_TYPE = 2;

    private List<RoomInfo> vertInfoList = new ArrayList<>();
    private List<DateInfo> horiInfoList = new ArrayList<>();
    private List<List<OrderInfo>> ordersList = new ArrayList<>();


    @Override
    public int getRowCount() {
        return vertInfoList.size() + 1;
    }

    @Override
    public int getColumnCount() {
        return horiInfoList.size() + 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int row, int column) {
        int viewType = getItemViewType(row, column);
        switch (viewType) {
            case HORI_TYPE:
                setDateView(column, (HoriViewHolder) holder);
                break;
            case VERT_TYPE:
                setRoomView(row, (VertViewHolder) holder);
                break;
            /*case ORDER_TYPE:
                setOrderView(row, column, (OrderViewHolder) holder);
                break;*/
            case TITLE_TYPE:
                break;
            default:
                setOrderView(row, column, (OrderViewHolder) holder);
        }
    }

    public int getItemViewType(int row, int column) {
        if (column == 0 && row == 0) {
            return TITLE_TYPE;
        }
        if (column == 0) {
            return VERT_TYPE;
        }
        if (row == 0) {
            return HORI_TYPE;
        }
        return ORDER_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HORI_TYPE:
                return new HoriViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_hori_info, parent, false));
            case VERT_TYPE:
                return new VertViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_vert_info, parent, false));
            case ORDER_TYPE:
                return new OrderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_order_info, parent, false));
            case TITLE_TYPE:
                return new TitleViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_title, parent, false));
            default:
                break;
        }
        return new OrderViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_order_info, parent, false));
    }


    private void setDateView(int pos, HoriViewHolder viewHolder) {
        DateInfo dateInfo = horiInfoList.get(pos - 1);
        if (dateInfo != null && pos > 0) {
            viewHolder.horiNameTextView.setText(dateInfo.getDate());
            viewHolder.horiMsgTextView.setText(dateInfo.getWeek());
        }
    }

    private void setRoomView(int pos, VertViewHolder viewHolder) {
        RoomInfo roomInfo = vertInfoList.get(pos - 1);
        if (roomInfo != null && pos > 0) {
            viewHolder.vertTypeTextView.setText(roomInfo.getRoomType());
            viewHolder.vertNameTextView.setText(roomInfo.getRoomName());
        }
    }

    private void setOrderView(final int row, final int column, OrderViewHolder viewHolder) {
        final OrderInfo orderInfo = ordersList.get(row - 1).get(column - 1);
        if (orderInfo != null) {
            if (orderInfo.getStatus() == OrderInfo.Status.ALARM) {
                viewHolder.view.setBackgroundResource(R.drawable.bg_white_gray_stroke);
                viewHolder.nameTextView.setText(orderInfo.getGuestName());
                viewHolder.statusTextView.setText("");
            } else if (orderInfo.getStatus() == OrderInfo.Status.NORMAL) {
                viewHolder.nameTextView.setText(orderInfo.getGuestName());
                viewHolder.statusTextView.setText(orderInfo.isBegin() ? "check in" : "");
                viewHolder.view.setBackgroundResource(orderInfo.isBegin() ? R.drawable.bg_room_red_begin_with_stroke : R.drawable.bg_room_red_with_stroke);
            } else if (orderInfo.getStatus() == OrderInfo.Status.WARN) {
                viewHolder.nameTextView.setText(orderInfo.getGuestName());
                viewHolder.statusTextView.setText(orderInfo.isBegin() ? "reverse" : "");
                viewHolder.view.setBackgroundResource(orderInfo.isBegin() ? R.drawable.bg_room_blue_begin_with_stroke : R.drawable.bg_room_blue_middle);
            }
            viewHolder.itemView.setOnClickListener(v -> Toast.makeText(v.getContext(), orderInfo.getGuestName(), Toast.LENGTH_SHORT).show());


        }
    }


    private static class HoriViewHolder extends RecyclerView.ViewHolder {
        public TextView horiNameTextView;
        public TextView horiMsgTextView;

        public HoriViewHolder(View itemView) {
            super(itemView);
            this.horiNameTextView = itemView.findViewById(R.id.date);
            this.horiMsgTextView = itemView.findViewById(R.id.msg);
        }

    }

    private static class VertViewHolder extends RecyclerView.ViewHolder {
        public TextView vertTypeTextView;
        public TextView vertNameTextView;

        public VertViewHolder(View view) {
            super(view);
            this.vertTypeTextView = view.findViewById(R.id.vert_type);
            this.vertNameTextView = view.findViewById(R.id.vert_name);
        }
    }

    private static class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView statusTextView;
        public View view;

        public OrderViewHolder(View view) {
            super(view);
            this.view = view;
            this.statusTextView = view.findViewById(R.id.status);
            this.nameTextView = view.findViewById(R.id.guest_name);
        }
    }

    private static class TitleViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;

        public TitleViewHolder(View view) {
            super(view);
            this.titleTextView = view.findViewById(R.id.title);
        }
    }


    public void setVertInfoList(List<RoomInfo> vertInfoList) {
        this.vertInfoList = vertInfoList;
    }

    public void setHoriInfoList(List<DateInfo> horiInfoList) {
        this.horiInfoList = horiInfoList;
    }

    public void setOrdersList(List<List<OrderInfo>> ordersList) {
        this.ordersList = ordersList;
    }
}
