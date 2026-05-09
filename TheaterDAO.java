package FinalProject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Date;

public class TheaterDAO {

    private Connection conn;

    // connect to database
    public TheaterDAO() {
        try {
            conn = DBConnection.getConnection();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // login user
    public ResultSet loginUser(String username, String password) {
        try {
            String sql = "SELECT * FROM RegisteredUser WHERE username = ? AND password = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            return stmt.executeQuery();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // create new user
    public boolean createUser(String username, String password, Role role,
                              String firstName, String lastName,
                              String address, String email, Long phone) {
        try {
            String sql = "INSERT INTO RegisteredUser "
                    + "(username, password, role, firstName, lastName, address, email, phone) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role.name());
            stmt.setString(4, firstName);
            stmt.setString(5, lastName);
            stmt.setString(6, address);
            stmt.setString(7, email);
            stmt.setLong(8, phone);

            return stmt.executeUpdate() > 0;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    // check if username already exists
    public boolean usernameExists(String username) {
        try {
            String sql = "SELECT userPK FROM RegisteredUser WHERE username = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            return rs.next();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    // add movie and return generated movie ID
    public int addMovie(String title, int ranking, String review) {
        try {
            String sql = "INSERT INTO Movie (title, ranking, review) VALUES (?, ?, ?)";

            PreparedStatement stmt =
                    conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, title);
            stmt.setInt(2, ranking);
            stmt.setString(3, review);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();

                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return -1;
    }

    // update movie
    public boolean updateMovie(int movieId, String title, int ranking, String review) {
        try {
            String sql = "UPDATE Movie SET title = ?, ranking = ?, review = ? WHERE moviePK = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, title);
            stmt.setInt(2, ranking);
            stmt.setString(3, review);
            stmt.setInt(4, movieId);

            return stmt.executeUpdate() > 0;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    // delete movie
    public boolean deleteMovie(int movieId) {
        try {
            String sql = "DELETE FROM Movie WHERE moviePK = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, movieId);

            return stmt.executeUpdate() > 0;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    // get all movies
    public ResultSet getAllMovies() {
        try {
            String sql = "SELECT * FROM Movie ORDER BY title";

            PreparedStatement stmt = conn.prepareStatement(sql);

            return stmt.executeQuery();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // add show
    public boolean addShow(int movieId, Date showDate, Time showTime,
                           int roomNumber, double price) {
        try {
            String sql = "INSERT INTO MovieShow "
                    + "(movieFK, showDate, showTime, roomNumber, price, availableSeats) "
                    + "VALUES (?, ?, ?, ?, ?, 40)";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, movieId);
            stmt.setDate(2, new java.sql.Date(showDate.getTime()));
            stmt.setTime(3, showTime);
            stmt.setInt(4, roomNumber);
            stmt.setDouble(5, price);

            return stmt.executeUpdate() > 0;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    // update show
    public boolean updateShow(int showId, int movieId, Date showDate,
                              Time showTime, int roomNumber, double price,
                              int availableSeats) {
        try {
            String sql = "UPDATE MovieShow SET movieFK = ?, showDate = ?, showTime = ?, "
                    + "roomNumber = ?, price = ?, availableSeats = ? WHERE showPK = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, movieId);
            stmt.setDate(2, new java.sql.Date(showDate.getTime()));
            stmt.setTime(3, showTime);
            stmt.setInt(4, roomNumber);
            stmt.setDouble(5, price);
            stmt.setInt(6, availableSeats);
            stmt.setInt(7, showId);

            return stmt.executeUpdate() > 0;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    // delete show
    public boolean deleteShow(int showId) {
        try {
            String sql = "DELETE FROM MovieShow WHERE showPK = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, showId);

            return stmt.executeUpdate() > 0;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    // get shows by date
    public ResultSet getShowsByDate(Date showDate) {
        try {
            String sql = "SELECT s.showPK, m.title, m.ranking, s.showDate, s.showTime, "
                    + "s.roomNumber, s.price, s.availableSeats "
                    + "FROM MovieShow s "
                    + "JOIN Movie m ON s.movieFK = m.moviePK "
                    + "WHERE s.showDate = ? "
                    + "ORDER BY s.showTime";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDate(1, new java.sql.Date(showDate.getTime()));

            return stmt.executeQuery();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // reduce seats by number of tickets
    public boolean reduceAvailableSeats(int showId, int numTickets) {
        try {
            String sql = "UPDATE MovieShow SET availableSeats = availableSeats - ? "
                    + "WHERE showPK = ? AND availableSeats >= ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, numTickets);
            stmt.setInt(2, showId);
            stmt.setInt(3, numTickets);

            return stmt.executeUpdate() > 0;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    // increase seats by number of tickets
    public boolean increaseAvailableSeats(int showId, int numTickets) {
        try {
            String sql = "UPDATE MovieShow SET availableSeats = availableSeats + ? "
                    + "WHERE showPK = ? AND availableSeats + ? <= 40";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, numTickets);
            stmt.setInt(2, showId);
            stmt.setInt(3, numTickets);

            return stmt.executeUpdate() > 0;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    // increase seats by 1, used during cancellation
    public boolean increaseAvailableSeats(int showId) {
        try {
            String sql = "UPDATE MovieShow SET availableSeats = availableSeats + 1 "
                    + "WHERE showPK = ? AND availableSeats < 40";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, showId);

            return stmt.executeUpdate() > 0;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    // get price for selected show
    public double getShowPrice(int showId) {
        try {
            String sql = "SELECT price FROM MovieShow WHERE showPK = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, showId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("price");
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return 0.0;
    }

    // create reservation
    public int createReservation(int userId, int showId, int numTickets) {
        try {
            String ticketNumber = "T" + System.currentTimeMillis();

            String sql = "INSERT INTO Reservation "
                    + "(userFK, showFK, reservationDate, ticketNumber, status, NumTickets) "
                    + "VALUES (?, ?, NOW(), ?, 'CONFIRMED', ?)";

            PreparedStatement stmt =
                    conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, userId);
            stmt.setInt(2, showId);
            stmt.setString(3, ticketNumber);
            stmt.setInt(4, numTickets);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();

                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return -1;
    }

    // cancel reservation
    public boolean cancelReservation(int reservationId) {
        try {
            String sql = "DELETE FROM Reservation WHERE reservationPK = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, reservationId);

            return stmt.executeUpdate() > 0;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    // check if reservation can be canceled at least 1 hour before showtime
    public boolean canCancelReservation(int reservationId) {
        try {
            String sql =
                    "SELECT TIMESTAMPDIFF(MINUTE, NOW(), "
                    + "TIMESTAMP(s.showDate, s.showTime)) AS minutesUntilShow "
                    + "FROM Reservation r "
                    + "JOIN MovieShow s ON r.showFK = s.showPK "
                    + "WHERE r.reservationPK = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, reservationId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int minutesUntilShow = rs.getInt("minutesUntilShow");
                return minutesUntilShow >= 60;
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    // get reservations by user
    public ResultSet getReservationsByUser(int userId) {
        try {
            String sql = "SELECT r.reservationPK, r.ticketNumber, r.reservationDate, "
                    + "m.title, s.showDate, s.showTime, s.roomNumber, s.price "
                    + "FROM Reservation r "
                    + "JOIN MovieShow s ON r.showFK = s.showPK "
                    + "JOIN Movie m ON s.movieFK = m.moviePK "
                    + "WHERE r.userFK = ? "
                    + "ORDER BY s.showDate, s.showTime";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);

            return stmt.executeQuery();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // get reservations for one user on a selected date
    public ResultSet getReservationsByUserAndDate(int userId, Date showDate) {
        try {
            String sql = "SELECT r.reservationPK, r.ticketNumber, r.reservationDate, "
                    + "m.title, s.showDate, s.showTime, s.roomNumber, s.price "
                    + "FROM Reservation r "
                    + "JOIN MovieShow s ON r.showFK = s.showPK "
                    + "JOIN Movie m ON s.movieFK = m.moviePK "
                    + "WHERE r.userFK = ? AND s.showDate = ? "
                    + "ORDER BY s.showTime";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setDate(2, new java.sql.Date(showDate.getTime()));

            return stmt.executeQuery();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
    
    //get reservation by reservation id
    public ResultSet getReservationbyReservationID(int reservationID) {
        try {
            String sql = "SELECT * FROM reservation WHERE reservationPK = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, reservationID);

            return stmt.executeQuery();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
    
    // create payment
    public boolean createPayment(int reservationId, double amount, String creditCardNumber) {
        try {
            String sql = "INSERT INTO Payment "
                    + "(reservationFK, amount, creditCardNumber, paymentDate) "
                    + "VALUES (?, ?, ?, NOW())";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, reservationId);
            stmt.setDouble(2, amount);
            stmt.setString(3, creditCardNumber);

            return stmt.executeUpdate() > 0;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    // delete payment when reservation is canceled
    public boolean deletePaymentByReservation(int reservationId) {
        try {
            String sql = "DELETE FROM Payment WHERE reservationFK = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, reservationId);

            return stmt.executeUpdate() > 0;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    // close connection
    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}