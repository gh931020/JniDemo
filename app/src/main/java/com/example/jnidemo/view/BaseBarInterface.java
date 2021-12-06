package com.example.jnidemo.view;

public interface BaseBarInterface {

    void setStrMinMax(String min, String max);

    int getProcess();

    void setProcess(int process);

    void reduceProcess(boolean flag);

    void addProcess(boolean flag);

    void setValueShow(String str);

    int getMax();

    void setMax(int max);

    void setIntValue(int baseValue, int unit, int max);

    int getIntValue();

    void setFloatValue(float unit_f, int baseValue, int unit, int max);

    boolean isEnabled();

    void setEnabled(boolean enabled);

}
