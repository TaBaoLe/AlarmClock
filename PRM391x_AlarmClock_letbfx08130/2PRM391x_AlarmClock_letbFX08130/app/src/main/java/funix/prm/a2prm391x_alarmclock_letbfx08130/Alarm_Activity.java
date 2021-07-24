package funix.prm.a2prm391x_alarmclock_letbfx08130;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TimePicker;

import java.util.Calendar;

public class Alarm_Activity extends AppCompatActivity {
    private TimePicker mTimPicker;
    private AppCompatButton mBtnAdd;
    private Calendar mCalendar;
    private ImageView mIconPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_alarm_ );
        mTimPicker=findViewById ( R.id.timePicker );
        mBtnAdd=findViewById ( R.id.btnAdd );
        mIconPlus=findViewById ( R.id.toolbar_logo );

        setToolbar ( );
        sendDataToMain ( );
    }

    private void sendDataToMain() {
        mBtnAdd.setOnClickListener ( v -> {
            mCalendar=Calendar.getInstance ( );
            mCalendar.set ( Calendar.HOUR_OF_DAY, mTimPicker.getCurrentHour ( ) );
            mCalendar.set ( Calendar.MINUTE, mTimPicker.getCurrentMinute ( ) );
            // get time from time picker
            int hour=mTimPicker.getCurrentHour ( );
            int minute=mTimPicker.getCurrentMinute ( );
            String getHour=String.valueOf ( hour );
            String getMinute=String.valueOf ( minute );
            // add o to set format hh:mm
            if (getHour.length ( ) == 1) {
                getHour="0" + getHour;
            }
            if (getMinute.length ( ) == 1) {
                getMinute="0" + getMinute;
            }
            Intent intent=new Intent ( );
            String time=getHour + ":" + getMinute;
            intent.putExtra ( "time", time );
            setResult ( RESULT_OK, intent );
            finish ( );
        } );
    }

    /**
     * tool bar
     */
    private void setToolbar() {
        Toolbar mToolbar=findViewById ( R.id.toolbar );
        mToolbar.setTitle ( "Alarm" );
        mToolbar.setNavigationIcon ( R.drawable.icon_back );
        this.setSupportActionBar ( mToolbar );
        mIconPlus.setVisibility ( View.INVISIBLE );
    }

    /**
     * back to main

     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId ( ) == android.R.id.home) {
            finish ( );
        }
        return super.onOptionsItemSelected ( menuItem );
    }
}