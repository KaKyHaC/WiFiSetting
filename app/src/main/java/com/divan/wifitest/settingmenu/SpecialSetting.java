package com.divan.wifitest.settingmenu;

import com.divan.wifitest.SettingActivity;

/**
 * Created by Димка on 11.01.2017.
 */

public class SpecialSetting extends SettingItem {
    public enum TypeSpecialItem{EXIT,DEFAULT,STATION,INSTRUCTION};
    public static final String[] stationNames={"ШУЛМ","ШК6000","УЭЛ","УЛ"};
    public int indexCurStation=0;
    private int indexBufStation=indexCurStation;

    TypeSpecialItem typeSpecialItem;
    SettingActivity activitySetting;//TODO bad idea
    String Name;

    public SpecialSetting(TypeSpecialItem typeSpecialItem, SettingActivity activitySetting, String name) {
        this.typeSpecialItem = typeSpecialItem;
        this.activitySetting = activitySetting;
        Name = name;
    }


    @Override
    public void onClick(Key key) {
        if(hasFocus){
            if (typeSpecialItem== TypeSpecialItem.STATION)
            {
                if(indexCurStation<stationNames.length)
                    indexCurStation+=stationNames.length;
                if(indexBufStation<stationNames.length)
                    indexBufStation+=stationNames.length;
                switch (key) {
                    case up:indexCurStation++;indexBufStation=indexCurStation;
                        activitySetting.setting.indexCurStation = indexCurStation;
                        break;
                    case down:indexCurStation--;indexBufStation=indexCurStation;
                        activitySetting.setting.indexCurStation = indexCurStation;
                        break;
                    case left:indexBufStation--;
                        break;
                    case right:indexCurStation++;
                        break;
                }


            }
            switch (key)
            {
                case ok:
                case up:
                case down:
                    switch (typeSpecialItem){
                        case EXIT:
                            activitySetting.Exit();
                            break;
                        case DEFAULT:activitySetting.MakeDefaultSetting();
                            break;
                        case STATION:
                            indexCurStation=indexBufStation;
//                            activitySetting.SendByte((byte)(indexCurStation%stationNames.length+1));
                            break;
                        case INSTRUCTION:
                            break;
                    }
                    break;

            }
        }
    }

    @Override
    public String getName() {
        return Name;
    }

    @Override
    public String getValue() {
        if(typeSpecialItem== TypeSpecialItem.STATION){
            return stationNames[indexBufStation%stationNames.length];
        }
        else  return "-";
    }

    @Override
    public String getColor() {
        return null;
    }

    @Override
    public void setFocus(boolean isFocus) {
        hasFocus=isFocus;
        if(typeSpecialItem== TypeSpecialItem.STATION) {
            if (isFocus)
                indexCurStation = activitySetting.setting.indexCurStation;//TODO it's bad code
            if (!isFocus)
                activitySetting.setting.indexCurStation = indexCurStation;
            indexBufStation = indexCurStation;
        }
    }

    @Override
    public String toString() {
        return  Name ;
    }
}
