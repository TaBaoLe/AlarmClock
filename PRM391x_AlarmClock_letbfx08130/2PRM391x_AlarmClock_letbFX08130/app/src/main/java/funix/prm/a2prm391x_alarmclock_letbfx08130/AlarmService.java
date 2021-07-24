package funix.prm.a2prm391x_alarmclock_letbfx08130;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class AlarmService extends Service {
    static MediaPlayer sMediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e ( "TAG", "START SERVICE" );
        //receive data from AlarmReceiver
        sMediaPlayer=MediaPlayer.create ( this, R.raw.senorita );
        sMediaPlayer.start ( );
        Toast.makeText ( this, "alarm start ringing", Toast.LENGTH_SHORT ).show ( );
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy ( );
        sMediaPlayer.stop ( );

    }
}
