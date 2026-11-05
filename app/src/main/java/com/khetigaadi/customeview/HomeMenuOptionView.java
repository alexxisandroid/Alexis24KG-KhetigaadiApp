package com.khetigaadi.customeview;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import androidx.core.content.res.ResourcesCompat;
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
        //int tclr =   a.getColor(R.styleable.HomeMenuOptions_android_textColor,android.R.color.white);
        int style = a.getInt(R.styleable.HomeMenuOptions_customTextStyle,1);
        int fontFamilyId = a.getResourceId(R.styleable.HomeMenuOptions_android_fontFamily,0);
        a.recycle();
        binding = ViewHomeMenuOptionsBinding.inflate(LayoutInflater.from(context),this,true);
        binding.tvLabel.setText(titleText);
        binding.tvLabel.setTextColor(Color.WHITE);
        binding.tvLabel.setTextSize(14f);
        binding.tvLabel.setAllCaps(true);
        // binding.tvLabel.caps
        // binding.tvLabel.setGravity(View.TEXT_ALIGNMENT_CENTER);
        binding.tvLabel.setSingleLine();
        // binding.tvLabel.setTextColor(tclr);
        binding.tvLabel.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL),style);
        if(fontFamilyId > 0) {
            binding.tvLabel.setTypeface(ResourcesCompat.getFont(getContext(),R.font.manrope_medium));
        }
        binding.parent.getBackground().setTint(valueColor);
        binding.ivImage.setImageResource(image);
        // setTypeface(getTypeface(),Typeface.BOLD);
    }
    public HomeMenuOptionView(Context context) {
        this(context,null);
    }
    public void setViewBackgroundColor(int color)
    {
        binding.parent.getBackground().setTint(color);
    }
    public void setImageVisible(boolean visible) {
        binding.ivImage.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}

