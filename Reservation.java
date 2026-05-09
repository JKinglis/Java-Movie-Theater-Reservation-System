package FinalProject;

import java.io.Serializable;

public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
	
	protected User user;
	protected MovieShow show;
	protected String ticketNumber;
	protected int numTickets;
	
	Reservation(User user, MovieShow show, int numTickets){
		this.user = user;
		this.show = show;
		this.ticketNumber = (char) ('A' + (int) (Math.random() * 26)) + Integer.toString((int) (Math.random() * 9000) + 1000);
		this.numTickets = numTickets;
	}

	public int getNumTickets() {
		return numTickets;
	}

	public void setNumTickets(int numTickets) {
		this.numTickets = numTickets;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public MovieShow getShow() {
		return show;
	}

	public void setShow(MovieShow show) {
		this.show = show;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}	


	@Override
	public String toString() {
		return "Reservation [user=" + user + ", show=" + show + ", ticketNumber=" + ticketNumber + "Number of Tickets:" + numTickets + "]";
	}
}