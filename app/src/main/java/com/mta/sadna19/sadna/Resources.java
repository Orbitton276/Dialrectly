package com.mta.sadna19.sadna;

import android.content.Context;

public class Resources {
    private static Resources instance = null;
    Context m_context;
    private Resources() {
    }

    public static Resources getInstance() {
        if(instance == null) {
            instance = new Resources();
        }
        return instance;
    }

    public String getString(int i_id) {
        return m_context.getString(i_id);
    }
}
