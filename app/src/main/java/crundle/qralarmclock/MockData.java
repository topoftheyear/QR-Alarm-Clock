package crundle.qralarmclock;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/*
 * This is just a temporary class for testing.
 * It will eventually get replaced by proper files n stuff.
 */
public class MockData {
    public static List<Alarm> getMockAlarms(){
        ArrayList<Alarm> alarms = new ArrayList<Alarm>();

        //IDK HOW TO GIT

        Alarm alarm1 = new Alarm();
        alarm1.setAlarmTime("3:00");
        alarm1.setActive(true);
        alarms.add(alarm1);

        Alarm alarm2 = new Alarm();
        alarm2.setAlarmTime("12:30");
        alarm2.setActive(false);
        alarms.add(alarm2);


        return alarms;
    }
}
