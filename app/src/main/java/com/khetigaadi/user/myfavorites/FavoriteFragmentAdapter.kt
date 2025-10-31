package com.khetigaadi.user.myfavorites
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
class FavoriteFragmentAdapter internal constructor(fm: MyFavoritesTabs?, private val items: List<String>) : FragmentStateAdapter(fm!!) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> MyFavoritesUsedTrList.getInstance()
            2 -> MyFavoritesRentTrList.getInstance()
            else -> MyFavoritesNewTrList.getInstance()
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }
}