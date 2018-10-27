package crundle.qralarmclock;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {
    private final List<Alarm> alarms;

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
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    protected static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        Switch swEnabled;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvTime = itemView.findViewById(R.id.tv_alarm_time);
            this.swEnabled = itemView.findViewById(R.id.sw_enabled);
        }
    }
}
