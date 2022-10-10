import java.util.ArrayList;
import java.util.Arrays;

public class LocalServer implements ServerInterface {
    /*
     * This is a dummy server implementation just to verify locally that things are
     * working
     * and so that we don't spam the database while testing
     */
    private ArrayList<AppointmentInfo> schedule;
    private AppointmentRequest[] appointments = {
            new AppointmentRequest(1, true, 0,
                    new String[] { "2021-11-01T00:00:00Z", "2021-11-03T00:00:00Z" },
                    new int[] { 2 }),
            new AppointmentRequest(1, false, 1,
                    new String[] { "2021-11-05T00:00:00Z", "2021-11-06T00:00:00Z",
                            "2021-11-24T00:00:00Z",
                            "2021-11-25T00:00:00Z" },
                    new int[] { 2, 3 }),
            new AppointmentRequest(2, true, 2, new String[] { "2021-11-03T00:00:00Z" }, new int[] { 1 }),
            new AppointmentRequest(3, false, 3,
                    new String[] { "2021-11-04T00:00:00Z", "2021-11-05T00:00:00Z" },
                    new int[] { 1, 2, 3 }),
            new AppointmentRequest(3, false, 4,
                    new String[] { "2021-11-18T00:00:00Z", "2021-11-19T00:00:00Z",
                            "2021-11-22T00:00:00Z" },
                    new int[] { 2, 3 }),
            new AppointmentRequest(4, false, 5,
                    new String[] { "2021-11-22T00:00:00Z", "2021-11-23T00:00:00Z",
                            "2021-11-24T00:00:00Z",
                            "2021-11-25T00:00:00Z",
                            "2021-11-26T00:00:00Z", "2021-11-29T00:00:00Z",
                            "2021-11-30T00:00:00Z",
                            "2021-12-01T00:00:00Z",
                            "2021-12-02T00:00:00Z", "2021-12-03T00:00:00Z" },
                    new int[] { 3 }),
            new AppointmentRequest(4, false, 6,
                    new String[] { "2021-11-22T00:00:00Z", "2021-11-23T00:00:00Z",
                            "2021-11-24T00:00:00Z",
                            "2021-11-25T00:00:00Z",
                            "2021-11-26T00:00:00Z", "2021-11-29T00:00:00Z",
                            "2021-11-30T00:00:00Z",
                            "2021-12-01T00:00:00Z",
                            "2021-12-02T00:00:00Z", "2021-12-03T00:00:00Z" },
                    new int[] { 3 }),
            new AppointmentRequest(5, false, 7,
                    new String[] { "2021-11-18T00:00:00Z", "2021-11-19T00:00:00Z" },
                    new int[] { 2, 3 }),
            new AppointmentRequest(6, false, 8,
                    new String[] { "2021-12-06T00:00:00Z", "2021-12-07T00:00:00Z" },
                    new int[] { 2 }),
            new AppointmentRequest(10, true, 9, new String[] { "2021-11-30T00:00:00Z" }, new int[] { 1 }),
            new AppointmentRequest(10, false, 10,
                    new String[] { "2021-12-23T00:00:00Z", "2021-12-24T00:00:00Z" },
                    new int[] { 1 }),
            new AppointmentRequest(12, true, 11,
                    new String[] { "2021-12-02T00:00:00Z", "2021-12-03T00:00:00Z" },
                    new int[] { 1 }),
            new AppointmentRequest(13, true, 12,
                    new String[] { "2021-12-02T00:00:00Z", "2021-12-03T00:00:00Z" },
                    new int[] { 1 }),
            new AppointmentRequest(16, false, 13,
                    new String[] { "2021-12-15T00:00:00Z", "2021-12-22T00:00:00Z" },
                    new int[] { 3 }),
            new AppointmentRequest(16, false, 14,
                    new String[] { "2021-12-27T00:00:00Z", "2021-12-28T00:00:00Z",
                            "2021-12-29T00:00:00Z",
                            "2021-12-30T00:00:00Z" },
                    new int[] { 3 }),
            new AppointmentRequest(19, true, 15,
                    new String[] { "2021-12-14T00:00:00Z", "2021-12-15T00:00:00Z",
                            "2021-12-16T00:00:00Z" },
                    new int[] { 3 }),
            new AppointmentRequest(21, true, 16, new String[] { "2021-12-24T00:00:00Z" }, new int[] { 3 }),
    };

    private AppointmentInfo[] initialSchedule = new AppointmentInfo[] {
            new AppointmentInfo(1, false, 2, "2021-11-08T08:00:00Z"),
            new AppointmentInfo(1, false, 3, "2021-12-15T09:00:00Z"),
            new AppointmentInfo(4, false, 3, "2021-12-15T13:00:00Z"),
            new AppointmentInfo(5, false, 3, "2021-12-15T11:00:00Z"),
            new AppointmentInfo(2, false, 2, "2021-11-16T12:00:00Z"),
            new AppointmentInfo(2, false, 3, "2021-12-09T14:00:00Z"),
            new AppointmentInfo(2, false, 3, "2021-12-24T10:00:00Z"),
            new AppointmentInfo(3, false, 2, "2021-12-24T09:00:00Z"),
            new AppointmentInfo(9, false, 1, "2021-12-24T16:00:00Z"),
            new AppointmentInfo(11, false, 1, "2021-12-24T13:00:00Z"),
            new AppointmentInfo(3, false, 3, "2021-12-01T10:00:00Z"),
            new AppointmentInfo(4, true, 1, "2021-11-09T15:00:00Z"),
            new AppointmentInfo(5, true, 2, "2021-11-12T16:00:00Z"),
            new AppointmentInfo(5, false, 3, "2021-12-02T11:00:00Z"),
            new AppointmentInfo(15, true, 1, "2021-12-02T14:00:00Z"),
            new AppointmentInfo(22, true, 2, "2021-12-02T15:00:00Z"),
            new AppointmentInfo(24, true, 3, "2021-12-02T15:00:00Z"),
            new AppointmentInfo(23, true, 2, "2021-12-02T16:00:00Z"),
            new AppointmentInfo(25, true, 3, "2021-12-02T16:00:00Z"),
            new AppointmentInfo(6, true, 2, "2021-11-22T16:00:00Z"),
            new AppointmentInfo(6, false, 2, "2021-11-30T10:00:00Z"),
            new AppointmentInfo(11, true, 1, "2021-11-30T16:00:00Z"),
            new AppointmentInfo(6, false, 2, "2021-12-21T09:00:00Z"),
            new AppointmentInfo(12, false, 1, "2021-12-21T14:00:00Z"),
            new AppointmentInfo(7, false, 3, "2021-11-23T08:00:00Z"),
            new AppointmentInfo(7, false, 3, "2021-12-07T10:00:00Z"),
            new AppointmentInfo(7, false, 3, "2021-12-22T14:00:00Z"),
            new AppointmentInfo(8, true, 3, "2021-11-26T15:00:00Z"),
            new AppointmentInfo(8, false, 3, "2021-12-06T13:00:00Z"),
            new AppointmentInfo(8, false, 3, "2021-12-20T14:00:00Z"),
            new AppointmentInfo(9, true, 2, "2021-11-29T16:00:00Z"),
            new AppointmentInfo(11, false, 1, "2021-12-16T11:00:00Z"),
            new AppointmentInfo(20, true, 1, "2021-12-16T16:00:00Z"),
            new AppointmentInfo(14, true, 1, "2021-12-03T16:00:00Z"),
            new AppointmentInfo(14, false, 1, "2021-12-17T14:00:00Z"),
            new AppointmentInfo(16, false, 1, "2021-12-13T08:00:00Z"),
            new AppointmentInfo(17, false, 1, "2021-12-13T12:00:00Z"),
            new AppointmentInfo(18, false, 2, "2021-12-14T13:00:00Z"),
            new AppointmentInfo(19, false, 2, "2021-12-23T14:00:00Z"),
    };

    public LocalServer() {
        schedule = new ArrayList<AppointmentInfo>(Arrays.asList(initialSchedule));
    }

    @Override
    public boolean start() {
        return true;
    }

    @Override
    public AppointmentInfo[] stop() {
        AppointmentInfo[] appointments = new AppointmentInfo[schedule.size()];
        return schedule.toArray(appointments);
    }

    @Override
    public AppointmentRequest[] getAppointmentRequests() {
        return appointments;
    }

    @Override
    public AppointmentInfo[] getInitialSchedule() {
        return initialSchedule;
    }

    @Override
    public boolean markAppointment(AppointmentInfoRequest appointment) {
        // check to make sure this appointment doesn't double book a doctor
        for (AppointmentInfo appt : schedule) {
            if (appointment.getAppointmentTime() == appt.getAppointmentTime()
                    && appointment.getDoctorId() == appt.getDoctorId()) {
                throw new RuntimeException(
                        "Appointment overlaps with a current appointment! "
                                + appointment.getRequestId());
            }
        }

        schedule.add(appointment);
        return true;
    }
}
