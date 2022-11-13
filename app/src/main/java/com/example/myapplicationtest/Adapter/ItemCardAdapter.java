package com.example.myapplicationtest.Adapter;

import static com.example.utils.SystemUtil.Dp2Px;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplicationtest.ListViewClass.ItemCard;
import com.example.myapplicationtest.R;
import com.example.utils.Ys.DownImage;
import com.example.utils.Log;

import java.util.List;

public class ItemCardAdapter extends ArrayAdapter<ItemCard> {
    TextView duoshaochoutext;
    ImageView iswaipng;
    TextView duoshaochouifxiaoyu15;
    //resourceID指定ListView的布局方式
    private int resourceID;
    //ItemCardAdapter
    public ItemCardAdapter(Context context,int textViewResourceID , List<ItemCard> objects){
        super(context,textViewResourceID,objects);
        resourceID = textViewResourceID;
    }
    //自定义item资源的解析方式
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取当前Browser实例
        ItemCard itemCard = getItem(position);
        //使用LayoutInfater为子项加载传入的布局
        View view = LayoutInflater.from(getContext()).inflate(resourceID,null);
        ImageView imageIcon = (ImageView)view.findViewById(R.id.item_icon);
        duoshaochoutext = (TextView)view.findViewById(R.id.duoshaochou);
        duoshaochouifxiaoyu15 = view.findViewById(R.id.duoshaochouifxiaoyu15);
        iswaipng = view.findViewById(R.id.iswaipng);
        View jindutiao = view.findViewById(R.id.jindutiaoforitemcard);
        //引入Browser对象的属性值

        if (itemCard.getImgType()==ItemCard.W_IMG&&itemCard.getImgUrl()!=null){
            DownImage downImage = new DownImage(itemCard.getImgUrl().toString());
            downImage.loadImage(new DownImage.ImageCallBack() {

                @Override
                public void getDrawable(Drawable drawable) {
                    imageIcon.setImageDrawable(drawable);
                }
            });
        }
        else{
            imageIcon.setImageDrawable(view.getContext().getDrawable(R.drawable.question_icon));
        }
        if (itemCard.getName().equals("暂未出金")){
            if (itemCard.getNums()<30){
                duoshaochouifxiaoyu15.setText("已垫"+itemCard.getNums()+"抽（暂未出金）");
                duoshaochoutext.setText("");
            }
            else{
                duoshaochoutext.setText("已垫"+itemCard.getNums()+"抽（暂未出金）");
            }
        }
        else {
            if (itemCard.getNums()<20){
                duoshaochouifxiaoyu15.setText(itemCard.getNums()+"抽");
                duoshaochoutext.setText("");
            }
            else{
                duoshaochoutext.setText(itemCard.getNums()+"抽");
            }
        }


        ViewGroup.LayoutParams layoutParams = jindutiao.getLayoutParams();
        if (itemCard.getNums()>45&&itemCard.getNums()<70){
            /**
             * note
             * 此处不用view.getContext().getColor的话颜色变紫色
             */
            jindutiao.setBackground(view.getContext().getDrawable(R.drawable.yinyingyuanjiaochengse));
        }
        else if (itemCard.getNums()>=70){
            jindutiao.setBackground(view.getContext().getDrawable(R.drawable.backyuanjiaored));
        }
        if (itemCard.isWai()){
            iswaipng.setImageDrawable(view.getContext().getDrawable(R.drawable.wai));
        }
        layoutParams.width = Dp2Px(view.getContext(), (float) (itemCard.getNums()*8/3));
        //Px2Dp(view.getContext(), (float) (itemCard.getNums()*3.0/10)),Px2Dp(view.getContext(), 25.0F)
        jindutiao.setLayoutParams(layoutParams);
        return view;
    }


}
