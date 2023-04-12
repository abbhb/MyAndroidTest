package com.example.myapplicationtest.novel.model.httpModel;

import com.renrui.libraries.model.baseObject.BaseHttpModel;
import com.example.myapplicationtest.novel.constant.ConstantInterFace;

public class InterFaceBaseHttpModel extends BaseHttpModel {

    @Override
    public String getUrl() {
        return "";
    }

    public String getInterFaceStart() {
        return ConstantInterFace.getInterfaceDomain() + "open/api/";
    }
}