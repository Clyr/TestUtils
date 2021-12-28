package com.clyr.testutils.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clyr.base.interfaces.OnItemClickListener;
import com.clyr.testutils.R;
import com.clyr.testutils.adapter.HomeFragmentAdapter;
import com.clyr.testutils.base.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class BlankFragment extends BaseFragment implements OnItemClickListener {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.layout_refresh)
    SmartRefreshLayout mRefreshLayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    Unbinder unbinder;

    private List<String> mList;
    private HomeFragmentAdapter homeFragmentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_blank, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        initData();
        return inflate;
    }

    private void initData() {
        //下拉刷新
        mRefreshLayout.setOnRefreshListener(refreshlayout -> {
            refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
        });

        //上拉加载更多
        mRefreshLayout.setOnLoadMoreListener(refreshlayout -> {
            refreshlayout.finishLoadMore(1000);
            //loadMore();
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(layoutManager);
        String[] stringArray = getResources().getStringArray(R.array.home_fun_list);
        mList = Arrays.asList(stringArray);

        homeFragmentAdapter = new HomeFragmentAdapter(mList, this);
        recyclerview.setAdapter(homeFragmentAdapter);
    }

    private void loadMore() {
        for (int i = 0; i < 5; i++) {
            mList.add(mList.get(i));
        }
        //homeFragmentAdapter.notifyDataSetChanged();
        mRefreshLayout.finishLoadMore();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        listView = getActivity().findViewById(R.id.list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onClick(int position, Object obj) {

    }
}
