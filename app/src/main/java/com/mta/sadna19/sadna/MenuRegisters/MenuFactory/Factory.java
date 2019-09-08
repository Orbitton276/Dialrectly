package com.mta.sadna19.sadna.MenuRegisters.MenuFactory;

import android.content.Context;
import android.graphics.Path;
import android.util.Log;

import com.mta.sadna19.sadna.MenuRegisters.DataOption;
import com.mta.sadna19.sadna.MenuRegisters.Menu;
import com.mta.sadna19.sadna.MenuRegisters.Option;
import com.mta.sadna19.sadna.R;
import com.mta.sadna19.sadna.Resources;

public class Factory {
    Context m_context;
    public static final String TAG = "Factory>>";

    public class MenuBluePrint{
        Option m_Option;
        public MenuBluePrint AddSubMenu(MenuBluePrint... i_SubMenus){
            for (MenuBluePrint menu : i_SubMenus) {
                this.m_Option.getSubMenu().add(menu.m_Option);
            }
            return this;
        }

        public MenuBluePrint Skip(){
            return AddPostKeys(",%23");
        }
        public MenuBluePrint AddPostKeys(String iKeys){
            m_Option.setPostKeys(m_Option.getPostKeys() + iKeys );
            return this;
        }
        public MenuBluePrint Wait(){
            return  AddPostKeys("");
        }
        private MenuBluePrint(Option i_Option){
            Log.e(TAG, "MenuBluePrint() >>");
            this.m_Option = i_Option;
            Log.e(TAG, "MenuBluePrint() << " + i_Option.getName() + "was added");
        }
        public Option GetOption(){
            return m_Option;
        }

        public MenuBluePrint ChangePostKeys(String iPostKeys) {
            m_Option.setPostKeys(iPostKeys);
            return this;
        }
    }
    public MenuBluePrint CreateOption(String i_number, String i_name){
        Log.e(TAG, "CreateOption() >>");
        MenuBluePrint newOption = new MenuBluePrint(new Option(i_number,i_name));
        Log.e(TAG, "CreateOption() <<");
        return newOption;
    }
    public MenuBluePrint CreateDataOption(String i_name, String i_type, String i_endTab){
        Log.e(TAG, "CreateDataOption() >>");
        MenuBluePrint newOption = new MenuBluePrint(new DataOption(i_name, i_type, i_endTab));
        Log.e(TAG, "CreateDataOption() <<");
        return newOption;
    }
}
