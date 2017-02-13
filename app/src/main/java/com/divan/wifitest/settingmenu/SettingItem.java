package com.divan.wifitest.settingmenu;

/**
 * Created by Димка on 06.01.2017.
 */

public abstract class SettingItem {
    protected boolean hasFocus=false;
    public enum Key{up,down,left,right,ok};
    public abstract void onClick(Key key);
    public abstract String getName();
    public abstract String getValue();
    public abstract String getColor();
    public abstract void setFocus(boolean isFocus);
}
