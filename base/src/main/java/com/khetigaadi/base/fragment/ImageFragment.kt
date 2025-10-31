package com.khetigaadi.base.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.khetigaadi.base.R
import com.khetigaadi.base.databinding.FragmentImageBinding
import com.khetigaadi.base.model.Banner

class ImageFragment : Fragment() {
    private var url: String? = null
    private var binding: FragmentImageBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            url = requireArguments().getString(ARG_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageBinding.inflate(inflater, container, false)
        val view: View = binding!!.getRoot()
        Glide.with(this)
            .load(url)
            .placeholder(R.mipmap.no_image)
            .error(R.mipmap.no_image)
            .into(binding!!.ivImage)
        //binding.ivImage
        view.setOnClickListener { v: View? ->
            if (onClickListener != null) onClickListener!!.onClick(
                v
            )
        }
        return view
    }

    companion object {
        private const val ARG_PARAM = "url"
        private var onClickListener: View.OnClickListener? = null
        fun newInstance(banner: Banner, onClickListener: View.OnClickListener?): ImageFragment {
            Companion.onClickListener = onClickListener
            val fragment = ImageFragment()
            val args = Bundle()
            args.putString(ARG_PARAM, banner.imageUrl)
            fragment.setArguments(args)
            return fragment
        }

        @JvmStatic
        fun newInstance(banner: Banner, onClickListener: View.OnClickListener): Fragment {
            Companion.onClickListener = onClickListener
            val fragment = ImageFragment()
            val args = Bundle()
            args.putString(ARG_PARAM, banner.imageUrl)
            fragment.setArguments(args)
            return fragment
        }
    }
}