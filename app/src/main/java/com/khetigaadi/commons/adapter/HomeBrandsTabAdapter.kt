package com.khetigaadi.commons.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.khetigaadi.commons.fragments.HomeTractorBrandsFragment
import com.khetigaadi.def.BrandType

class HomeBrandsTabAdapter(fragment: Fragment?, private val items: List<String>) : FragmentStateAdapter(fragment!!) {
    override fun createFragment(position: Int): Fragment {
        return if (position == 1) HomeTractorBrandsFragment.newInstance(BrandType.IMPLEMENT) else HomeTractorBrandsFragment.newInstance(BrandType.NEW_TRACTOR)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}