package crundle.qralarmclock;

import android.media.Ringtone;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Alarm implements Comparable, Serializable {
    private String alarmTime;
    private boolean isActive;
    private int hour;
    private int min;
    private boolean AM;
    private String alarm_id;

    // 0 = Sun, 1 = Mon, 2 = Tues, etc.
    boolean[] daysActive = new boolean[]{false,false,false,false,false,false,false};


    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public void setHour(int a){
        hour = a;
    }

    public int getHour(){
        return hour;
    }

    public void setAM(boolean a){
        AM = a;
    }

    public boolean getAM(){
        return AM;
    }

    public void setMin(int a){
        min = a;
    }

    public int getMin(){
        return min;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean getDaysActive(int i) {
        return daysActive[i];
    }

    // i = day of the week.
    // 0 = Sun, 1 = Mon, 2 = Tues, etc.
    public void setDaysActive(int i) {
        if (daysActive[i] == false){
            daysActive[i] = true;
        } else {
            daysActive[i] = false;
        }
    }

    //returns 1 if this is after a, 0 if equal, and -1 if this is before a,
    @Override
    public int compareTo(Object o){
        Alarm a = (Alarm)o;
        String thisTime = alarmTime;
        String aTime = a.getAlarmTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = sdf.parse(thisTime);
            d2 = sdf.parse(aTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = d2.getTime() - d1.getTime();
        int result;
        if(difference > 0)
            result = -1;
        else if(difference < 0)
            result = 1;
        else
            result = 0;
        return result;
    }

    public String getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(String alarm_id) {
        this.alarm_id = alarm_id;
    }
}
