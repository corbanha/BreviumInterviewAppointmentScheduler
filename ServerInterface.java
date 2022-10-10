public interface ServerInterface {
    boolean start();

    AppointmentInfo[] stop();

    AppointmentRequest[] getAppointmentRequests();

    AppointmentInfo[] getInitialSchedule();

    boolean markAppointment(AppointmentInfoRequest appointment);
}