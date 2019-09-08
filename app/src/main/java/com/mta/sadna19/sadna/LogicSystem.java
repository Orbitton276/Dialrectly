package com.mta.sadna19.sadna;

import android.util.Log;

import com.mta.sadna19.sadna.Adapter.OptionAdapter;
import com.mta.sadna19.sadna.MenuRegisters.Option;
import java.util.LinkedList;

public class LogicSystem {
    public static final String TAG = "$LogicSystem$";
    private LinkedList<Option> m_selectedOptionsArr = new LinkedList<>();
    private BackListener m_onBackSelectedListener;
    private SelectedListener m_onOptionSelectedListener;
    private LastOptionListener m_onLastOptionListener;


   public LogicSystem(Option iOption){
       addOptionToSelectedOpsArr(iOption);
   }

    public void Back(Option i_op)
    {
        m_selectedOptionsArr.removeLast();
        if(m_onBackSelectedListener != null)
            m_onBackSelectedListener.onBackSelected();
    }
    public void SelectedOption(Option i_op)
    {
        addOptionToSelectedOpsArr(i_op);
        if(m_onOptionSelectedListener != null)
            m_onOptionSelectedListener.onOptionSelected(i_op);
        if(isLastOption(i_op) == true && m_onLastOptionListener != null)
            m_onLastOptionListener.onLastOption();
    }
    public String GetAllKeysString()
    {
        String allPressedKeys = "";
        for (Option op : m_selectedOptionsArr)
        {
            allPressedKeys = allPressedKeys + op.pressKeys();
            Log.e(TAG, String.format("%s, ",op.pressKeys()));

        }
        Log.e(TAG, String.format("GetAllKeysString => %s",allPressedKeys));
        return allPressedKeys;
    }
    private void addOptionToSelectedOpsArr(Option i_op)
    {
        m_selectedOptionsArr.addLast(i_op);
    }
    public Option GetLastOption()
    {
        return m_selectedOptionsArr.getLast();
    }
    private boolean isLastOption(Option i_op)
    {
        if (i_op.getSubMenu() == null){
            return true;
        }
        if (i_op.getSubMenu().isEmpty()){
            return true;
        }
        return false;
    }

    public void setOnBackSelectedListener(BackListener i_BackListener){
        m_onBackSelectedListener = i_BackListener;
    }
    public void setOnOptionSelectedListener(SelectedListener i_SelectListener)
    {
        m_onOptionSelectedListener = i_SelectListener;
    }
    public void setOnLastOptionListener(LastOptionListener i_LastOptionListener)
    {
        m_onLastOptionListener = i_LastOptionListener;
    }

    public interface BackListener
    {
        void onBackSelected();
    }
    public interface SelectedListener
    {
        void onOptionSelected(Option i_op);
    }
    public interface LastOptionListener
    {
        void onLastOption();
    }
}
