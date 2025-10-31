package com.khetigaadi.commons.fragments;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.activities.OnRoadPriceActivity;
import com.khetigaadi.commons.utils.Constants;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
public abstract class BaseFragment extends Fragment {
    private FragmentActivity activity;
    private ProgressDialog progressDialog;
    private RapidFloatingActionLayout rapidFloatingActionLayout;
    protected RapidFloatingActionHelper rfabHelper;
    protected RapidFloatingActionContentLabelList rfaContent;
    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
        activity = getActivity();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (!Constants.isConnectionAvailable(activity)) {
                ((MainActivity) activity).noInternetConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return super.onCreateView(inflater,container,savedInstanceState);
    }
    public void removeFabButton() {
        if (rapidFloatingActionLayout != null)
            rapidFloatingActionLayout.setVisibility(View.GONE);
    }
    public void addFabLayout(List<RFACLabelItem> items) {
        rapidFloatingActionLayout = (RapidFloatingActionLayout) this.getLayoutInflater().inflate(R.layout.rapid_floating_action_layout,null,false);
        activity.addContentView(rapidFloatingActionLayout,new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
        initFabButtons(items);
    }
    private void initFabButtons(List<RFACLabelItem> items) {
        rfaContent = new RapidFloatingActionContentLabelList(getActivity());
        setOnRapidFloatingActionContentLabelListListener(rfaContent);
        rfaContent.setItems(items).setIconShadowColor(0xff888888);
        rfabHelper = new RapidFloatingActionHelper(getActivity(),rapidFloatingActionLayout,rapidFloatingActionLayout.findViewById(R.id.activity_main_rfab),rfaContent).build();
    }
    protected void setOnRapidFloatingActionContentLabelListListener(RapidFloatingActionContentLabelList rfaContent) {
        rfaContent.setOnRapidFloatingActionContentLabelListListener(new RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener() {
            @Override
            public void onRFACItemLabelClick(int position,RFACLabelItem item) {
                fabAction(position);
            }
            @Override
            public void onRFACItemIconClick(int position,RFACLabelItem item) {
                fabAction(position);
            }
        });
    }
    protected void fabAction(int position) {
        switch (position) {
            case 1: {
                try {
                    startActivity(new Intent(getActivity(),OnRoadPriceActivity.class));
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                rfabHelper.toggleContent();
                break;
            }
            case 0: {
                onWhatsAppClick(getActivity());
                rfabHelper.toggleContent();
                break;
            }
            default: {
                rfabHelper.toggleContent();
            }
        }
    }
    protected void addFabButtonItems() {
        addFabLayout(initFabItems());
    }

    private List<RFACLabelItem> initFabItems() {
        ArrayList<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel(getString(R.string.whatsApp))
                .setResId(R.drawable.whats_app)
                .setIconNormalColor(ContextCompat.getColor(activity, R.color.black))
                .setLabelColor(ContextCompat.getColor(activity, R.color.black))
                .setWrapper(items.size())
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel(getString(R.string.getOnRoadPrice))
                .setResId(R.drawable.ic_rupees)
                .setLabelColor(ContextCompat.getColor(activity, R.color.black))
                .setIconNormalColor(ContextCompat.getColor(activity, R.color.black))
                .setWrapper(items.size())
        );
        return items;
    }

    protected void addFabButtonItems(ArrayList<RFACLabelItem<Integer>> newItems) {
        List<RFACLabelItem> items = initFabItems();
        if (newItems != null)
            items.addAll(newItems);
        addFabLayout(items);
    }

    protected void onWhatsAppClick(FragmentActivity activity) {
        try {
            PackageManager packageManager = activity.getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone=" + getString(R.string.whatsAppNo) + "&text=" + URLEncoder.encode("Hello KhetiGaadi", "UTF-8");
//            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                this.startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPause() {
        removeFabButton();
        super.onPause();
    }

    protected void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }
        if (!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    protected void showOrUpdateProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }

        if (!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(message);
            progressDialog.show();
//        }
        } else progressDialog.setMessage(message);
    }

    protected void dismissProgressDialog() {
        // For handling orientation changed.
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
