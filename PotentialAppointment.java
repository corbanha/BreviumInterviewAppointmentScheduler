public class PotentialAppointment {
    private String day;
    private int time;
    private int doctorId;
    private int requestId;

    public PotentialAppointment(String day, int time, int doctorId, int requestId) {
        this.day = day;
        this.time = time;
        this.doctorId = doctorId;
        this.requestId = requestId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
}
