package es.udc.fi.dc.fd.model.persistence;

public class FriendListOut {

  private UserImpl user;

  private int myRating;

  public FriendListOut() {

  }

  /**
   * Default constructor of an user's friend entity.
   *
   * @param user     The user entity of the friend
   * @param myRating The rating that the user gave to the friend
   */
  public FriendListOut(UserImpl user, int myRating) {
    super();
    this.user = user;
    this.myRating = myRating;
  }

  public UserImpl getUser() {
    return user;
  }

  public void setUser(UserImpl user) {
    this.user = user;
  }

  public int getMyRating() {
    return myRating;
  }

  public void setMyRating(int myRating) {
    this.myRating = myRating;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + myRating;
    result = prime * result + (user == null ? 0 : user.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final FriendListOut other = (FriendListOut) obj;
    if (myRating != other.myRating) {
      return false;
    }
    if (user == null) {
      if (other.user != null) {
        return false;
      }
    } else if (!user.equals(other.user)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "FriendListOut [user=" + user + ", myRating=" + myRating + "]";
  }

}
