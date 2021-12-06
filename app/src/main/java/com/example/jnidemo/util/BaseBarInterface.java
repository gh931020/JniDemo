package com.example.jnidemo.util;

public interface BaseBarInterface {

    void setStrMinMax(String min, String max);
    int getProcess();

    void setProcess(int process);

    void reduceProcess();

    void addProcess();
    void addProcess(int idx); // 20180416

    int getMax();

    void setMax(int max);

    void setIntValue(int baseValue, int unit, int max);

    int getIntValue();

    void setFloatValue(float unit_f, int baseValue, int unit, int max);

    float getFloatValue();

    boolean isEnabled();

    void setEnabled(boolean enabled);


}
