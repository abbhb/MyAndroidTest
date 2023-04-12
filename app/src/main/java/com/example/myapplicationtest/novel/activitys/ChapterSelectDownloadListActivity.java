package com.example.myapplicationtest.novel.activitys;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.renrui.libraries.util.UtilitySecurity;
import com.renrui.libraries.util.UtilitySecurityListener;
import com.example.myapplicationtest.R;
import com.example.myapplicationtest.novel.activitys.baseInfo.BaseActivity;
import com.example.myapplicationtest.novel.adapters.BookSelectDownloadMenuListAdapter;
import com.example.myapplicationtest.novel.database.AppDatabase;
import com.example.myapplicationtest.novel.database.tb.TbBookChapter;
import com.example.myapplicationtest.novel.database.tb.TbReadHistory;
import com.example.myapplicationtest.novel.interfaces.IDownloadContentListener;
import com.example.myapplicationtest.novel.model.eventBus.OnDownloadBackUpChangeEvent;
import com.example.myapplicationtest.novel.model.eventBus.OnDownloadMenuFinishChangeEvent;
import com.example.myapplicationtest.novel.model.standard.DownloadBookContentItemInfo;
import com.example.myapplicationtest.novel.utils.UtilityBusiness;
import com.example.myapplicationtest.novel.utils.UtilityToasty;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 选择下载页
 */
public class ChapterSelectDownloadListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String EXTRA_INT_BOOKID = "bookId";
    private int bookId;

    public static Intent getIntent(Context context, int bookId) {
        Intent intent = new Intent(context, ChapterSelectDownloadListActivity.class);
        intent.putExtra(EXTRA_INT_BOOKID, bookId);
        return intent;
    }

    @BindView(R.id.tvSdTitle)
    protected TextView tvSdTitle;

    @BindView(R.id.lvSdList)
    protected ListView lvSdList;

    private List<String> lis = new ArrayList<>();
    private BookSelectDownloadMenuListAdapter adapter;

    private static boolean isDownloading = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_selectdownload;
    }

    @Override
    protected void initExtra() {
        bookId = UtilitySecurity.getExtrasInt(getIntent(), EXTRA_INT_BOOKID);

        if (bookId < 1) {
            UtilityToasty.error(R.string.Utility_unknown);
            finish();
        }
    }

    @Override
    protected void initListener() {
        UtilitySecurityListener.setOnItemClickListener(lvSdList, this);
    }

    @Override
    protected void initData() {
        initMyAppTitle(R.string.BSelectDownloadActivity_Title);

        lis.clear();
        lis.add(getString(R.string.BSelectDownloadActivity_item1));
        lis.add(getString(R.string.BSelectDownloadActivity_item2));
        lis.add(getString(R.string.BSelectDownloadActivity_item3));
        lis.add(getString(R.string.BSelectDownloadActivity_item4));

        adapter = new BookSelectDownloadMenuListAdapter(lis);
        lvSdList.setAdapter(adapter);

        if (isDownloading)
            getStatusTip().showDownloadProgress();
    }

    private void download(int afterCounts) {

        if (getStatusTip().isShowing()) {
            UtilityToasty.warning(R.string.BSelectDownloadActivity_downloading);
            return;
        }

        List<TbBookChapter> lisWaitDownloadChapter;

        // 下载全部章节
        if (afterCounts == Integer.MAX_VALUE) {
            lisWaitDownloadChapter = AppDatabase.getInstance().ChapterDao().getAllUnDownloadChapterId(bookId);
        } else {
            // 有阅读记录 从阅读章节开始下载
            // 没有阅读记录 从第一章开始下载
            long startDownloadChapterId = 0;
            TbReadHistory readHistory = AppDatabase.getInstance().ReadHistoryDao().getEntity(bookId);
            if (readHistory != null && readHistory.chapterId > 0) {
                startDownloadChapterId = readHistory.chapterId;
            } else {
                TbBookChapter bookChapter = AppDatabase.getInstance().ChapterDao().getFirstChapter(bookId);
                if (bookChapter != null && bookChapter.chapterId > 0) {
                    startDownloadChapterId = bookChapter.chapterId;
                }
            }

            if (startDownloadChapterId < 1) {
                UtilityToasty.warning(R.string.BSelectDownloadActivity_noDownload);
                return;
            }

            // 获取待下载的章id
            lisWaitDownloadChapter = AppDatabase.getInstance().ChapterDao().getUnDownloadAfterChapterId(bookId, startDownloadChapterId, afterCounts);
        }

        if (UtilitySecurity.isEmpty(lisWaitDownloadChapter)) {
            UtilityToasty.warning(R.string.BSelectDownloadActivity_noDownload);
            return;
        }
        List<Long> lisWaitDownloadChapterId = new ArrayList<>();
        for (int i = 0; i < lisWaitDownloadChapter.size(); i++) {
            if (UtilitySecurity.isEmpty(lisWaitDownloadChapter.get(i).content))
                lisWaitDownloadChapterId.add(lisWaitDownloadChapter.get(i).chapterId);
        }

        isDownloading = true;
        UtilityBusiness.downloadContent(this, bookId, lisWaitDownloadChapterId, false, new IDownloadContentListener() {
            @Override
            public void onDownloadSuccess(List<DownloadBookContentItemInfo> list) {
                isDownloading = false;
                getStatusTip().hideProgress();

                if (!ChapterSelectDownloadListActivity.this.isFinishing()) {
                    UtilityToasty.success(R.string.BSelectDownloadActivity_downloadEnd);
                }
            }

            @Override
            public void onDownloadLoadFail() {
                isDownloading = false;
                getStatusTip().hideProgress();
            }
        });
    }

    @Subscribe
    public void onEvent(OnDownloadMenuFinishChangeEvent event) {
        getStatusTip().hideProgress();
        UtilityToasty.success(R.string.BSelectDownloadActivity_downloadEnd);
    }

    @Subscribe
    public void onEvent(OnDownloadBackUpChangeEvent event) {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (isDownloading || getStatusTip().isShowing())
            return;

        switch (i) {
            case 0:
                download(50);
                break;

            case 1:
                download(100);
                break;

            case 2:
                download(200);
                break;

            case 3:
                download(Integer.MAX_VALUE);
                break;
        }
    }
}
