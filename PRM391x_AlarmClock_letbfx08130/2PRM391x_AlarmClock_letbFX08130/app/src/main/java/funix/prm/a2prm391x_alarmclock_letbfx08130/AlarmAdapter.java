package funix.prm.a2prm391x_alarmclock_letbfx08130;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import static funix.prm.a2prm391x_alarmclock_letbfx08130.AlarmService.sMediaPlayer;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {
    private List<Alarm> mAlarmList;
    final private Context mContext;
    private AlarmDatabase mAlarmDatabase;
    final private Activity mActivity;
    AlarmManager mAlarmManager;
    PendingIntent mPendingIntent;
    List<PendingIntent> mPendingList;

    public AlarmAdapter(Context context, Activity activity, List<Alarm> mAalarm) {
        this.mContext=context;
        this.mAlarmList=mAalarm;
        this.mActivity=activity;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from ( parent.getContext ( ) ).inflate ( R.layout.list_alarm, parent, false );
        return new AlarmViewHolder ( view );
    }

    @RequiresApi(api=Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        final Alarm alarm=mAlarmList.get ( position );
        if (alarm == null) {
            return;
        }
        holder.txtTime.setText ( alarm.getTime ( ) );
        holder.txtTitle.setText ( "Alarm Clock" + String.valueOf ( position + 1 ) );
        mAlarmDatabase=new AlarmDatabase ( mContext );
        setAlarm ( holder, position );

        updateAlarm ( holder, position );
        deleteAlarm ( holder, position );
    }

    /**
     * update alarm
     *
     *
     */

    private void updateAlarm(@NonNull AlarmViewHolder holder, int position) {
        holder.btnUpdate.setOnClickListener ( v -> showDialog ( position ) );
    }

    /**
     * delete aalrm
     *
     */
    private void deleteAlarm(@NonNull AlarmViewHolder holder, int position) {
        holder.btnDelete.setOnClickListener ( v -> {
            mAlarmDatabase.delete ( mAlarmList.get ( position ).getId ( ) );
            mAlarmList.remove ( position );
            notifyDataSetChanged ( );
        } );
    }

    @Override
    public int getItemCount() {
        if (mAlarmList != null) {
            return mAlarmList.size ( );
        }
        return 0;
    }

    public static class AlarmViewHolder extends RecyclerView.ViewHolder {
        final private TextView txtTime;
        final private TextView txtTitle;
        public final ToggleButton btnOnOff;
        final private AppCompatButton btnUpdate;
        final private AppCompatButton btnDelete;


        public AlarmViewHolder(@NonNull View itemView) {
            super ( itemView );
            txtTime=itemView.findViewById ( R.id.txtTime );
            txtTitle=itemView.findViewById ( R.id.txtTitle );
            btnOnOff=itemView.findViewById ( R.id.btnOnOff );
            btnUpdate=itemView.findViewById ( R.id.btnUpdate );
            btnDelete=itemView.findViewById ( R.id.btnDelete );

        }
    }

    /**
     * show dialog edit text for edit time purpose
     */
    public void showDialog(final int pos) {
        final EditText time;
        Button submit;
        //create dialog
        final Dialog dialog=getDialog ( );

        time=(EditText) dialog.findViewById ( R.id.editTime );
        submit=(Button) dialog.findViewById ( R.id.submit );

        time.setText ( mAlarmList.get ( pos ).getTime ( ) );

        submitBtnEvent ( pos, time, submit, dialog );
    }

    /**
     * submit even listener for showDialog method
     */
    private void submitBtnEvent(int pos, EditText time, Button submit, Dialog dialog) {
        submit.setOnClickListener ( v -> {
            if (time.getText ( ).toString ( ).isEmpty ( )) {
                time.setError ( "Please Enter Time" );
            } else {
                //updating
                mAlarmDatabase.updateAlarm ( time.getText ( ).toString ( ), mAlarmList.get ( pos ).getId ( ) );
                mAlarmList.get ( pos ).setTime ( time.getText ( ).toString ( ) );
                dialog.cancel ( );
                //notify list
                notifyDataSetChanged ( );
            }
        } );
    }

    /**
     * creat dialog for method showDialog

     */
    private Dialog getDialog() {
        final Dialog dialog=new Dialog ( mActivity );
        dialog.requestWindowFeature ( Window.FEATURE_NO_TITLE );
        WindowManager.LayoutParams params=new WindowManager.LayoutParams ( );
        dialog.setContentView ( R.layout.dialog );
        params.copyFrom ( dialog.getWindow ( ).getAttributes ( ) );
        params.height=WindowManager.LayoutParams.MATCH_PARENT;
        params.width=WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity=Gravity.CENTER;
        dialog.getWindow ( ).setAttributes ( params );
        dialog.getWindow ( ).setBackgroundDrawable ( new ColorDrawable ( Color.TRANSPARENT ) );
        dialog.show ( );
        return dialog;
    }

    /**
     * set alarm
     */

    @RequiresApi(api=Build.VERSION_CODES.N)
    public void setAlarm(AlarmAdapter.AlarmViewHolder holder, int position) {
        final Intent mPending=new Intent ( mContext, AlarmReceiver.class );
        // get data from sqlite
        mAlarmList=mAlarmDatabase.getAllAlarm ( );
        // create pending list for set multi alarm
        mPendingList=new ArrayList<> ( );
        for (int i=0; i < mAlarmList.size ( ); i++) {
            mPendingList.add ( null );
        }
        toogleBtnOnOffAlarmSetUp ( holder, position, mPending );
    }

    /**
     * set up on off alarm for setAlarm method
     */
    @RequiresApi(api=Build.VERSION_CODES.N)
    private void toogleBtnOnOffAlarmSetUp(AlarmViewHolder holder, int position, Intent mPending) {
        holder.btnOnOff.setOnCheckedChangeListener ( (buttonView, isChecked) -> {
            // On alarm
            if (isChecked) {
                alarmOnSetUp ( position, mPending );
                // Off alarm
            } else if (!isChecked) {
                alarmOffSetUp ( );
            }
        } );
    }

    /**
     * alarm off setup for setAlarm method
     */
    private void alarmOffSetUp() {
        try {
            mAlarmManager.cancel ( mPendingIntent );
            sMediaPlayer.stop ( );
            Log.e ( "TAG", "Cancelling all pending intents" );
        } catch (Exception e) {
            Log.e ( "TAG", "AlarmManager update was not canceled. " + e.toString ( ) );
        }
    }

    /**
     * alarm on setup for setAlarm method

     */
    @RequiresApi(api=Build.VERSION_CODES.N)
    private void alarmOnSetUp(int position, Intent mPending) {
        Log.d ( "TAG", "Alarm On" );
        //get time from list
        String setAlarmTime=mAlarmList.get ( position ).getTime ( );
        Log.d ( "TAG", "ALARM TIME" + setAlarmTime );
        Calendar mCalendar=Calendar.getInstance ( );
        //the time text is hh:mm.So use subtring to get hh and mm
        mCalendar.set ( java.util.Calendar.HOUR_OF_DAY, Integer.parseInt ( setAlarmTime.substring ( 0, 2 ) ) );
        mCalendar.set ( java.util.Calendar.MINUTE, Integer.parseInt ( setAlarmTime.substring ( 3, 5 ) ) );

        Log.d ( "TAG", String.valueOf ( mCalendar.get ( java.util.Calendar.HOUR_OF_DAY ) ) );
        Log.d ( "TAG", String.valueOf ( mCalendar.get ( java.util.Calendar.MINUTE ) ) );
        //start alarm manager
        mAlarmManager=(AlarmManager) mContext.getSystemService ( Context.ALARM_SERVICE );
        mPendingIntent=PendingIntent.getBroadcast (
                mContext, position, mPending, PendingIntent.FLAG_UPDATE_CURRENT
        );
        mPendingList.set ( position, mPendingIntent );
        mAlarmManager.set ( AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis ( ), mPendingList.get ( position ) );
        Log.d ( "TAGG", String.valueOf ( mCalendar.getTimeInMillis ( ) ) );
    }
}