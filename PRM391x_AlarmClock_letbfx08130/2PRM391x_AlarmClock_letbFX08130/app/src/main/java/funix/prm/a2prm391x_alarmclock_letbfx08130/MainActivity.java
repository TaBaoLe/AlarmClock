package funix.prm.a2prm391x_alarmclock_letbfx08130;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE=0;
    private ImageView mAdd;
    private static List<Alarm> mAlarmList;
    private RecyclerView rcvAlarm;
    private String mTime;
    private static AlarmDatabase mAlarmDatabase;

    @RequiresApi(api=Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        mAlarmDatabase=new AlarmDatabase ( this );
        setToolbar ( );
        init ( );
        addAlarm ( );
        displayAlarm ( );
    }


    private void init() {
        mAdd=findViewById ( R.id.toolbar_logo );
        mAlarmList=new ArrayList<> ( );
        rcvAlarm=findViewById ( R.id.rcvAlarmlist );
    }
// add new alarm
    private void addAlarm() {
        mAdd.setOnClickListener ( v -> {
            Intent intent=new Intent ( MainActivity.this, Alarm_Activity.class );
            startActivityForResult ( intent, REQUEST_CODE );
        } );
    }
// set tool bar
    private void setToolbar() {
        Toolbar mToolbar=findViewById ( R.id.toolbar );
        this.setSupportActionBar ( mToolbar );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            mTime=data.getStringExtra ( "time" );
            Toast.makeText ( this, mTime, Toast.LENGTH_SHORT ).show ( );

            mAlarmDatabase.addAlarm ( mTime);
            displayAlarm ( );
        }
    }

    //display alarm list
    public void displayAlarm() {
        mAlarmList=new ArrayList<> ( mAlarmDatabase.getAllAlarm ( ) );
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager ( MainActivity.this );
        rcvAlarm.setLayoutManager ( linearLayoutManager );
        AlarmAdapter adapter=new AlarmAdapter ( getApplicationContext ( ), this, mAlarmList );
        rcvAlarm.setAdapter ( adapter );
    }

}