package com.example.myapplicationtest.novel.adapters;

import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.myapplicationtest.R;
import com.example.myapplicationtest.novel.model.standard.CategoriesListItem;
import com.example.myapplicationtest.novel.utils.UtilityData;
import com.example.myapplicationtest.novel.utils.images.UtilityImage;

import java.util.List;

/**
 * 目录列表item
 */
public class BookPreviewItemAdapter extends BaseQuickAdapter<CategoriesListItem, BaseViewHolder> implements View.OnClickListener {

    public BookPreviewItemAdapter(List<CategoriesListItem> inviteList) {
        super(R.layout.view_adapter_item_category_list, inviteList);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CategoriesListItem item) {

        com.makeramen.roundedimageview.RoundedImageView rivBciCoverImg = helper.getView(R.id.rivBciCoverImg);
        UtilityImage.setImage(rivBciCoverImg, item.coverImg,R.mipmap.ic_book_list_default);

        helper.setText(R.id.tvBciTitle,item.title);
        helper.setText(R.id.tvBciAuthor,item.author);
        helper.setText(R.id.tvBciDesc,UtilityData.deleteStartAndEndNewLine( item.desc));

        helper.addOnClickListener(R.id.llBciBaseInfo,R.id.tvBciTitle,R.id.tvBciAuthor,R.id.tvBciDesc,R.id.llBciShowDetail);
    }

    @Override
    public void onClick(View view) {

    }
}