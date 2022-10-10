import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AppointmentInfo extends AppointmentBase implements Comparable<AppointmentInfo> {
  private int doctorId;
  private String appointmentTime;
  private long milliseconds;

  public AppointmentInfo(int personId, boolean isNew, int doctorId, String appointmentTime) {
    super(personId, isNew);
    this.doctorId = doctorId;
    this.appointmentTime = appointmentTime;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    try {
      milliseconds = df.parse(appointmentTime).getTime();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  public int getDoctorId() {
    return doctorId;
  }

  public void setDoctorId(int doctorId) {
    this.doctorId = doctorId;
  }

  public String getAppointmentTime() {
    return appointmentTime;
  }

  public void setAppointmentTime(String appointmentTime) {
    this.appointmentTime = appointmentTime;
    // TODO update milliseconds
  }

  public long getMilliseconds() {
    return milliseconds;
  }

  public String toString() {
    return String.format("%s, doctorId: %d, time: %s", super.toString(), doctorId, appointmentTime);
  }

  @Override
  public int compareTo(AppointmentInfo b) {
    if (b.getMilliseconds() == milliseconds) {
      return 0;
    }
    return b.getMilliseconds() > milliseconds ? -1 : 1;
  }
}
