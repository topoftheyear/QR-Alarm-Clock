package crundle.qralarmclock;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MockData {
    public static List<Alarm> getMockAlarms(){
        ArrayList<Alarm> alarms = new ArrayList<Alarm>();

        //IDK HOW TO GIT

        Alarm alarm1 = new Alarm();
        alarm1.setAlarmTime("3:00");
        alarms.add(alarm1);

        Alarm alarm2 = new Alarm();
        alarm2.setAlarmTime("12:30");
        alarms.add(alarm2);

        return alarms;
    }
}
