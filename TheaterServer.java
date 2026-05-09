package FinalProject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

public class TheaterServer {

    // server port
    private int port;

    // database access object
    private TheaterDAO dao;

    // constructor
    public TheaterServer(int port) {
        this.port = port;
        startServer();
    }

    // starts server and waits for clients
    private void startServer() {

        try {
            ServerSocket serverSocket = new ServerSocket(port);

            // connect to database through DAO
            dao = new TheaterDAO();

            System.out.println("Database connection established.");
            System.out.println("Theater Server started on port " + port);

            while (true) {

                // wait for client connection
                Socket socket = serverSocket.accept();

                // create and start client thread
                HandleAClient task = new HandleAClient(socket);
                new Thread(task).start();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // handles one connected client
    class HandleAClient implements Runnable {

        private Socket socket;

        // constructor
        public HandleAClient(Socket socket) {
            this.socket = socket;
        }

        // thread logic
        public void run() {

            try {

                ObjectInputStream inputFromClient =
                        new ObjectInputStream(socket.getInputStream());

                ObjectOutputStream outputToClient =
                        new ObjectOutputStream(socket.getOutputStream());

                while (true) {

                    // receive message from client
                    Message msg =
                            (Message) inputFromClient.readObject();

                    // process request
                    msg = processMessage(msg);

                    // send response back
                    outputToClient.writeObject(msg);
                    outputToClient.flush();
                }
            }
            catch (IOException ex) {
                System.out.println("Client disconnected.");
            }
            catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    // routes requests based on action
    private Message processMessage(Message msg) {

        try {

            switch (msg.getAction()) {

                case "LOGIN":
                    return handleLogin(msg);

                case "CREATE_USER":
                    return handleCreateUser(msg);

                case "USERNAME_EXISTS":
                    return handleUsernameExists(msg);

                case "ADD_MOVIE":
                    return handleAddMovie(msg);

                case "UPDATE_MOVIE":
                    return handleUpdateMovie(msg);

                case "DELETE_MOVIE":
                    return handleDeleteMovie(msg);

                case "GET_ALL_MOVIES":
                    return handleGetAllMovies(msg);

                case "ADD_SHOW":
                    return handleAddShow(msg);

                case "UPDATE_SHOW":
                    return handleUpdateShow(msg);

                case "DELETE_SHOW":
                    return handleDeleteShow(msg);

                case "GET_SHOWS_BY_DATE":
                    return handleGetShowsByDate(msg);

                case "CREATE_RESERVATION":
                    return handleCreateReservation(msg);

                case "CANCEL_RESERVATION":
                    return handleCancelReservation(msg);

                case "GET_RESERVATION_BY_USER":
                    return handleGetReservationsByUser(msg);

                case "GET_RESERVATIONS_BY_USER_AND_DATE":
                    return handleGetReservationsByUserAndDate(msg);

                case "CREATE_PAYMENT":
                    return handleCreatePayment(msg);

                case "DELETE_PAYMENTS":
                    return handleDeletePayment(msg);

                default:
                    msg.setAction("UNKNOWN_ACTION");
                    msg.setData("Unknown request.");
                    return msg;
            }
        }
        catch (Exception ex) {

            ex.printStackTrace();

            msg.setAction("SERVER_ERROR");
            msg.setData("Server error occurred.");

            return msg;
        }
    }

    // handles login
    private Message handleLogin(Message msg) {

        try {

            ResultSet rs = dao.loginUser(
                    msg.getUser().getUsername(),
                    msg.getUser().getPassword()
            );

            if (rs != null && rs.next()) {

                User user = new User();

                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(Role.valueOf(rs.getString("role")));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setAddress(rs.getString("address"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getLong("phone"));

                msg.setUser(user);
                msg.setUserID(rs.getInt("userPK"));

                msg.setAction("LOGIN_SUCCESS");
            }
            else {

                msg.setUser(null);
                msg.setAction("LOGIN_FAILED");
            }
        }
        catch (Exception ex) {

            ex.printStackTrace();

            msg.setUser(null);
            msg.setAction("LOGIN_FAILED");
        }

        return msg;
    }

    // handles new user signup
    private Message handleCreateUser(Message msg) {

        boolean created = dao.createUser(
                msg.getUser().getUsername(),
                msg.getUser().getPassword(),
                msg.getUser().getRole(),
                msg.getUser().getFirstName(),
                msg.getUser().getLastName(),
                msg.getUser().getAddress(),
                msg.getUser().getEmail(),
                msg.getUser().getPhone()
        );

        msg.setAction(
                created ?
                        "CREATE_USER_SUCCESS"
                        :
                        "CREATE_USER_FAILED"
        );

        return msg;
    }

    // checks duplicate username
    private Message handleUsernameExists(Message msg) {

        boolean exists =
                dao.usernameExists(msg.getUser().getUsername());

        msg.setAction(
                exists ?
                        "USERNAME_EXISTS_TRUE"
                        :
                        "USERNAME_EXISTS_FALSE"
        );

        return msg;
    }

    // adds movie
    private Message handleAddMovie(Message msg) {

        int movieID = dao.addMovie(
                msg.getMovie().getTitle(),
                msg.getMovie().getRanking(),
                msg.getMovie().getReview()
        );

        msg.setMovieID(movieID);

        msg.setAction(
                movieID > 0 ?
                        "ADD_MOVIE_SUCCESS"
                        :
                        "ADD_MOVIE_FAILED"
        );

        return msg;
    }

    // updates movie
    private Message handleUpdateMovie(Message msg) {

        boolean success = dao.updateMovie(
                msg.getMovieID(),
                msg.getMovie().getTitle(),
                msg.getMovie().getRanking(),
                msg.getMovie().getReview()
        );

        msg.setAction(
                success ?
                        "UPDATE_MOVIE_SUCCESS"
                        :
                        "UPDATE_MOVIE_FAILED"
        );

        return msg;
    }

    // deletes movie
    private Message handleDeleteMovie(Message msg) {

        boolean success =
                dao.deleteMovie(msg.getMovieID());

        msg.setAction(
                success ?
                        "DELETE_MOVIE_SUCCESS"
                        :
                        "DELETE_MOVIE_FAILED"
        );

        return msg;
    }

    // returns all movies
    private Message handleGetAllMovies(Message msg) {

        try {

            ResultSet rs = dao.getAllMovies();

            StringBuilder data = new StringBuilder();

            data.append("Movies:\n\n");

            while (rs != null && rs.next()) {

                data.append("Movie ID: ")
                        .append(rs.getInt("moviePK"))
                        .append("\n");

                data.append("Title: ")
                        .append(rs.getString("title"))
                        .append("\n");

                data.append("Ranking: ")
                        .append(rs.getInt("ranking"))
                        .append(" stars\n");

                data.append("Review: ")
                        .append(rs.getString("review"))
                        .append("\n");

                data.append("-------------------------\n");
            }

            msg.setData(data.toString());
            msg.setAction("GET_ALL_MOVIES_DONE");
        }
        catch (Exception ex) {

            ex.printStackTrace();

            msg.setAction("GET_ALL_MOVIES_FAILED");
            msg.setData("Could not retrieve movies.");
        }

        return msg;
    }

    // adds show
    private Message handleAddShow(Message msg) {

        boolean success = dao.addShow(
                msg.getMovieID(),
                msg.getShow().getShowDate(),
                msg.getShow().getTime(),
                msg.getShow().getRoomNumber(),
                msg.getShow().getPrice()
        );

        msg.setAction(
                success ?
                        "ADD_SHOW_SUCCESS"
                        :
                        "ADD_SHOW_FAILED"
        );

        return msg;
    }

    // updates show
    private Message handleUpdateShow(Message msg) {

        boolean success = dao.updateShow(
                msg.getShowID(),
                msg.getMovieID(),
                msg.getShow().getShowDate(),
                msg.getShow().getTime(),
                msg.getShow().getRoomNumber(),
                msg.getShow().getPrice(),
                msg.getShow().getAvailableSeats()
        );

        msg.setAction(
                success ?
                        "UPDATE_SHOW_SUCCESS"
                        :
                        "UPDATE_SHOW_FAILED"
        );

        return msg;
    }

    // deletes show
    private Message handleDeleteShow(Message msg) {

        boolean success =
                dao.deleteShow(msg.getShowID());

        msg.setAction(
                success ?
                        "DELETE_SHOW_SUCCESS"
                        :
                        "DELETE_SHOW_FAILED"
        );

        return msg;
    }

    // returns shows for selected date
    private Message handleGetShowsByDate(Message msg) {

        try {

            ResultSet rs =
                    dao.getShowsByDate(
                            msg.getShow().getShowDate()
                    );

            StringBuilder data = new StringBuilder();

            data.append("Available Shows:\n\n");

            while (rs != null && rs.next()) {

                data.append("Show ID: ")
                        .append(rs.getInt("showPK"))
                        .append("\n");

                data.append("Movie: ")
                        .append(rs.getString("title"))
                        .append("\n");

                data.append("Ranking: ")
                        .append(rs.getInt("ranking"))
                        .append(" stars\n");

                data.append("Date: ")
                        .append(rs.getDate("showDate"))
                        .append("\n");

                data.append("Time: ")
                        .append(rs.getTime("showTime"))
                        .append("\n");

                data.append("Room: ")
                        .append(rs.getInt("roomNumber"))
                        .append("\n");

                data.append("Price: $")
                        .append(rs.getDouble("price"))
                        .append("\n");

                data.append("Available Seats: ")
                        .append(rs.getInt("availableSeats"))
                        .append("\n");

                data.append("-------------------------\n");
            }

            msg.setData(data.toString());
            msg.setAction("GET_SHOWS_BY_DATE_DONE");
        }
        catch (Exception ex) {

            ex.printStackTrace();

            msg.setAction("GET_SHOWS_BY_DATE_FAILED");
            msg.setData("Could not retrieve shows.");
        }

        return msg;
    }

    // creates reservation and payment
    private Message handleCreateReservation(Message msg) {

        int numTickets =
                msg.getReservation().getNumTickets();

        boolean seatsReduced =
                dao.reduceAvailableSeats(
                        msg.getShowID(),
                        numTickets
                );

        if (!seatsReduced) {

            msg.setAction("NO_SEATS_AVAILABLE");
            return msg;
        }

        int reservationID =
                dao.createReservation(
                        msg.getUserID(),
                        msg.getShowID(),
                        msg.getReservation().getNumTickets()
                );

        if (reservationID > 0) {

            double price =
                    dao.getShowPrice(msg.getShowID());

            double amount =
                    price * numTickets;

            boolean paymentCreated =
                    dao.createPayment(
                            reservationID,
                            amount,
                            msg.getCCNumber()
                    );

            if (paymentCreated) {

                msg.setReservationID(reservationID);

                msg.setAction(
                        "CREATE_RESERVATION_SUCCESS"
                );
            }
            else {

                dao.increaseAvailableSeats(
                        msg.getShowID(),
                        numTickets
                );

                dao.cancelReservation(reservationID);

                msg.setAction(
                        "CREATE_PAYMENT_FAILED"
                );
            }
        }
        else {

            dao.increaseAvailableSeats(
                    msg.getShowID(),
                    numTickets
            );

            msg.setAction(
                    "CREATE_RESERVATION_FAILED"
            );
        }

        return msg;
    }

    // cancels reservation
    private Message handleCancelReservation(Message msg) {

        boolean canCancel =
                dao.canCancelReservation(
                        msg.getReservationID()
                );

        if (!canCancel) {

            msg.setAction("CANCEL_TOO_LATE");
            return msg;
        }

        dao.deletePaymentByReservation(
                msg.getReservationID());
                
        // increase available seats        
    	try {
        	ResultSet reservation = dao.getReservationbyReservationID(msg.getReservationID());
        	
        	if (reservation.next()) {
        		int numTickets = reservation.getInt("NumTickets");
        		if (numTickets > 1)
        			dao.increaseAvailableSeats(msg.getShowID(), numTickets);
            	else 
            		dao.increaseAvailableSeats(msg.getShowID());
        	}
        	}
        	catch (Exception ex) {
        		msg.setAction("CANCEL_RESERVATION_FAILED");
        	}        
        
        boolean success =
                dao.cancelReservation(
                        msg.getReservationID()
                );

        if (success) {
//        	try {
//        	ResultSet reservation = dao.getReservationbyReservationID(msg.getReservationID());
//        	JOptionPane.showMessageDialog(null, "NumTickets: " + reservation.getInt("NumTickets"));
//        	
//        	if (reservation.next() && reservation.getInt("NumTickets") > 1)
//                dao.increaseAvailableSeats(msg.getShowID(), reservation.getInt("NumTickets"));
//        	else {
//        		dao.increaseAvailableSeats(msg.getShowID());
//        	}
//        		
//        	}
//        	catch (Exception ex) {
//        		JOptionPane.showMessageDialog(null, "Could not fetch reservation, check if reservation ID exists.");
//        	};

            msg.setAction(
                    "CANCEL_RESERVATION_SUCCESS"
            );
        }
        else {

            msg.setAction(
                    "CANCEL_RESERVATION_FAILED"
            );
        }

        return msg;
    }

    // returns all reservations for a user
    private Message handleGetReservationsByUser(Message msg) {

        try {

            ResultSet rs =
                    dao.getReservationsByUser(
                            msg.getUserID()
                    );

            StringBuilder data = new StringBuilder();

            data.append("Your Reservations:\n\n");

            while (rs != null && rs.next()) {

                data.append("Reservation ID: ")
                        .append(rs.getInt("reservationPK"))
                        .append("\n");

                data.append("Ticket Number: ")
                        .append(rs.getString("ticketNumber"))
                        .append("\n");

                data.append("Movie: ")
                        .append(rs.getString("title"))
                        .append("\n");

                data.append("Date: ")
                        .append(rs.getDate("showDate"))
                        .append("\n");

                data.append("Time: ")
                        .append(rs.getTime("showTime"))
                        .append("\n");

                data.append("Room: ")
                        .append(rs.getInt("roomNumber"))
                        .append("\n");

                data.append("Price: $")
                        .append(rs.getDouble("price"))
                        .append("\n");

                data.append("-------------------------\n");
            }

            msg.setData(data.toString());

            msg.setAction(
                    "GET_RESERVATION_BY_USER_DONE"
            );
        }
        catch (Exception ex) {

            ex.printStackTrace();

            msg.setAction(
                    "GET_RESERVATION_BY_USER_FAILED"
            );

            msg.setData(
                    "Could not retrieve reservations."
            );
        }

        return msg;
    }

    // returns reservations for selected date
    private Message handleGetReservationsByUserAndDate(Message msg) {

        try {

            ResultSet rs =
                    dao.getReservationsByUserAndDate(
                            msg.getUserID(),
                            msg.getShow().getShowDate()
                    );

            StringBuilder data = new StringBuilder();

            data.append("Your Reservations For Selected Date:\n\n");

            while (rs != null && rs.next()) {

                data.append("Reservation ID: ")
                        .append(rs.getInt("reservationPK"))
                        .append("\n");

                data.append("Ticket Number: ")
                        .append(rs.getString("ticketNumber"))
                        .append("\n");

                data.append("Movie: ")
                        .append(rs.getString("title"))
                        .append("\n");

                data.append("Date: ")
                        .append(rs.getDate("showDate"))
                        .append("\n");

                data.append("Time: ")
                        .append(rs.getTime("showTime"))
                        .append("\n");

                data.append("Room: ")
                        .append(rs.getInt("roomNumber"))
                        .append("\n");

                data.append("Price: $")
                        .append(rs.getDouble("price"))
                        .append("\n");

                data.append("-------------------------\n");
            }

            msg.setData(data.toString());

            msg.setAction(
                    "GET_RESERVATIONS_BY_USER_AND_DATE_DONE"
            );
        }
        catch (Exception ex) {

            ex.printStackTrace();

            msg.setAction(
                    "GET_RESERVATIONS_BY_USER_AND_DATE_FAILED"
            );

            msg.setData(
                    "Could not retrieve reservations for that date."
            );
        }

        return msg;
    }

    // creates payment
    private Message handleCreatePayment(Message msg) {

        double amount =
                msg.getShow().getPrice()
                        * msg.getReservation().getNumTickets();

        boolean success =
                dao.createPayment(
                        msg.getReservationID(),
                        amount,
                        msg.getCCNumber()
                );

        msg.setAction(
                success ?
                        "CREATE_PAYMENT_SUCCESS"
                        :
                        "CREATE_PAYMENT_FAILED"
        );

        return msg;
    }

    // deletes payment
    private Message handleDeletePayment(Message msg) {

        boolean success =
                dao.deletePaymentByReservation(
                        msg.getReservationID()
                );

        msg.setAction(
                success ?
                        "DELETE_PAYMENT_SUCCESS"
                        :
                        "DELETE_PAYMENT_FAILED"
        );

        return msg;
    }

    // main method
    public static void main(String[] args) {

        new TheaterServer(8000);
    }
}