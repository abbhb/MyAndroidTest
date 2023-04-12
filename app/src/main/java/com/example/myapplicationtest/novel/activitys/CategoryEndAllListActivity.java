package com.example.myapplicationtest.novel.activitys;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.renrui.libraries.interfaces.IHttpRequestInterFace;
import com.renrui.libraries.util.UtilitySecurity;
import com.renrui.libraries.util.UtilitySecurityListener;
import com.renrui.libraries.util.mHttpClient;
import com.example.myapplicationtest.R;
import com.example.myapplicationtest.novel.activitys.baseInfo.BaseActivity;
import com.example.myapplicationtest.novel.adapters.CategoryBookListAdapter;
import com.example.myapplicationtest.novel.constant.ConstantInterFace;
import com.example.myapplicationtest.novel.model.httpModel.CategoriesListHttpModel;
import com.example.myapplicationtest.novel.model.responseModel.CategoriesListResponse;
import com.example.myapplicationtest.novel.model.standard.CategoriesListItem;
import com.example.myapplicationtest.novel.utils.UtilityData;
import com.example.myapplicationtest.novel.utils.UtilityException;
import com.example.myapplicationtest.novel.utils.UtilityToasty;

import butterknife.BindView;

/**
 * 完本-查看全部
 */
public class CategoryEndAllListActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener {

    private static final String EXTRA_STRING_TITLE = "title";
    private String bTitle;

    private static final String EXTRA_INT_CATEGORYID = "categoryId";
    private int categoryId;

    public static Intent getIntent(Context context, String title, int bId) {
        Intent intent = new Intent(context, CategoryEndAllListActivity.class);
        if (!UtilitySecurity.isEmpty(title))
            intent.putExtra(EXTRA_STRING_TITLE, title);
        intent.putExtra(EXTRA_INT_CATEGORYID, bId);
        return intent;
    }

    @BindView(R.id.srlCgalList)
    protected SwipeRefreshLayout srlCgalList;
    @BindView(R.id.rvCgalList)
    protected RecyclerView rvCgalList;

    private CategoriesListResponse res;
    protected CategoryBookListAdapter adapter;

    private int thisPage = 1;
    private boolean isEnd = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_categoryend_alllist;
    }

    @Override
    protected void initExtra() {
        bTitle = UtilitySecurity.getExtrasString(getIntent(), EXTRA_STRING_TITLE);
        categoryId = UtilitySecurity.getExtrasInt(getIntent(), EXTRA_INT_CATEGORYID);
    }

    @Override
    protected void initListener() {
        UtilitySecurityListener.setOnRefreshListener(srlCgalList, this);
    }

    @Override
    protected void initData() {
        initMyAppTitle(bTitle);

        adapter = new CategoryBookListAdapter(null);
        adapter.setOnLoadMoreListener(this, rvCgalList);
        rvCgalList.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);
        rvCgalList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        loadData(true);
    }

    private void loadData(boolean isLoadHeader) {

        if (isLoadHeader)
            thisPage = 1;

        CategoriesListHttpModel httpModel = new CategoriesListHttpModel();
        httpModel.categoryId = categoryId;
        httpModel.pageNum = thisPage;
        httpModel.pageSize = ConstantInterFace.pageSize;
        httpModel.orderBy = getString(R.string.CategoryChannelItemListActivity_filter3_value);
        mHttpClient.Request(this, httpModel, new IHttpRequestInterFace() {
            @Override
            public void onStart() {
                if (isLoadHeader)
                    srlCgalList.setRefreshing(true);
            }

            @Override
            public void onResponse(String s) {
                if (!UtilityData.CheckResponseString(s)) {
                    adapter.loadMoreFail();
                    return;
                }

                try {
                    setResponse(s, isLoadHeader);
                } catch (Exception ex) {
                    UtilityException.catchException(ex);
                }
            }

            @Override
            public void onErrorResponse(String s) {
                UtilityToasty.error(s);
            }

            @Override
            public void onFinish() {
                srlCgalList.setRefreshing(false);
            }
        });
    }

    private void setResponse(String s, boolean isLoadHeader) {
        try {
            res = mHttpClient.fromDataJson(s, CategoriesListResponse.class);
        } catch (Exception ex) {
            UtilityException.catchException(ex);
            res = null;
        }

        if (res == null || res.list == null) {
            isEnd = true;
            adapter.loadMoreEnd(false);
            return;
        }

        try {
            if (isLoadHeader) {
                adapter.setNewData(res.list);
            } else {
                adapter.addData(res.list);
            }

            isEnd = (adapter.getData().size() < (thisPage * ConstantInterFace.categoriesListPageSize)) || UtilitySecurity.isEmpty(res.list);

            if (!UtilitySecurity.isEmpty(res.list))
                thisPage++;

            if (isEnd) {
                adapter.loadMoreEnd(false);
            } else {
                adapter.loadMoreComplete();
            }
        } catch (Exception ex) {
            adapter.loadMoreFail();
            UtilityException.catchException(ex);
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        try {
            CategoriesListItem item = (CategoriesListItem) adapter.getData().get(position);
            Intent intent = PreviewDetailActivity.getIntent(this, item.bookId);
            startActivity(intent);
        } catch (Exception ex) {
            UtilityException.catchException(ex);
        }
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    public void onLoadMoreRequested() {
        loadData(false);
    }

    @Override
    public void onBackPressed() {
        if (getStatusTip().isShowing())
            getStatusTip().hideProgress();

        super.onBackPressed();
    }
}