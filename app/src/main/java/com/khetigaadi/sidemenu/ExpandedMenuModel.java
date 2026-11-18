package com.khetigaadi.sidemenu;
import com.khetigaadi.def.DrawerMenu;
public class ExpandedMenuModel {
    String iconName = "";
    int iconImg = -1; // menu icon resource id
    int name;
//    public ExpandedMenuModel() {
//
//    }
    public ExpandedMenuModel(@DrawerMenu int name,String iconName,int iconImg) {
        this.name = name;
        this.iconName = iconName;
        this.iconImg = iconImg;
    }
    public String getIconName() {
        return iconName;
    }
//    public void setIconName(String iconName) {
//        this.iconName = iconName;
//    }
    public int getIconImg() {
        return iconImg;
    }
//    public void setIconImg(int iconImg) {
//        this.iconImg = iconImg;
//    }
    public @DrawerMenu
    int getName() {
        return name;
    }
    public void setName(int name) {
        this.name = name;
    }
}