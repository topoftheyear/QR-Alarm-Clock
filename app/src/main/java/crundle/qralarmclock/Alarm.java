package crundle.qralarmclock;

import java.util.List;

public class Alarm {
    private String alarmTime;
    private boolean isActive;
    private List<String> daysActive;

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

    public List<String> getDaysActive() {
        return daysActive;
    }

    public void setDaysActive(List<String> daysActive) {
        this.daysActive = daysActive;
    }
}
