package com.treasure.lovetravel.ui.fragment;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.treasure.lovetravel.R;
import com.treasure.lovetravel.adapter.base.OnItemClickListeners;
import com.treasure.lovetravel.adapter.base.ViewHolder;
import com.treasure.lovetravel.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FindFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private boolean isLoadMore;//是否是底部加载更多
    private int PAGE_COUNT = 1;
//    private List<FindRecyclerBean> list;
//    private FindRecyclerAdapter findRecyclerAdapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_find);
        initRefresh();
        initRecyclerView();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void onUserVisible() {

    }

    private void initRefresh() {
        refreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(this);
        //实现首次自动显示加载提示
        refreshLayout.setRefreshing(true);
        getData();
    }

    private void initRecyclerView() {
//        list = new ArrayList<>();
//        findRecyclerAdapter = new FindRecyclerAdapter(getContext(), list, true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(findRecyclerAdapter);
//        findRecyclerAdapter.setOnItemClickListener(this);
    }

    private void getData() {
//        BmobQuery<FindRecyclerBean> query = new BmobQuery<FindRecyclerBean>();
//        query.addWhereEqualTo("head_simi", "公开");
//        //执行查询方法
//        query.findObjects(new FindListener<FindRecyclerBean>() {
//            @Override
//            public void done(List<FindRecyclerBean> object, BmobException e) {
//                refreshLayout.setRefreshing(false);
//                if (e == null) {
//                    Collections.reverse(object);
//                    list.clear();
//                    list.addAll(object);
//                    findRecyclerAdapter.notifyDataSetChanged();
//                } else {
//                    LogUtil.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
//                }
//            }
//        });
    }

    @Override
    public void onRefresh() {
        isLoadMore = false;
        getData();
        refreshLayout.setRefreshing(true);
    }

//    @Override
//    public void onItemClick(ViewHolder viewHolder, FindRecyclerBean data, int position) {
//        DynamicDetailActivity.start(getContext(),data);
//    }
}
