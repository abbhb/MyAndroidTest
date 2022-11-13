package com.example.myapplicationtest.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.View.HorizontalBarView;
import com.example.myapplicationtest.Adapter.ItemCardAdapter;
import com.example.myapplicationtest.CommUtil;
import com.example.myapplicationtest.ListViewClass.ItemCard;
import com.example.myapplicationtest.R;
import com.example.myapplicationtest.entity.WishVo;

import com.example.utils.Log;
import com.example.utils.Ys.CharacterStyle;
import com.example.utils.GsonUtil;
import com.example.utils.SystemUtil;
import com.example.utils.Ys.CharacterStyleForImgUrl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MyFragmentForWQ extends Fragment {
    TextView partOverview;
    TextView partFiveNum;
    TextView partFourNum;
    TextView partThreeNum;
    TextView partFivePro;
    TextView partFourPro;
    TextView partThreePro;
    TextView partFiveAvg;
    TextView partFourAvg;
    TextView element;
    TextView weapon_four_seq_text;
    ListView listViewforwq;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myfragment_for_w_q, null);
        partOverview = view.findViewById(R.id.weapon_overview);
        partFiveNum = view.findViewById(R.id.weapon_five_num);
        partFourNum = view.findViewById(R.id.weapon_four_num);
        partThreeNum = view.findViewById(R.id.weapon_three_num);
        partFivePro = view.findViewById(R.id.weapon_five_pro);
        partFourPro = view.findViewById(R.id.weapon_four_pro);
        partThreePro = view.findViewById(R.id.weapon_three_pro);
        partFiveAvg = view.findViewById(R.id.weapon_five_avg);
        partFourAvg = view.findViewById(R.id.weapon_four_avg);
        element = view.findViewById(R.id.weapon_four_seq);
        listViewforwq = view.findViewById(R.id.listviewforwq);
        weapon_four_seq_text = view.findViewById(R.id.weapon_four_seq_text);
//        Bundle bundle=getArguments();
        SharedPreferences sharedPreferences = CommUtil.getInstance().getSharedPreferences(getContext());
        String data = sharedPreferences.getString("datawq","");
        if (!data.equals("")){
            List<WishVo> wishList = GsonUtil.jsonToList(data, WishVo.class);
            showDetail(wishList, "301",view.getContext());
        }
//        String data=bundle.getString("dataup");
        weapon_four_seq_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                element.setText(getResources().getString(R.string.show_four_sequence));
                element.setTextColor(getResources().getColor(R.color.blue));
            }
        });
        return view;
    }

    public void showDetail(List<WishVo> wishVo, String code, Context context) {
        clearDetail();
        if (wishVo.isEmpty()) {
            return;
        }
        Map<String, Object> result = analysis(wishVo);

        partFiveNum.setText((String) result.get("five_num"));
        partFourNum.setText((String) result.get("four_num"));
        partThreeNum.setText((String) result.get("three_num"));
        partFivePro.setText((String) result.get("five_pro"));
        partFourPro.setText((String) result.get("four_pro"));
        partThreePro.setText((String) result.get("three_pro"));
        com.example.utils.Log.d("2022test",(String) result.get("five_pro"));
        com.example.utils.Log.d("2022test", result.toString());
        String five_avg = (String) result.get("five_avg");
        partFiveAvg.setText(five_avg);
        String four_avg = (String) result.get("four_avg");
        partFourAvg.setText(four_avg);
        // 概览样式
        SpannableStringBuilder overviewStyle = getOverviewStyle(wishVo, (String) result.get("overview"));
        partOverview.setText(overviewStyle);
        int overviewsss = getOverviewStyleforfive(wishVo, (String) result.get("overview"));
        int num = getOverviewfive(wishVo);
        // 五星平均出货抽数样式
        SpannableStringBuilder fiveExpendStyle = new SpannableStringBuilder(five_avg);
        fiveExpendStyle.setSpan(new ForegroundColorSpan(Color.MAGENTA), 9, five_avg.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        partFiveAvg.setText(fiveExpendStyle);

        // 四星平均出货抽数样式
        SpannableStringBuilder fourExpendStyle = new SpannableStringBuilder(four_avg);
        fourExpendStyle.setSpan(new ForegroundColorSpan(Color.MAGENTA), 9, four_avg.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        partFourAvg.setText(fourExpendStyle);
        // 设置五星出货顺序
        addSequencefive((List<WishVo>) result.get("five_seq"), context,num);



        // 设置四星出货顺序
//        addSequence((List<WishVo>) result.get("four_seq"), view.findViewById(R.id.weapon_four_seq"), "紫");

//        TextView textView = CommUtil.getInstance().generateTextView(this, R.color.blue, showStr);
//        element.addView(textView);
        element.setText(getResources().getString(R.string.show_four_sequence));
        element.setTextColor(getResources().getColor(R.color.blue));
        element.setOnClickListener((view) -> {
//            String weapon = CommUtil.getInstance().readCacheFile(this, currentAccount-" + code.json", "[]");
//            showDetail(GsonUtil.jsonToList(weapon, WishVo.class), prefix, code);
            addSequence((List<WishVo>) result.get("four_seq"), view.findViewById(R.id.weapon_four_seq), "紫");
        });

    }
    private void addSequencefive(List<WishVo> wishList,Context context,int numsss) {
        Set<String> standardCharacter = new HashSet<>(Arrays.asList("迪卢克", "琴", "莫娜", "刻晴", "七七", "提纳里"));

        List<ItemCard> itemCards = new ArrayList<>();
        ItemCard itemCard;
        if (numsss>0){
            itemCard = new ItemCard("暂未出金",R.drawable.question_icon,numsss,false,ItemCard.QUESTION_IMG);
            itemCards.add(itemCard);
        }
        for (int i = 0; i < wishList.size(); i++) {
            WishVo wishVo = wishList.get(i);
            String s = "";
            s = CharacterStyleForImgUrl.get(wishVo.getName());
            String count = wishVo.getCount();
            String[] ss = count.split("抽");
            boolean contains = standardCharacter.contains(wishVo.getName());

            int i1ss = Integer.parseInt(ss[0]);
            if (s==null) {
                itemCard = new ItemCard(wishVo.getName(),R.drawable.question_icon,i1ss,contains,ItemCard.QUESTION_IMG);
                itemCards.add(itemCard);
            }
            else{
                itemCard = new ItemCard(wishVo.getName(),s,i1ss,contains);
                itemCards.add(itemCard);
            }
        }

        ItemCardAdapter itemCardAdapter = new ItemCardAdapter(context,R.layout.item_card,itemCards);
        listViewforwq.setAdapter(itemCardAdapter);

        setListViewHeight(listViewforwq);
        itemCardAdapter.notifyDataSetChanged();

    }
    private void addSequence(List<WishVo> wishList, TextView content, String color) {
        content.setText("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            //可能bug
            content.setLineHeight(SystemUtil.Px2Dp(getContext(), 200));
        }
        SpannableStringBuilder style = new SpannableStringBuilder();
        for (int i = 0; i < wishList.size(); i++) {
            WishVo wishVo = wishList.get(i);
            // 四星模拟数据跳过
            if ("紫".equals(color) && "-1".equals(wishVo.getId())) {
                continue;
            }
            // 计算颜色
            Integer colorId = CharacterStyle.get(wishVo.getName());
            if (colorId == null) {
                if ("4".equals(wishVo.getRank_type())) {
                    colorId = R.color.purple_200;
                }
                if ("5".equals(wishVo.getRank_type())) {
                    colorId = R.color.gold;
                }
                colorId = colorId == null ? R.color.default_color : colorId;
            }
            String name = wishVo.getName() + "(" + wishVo.getCount() + ")";
            name = i == 0 ? name : "   " + name;
            int start = style.length();
            style.append(name);
            style.setSpan(new ForegroundColorSpan(getResources().getColor(colorId)), start, start + name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        content.setText(style);
    }
    private void clearDetail() {

//        LinearLayout fiveSeq = view.findViewById(R.id.weapon_five_seq");
//        LinearLayout fourSeq = view.findViewById(R.id.weapon_four_seq");
        partOverview.setText(getResources().getString(R.string.overview));
        partFiveNum.setText(getResources().getString(R.string.five_level));
        partFourNum.setText(getResources().getString(R.string.four_level));
        partThreeNum.setText(getResources().getString(R.string.three_level));
        partFivePro.setText(getResources().getString(R.string.empty_probability));
        partFourPro.setText(getResources().getString(R.string.empty_probability));
        partThreePro.setText(getResources().getString(R.string.empty_probability));
        partFiveAvg.setText(getResources().getString(R.string.five_avg));
        partFourAvg.setText(getResources().getString(R.string.four_avg));
//        fiveSeq.removeAllViews();
//        fourSeq.removeAllViews();
    }
    private Map<String, Object> analysis(List<WishVo> wishList) {
        Map<String, Object> result = new HashMap<>();
        result.put("total", wishList.size() + "");
        Set<String> standardweapon = new HashSet<>(Arrays.asList("迪卢克", "琴", "莫娜", "刻晴", "七七", "提纳里"));
        // 五星角色
        List<WishVo> fivePart = getweaponSequence(wishList, 5);
        // 四星角色
        List<WishVo> fourPart = getweaponSequence(wishList, 4);
        // 五星出货顺序
        result.put("five_seq", fivePart);
        // 四星出货顺序
        result.put("four_seq", fourPart);
        // up五星数量
        long up_five_num = fivePart.stream().filter(wishVo -> !standardweapon.contains(wishVo.getName())).count();
        result.put("weapon_up_five_num", "up五星：" + (up_five_num == 0L ? "还未出up" : up_five_num));
        result.put("weapon_up_five_pro", "【 " + (wishList.size() == 0 ? "0": round(up_five_num * 1.0 / wishList.size())) + "% 】");
        // 五星数量
        long five = fivePart.size();
        result.put("five_num", "五星：" + (five == 0L ? "还未出金" : five));
        result.put("five_pro", "【 " + (wishList.size() == 0 ? "0": round(five * 1.0 / wishList.size())) + "% 】");
        // 四星数量
        long four = wishList.stream().filter(wishVo -> "4".equals(wishVo.getRank_type())).count();
        result.put("four_num", "四星：" + (four == 0L ? "还未出紫" : four));
        result.put("four_pro", "【 " + (wishList.size() == 0 ? "0": round(four * 1.0 / wishList.size())) + "% 】");
        // 三星数量
        long three = wishList.stream().filter(wishVo -> "3".equals(wishVo.getRank_type())).count();
        result.put("three_num", "三星：" + (three == 0L ? "还未出蓝" : three));
        result.put("three_pro", "【 " + (wishList.size() == 0 ? "0": round(three * 1.0 / wishList.size())) + "% 】");
        // 五星平均出货抽数
        IntSummaryStatistics five_total = fivePart.stream().map(WishVo::getCount).map(Integer::parseInt).collect(Collectors.summarizingInt(Integer::intValue));
        result.put("five_avg", "五星平均出货抽数：" + (five == 0L ? "还未出金" : round(five_total.getAverage() / 100)));
        // up五星平均出货抽数
        result.put("five_avg_up", "up五星平均出货抽数：" + (five == 0L || up_five_num == 0L ? "还未出up五星" : round(five_total.getSum() / up_five_num * 1.0 / 100)));
        // 四星平均出货抽数
        Double four_avg = fourPart.stream().map(WishVo::getCount).map(Integer::parseInt).collect(Collectors.averagingInt((Integer::intValue)));
        result.put("four_avg", "四星平均出货抽数：" + (four == 0L ? "还未出紫" : round( four_avg / 100)));
        // 概览（多少抽未出紫，多少抽未出金）
        result.put("overview", getOverview(wishList));
        // 五星不歪率
        // 第一个是常驻角色，则少减一个
        int standard = 0;
        if (five != 0 && standardweapon.contains(fivePart.get(0).getName())) {
            standard = 1;
        }
        long notWaiRatio = five == 0L ? 0 : (five - 2 * (five - up_five_num) + standard) * 100 / (up_five_num + standard);
        result.put("not_wai_ratio", notWaiRatio + "%");
        // 五星已歪率
        result.put("wai_ratio", (100 - notWaiRatio) + "%");
        // 最大连续不歪数
        int notWai = continueNotWaiweaponNum(fivePart);
        result.put("continue_not_wai_num", notWai);
        // 最大连续不歪数
        int continue_wai_num = continueWaiweaponNum(fivePart);
        result.put("continue_wai_num", continue_wai_num);
        return result;
    }
    private String getOverview(List<WishVo> wishList) {
        int noGold = 0, noPurple = 0;
        boolean gold = false, purple = false;
        for (WishVo wishVo : wishList) {
            if (!"4".equals(wishVo.getRank_type()) && !purple) {
                noPurple ++;
            }
            if ("4".equals(wishVo.getRank_type())) {
                purple = true;
            }
            if (!"5".equals(wishVo.getRank_type()) && !gold) {
                noGold ++;
            }
            if ("5".equals(wishVo.getRank_type())) {
                gold = true;
            }
            if (gold && purple) {
                break;
            }
        }
        return "一共 " + wishList.size() + " 抽，已累计 " + noGold + " 抽未出金，累计 " + noPurple + " 抽未出紫";
    }
    private int continueWaiweaponNum(List<WishVo> fivePart) {
        Set<String> standardweapon = new HashSet<>(Arrays.asList("迪卢克", "琴", "莫娜", "刻晴", "七七", "提纳里"));
        if (fivePart == null || fivePart.isEmpty()) {
            return 0;
        }
        int max = 0;
        int count = 0;
        for (int i = fivePart.size() - 1; i >= 0; i--) {
            // 当前命中
            boolean wai = standardweapon.contains(fivePart.get(i).getName());
            if (wai) {
                count++;
                max = Math.max(max, count);
                i --;
            } else {
                count = 0;
            }
        }
        return max;
    }
    private int continueNotWaiweaponNum(List<WishVo> fivePart) {
        Set<String> standardweapon = new HashSet<>(Arrays.asList("迪卢克", "琴", "莫娜", "刻晴", "七七", "提纳里"));
        if (fivePart == null || fivePart.isEmpty()) {
            return 0;
        }
        int max = 0;
        int count = !standardweapon.contains(fivePart.get(fivePart.size() - 1).getName()) ? 1 : 0;
        for (int i = fivePart.size() - 2; i >= 0; i--) {
            // 当前命中
            boolean currentHit = !standardweapon.contains(fivePart.get(i).getName());
            // 上一个命中
            boolean lastHit = !standardweapon.contains(fivePart.get(i + 1).getName());
            if (currentHit && lastHit) {
                count ++;
            } else {
                max = Math.max(max, count);
                count =  0;
            }
        }
        return Math.max(max, count);
    }
    private String round(double num) {
        return String.format(Locale.CHINA, "%.1f", num * 100);
    }
    private List<WishVo> getweaponSequence(List<WishVo> wishList, int level) {
        Collections.reverse(wishList);
        int expend = 0;
        List<WishVo> sequence = new ArrayList<>();
        for (WishVo wishVo : wishList) {
            expend++;
            if ((level + "").equals(wishVo.getRank_type())) {
                wishVo.setCount(expend + "");
                sequence.add(wishVo);
                expend = 0;
            }
        }
        Collections.reverse(wishList);
        Collections.reverse(sequence);
        return sequence;
    }
    private SpannableStringBuilder getOverviewStyle(List<WishVo> wishVo, String overview) {
        SpannableStringBuilder overviewStyle = new SpannableStringBuilder(overview);
        int length = (wishVo.size() + "").length();
        int first = overview.indexOf("抽");
        int second = overview.indexOf("抽", first + 1);
        int third = overview.indexOf("抽", second + 1);

        overviewStyle.setSpan(new ForegroundColorSpan(Color.MAGENTA), 3, 3 + length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        overviewStyle.setSpan(new ForegroundColorSpan(Color.MAGENTA), 9 + length, second, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        overviewStyle.setSpan(new ForegroundColorSpan(Color.MAGENTA), second + 7, third, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return overviewStyle;
    }

    /**
     * 用来获取目前多少抽没出金
     * @param wishVo
     * @param overview
     * @return
     */
    private int getOverviewStyleforfive(List<WishVo> wishVo, String overview) {
        SpannableStringBuilder overviewStyle = new SpannableStringBuilder(overview);
        int first = overview.indexOf("抽");
        int second = overview.indexOf("抽", first + 1);
        int third = overview.indexOf("抽", second + 1);
        Log.d("2211",first+"q"+second+"a"+third);

        return second-first;
    }

    private int getOverviewfive(List<WishVo> wishList) {
        int noGold = 0, noPurple = 0;
        boolean gold = false, purple = false;
        for (WishVo wishVo : wishList) {
            if (!"4".equals(wishVo.getRank_type()) && !purple) {
                noPurple ++;
            }
            if ("4".equals(wishVo.getRank_type())) {
                purple = true;
            }
            if (!"5".equals(wishVo.getRank_type()) && !gold) {
                noGold ++;
            }
            if ("5".equals(wishVo.getRank_type())) {
                gold = true;
            }
            if (gold && purple) {
                break;
            }
        }
        return noGold;
    }

    public void setListViewHeight(ListView listView) {
        //获取listView的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        //listAdapter.getCount()返回数据项的数目
        for (int i = 0,len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() *  (listAdapter .getCount() - 1));
        listView.setLayoutParams(params);
    }
}

