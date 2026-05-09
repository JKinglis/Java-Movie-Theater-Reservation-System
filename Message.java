package FinalProject;

import java.io.Serializable;


// send data between client and server.


public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    // objects sent between client/server
    protected User user;
    protected Reservation reservation;
    protected MovieShow show;
    protected Movie movie;

    // action requested by client
    protected String action;

    // text data returned from server
    protected String data;

    // IDs used for database operations
    protected int movieID;
    protected int showID;
    protected int userID;
    protected int reservationID;

    // payment info
    protected String ccNumber;

    // empty constructor
    public Message() {

        this.user = null;
        this.reservation = null;
        this.show = null;
        this.movie = null;

        this.action = null;
        this.data = "";

        this.movieID = 0;
        this.showID = 0;
        this.userID = 0;
        this.reservationID = 0;

        this.ccNumber = "";
    }

    // full constructor
    public Message(User user,
                   Reservation reservation,
                   MovieShow show,
                   Movie movie,
                   String action) {
    	
    	
    	/*Add Arrays to handle lists?
 *         Movie[] movieList,
           MovieShow[] showList,
           Reservation[] reservationList*/
    	

        this.user = user;
        this.reservation = reservation;
        this.show = show;
        this.movie = movie;

        this.action = action;
        this.data = "";

        this.movieID = 0;
        this.showID = 0;
        this.userID = 0;
        this.reservationID = 0;
       
        

        this.ccNumber = "";
    }

    // =========================
    // GETTERS AND SETTERS
    // =========================

    // user getter
    public User getUser() {
        return user;
    }

    // user setter
    public void setUser(User user) {
        this.user = user;
    }

    // reservation getter
    public Reservation getReservation() {
        return reservation;
    }

    // reservation setter
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    // show getter
    public MovieShow getShow() {
        return show;
    }

    // show setter
    public void setShow(MovieShow show) {
        this.show = show;
    }

    // movie getter
    public Movie getMovie() {
        return movie;
    }

    // movie setter
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    // action getter
    public String getAction() {
        return action;
    }

    // action setter
    public void setAction(String action) {
        this.action = action;
    }

    // data getter
    public String getData() {
        return data;
    }

    // data setter
    public void setData(String data) {
        this.data = data;
    }

    // movie ID getter
    public int getMovieID() {
        return movieID;
    }

    // movie ID setter
    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    // show ID getter
    public int getShowID() {
        return showID;
    }

    // show ID setter
    public void setShowID(int showID) {
        this.showID = showID;
    }

    // user ID getter
    public int getUserID() {
        return userID;
    }

    // user ID setter
    public void setUserID(int userID) {
        this.userID = userID;
    }

    // reservation ID getter
    public int getReservationID() {
        return reservationID;
    }

    // reservation ID setter
    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    // credit card getter
    public String getCCNumber() {
        return ccNumber;
    }

    // credit card setter
    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }

    // debug print
    @Override
    public String toString() {

        return "Message [user=" + user
                + ", reservation=" + reservation
                + ", show=" + show
                + ", movie=" + movie
                + ", action=" + action
                + ", data=" + data
                + ", movieID=" + movieID
                + ", showID=" + showID
                + ", userID=" + userID
                + ", reservationID=" + reservationID
                + "]";
    }
}