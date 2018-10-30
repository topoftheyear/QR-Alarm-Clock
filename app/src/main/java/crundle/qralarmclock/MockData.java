package crundle.qralarmclock;

import android.content.Context;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/*
 * This is just a temporary class for testing.
 * It will eventually get replaced by proper files n stuff.
 */
public class MockData {
    private static ArrayList<Alarm> alarms = new ArrayList<Alarm>();
    private static int i = 0;


    public static List<Alarm> getMockAlarms(Context context) throws IOException, ClassNotFoundException {
        File directory = context.getFilesDir();
        File file = new File(directory, "AlarmList.txt");
        FileInputStream fi = new FileInputStream(file);
        ObjectInputStream oi = new ObjectInputStream(fi);
        Boolean keepGoing = true;
        try{
            while(keepGoing){
                alarms.add((Alarm) oi.readObject());
            }
        }
        catch (EOFException e) {
            keepGoing = false;
        }

        return alarms;
    }

    private static void generateMockAlarms() {
        if (i == 0) {
            Alarm alarm1 = new Alarm();
            alarm1.setAlarmTime("3:00");
            alarm1.setActive(true);
            alarms.add(alarm1);

            Alarm alarm2 = new Alarm();
            alarm2.setAlarmTime("12:30");
            alarm2.setActive(false);
            alarms.add(alarm2);

            i++;
        }
    }

    public void addAlarmToMockData(Alarm alarm) {
        alarms.add(alarm);
    }
}
