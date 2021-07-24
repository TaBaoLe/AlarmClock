package funix.prm.a2prm391x_alarmclock_letbfx08130;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent receiveIntent=new Intent ( context, AlarmService.class );
        context.startService ( receiveIntent );
        Log.e ( "TAG", "BROADCAST RUNNING" );
    }
}
