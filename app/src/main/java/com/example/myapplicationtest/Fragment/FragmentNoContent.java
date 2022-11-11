package com.example.myapplicationtest.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplicationtest.CommUtil;
import com.example.myapplicationtest.R;
import com.example.myapplicationtest.entity.WishVo;
import com.example.utils.GsonUtil;

import java.util.List;

public class FragmentNoContent extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myfragment_no_content, null);
        return view;
    }

}
