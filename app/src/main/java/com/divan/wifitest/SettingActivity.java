package com.divan.wifitest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.divan.wifitest.settingmenu.DateSetting;
import com.divan.wifitest.settingmenu.SettingItem;
import com.divan.wifitest.settingmenu.SpecialSetting;

import java.util.Vector;

public class SettingActivity extends Activity {
    public static final int UiSetting= View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
    final String SettingFolder="LiftApp",settingFile="setting.txt",GONG="gong.wav",EXIT="Применить",DEFAULT="По умолчанию",STATION="Станции";
    Vector<SettingItem> settingItems;
    public Setting setting;

    ListView listView;
    TextView name,value;
    //int indexSelected=-1;
    // boolean isSelect=false;
    View selectedView;
    SettingItem curItem;
    CharSequence curText=null;
    byte byteToSend=0;

    Context context;
    boolean isAsync=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//        getWindow().getDecorView().setSystemUiVisibility(UiSetting);

        setting=new Setting(getString(R.string.SettingFolder),getString(R.string.settingFile));   //getString(R.string.SettingFolder),getString(R.string.settingFile)

        // получаем экземпляр элемента ListView
        listView= (ListView)findViewById(R.id.listView);
        name=(TextView)findViewById(R.id.textName);
        name.setClickable(true);
        value=(TextView)findViewById(R.id.textValue);

        name.setBackgroundColor(Color.WHITE);
        value.setBackgroundColor(Color.WHITE);

        name.setTextColor(Color.BLACK);
        value.setTextColor(Color.BLACK);

//        name.setBackgroundResource(R.drawable.up);
//        value.setBackgroundResource(R.drawable.down);

        InitSettingItems();
        FillList();
        setListener();

        onListItemClick(0);
        context=this;

        updateView();

    }


    private void InitSettingItems(){
        settingItems=new Vector<>();
        settingItems.add(setting.LayOutBackGraundColor);
        settingItems.add(setting.textColorHex);
        settingItems.add(setting.textFragmentColor);
        settingItems.add(setting.iconColor);
        settingItems.add(setting.NumberSize);
        settingItems.add(setting.TextDateSize);
        settingItems.add(setting.TextInfoSize);
        settingItems.add(setting.TextMassageSize);
        settingItems.add(setting.textFragmenSize);
        settingItems.add(setting.sizeTextSetting);
        settingItems.add(setting.sizeOfBuffer);
        settingItems.add(setting.indexBAUDRATE);
        settingItems.add(setting.volumeDay);
        settingItems.add(setting.volumeNight);
        settingItems.add(setting.accessMusic);
        settingItems.add(setting.accessVideo);
        settingItems.add(setting.year);
        settingItems.add(setting.month);
        settingItems.add(setting.day);
        settingItems.add(setting.hour);
        settingItems.add(setting.min);

        settingItems.add(new SpecialSetting(SpecialSetting.TypeSpecialItem.STATION,this,STATION));
        settingItems.add(new SpecialSetting(SpecialSetting.TypeSpecialItem.DEFAULT,this,DEFAULT));
        settingItems.add(new SpecialSetting(SpecialSetting.TypeSpecialItem.INSTRUCTION,this,"Инструкция"));
        settingItems.add(new SpecialSetting(SpecialSetting.TypeSpecialItem.EXIT,this,EXIT));


        ;
    }
    private void FillList(){
// определяем массив типа String
        String[] catNames = new String[settingItems.size()];
        for(int i=0;i<settingItems.size();i++){
            catNames[i]=settingItems.elementAt(i).getName();
        }

// используем адаптер данных
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, catNames){
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);

            /*YOUR CHOICE OF COLOR*/
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(setting.sizeTextSetting.value);

                return view;

            }
        };

        listView.setAdapter(adapter);
    }

    private void setListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                listView.smoothScrollToPosition(position);

                if(curItem!=null) {
                    curItem.setFocus(false);
                }
                //indexSelected = position;
                curItem= settingItems.elementAt(position);
                curItem.setFocus(true);

                updateView();

                curText=((TextView)itemClicked).getText();
                selectedView=itemClicked;
                focusItem(itemClicked);
                //itemClicked.setBackgroundColor(Color.BLUE);

            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                curItem.onClick(SettingItem.Key.up);
                updateView();

                if(curText.toString()==DEFAULT) {
                    MakeDefaultSetting();
                }
                else if(curText.toString()==EXIT)
                    Exit();

            }
        });
        value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                curItem.onClick(SettingItem.Key.down);
                updateView();


                if(curText.toString()==DEFAULT) {
                    MakeDefaultSetting();
                }
                else if(curText.toString()==EXIT)
                    Exit();

            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //focusItem(selected);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                focusItem(selectedView);
            }
        });
}
    private void focusItem(View view){
        for(int i=0;i<listView.getCount();i++)
        {
            View a= listView.getChildAt(i);
            if(a!=null) {
                a.setBackgroundColor(Color.WHITE);
                if(((TextView)a).getText()==curText)
                    a.setBackgroundColor(Color.YELLOW);
            }
        }

    }
    private void updateView(){
        if(curItem!=null) {
            name.setText(curItem.getName());
            value.setText(curItem.getValue());
            String color = curItem.getColor();
            Drawable drawable= value.getBackground();
            if (color != null) {
//                drawable.setColorFilter((int) Long.parseLong(color, 16), PorterDuff.Mode.SRC_OUT);
                value.setBackgroundColor((int) Long.parseLong(color, 16));
            } else {
//                drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_OUT);
                value.setBackgroundColor(Color.WHITE);
            }
            value.setBackground(drawable);
            setSizeSetting();//bad idea
        }
    }
    private void setSizeSetting(){
        for(View e:listView.getTouchables()){
            ((TextView)e).setTextSize(setting.sizeTextSetting.value);

        }
        name.setTextSize(setting.sizeTextSetting.value);
        value.setTextSize(setting.sizeTextSetting.value);
    }

    public void onListItemClick(int pos) {

        int activePosition = pos; // первый элемент списка
        listView.performItemClick(listView.getAdapter().
                getView(activePosition, null, null), activePosition, listView.getAdapter().
                getItemId(activePosition));
    }
    @Override
    protected void onResume() {
        super.onResume();
        DateSetting.StopTime();
        setting.StartRead();
        setSizeSetting();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setting.WriteSetting();
    }

    public void Exit(){
        setting.WriteSetting();
        Intent intent = new Intent();
        intent.setData(setting.getUri());
        setResult(RESULT_OK, intent);
        finish();
    }
    public void MakeDefaultSetting(){
        setting.InitDefault();
        InitSettingItems();
        value.setText("Установлены значения по умолчанию");
        Toast.makeText(context, "make default", Toast.LENGTH_LONG);
    }


}

