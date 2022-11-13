package com.example.myapplicationtest.ListViewClass;

public class ItemCard {
    private String name;
    private String imgUrl;
    private Integer questionImg;
    private int nums;
    private boolean isWai;
    private int imgType;
    public final static int QUESTION_IMG = 504;
    public final static int W_IMG = 504;


    /***
     *
     * @param name 角色名，保留备用
     * @param imgUrl 角色照片
     * @param nums 角色抽卡的数量
     * @param isWai 是否显示歪
     */
    public ItemCard(String name, String imgUrl, int nums, boolean isWai) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.nums = nums;
        this.isWai = isWai;
        this.imgType = W_IMG;
    }

    public ItemCard(String name, Integer imgUrl, int nums, boolean isWai, int imgType) {
        this.name = name;
        this.questionImg = imgUrl;
        this.nums = nums;
        this.isWai = isWai;
        this.imgType = imgType;
    }

    public Integer getQuestionImg() {
        return questionImg;
    }

    public void setQuestionImg(Integer questionImg) {
        this.questionImg = questionImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgType() {
        return imgType;
    }

    public void setImgType(int imgType) {
        this.imgType = imgType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public boolean isWai() {
        return isWai;
    }

    public void setWai(boolean wai) {
        isWai = wai;
    }
}
