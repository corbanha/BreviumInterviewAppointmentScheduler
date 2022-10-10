import java.util.Arrays;

class Main {

  public static void main(String[] args) {
    ServerInterface server = new LocalServer(); // use local server for testing
    Scheduler scheduler = new Scheduler(server);
    AppointmentInfo[] infos = scheduler.calculateSchedule();
    Arrays.sort(infos);
    for (AppointmentInfo info : infos) {
      System.out.println(info);
    }
  }
}
