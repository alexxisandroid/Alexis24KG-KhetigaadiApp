package com.khetigaadi.customeview;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.khetigaadi.R;
import com.khetigaadi.databinding.ViewHomeMenuOptionsBinding;
public class HomeMenuOptionView extends LinearLayout {
    ViewHomeMenuOptionsBinding binding;
    public HomeMenuOptionView(Context context,AttributeSet attrs) {
        super(context,attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.HomeMenuOptions,0,0);
        String titleText = a.getString(R.styleable.HomeMenuOptions_titleText);
        @SuppressWarnings("ResourceAsColor")
        int valueColor = a.getColor(R.styleable.HomeMenuOptions_viewBackgroundColor,android.R.color.holo_blue_light);
        int image = a.getResourceId(R.styleable.HomeMenuOptions_iconImage,R.mipmap.no_image);
        a.recycle();
        binding = ViewHomeMenuOptionsBinding.inflate(LayoutInflater.from(context),this,true);
        binding.tvLabel.setText(titleText);
        binding.parent.getBackground().setTint(valueColor);
        binding.ivImage.setImageResource(image);
    }
    public HomeMenuOptionView(Context context) {
        this(context, null);
    }
    public void setViewBackgroundColor(int color) {
        binding.parent.getBackground().setTint(color);
    }
    public void setImageVisible(boolean visible) {
        binding.ivImage.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}

