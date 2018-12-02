package crundle.qralarmclock;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {
    private List<Alarm> alarms;

    public AlarmAdapter(List<Alarm> alarms) {
        this.alarms = alarms;
    }

    @NonNull
    @Override
    public  AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder alarmViewHolder, int i) {
        alarmViewHolder.tvTime.setText(alarms.get(i).getAlarmTime());
        alarmViewHolder.swEnabled.setChecked(alarms.get(i).isActive());
        boolean[] daysList = alarms.get(i).daysActive;
        for (int j = 0; j < alarmViewHolder.days.getChildCount(); j++) {
            View v = alarmViewHolder.days.getChildAt(j);
            if(daysList[j])
                ((TextView) v).setTextColor(Color.parseColor("#FF0000"));
            else
                ((TextView) v).setTextColor(Color.parseColor("#800000"));
        }
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    protected static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        Switch swEnabled;
        LinearLayout days;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvTime = itemView.findViewById(R.id.tv_alarm_time);
            this.swEnabled = itemView.findViewById(R.id.sw_enabled);
            this.days = itemView.findViewById(R.id.linearLayout3);
        }
    }

    public static List<Alarm> getAlarms(Context context) throws IOException, ClassNotFoundException {
        ArrayList<Alarm> alarms = new ArrayList<Alarm>();
        File directory = context.getFilesDir();
        File file = new File(directory, "AlarmList.txt");
        Boolean keepGoing = true;
        FileInputStream fi = null;
        ObjectInputStream oi = null;
        try{
            fi = new FileInputStream(file);
            oi = new ObjectInputStream(fi);
            while(keepGoing){
                alarms.add((Alarm) oi.readObject());
            }
        }
        catch (EOFException e) {
            keepGoing = false;
        }
        if(fi != null)
            fi.close();
        if(oi != null)
            oi.close();


        return alarms;
    }
}
