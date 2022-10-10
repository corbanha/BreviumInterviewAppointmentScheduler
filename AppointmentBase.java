public abstract class AppointmentBase {
  private int personId;
  private boolean isNew;

  public AppointmentBase(int personId, boolean isNew){
    this.personId = personId;
    this.isNew = isNew;
  }

  public int getPersonId(){
    return personId;
  }

  public void setPersonId(int personId){
    this.personId = personId;
  }

  public boolean getIsNew(){
    return isNew;
  }

  public void setIsNew(boolean isNew){
    this.isNew = isNew;
  }

  public String toString(){
    return String.format("PersonId: %s, New: %b", personId, isNew);
  }
}
