import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Scheduler {

    // this is more janky than I would have liked because I didn't realize that
    // there were already appointments on the schedule until I had already done a
    // majority of this
    private class AppointmentContainer implements Comparable<AppointmentContainer> {
        private AppointmentInfo appointmentInfo;
        private AppointmentRequest appointmentRequest;

        public AppointmentContainer(AppointmentInfo appointmentInfo, AppointmentRequest appointmentRequest) {
            if (appointmentInfo != null && appointmentRequest != null) {
                throw new RuntimeException("Only one of appointmentInfo and appointmentRequest can be nonnull");
            }
            this.appointmentInfo = appointmentInfo;
            this.appointmentRequest = appointmentRequest;
        }

        public AppointmentInfo getAppointmentInfo() {
            return appointmentInfo;
        }

        public AppointmentRequest getAppointmentRequest() {
            return appointmentRequest;
        }

        @Override
        public int compareTo(AppointmentContainer b) {
            if (appointmentInfo != null && b.getAppointmentInfo() != null) {
                return appointmentInfo.compareTo(b.getAppointmentInfo());
            } else if (appointmentRequest != null && b.getAppointmentRequest() != null) {
                return appointmentRequest.compareTo(b.getAppointmentRequest());
            } else if (appointmentInfo != null) {
                if (appointmentInfo.getMilliseconds() == b.getAppointmentRequest().getNextAvailableAppointment()) {
                    return -1; // put appointment info first
                } else {
                    // return the one with earlier date
                    return appointmentInfo.getMilliseconds() < b.getAppointmentRequest().getNextAvailableAppointment()
                            ? -1
                            : 1;
                }
            } else {
                if (appointmentRequest.getNextAvailableAppointment() == b.getAppointmentInfo().getMilliseconds()) {
                    return 1; // put appointment info first
                } else {
                    // return the one with earlier date
                    return appointmentRequest.getNextAvailableAppointment() < b.getAppointmentInfo().getMilliseconds()
                            ? -1
                            : 1;
                }
            }
        }
    }

    private ServerInterface server;

    public Scheduler(ServerInterface server) {
        this.server = server;
    }

    public AppointmentInfo[] calculateSchedule() {
        server.start();

        AppointmentRequest[] requests = server.getAppointmentRequests();
        PriorityQueue<AppointmentContainer> queue = new PriorityQueue<AppointmentContainer>();
        for (AppointmentRequest request : requests) {
            queue.add(new AppointmentContainer(null, request));
        }

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        long lastMillis = 0;

        ArrayList<Integer> doctorsUsed = new ArrayList<>(); // make sure to not overbook a doctor in a given hour
        HashMap<Integer, Long> personLastAppointment = new HashMap<>(); // ensure doesn't have appointment too soon

        // be careful around already scheduled appointments:
        AppointmentInfo[] previousAppointments = server.getInitialSchedule();
        for (AppointmentInfo info : previousAppointments) {
            queue.add(new AppointmentContainer(info, null));
        }

        while (queue.size() > 0) {
            AppointmentContainer container = queue.poll();

            if (container.getAppointmentInfo() != null) {
                // this is one that is already in the schedule
                AppointmentInfo info = container.getAppointmentInfo();

                if (info.getMilliseconds() != lastMillis) {
                    doctorsUsed.clear();
                }

                lastMillis = info.getMilliseconds();
                doctorsUsed.add(info.getDoctorId());
                personLastAppointment.put(info.getPersonId(), info.getMilliseconds());
                continue;
            }

            AppointmentRequest request = container.getAppointmentRequest();
            if (request.getNextAvailableAppointment() == -1) {
                System.out.println("Couldn't place an appointment!");
                System.out.println(request);
                continue;
            }

            if (request.getNextAvailableAppointment() != lastMillis) {
                doctorsUsed.clear();
            }

            // if appointment too soon skip them, Monday to Monday is ok
            if (!personLastAppointment.containsKey(request.getPersonId()) || request.getNextAvailableAppointment()
                    - personLastAppointment.get(request.getPersonId()) > 7 * 24 * 60 * 60 * 1000 - 1) {
                boolean setAppointment = false;
                for (int doctor : request.getPreferredDocs()) {
                    if (!doctorsUsed.contains(doctor)) {
                        doctorsUsed.add(doctor);

                        setAppointment = true;
                        server.markAppointment(new AppointmentInfoRequest(request.getPersonId(), request.getIsNew(),
                                doctor, df.format(request.getNextAvailableAppointment()), request.getRequestId()));
                        personLastAppointment.put(request.getPersonId(), request.getNextAvailableAppointment());
                        break;
                    }
                }

                lastMillis = request.getNextAvailableAppointment();

                if (setAppointment) {
                    continue;
                }
                // didn't place because good doctors used up
                request.calcNextAvailableAppointment(request.getNextAvailableAppointment() + 1);
            } else {
                // didn't place because too close to last appointment
                request.calcNextAvailableAppointment(
                        personLastAppointment.get(request.getPersonId()) + 7 * 24 * 60 * 60 * 1000 - 1);
            }

            // didn't place, reassign this request to the next available
            request.setAvailableSpots(request.getAvailableSpots() - 1);
            queue.add(new AppointmentContainer(null, request));
        }

        return server.stop();
    }
}
