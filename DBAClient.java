package FinalProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class DBAClient extends JFrame implements ActionListener {

    // server connection info
    private String hostname;
    private int port;

    // streams for client/server communication
    private ObjectOutputStream clientOutputStream;
    private ObjectInputStream clientInputStream;
    private Socket conn;

    // text fields for movie input
    private JTextField txtMovieID;
    private JTextField txtTitle;
    private JTextField txtRanking;
    private JTextField txtReview;

    // text fields for show input
    private JTextField txtShowID;
    private JTextField txtShowDate;
    private JTextField txtShowTime;
    private JTextField txtRoomNumber;
    private JTextField txtPrice;
    private JTextField txtAvailableSeats;

    // display areas
    private JTextArea allMovies;
    private JTextArea allShows;

    // buttons
    private JButton btnAddMovie;
    private JButton btnViewMovie;
    private JButton btnUpdateMovie;
    private JButton btnDeleteMovie;
    private JButton btnAddShow;
    private JButton btnViewShow;
    private JButton btnUpdateShow;
    private JButton btnDeleteShow;
    private JButton btnClear;
    private JButton btnClose;

    public DBAClient(String hostname, int port) {

        this.hostname = hostname;
        this.port = port;

        // connect to server
        try {
            conn = new Socket(hostname, port);
            clientOutputStream = new ObjectOutputStream(conn.getOutputStream());
            clientInputStream = new ObjectInputStream(conn.getInputStream());

            System.out.println("Connected to " + hostname + " on port " + port);
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Connection failed.");
            return;
        }

        // build GUI
        initComponent();
        doTheLayout();

        setTitle("DBA Client");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // initialize GUI components
    private void initComponent() {

        txtMovieID = new JTextField(15);
        txtTitle = new JTextField(15);
        txtRanking = new JTextField(15);
        txtReview = new JTextField(15);

        txtShowID = new JTextField(15);
        txtShowDate = new JTextField(15);
        txtShowTime = new JTextField(15);
        txtRoomNumber = new JTextField(15);
        txtPrice = new JTextField(15);
        txtAvailableSeats = new JTextField(15);

        allMovies = new JTextArea(10, 25);
        allShows = new JTextArea(10, 25);

        allMovies.setEditable(false);
        allShows.setEditable(false);

        btnAddMovie = new JButton("Add Movie");
        btnViewMovie = new JButton("View Movies");
        btnUpdateMovie = new JButton("Update Movie");
        btnDeleteMovie = new JButton("Delete Movie");

        btnAddShow = new JButton("Add Show");
        btnViewShow = new JButton("View Shows By Date");
        btnUpdateShow = new JButton("Update Show");
        btnDeleteShow = new JButton("Delete Show");

        btnClear = new JButton("Clear");
        btnClose = new JButton("Close");

        btnAddMovie.addActionListener(this);
        btnViewMovie.addActionListener(this);
        btnUpdateMovie.addActionListener(this);
        btnDeleteMovie.addActionListener(this);

        btnAddShow.addActionListener(this);
        btnViewShow.addActionListener(this);
        btnUpdateShow.addActionListener(this);
        btnDeleteShow.addActionListener(this);

        btnClear.addActionListener(this);
        btnClose.addActionListener(this);
    }

    // arrange the GUI layout
    private void doTheLayout() {

        Border line = BorderFactory.createLineBorder(Color.BLACK, 1);
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);

        JPanel moviePanel = new JPanel(new BorderLayout());
        moviePanel.setBorder(BorderFactory.createCompoundBorder(line, padding));

        JPanel movieInputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        movieInputPanel.add(new JLabel("Movie ID:"));
        movieInputPanel.add(txtMovieID);
        movieInputPanel.add(new JLabel("Title:"));
        movieInputPanel.add(txtTitle);
        movieInputPanel.add(new JLabel("Ranking (1-5):"));
        movieInputPanel.add(txtRanking);
        movieInputPanel.add(new JLabel("Review:"));
        movieInputPanel.add(txtReview);

        JPanel movieButtonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        movieButtonPanel.add(btnAddMovie);
        movieButtonPanel.add(btnViewMovie);
        movieButtonPanel.add(btnUpdateMovie);
        movieButtonPanel.add(btnDeleteMovie);

        moviePanel.add(movieInputPanel, BorderLayout.WEST);
        moviePanel.add(movieButtonPanel, BorderLayout.CENTER);
        moviePanel.add(new JScrollPane(allMovies), BorderLayout.EAST);

        JPanel showPanel = new JPanel(new BorderLayout());
        showPanel.setBorder(BorderFactory.createCompoundBorder(line, padding));

        JPanel showInputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        showInputPanel.add(new JLabel("Show ID:"));
        showInputPanel.add(txtShowID);
        showInputPanel.add(new JLabel("Show Date (YYYY-MM-DD):"));
        showInputPanel.add(txtShowDate);
        showInputPanel.add(new JLabel("Show Time (HH:MM:SS):"));
        showInputPanel.add(txtShowTime);
        showInputPanel.add(new JLabel("Room Number:"));
        showInputPanel.add(txtRoomNumber);
        showInputPanel.add(new JLabel("Price:"));
        showInputPanel.add(txtPrice);
        showInputPanel.add(new JLabel("Available Seats:"));
        showInputPanel.add(txtAvailableSeats);

        JPanel showButtonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        showButtonPanel.add(btnAddShow);
        showButtonPanel.add(btnViewShow);
        showButtonPanel.add(btnUpdateShow);
        showButtonPanel.add(btnDeleteShow);

        showPanel.add(showInputPanel, BorderLayout.WEST);
        showPanel.add(showButtonPanel, BorderLayout.CENTER);
        showPanel.add(new JScrollPane(allShows), BorderLayout.EAST);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(btnClear);
        bottomPanel.add(btnClose);

        add(moviePanel, BorderLayout.NORTH);
        add(showPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // handles button clicks
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnAddMovie) {
            addMovieButtonClicked();
        }
        else if (e.getSource() == btnViewMovie) {
            viewMovieButtonClicked();
        }
        else if (e.getSource() == btnUpdateMovie) {
            updateMovieButtonClicked();
        }
        else if (e.getSource() == btnDeleteMovie) {
            deleteMovieButtonClicked();
        }
        else if (e.getSource() == btnAddShow) {
            addShowButtonClicked();
        }
        else if (e.getSource() == btnViewShow) {
            viewShowButtonClicked();
        }
        else if (e.getSource() == btnUpdateShow) {
            updateShowButtonClicked();
        }
        else if (e.getSource() == btnDeleteShow) {
            deleteShowButtonClicked();
        }
        else if (e.getSource() == btnClear) {
            clearButtonClicked();
        }
        else if (e.getSource() == btnClose) {
            closeButtonClicked();
        }
    }

    // sends a message to the server and gets the response
    private Message sendMessage(Message msg) throws IOException, ClassNotFoundException {
        clientOutputStream.writeObject(msg);
        clientOutputStream.flush();
        return (Message) clientInputStream.readObject();
    }

    // add movie
    private void addMovieButtonClicked() {

        Movie movie = getMovieFromFields();
        if (movie == null) {
            return;
        }

        Message msg = new Message();
        msg.setAction("ADD_MOVIE");
        msg.setMovie(movie);

        try {
            msg = sendMessage(msg);

            if (msg.getAction().equals("ADD_MOVIE_SUCCESS")) {
                txtMovieID.setText(String.valueOf(msg.getMovieID()));
                JOptionPane.showMessageDialog(this, "Movie added.");
            }
            else {
                JOptionPane.showMessageDialog(this, "Movie could not be added.");
            }
        }
        catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Add movie request failed.");
            ex.printStackTrace();
        }
    }

    // view all movies
    private void viewMovieButtonClicked() {

        Message msg = new Message();
        msg.setAction("GET_ALL_MOVIES");

        try {
            msg = sendMessage(msg);
            if (msg.getData() != null && !msg.getData().trim().isEmpty()) {
                allMovies.setText(msg.getData());
            }
            else {
                allMovies.setText("No movies found.");
            }
        }
        catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Could not view movies.");
            ex.printStackTrace();
        }
    }

    // update movie
    private void updateMovieButtonClicked() {

        int movieID = getIntValue(txtMovieID, "Movie ID");
        if (movieID <= 0) {
            return;
        }

        Movie movie = getMovieFromFields();
        if (movie == null) {
            return;
        }

        Message msg = new Message();
        msg.setAction("UPDATE_MOVIE");
        msg.setMovieID(movieID);
        msg.setMovie(movie);

        try {
            msg = sendMessage(msg);

            if (msg.getAction().equals("UPDATE_MOVIE_SUCCESS")) {
                JOptionPane.showMessageDialog(this, "Movie updated.");
            }
            else {
                JOptionPane.showMessageDialog(this, "Movie could not be updated.");
            }
        }
        catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Update movie request failed.");
            ex.printStackTrace();
        }
    }

    // delete movie
    private void deleteMovieButtonClicked() {

        int movieID = getIntValue(txtMovieID, "Movie ID");
        if (movieID <= 0) {
            return;
        }

        Message msg = new Message();
        msg.setAction("DELETE_MOVIE");
        msg.setMovieID(movieID);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this movie?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        try {
            msg = sendMessage(msg);

            if (msg.getAction().equals("DELETE_MOVIE_SUCCESS")) {
                JOptionPane.showMessageDialog(this, "Movie deleted.");
            }
            else {
                JOptionPane.showMessageDialog(this, "Movie could not be deleted. It may not exist or may still have shows linked to it.");
            }
        }
        catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Delete movie request failed.");
            ex.printStackTrace();
        }
    }

    // add show
    private void addShowButtonClicked() {

        int movieID = getIntValue(txtMovieID, "Movie ID");
        if (movieID <= 0) {
            return;
        }

        MovieShow show = getShowFromFields(false);
        if (show == null) {
            return;
        }

        Message msg = new Message();
        msg.setAction("ADD_SHOW");
        msg.setMovieID(movieID);
        msg.setShow(show);

        try {
            msg = sendMessage(msg);

            if (msg.getAction().equals("ADD_SHOW_SUCCESS")) {
                JOptionPane.showMessageDialog(this, "Show added.");
            }
            else {
                JOptionPane.showMessageDialog(this, "Show could not be added.");
            }
        }
        catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Add show request failed.");
            ex.printStackTrace();
        }
    }

    // view shows by date
    private void viewShowButtonClicked() {

        Date showDate = getDateValue();
        if (showDate == null) {
            return;
        }

        MovieShow show = new MovieShow();
        show.setShowDate(showDate);

        Message msg = new Message();
        msg.setAction("GET_SHOWS_BY_DATE");
        msg.setShow(show);

        try {
            msg = sendMessage(msg);
            if (msg.getData() != null && !msg.getData().trim().isEmpty()) {
                allShows.setText(msg.getData());
            }
            else {
                allShows.setText("No shows found for that date.");
            }
        }
        catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Could not view shows.");
            ex.printStackTrace();
        }
    }

    // update show
    private void updateShowButtonClicked() {

        int showID = getIntValue(txtShowID, "Show ID");
        if (showID <= 0) {
            return;
        }

        int movieID = getIntValue(txtMovieID, "Movie ID");
        if (movieID <= 0) {
            return;
        }

        MovieShow show = getShowFromFields(true);
        if (show == null) {
            return;
        }

        Message msg = new Message();
        msg.setAction("UPDATE_SHOW");
        msg.setShowID(showID);
        msg.setMovieID(movieID);
        msg.setShow(show);

        try {
            msg = sendMessage(msg);

            if (msg.getAction().equals("UPDATE_SHOW_SUCCESS")) {
                JOptionPane.showMessageDialog(this, "Show updated.");
            }
            else {
                JOptionPane.showMessageDialog(this, "Show could not be updated. Check that the Movie ID exists.");
            }
        }
        catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Update show request failed.");
            ex.printStackTrace();
        }
    }

    // delete show
    private void deleteShowButtonClicked() {

        int showID = getIntValue(txtShowID, "Show ID");
        if (showID <= 0) {
            return;
        }

        Message msg = new Message();
        msg.setAction("DELETE_SHOW");
        msg.setShowID(showID);
        
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this show?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            msg = sendMessage(msg);

            if (msg.getAction().equals("DELETE_SHOW_SUCCESS")) {
                JOptionPane.showMessageDialog(this, "Show deleted.");
            }
            else {
                JOptionPane.showMessageDialog(this, "Show could not be deleted. It may not exist or may be linked to reservations.");
            }
        }
        catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Delete show request failed.");
            ex.printStackTrace();
        }
    }

    // builds a Movie object from the text fields
    private Movie getMovieFromFields() {

        String title = txtTitle.getText().trim();
        String review = txtReview.getText().trim();

        if (title.isEmpty() || title.length() > 100) {
            JOptionPane.showMessageDialog(this, "Title is required and must be 100 characters or less.");
            return null;
        }

        int ranking = getIntValue(txtRanking, "Ranking");
        if (ranking < 1 || ranking > 5) {
            JOptionPane.showMessageDialog(this, "Ranking must be between 1 and 5.");
            return null;
        }

        if (review.isEmpty() || review.length() > 255) {
            JOptionPane.showMessageDialog(this, "Review is required and must be 255 characters or less.");
            return null;
        }

        return new Movie(title, ranking, review);
    }

    // builds a MovieShow object from the text fields
    private MovieShow getShowFromFields(boolean needsSeats) {

        Date showDate = getDateValue();
        if (showDate == null) {
            return null;
        }

        Time showTime = getTimeValue();
        if (showTime == null) {
            return null;
        }

        int roomNumber = getIntValue(txtRoomNumber, "Room Number");
        if (roomNumber <= 0) {
            return null;
        }

        double price = getDoubleValue(txtPrice, "Price");
        if (price <= 0) {
            return null;
        }

        int seats = 40;

        if (needsSeats) {
            seats = getIntValue(txtAvailableSeats, "Available Seats");

            if (seats < 0 || seats > 40) {
                JOptionPane.showMessageDialog(this, "Available seats must be between 0 and 40.");
                return null;
            }
        }

        return new MovieShow(showDate, showTime, roomNumber, price, seats);
    }

    // gets date from show date field
    private Date getDateValue() {

        String dateText = txtShowDate.getText().trim();

        if (dateText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Show date is required.");
            return null;
        }

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            formatter.setLenient(false);
            return formatter.parse(dateText);
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Date must be in YYYY-MM-DD format.");
            return null;
        }
    }

    // gets time from show time field
    private Time getTimeValue() {

        String timeText = txtShowTime.getText().trim();

        if (timeText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Show time is required.");
            return null;
        }

        try {
            return Time.valueOf(timeText);
        }
        catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Time must be in HH:MM:SS format.");
            return null;
        }
    }

    // reads an integer field safely
    private int getIntValue(JTextField field, String fieldName) {

        try {
            return Integer.parseInt(field.getText().trim());
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, fieldName + " must be numeric.");
            return -1;
        }
    }

    // reads a decimal field safely
    private double getDoubleValue(JTextField field, String fieldName) {

        try {
            return Double.parseDouble(field.getText().trim());
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, fieldName + " must be numeric.");
            return -1;
        }
    }

    // clears all fields
    private void clearButtonClicked() {

        txtMovieID.setText("");
        txtTitle.setText("");
        txtRanking.setText("");
        txtReview.setText("");

        txtShowID.setText("");
        txtShowDate.setText("");
        txtShowTime.setText("");
        txtRoomNumber.setText("");
        txtPrice.setText("");
        txtAvailableSeats.setText("");

        allMovies.setText("");
        allShows.setText("");
    }

    // close application
    private void closeButtonClicked() {
        System.exit(0);
    }

    // main method
    public static void main(String[] args) {
        new DBAClient("localhost", 8000);
    }
}