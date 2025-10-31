package com.khetigaadi.sidemenu;

import com.khetigaadi.def.DrawerSubMenu;

public class ExpandedSubMenu {

    String iconName = "";
    int name;

    public ExpandedSubMenu() {

    }

    public ExpandedSubMenu(@DrawerSubMenu int name, String iconName) {
        this.name = name;
        this.iconName = iconName;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }


    public @DrawerSubMenu
    int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }
}