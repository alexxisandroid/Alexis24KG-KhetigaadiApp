package com.khetigaadi
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.khetigaadi.bottombar.BottomSheetListItemAdapter
import com.khetigaadi.databinding.FragmentBottomMenuListDialogListDialogBinding
import com.khetigaadi.def.CallFrom
import com.khetigaadi.model.NameAndImage
import com.khetigaadi.tractor.dealer.DealerShipActivity
class BottomMenuListDialogFragment:BottomSheetDialogFragment() {
    private  val  SHOP: String = "Shop"
    private val DEALERSHIP: String = "Dealership"
    private val NEWS: String = "News"
    private val IMPLEMENTS: String = "Implements"
    private val SELL_TRACTOR: String = "Sell Tractor"
    private val COMPARE = "Compare"
    private var _binding: FragmentBottomMenuListDialogListDialogBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater:LayoutInflater,container:ViewGroup?,savedInstanceState:Bundle?):View {
        _binding = FragmentBottomMenuListDialogListDialogBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view:View,savedInstanceState:Bundle?) {
        val items = ArrayList<NameAndImage>()
        items.add(NameAndImage(SHOP,R.drawable.ic_shop_new))
        items.add(NameAndImage(NEWS,R.drawable.ic_news_new))
        items.add(NameAndImage(DEALERSHIP,R.drawable.ic_dealership))
        items.add(NameAndImage(COMPARE,R.drawable.ic_compare_tractor_new))
        items.add(NameAndImage(SELL_TRACTOR,R.drawable.ic_sell_new))
        items.add(NameAndImage(IMPLEMENTS,R.drawable.ic_implement_new))
        val adapter = BottomSheetListItemAdapter(items, requireContext(),object:BottomSheetListItemAdapter.ClickListener {
                override fun onItemClick(position:Int,view:View) {
                    when (items[position].name) {
                        COMPARE ->
                            (activity as MainActivity).navigateToCompareFragment(CallFrom.NEW_TRACTOR)
                        SELL_TRACTOR ->
                            (activity as MainActivity).navigateToSellTractor()
                        IMPLEMENTS ->
                            (activity as MainActivity).navigateToImplementFragment()
                        SHOP ->
                            (activity as MainActivity).navigateToShop()
                        NEWS ->
                            (activity as MainActivity).navigateToNewsFragment()
                        DEALERSHIP ->
                            startActivity(Intent(activity,DealerShipActivity::class.java))
                    }
                    dismiss()
                }
            })
        binding.list.adapter = adapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        fun newInstance():BottomMenuListDialogFragment = BottomMenuListDialogFragment()
    }
}