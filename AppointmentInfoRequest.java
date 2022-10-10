public class AppointmentInfoRequest extends AppointmentInfo {
  private int requestId;

  public AppointmentInfoRequest(int personId, boolean isNew, int doctorId, String appointmentTime, int requestId) {
    super(personId, isNew, doctorId, appointmentTime);
    this.requestId = requestId;
  }

  public int getRequestId(){
    return requestId;
  }

  public void setRequestId(int requestId){
    this.requestId = requestId;
  }

  public String toString(){
    return String.format("%s, requestId: %d", super.toString(), requestId);
  }
}
