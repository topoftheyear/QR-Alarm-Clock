package crundle.qralarmclock;

import java.util.List;

public class Alarm {
    private String alarmTime;
    private boolean isActive;

    // 0 = Sun, 1 = Mon, 2 = Tues, etc.
    boolean[] daysActive = new boolean[7];

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
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
}
