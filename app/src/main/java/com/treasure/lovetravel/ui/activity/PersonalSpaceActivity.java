package com.treasure.lovetravel.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.treasure.lovetravel.R;
import com.treasure.lovetravel.adapter.base.OnItemClickListeners;
import com.treasure.lovetravel.adapter.base.ViewHolder;
import com.treasure.lovetravel.helper.Contacts;
import com.treasure.lovetravel.utils.LogUtil;
import com.treasure.lovetravel.utils.SharedPreferencesUtil;
import com.treasure.lovetravel.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class PersonalSpaceActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    public static void start(Context context) {
        Intent intent = new Intent(context, PersonalSpaceActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.title)
    TextView title;

    private boolean isLoadMore;//是否是底部加载更多
    private int PAGE_COUNT = 1;
//    private List<FindRecyclerBean> list;
//    private FindRecyclerAdapter findRecyclerAdapter;

    @Override
    protected void loadContentLayout() {
        setContentView(R.layout.activity_personal_space);
    }

    @Override
    protected void initView() {
        title.setText("我的空间");
        initRefresh();
        initRecyclerView();
    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.back})
    public void onViewclick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
        }
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
//        findRecyclerAdapter = new FindRecyclerAdapter(this, list, true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(findRecyclerAdapter);
//        findRecyclerAdapter.setOnItemClickListener(this);
    }

    private void getData() {
//        BmobQuery<FindRecyclerBean> query = new BmobQuery<FindRecyclerBean>();
//        query.addWhereEqualTo("head_name", SharedPreferencesUtil.getInstance().getString(Contacts.USER_NAME));
//        //执行查询方法
//        query.findObjects(new FindListener<FindRecyclerBean>() {
//            @Override
//            public void done(List<FindRecyclerBean> object, BmobException e) {
//                refreshLayout.setRefreshing(false);
//                if (e == null) {
//                    Collections.reverse(object);
//                    if (object.size() == 0){
//                        ToastUtils.showToast("您还没有分享自己的动态");
//                    }
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
//        DynamicDetailActivity.start(PersonalSpaceActivity.this,data);
//    }
}
