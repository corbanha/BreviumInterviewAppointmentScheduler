import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class AppointmentRequest extends AppointmentBase implements Comparable<AppointmentRequest> {
    private int requestId;
    private String[] preferredDays;
    private int[] preferredDocs;

    private int availableSpots;
    private long nextAvailableAppointment;
    private long[] availableAppointments;

    private final int HOUR = 60 * 60 * 1000;
    private final int MILLIS_HOUR_ADDS[] = new int[] { 8 * HOUR, 9 * HOUR, 10 * HOUR, 11 * HOUR, 12 * HOUR, 13 * HOUR,
            14 * HOUR, 15 * HOUR, 16 * HOUR };
    private final int NEW_APPOINTMENT_SPOTS = 2;

    public AppointmentRequest(
            int personId,
            boolean isNew,
            int requestId,
            String[] preferredDays,
            int[] preferredDocs) {
        super(personId, isNew);
        this.requestId = requestId;
        this.preferredDays = preferredDays;
        this.preferredDocs = preferredDocs;

        calcAvailableAppointments();
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String[] getPreferredDays() {
        return preferredDays;
    }

    public void setPreferredDays(String[] preferredDays) {
        this.preferredDays = preferredDays;
    }

    public int[] getPreferredDocs() {
        return preferredDocs;
    }

    public void setPreferredDocs(int[] preferredDocs) {
        this.preferredDocs = preferredDocs;
    }

    public int getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(int availableSpots) {
        this.availableSpots = availableSpots;
    }

    public long getNextAvailableAppointment() {
        return nextAvailableAppointment;
    }

    private void calcAvailableAppointments() {
        ArrayList<Long> appointmentSpots = new ArrayList<>();
        // note: assumes preferredDays is sorted
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        for (String preferredDay : preferredDays) {
            try {
                long millis = df.parse(preferredDay).getTime();
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                cal.setTimeInMillis(millis);

                // no scheduling on weekends
                if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
                        || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                    continue;
                }

                // Only scheduling in November and December
                if (cal.get(Calendar.MONTH) != Calendar.NOVEMBER && cal.get(Calendar.MONTH) != Calendar.DECEMBER) {
                    continue;
                }

                // Only scheduling in 2021
                if (cal.get(Calendar.YEAR) != 2021) {
                    continue;
                }

                for (int i = (getIsNew() ? MILLIS_HOUR_ADDS.length - NEW_APPOINTMENT_SPOTS
                        : 0); i < MILLIS_HOUR_ADDS.length; i++) {
                    appointmentSpots.add(millis + MILLIS_HOUR_ADDS[i]);
                }

            } catch (ParseException exception) {
                System.out.println(exception.getMessage());
            }
        }
        availableAppointments = new long[appointmentSpots.size()];
        for (int i = 0; i < appointmentSpots.size(); i++) {
            availableAppointments[i] = appointmentSpots.get(i);
        }
        availableSpots = availableAppointments.length;
        calcNextAvailableAppointment();
    }

    public void calcNextAvailableAppointment(long after) {
        if (availableAppointments.length == 0 || after + 1 > availableAppointments[availableAppointments.length - 1]) {
            nextAvailableAppointment = -1;
        }
        long nextApt = availableAppointments[0];
        for (int i = 1; i < availableAppointments.length && after + 1 > nextApt; i++) {
            nextApt = availableAppointments[i];
        }
        nextAvailableAppointment = nextApt;
    }

    public void calcNextAvailableAppointment() {
        calcNextAvailableAppointment(0);
    }

    public String toString() {
        String preferredDaysString = String.format("%s", String.join(", ", preferredDays));
        String preferredDoctors = "";
        for (int i = 0; i < preferredDocs.length; i++) {
            if (i != 0) {
                preferredDoctors += ", ";
            }
            preferredDoctors += Integer.toString(preferredDocs[i]);
        }
        return String.format("%s, requestId: %d, days: [%s], docs: [%s], spots: %d, nextApt: %d", super.toString(),
                requestId,
                preferredDaysString, preferredDoctors, availableSpots, nextAvailableAppointment);
    }

    @Override
    public int compareTo(AppointmentRequest b) {
        if (nextAvailableAppointment == b.getNextAvailableAppointment()) {
            return availableSpots - b.getAvailableSpots();
        } else {
            return (nextAvailableAppointment > b.getNextAvailableAppointment() ? 1 : -1);
        }
    }
}
