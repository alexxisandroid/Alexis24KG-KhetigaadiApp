package com.khetigaadi.sidemenu;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.khetigaadi.R;
import java.util.HashMap;
import java.util.List;
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private final Context mContext;
    private final List<ExpandedMenuModel> mListDataHeader; // header titles
    // child data in format of header title, child title
    private final HashMap<ExpandedMenuModel,List<ExpandedSubMenu>> mListDataChild;
    ExpandableListView expandList;
    public ExpandableListAdapter(Context context,List<ExpandedMenuModel> listDataHeader,HashMap<ExpandedMenuModel,List<ExpandedSubMenu>> listChildData,ExpandableListView mView) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
        this.expandList = mView;
    }
    @Override
    public int getGroupCount() {
        return mListDataHeader.size();
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        int childCount = 0;
        if(groupPosition != 0) {
            List<ExpandedSubMenu> expandedSubMenus = this.mListDataChild.get(this.mListDataHeader.get(groupPosition));
            if(expandedSubMenus != null)
                childCount = expandedSubMenus.size();
        }
        return childCount;
    }
    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }
    @Override
    public Object getChild(int groupPosition,int childPosition) {
        List<ExpandedSubMenu> expandedSubMenus = this.mListDataChild.get(this.mListDataHeader.get(groupPosition));
        if (expandedSubMenus != null)
            return expandedSubMenus.get(childPosition).getIconName();
        else
            return null;
    }
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    @Override
    public long getChildId(int groupPosition,int childPosition) {
        return childPosition;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public View getGroupView(int groupPosition,boolean isExpanded,View convertView,ViewGroup parent) {
        ExpandedMenuModel headerTitle = (ExpandedMenuModel)getGroup(groupPosition);
//      if (convertView == null) {
        LayoutInflater inflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView =  inflater.inflate(R.layout.listheader,null);
//      }
        TextView lblListHeader = convertView.findViewById(R.id.submenu);
        ImageView headerIcon = convertView.findViewById(R.id.iconimage);
        ImageView indicator = convertView.findViewById(R.id.indicator);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle.getIconName());
        headerIcon.setImageResource(headerTitle.getIconImg());
        Log.d("cvbvbcbcv====",""+headerTitle.getIconName()+"=="+headerTitle.getIconImg());
       /* if(headerTitle.getIconName().equalsIgnoreCase("Shop")) {
            lblListHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_new, 0);
            headerIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);
            lblListHeader.setTypeface(null, Typeface.NORMAL);
        } else*/
        if(headerTitle.getIconName().equalsIgnoreCase("KhetiGuru")) {
            lblListHeader.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
            lblListHeader.setTypeface(null, Typeface.BOLD);
            headerIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        else {
            lblListHeader.setTypeface(null, Typeface.NORMAL);
            lblListHeader.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        }
        if(getChildrenCount(groupPosition) == 0) {
            indicator.setVisibility(View.INVISIBLE);
        }
        else {
            indicator.setVisibility(View.VISIBLE);
            indicator.setImageResource(isExpanded ? R.drawable.group_expanded : R.drawable.group_closed);
            Log.d("cvbcbcvbcb1111222222====",""+getChildrenCount(groupPosition)+"=="+isExpanded);
        }
            Log.d("cvbcbcvbcb====",""+getChildrenCount(groupPosition));
        return convertView;
    }
    @Override
    public View getChildView(final int groupPosition,final int childPosition,boolean isLastChild,View convertView,final ViewGroup parent) {
        final String childText = (String)getChild(groupPosition,childPosition);
//        if (convertView == null) {
        LayoutInflater infalInflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.list_submenu,null);
//        }
        TextView txtListChild = (TextView)convertView.findViewById(R.id.submenu);
        txtListChild.setText(childText);
        return convertView;
    }
    @Override
    public boolean isChildSelectable(int groupPosition,int childPosition) {
        return true;
    }
}