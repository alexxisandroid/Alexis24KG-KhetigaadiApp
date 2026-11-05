package com.khetigaadi.commons.adapter
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.khetigaadi.commons.fragments.TabCompare
import com.khetigaadi.implement.compare.CompareImpSelection
import com.khetigaadi.tractor.compare.CompareTractorSelection
class CompareTabFragmentAdapter(fm:TabCompare?,private val items:List<String>):FragmentStateAdapter(fm!!) {
    override fun createFragment(position:Int):Fragment {
        println("bvnvncbcvbvcbc===="+position+"=="+items.size)
        return if(position == 1) {
            CompareImpSelection()
        }
        else CompareTractorSelection()
    }
    override fun getItemCount():Int {
        return items.size
    }
}