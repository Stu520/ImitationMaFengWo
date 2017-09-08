package com.imitationmafengwo.utils;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.Map;
import java.util.Set;

public class SharePreferenceWrapper implements SharedPreferences {
    private SharedPreferences sp;
    private EditorWrapper mEditorWrapper;

    private class EditorWrapper implements Editor {

        private Editor mEditor;

        public EditorWrapper(Editor edit) {
            mEditor = edit;
        }

        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void apply() {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                commit();
            } else {
                mEditor.apply();
            }
        }

        @Override
        public Editor clear() {

            mEditor.clear();
            return this;
        }

        @Override
        public boolean commit() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                mEditor.commit();
                return true;
            } else {
                mEditor.apply();
                return true;
            }
        }

        @Override
        public Editor putBoolean(String key, boolean value) {

            mEditor.putBoolean(key, value);

            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {


            mEditor.putFloat(key, value);
            return this;
        }

        @Override
        public Editor putInt(String key, int value) {

            mEditor.putInt(key, value);
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {

            mEditor.putLong(key, value);
            return this;
        }

        @Override
        public Editor putString(String key, String value) {

            mEditor.putString(key, value);
            return this;
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public Editor putStringSet(String key, Set<String> value) {
            mEditor.putStringSet(key, value);
            return this;
        }

        @Override
        public Editor remove(String key) {

            mEditor.remove(key);
            return this;
        }

    }

    public SharePreferenceWrapper(SharedPreferences sp) {
        this.sp = sp;
    }


    @Override
    public boolean contains(String key) {
        return sp.contains(key);
    }

    @Override
    public Editor edit() {
        if (mEditorWrapper == null) {
            mEditorWrapper = new EditorWrapper(sp.edit());

        } else {
            mEditorWrapper.mEditor = sp.edit();
        }

        return mEditorWrapper;
    }

    @Override
    public Map<String, ?> getAll() {
        return sp.getAll();
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {

        return sp.getBoolean(key, defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {

        return sp.getFloat(key, defValue);
    }

    @Override
    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    @Override
    public long getLong(String key, long defValue) {

        return sp.getLong(key, defValue);
    }

    @Override
    public String getString(String key, String defValue) {


        return sp.getString(key, defValue);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public Set<String> getStringSet(String arg0, Set<String> arg1) {
        return sp.getStringSet(arg0, arg1);
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        sp.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        sp.unregisterOnSharedPreferenceChangeListener(listener);
    }

}
