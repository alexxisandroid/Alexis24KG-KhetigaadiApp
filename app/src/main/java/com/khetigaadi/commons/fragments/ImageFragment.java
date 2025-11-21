package com.khetigaadi.commons.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.databinding.FragmentImageBinding;
public class ImageFragment extends Fragment {
    private static final String ARG_PARAM = "url";
    private static View.OnClickListener onClickListener;
    private String url;
    private FragmentImageBinding binding;
    public ImageFragment() {
    }
    public static ImageFragment newInstance(String url,View.OnClickListener onClickListener) {
        ImageFragment.onClickListener = onClickListener;
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, url);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            url = getArguments().getString(ARG_PARAM);
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentImageBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        Glide.with(this)
                .load(url)
                .placeholder(R.mipmap.no_image)
                .error(R.mipmap.no_image)
                .into(binding.ivImage);
        view.setOnClickListener(v -> {
            if(onClickListener != null)
                onClickListener.onClick(v);
        });
        return view;
    }
}