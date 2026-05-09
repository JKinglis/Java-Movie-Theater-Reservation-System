package FinalProject;

import java.sql.Time;
import java.util.Date;

import java.io.Serializable;

public class MovieShow implements Serializable {

    private static final long serialVersionUID = 1L;
	protected Date showDate;
	protected Time time;
	protected int roomNumber;
	protected double price;
	protected int availableSeats;

	MovieShow(){
		this.showDate = null;
		this.time = null;
		this.roomNumber = 0;
		this.price = 0.0;
		this.availableSeats = 0;
	}

	MovieShow(Date date, Time time, int roomNumber, double price, int seats){
		this.showDate = date;
		this.time = time;
		this.roomNumber = roomNumber;
		this.price = price;
		this.availableSeats = seats;
	}
	
	//Getters and Setters

	public Date getShowDate() {
		return showDate;
	}

	public void setShowDate(Date showDate) {
		this.showDate = showDate;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}
	
	@Override
	public String toString() {
		return "MovieShow [showDate=" + showDate + ", time=" + time + ", roomNumber=" + roomNumber + ", price=" + price
				+ ", availableSeats=" + availableSeats + "]";
	}
	
	
}
