package funix.prm.a2prm391x_alarmclock_letbfx08130;


public class Alarm {
    private String id;
    private String time;

    public Alarm() {
    }


    public Alarm(String id, String time) {
        this.id=id;
        this.time=time;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id=id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time=time;
    }
}
