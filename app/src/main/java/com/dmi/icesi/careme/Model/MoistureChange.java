package com.dmi.icesi.careme.Model;

/**
 * Created by Paula on 29/11/2017.
 */

public class MoistureChange {

    public int humidity;
    public int hour, minute;
    public int changeState;

    public MoistureChange() {

    }

    public MoistureChange(int humidity, int hour, int minute) {
        this.humidity = humidity;
        this.hour = hour;
        this.minute = minute;
    }

    public int getMoisturePercentage() {
        return humidity;
    }

    public void sethumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getChangeState() {
        return changeState;
    }

    public void setChangeState(int changeState) {
        this.changeState = changeState;
    }
}
