package com.khetigaadi.user.myAddedTractor
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.analytics.Tracker
import com.khetigaadi.BuildConfig
import com.khetigaadi.MainActivity
import com.khetigaadi.R
import com.khetigaadi.agri.activity.ImageViewActivity
import com.khetigaadi.base.model.MyResponse
import com.khetigaadi.base.util.StateData
import com.khetigaadi.commons.SharedPreferenceUtils
import com.khetigaadi.commons.fragments.BaseFragment
import com.khetigaadi.databinding.DeleteTrFeedbackFormDialogBinding
import com.khetigaadi.databinding.RecycleListBinding
import com.khetigaadi.retrofit.KhetiAPIs
import com.khetigaadi.tractor.rent.RentTractorDetails
import com.khetigaadi.user.SessionManager
import com.khetigaadi.user.model.User
import com.khetigaadi.user.profile.ProfileOtherActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
@AndroidEntryPoint
class MyAddedTractorList:BaseFragment() {
    private var param1:String? = null
    private var param2:String? = null
    private var mAdapter:MyAddedTractorListAdapter? = null
    private var activity: FragmentActivity? = null
    private var filter:String? = null
    @Inject
    lateinit var mTracker:Tracker
    @Inject
    lateinit var user:User
  //  @Inject
  //  lateinit var usedTractorRepository:MyAddedTractorRepository
    @Inject
    lateinit var khetiAPIs:KhetiAPIs
    private var binding:RecycleListBinding? = null
    @Inject
    lateinit var sessionManager:SessionManager
    @Inject
    lateinit var pref:SharedPreferenceUtils
    private var viewModel:MyAddedTractorListViewModel? = null
    var disposable:CompositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        activity = requireActivity()
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(inflater:LayoutInflater,container:ViewGroup?,savedInstanceState:Bundle?):View? {
        // Inflate the layout for this fragment
        binding = RecycleListBinding.inflate(getLayoutInflater(), container, false)

        Glide.with(requireActivity())
            .load(R.drawable.loader_white)
            .into(binding!!.ivLoader)
        val mLayoutManager = LinearLayoutManager(activity)
        binding!!.myRecyclerView.setLayoutManager(mLayoutManager)
        binding!!.filterIcon.visibility = View.GONE
        return binding!!.getRoot()
    }
    override fun onViewCreated(view:View,savedInstanceState:Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        viewModel = ViewModelProvider(this)[MyAddedTractorListViewModel::class.java];
        viewModel?.addedTractors?.observe(viewLifecycleOwner) { rentTractorRespStateData ->
            when(rentTractorRespStateData.status) {
                StateData.Status.LOADING -> {
                    binding?.llProgress?.visibility = View.VISIBLE
                }
                StateData.Status.SUCCESS -> {
                    binding?.llProgress?.visibility = View.GONE
                    println("nbcvcvb==="+ rentTractorRespStateData.data?.data)
                    rentTractorRespStateData.data?.let { data ->
                        mAdapter = MyAddedTractorListAdapter(activity,data.data,data.others)
                        binding?.myRecyclerView?.adapter = mAdapter
                        mAdapter?.setOnItemClickListener { position,v ->
                            val intent = Intent(activity,RentTractorDetails::class.java)
                            intent.putExtra("tractorId",mAdapter!!.getItem(position).tractorId)
                           // startActivity(intent)
                            var ltdfvd = ArrayList<String>()
                            for(i in 0 until mAdapter!!.getItem(position).images.size) {
                               // fm.popBackStackImmediate()
                                val strr = mAdapter!!.getItem(position).images[i].name
                                ltdfvd.add(BuildConfig.BASE_URL+"images/owner_data/"+strr)
                            }
                            initImagesAdapter(ltdfvd)
                        }
                        mAdapter?.setOnTrashClickListener { position, v ->
                            showNewDeleteDialog(mAdapter!!.getItem(position).tractorId)
                        }
                    }
                }
                StateData.Status.ERROR -> {
                    binding?.llProgress?.visibility = View.GONE
                    println("cvbcbcv==="+rentTractorRespStateData.error)
                    Toast.makeText(activity,rentTractorRespStateData.error, Toast.LENGTH_SHORT).show()
                }
                StateData.Status.CREATED -> {}
            }
        }
    }
    private fun initImagesAdapter(urls: List<String>?) {
        val intent = Intent(activity, ImageViewActivity::class.java)
        intent.putStringArrayListExtra("urls", urls as ArrayList<String>?)
        startActivity(intent)
    }
    private fun init() {


//        mAdapter.setOnInterestedBuyersClickListener(IntrestedBuyersListner { position:Int,v:View? ->
//            if(mAdapter?.getItem(position).isBuyersOpen()) {
//                mAdapter?.getItem(position).setInterestedBuyers(null)
//                mAdapter?.getItem(position).setBuyersOpen(false)
//                mAdapter?.notifyItemChanged(position)
//            }
//            else {
//                getInterestedBuyers(position, mAdapter?.getItem(position).getId())
//                mAdapter?.getItem(position).setBuyersOpen(true)
//            }
//        })
//        mAdapter.setOnSoldClickListener(MySoldClickListener { position:Int,v:View? ->
//            changeSoldStatus(mAdapter?.getItem(position).getId(),mAdapter.getItem(position).getSoldUnsoldStatus(),position)
//        })
//        mAdapter.setOnChatClickListener(MyTrashClickListener { position:Int,v:View? -> })
//
    }
    private fun showNewDeleteDialog(id:String?) {
        // get prompts.xml view
        val d = Dialog(requireActivity())
        d.requestWindowFeature(Window.FEATURE_NO_TITLE)
        d.setCancelable(false)
        d.setCanceledOnTouchOutside(false)
        val dialogBinding = DeleteTrFeedbackFormDialogBinding.inflate(getLayoutInflater())
        d.setContentView(dialogBinding.getRoot())
        d.show()
        // set prompts.xml to be the layout file of the alertdialog builder
        // create an alert dialog
        dialogBinding.btnClose.setOnClickListener({ v ->
            d.dismiss()
            if (activity is ProfileOtherActivity) (activity as ProfileOtherActivity).hideSoftKeyboard()
            else (activity as MainActivity).hideSoftKeyboard()
        })
        dialogBinding.btnSubmit.setOnClickListener({ v ->
            if(dialogBinding.spReasonType.getSelectedItem().toString() != "Select Reason") {
                if(dialogBinding.tvReason.getText().toString().length !== 0) {
                    try {
                        val params:MutableMap<String?,Any?> = java.util.HashMap<String?,Any?>()
                        params.put("rent_tractor_id",id)
                        params.put("delete_request_type_id",dialogBinding.spReasonType.selectedItemPosition)
                        params.put("reason",dialogBinding.tvReason.getText().toString())
                        params.put("ttype","myAddedTractor")
                        disposable.add(
                            khetiAPIs.rentTrDeleteReq(params)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(object : DisposableSingleObserver<MyResponse>() {
                                    override fun onSuccess(myResponse: MyResponse) {
                                        try {
                                            if (!myResponse.isError) {
                                                myResponse.msg.forEach { message ->
                                                    Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
                                                }
                                                d.dismiss()
                                                if (activity is ProfileOtherActivity) {
                                                    (activity as ProfileOtherActivity).hideSoftKeyboard()
                                                } else if (activity is MainActivity) {
                                                    (activity as MainActivity).hideSoftKeyboard()
                                                }
                                            } else {
                                                myResponse.msg.forEach { message ->
                                                    Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
                                                }
                                                d.dismiss()
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }

                                    override fun onError(e: Throwable) {
                                        Timber.e(e, "onError: ")
                                    }
                                })
                        )
                    }
                    catch(e:java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
                else {
                    Toast.makeText(activity,"Please enter Reason",Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(activity,"Please Select Reason.",Toast.LENGTH_SHORT).show()
            }
        })
    }
    override fun onStop() {
        super.onStop()
    }

//    private fun getInterestedBuyers(position:Int,id:String?) {
//        khetiAPIs.getInterestedBuyers(id)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object :DisposableSingleObserver<InterestedBuyersResp?>() {
//                override fun onSuccess(buyersResp:InterestedBuyersResp) {
//                    try {
//                        if(!buyersResp.isError) {
//                            mAdapter?.getItem(position).setInterestedBuyers(buyersResp.data)
//                            mAdapter?.notifyItemChanged(position)
//                        }
//                        else {
//                            for(i in buyersResp.msg.indices) {
//                                Toast.makeText(activity,buyersResp.msg[i],Toast.LENGTH_LONG).show()
//                            }
//                        }
//                    }
//                    catch(e:Exception) {
//                        e.printStackTrace()
//                    }
//                }
//                override fun onError(e:Throwable) {
//                    Timber.e("response Error: %s",e)
//                    binding!!.llProgress.visibility = View.GONE
//                }
//            })
//    }
//    private fun changeSoldStatus(id:String,soldStatus:String?,position:Int) {
//        try {
//            val params: MutableMap<String?, Any?> = HashMap<String?,Any?>()
//            params.put("used_tractor_id", id.toInt())
//            khetiAPIs.changeSoldStatusOfTr(params)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object:DisposableSingleObserver<MyResponse?>() {
//                    override fun onSuccess(myResponse: MyResponse) {
//                        try {
//                            if(!myResponse.isError) {
//                                for(i in myResponse.msg.indices) {
//                                    if(myResponse.msg[i].equals("Sold Successfully",ignoreCase = true)) {
//                                        mAdapter?.getItem(position).setSoldUnsoldStatus("1")
//                                    }
//                                    else {
//                                        mAdapter?.getItem(position).setSoldUnsoldStatus("0")
//                                    }
//                                }
//                            }
//                            else {
//                                for(i in myResponse.msg.indices) {
//                                    Toast.makeText(activity,myResponse.msg[i],Toast.LENGTH_LONG).show()
//                                }
//                            }
//                            mAdapter?.notifyItemChanged(position)
//                        }
//                        catch(e:Exception) {
//                            e.printStackTrace()
//                        }
//                    }
//                    override fun onError(e:Throwable) {
//                        Timber.e("response Error: %s", e)
//                        mAdapter?.notifyItemChanged(position)
//                    }
//                })
//        }
//        catch(e:Exception) {
//            e.printStackTrace()
//        }
//    }
    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1:String,param2:String) =
            MyAddedTractorList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}