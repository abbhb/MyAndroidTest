package com.example.myapplicationtest.novel.adapters;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.renrui.libraries.util.UtilitySecurity;
import com.example.myapplicationtest.R;
import com.example.myapplicationtest.novel.model.standard.CategoriesListItem;
import com.example.myapplicationtest.novel.utils.images.UtilityImage;

import java.util.List;

/**
 * 搜索图书页 默认列表
 */
public class SearchBookDefaultListAdapter extends BaseQuickAdapter<CategoriesListItem, BaseViewHolder> {

    public SearchBookDefaultListAdapter(List<CategoriesListItem> inviteList) {
        super(R.layout.view_adapter_searchdefault_book, inviteList);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CategoriesListItem item) {

        TextView tvApSiPosition = helper.getView(R.id.tvApSiPosition);
        UtilitySecurity.setText(tvApSiPosition, (helper.getAdapterPosition()) + "");
        UtilitySecurity.setTextColor(tvApSiPosition, helper.getAdapterPosition() <= 3 ? R.color.red_3f42 : R.color.gray_9999);

        com.makeramen.roundedimageview.RoundedImageView rivApSiCoverImg = helper.getView(R.id.rivApSiCoverImg);
        UtilityImage.setImage(rivApSiCoverImg, item.coverImg, R.mipmap.ic_book_list_default);

        helper.setText(R.id.tvApSiTitle, item.title);
        helper.setText(R.id.tvApSiDesc, item.desc);

        // 点击事件
        helper.addOnClickListener(
                R.id.tvApSiPosition, R.id.rlApSiContent,
                R.id.rivApSiCoverImg, R.id.tvApSiTitle, R.id.tvApSiDesc);
    }
}