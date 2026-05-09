package FinalProject;

import java.awt.BorderLayout;
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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CustomerClient extends JFrame implements ActionListener {

    // server connection info
    private String hostname;
    private int port;

    // streams for client/server communication
    private ObjectOutputStream clientOutputStream;
    private ObjectInputStream clientInputStream;
    private Socket conn;

    // logged-in customer ID
    private int userID;

    // display areas
    private JTextArea showInfo;
    private JTextArea reservationInfo;

    // input fields
    private JTextField txtShowDate;
    private JTextField txtShowID;
    private JTextField txtReservationID;
    private JTextField txtNumTickets;
    private JTextField txtCCNumber;

    // buttons
    private JButton btnViewShows;
    private JButton btnReserve;
    private JButton btnViewReservations;
    private JButton btnCancelReservation;
    private JButton btnClear;
    private JButton btnClose;

    // constructor without user ID
    public CustomerClient(String hostname, int port) {
        this(hostname, port, 0);
    }

    // constructor with user ID
    public CustomerClient(String hostname, int port, int userID) {

        this.hostname = hostname;
        this.port = port;
        this.userID = userID;

        // connect to server
        try {
            conn = new Socket(hostname, port);

            clientOutputStream =
                    new ObjectOutputStream(conn.getOutputStream());

            clientInputStream =
                    new ObjectInputStream(conn.getInputStream());

            System.out.println("Connected to " + hostname + " on port " + port);
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Connection failed. Make sure the server is running.");
            return;
        }

        // build GUI
        initComponent();
        doTheLayout();

        setTitle("Customer Client");
        setSize(750, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // remind user if opened without login
        if (userID <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Warning: No customer account is logged in. Please login before reserving tickets."
            );
        }
    }

    // initialize GUI components
    private void initComponent() {

        showInfo = new JTextArea(10, 40);
        reservationInfo = new JTextArea(10, 40);

        showInfo.setEditable(false);
        reservationInfo.setEditable(false);

        txtShowDate = new JTextField(10);
        txtShowID = new JTextField(10);
        txtReservationID = new JTextField(10);
        txtNumTickets = new JTextField(10);
        txtCCNumber = new JTextField(10);

        btnViewShows = new JButton("View Shows");
        btnReserve = new JButton("Reserve");
        btnViewReservations = new JButton("View Reservations");
        btnCancelReservation = new JButton("Cancel Reservation");
        btnClear = new JButton("Clear");
        btnClose = new JButton("Close");

        btnViewShows.addActionListener(this);
        btnReserve.addActionListener(this);
        btnViewReservations.addActionListener(this);
        btnCancelReservation.addActionListener(this);
        btnClear.addActionListener(this);
        btnClose.addActionListener(this);
    }

    // arrange GUI layout
    private void doTheLayout() {

        JPanel topPanel = new JPanel(new GridLayout(5, 2, 5, 5));

        topPanel.add(new JLabel("Show Date (YYYY-MM-DD):"));
        topPanel.add(txtShowDate);

        topPanel.add(new JLabel("Show ID:"));
        topPanel.add(txtShowID);

        topPanel.add(new JLabel("Reservation ID:"));
        topPanel.add(txtReservationID);

        topPanel.add(new JLabel("Number of Tickets:"));
        topPanel.add(txtNumTickets);

        topPanel.add(new JLabel("Credit Card Number (5 digits):"));
        topPanel.add(txtCCNumber);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        centerPanel.add(new JScrollPane(showInfo));
        centerPanel.add(new JScrollPane(reservationInfo));

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(btnViewShows);
        buttonPanel.add(btnReserve);
        buttonPanel.add(btnViewReservations);
        buttonPanel.add(btnCancelReservation);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnClose);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // button click handler
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnViewShows) {
            viewShowsButtonClicked();
        }
        else if (e.getSource() == btnReserve) {
            reserveButtonClicked();
        }
        else if (e.getSource() == btnViewReservations) {
            viewReservationsButtonClicked();
        }
        else if (e.getSource() == btnCancelReservation) {
            cancelReservationButtonClicked();
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

    // view shows for a selected date
    private void viewShowsButtonClicked() {

        Date parsedDate = getDateValue();
        if (parsedDate == null) {
            return;
        }

        MovieShow show = new MovieShow();
        show.setShowDate(parsedDate);

        Message msg = new Message();
        msg.setAction("GET_SHOWS_BY_DATE");
        msg.setShow(show);

        try {
            msg = sendMessage(msg);

            if (msg.getData() != null && !msg.getData().trim().isEmpty()) {
                showInfo.setText(msg.getData());
            }
            else {
                showInfo.setText("No shows found for that date.");
            }
        }
        catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Could not view shows. Please check the server connection.");
            ex.printStackTrace();
        }
    }

    // reserve tickets
    private void reserveButtonClicked() {

        // make sure user logged in
        if (userID <= 0) {
            JOptionPane.showMessageDialog(this, "You must login before reserving tickets.");
            return;
        }

        int showID = getIntValue(txtShowID, "Show ID");
        if (showID <= 0) {
            return;
        }

        int numTickets = getIntValue(txtNumTickets, "Number of tickets");
        if (numTickets <= 0) {
            JOptionPane.showMessageDialog(this, "Number of tickets must be greater than 0.");
            return;
        }

        String ccNumber = txtCCNumber.getText().trim();

        // credit card must be exactly 5 digits
        if (!ccNumber.matches("\\d{5}")) {
            JOptionPane.showMessageDialog(this, "Credit card number must be exactly 5 digits.");
            return;
        }

        Reservation reservation = new Reservation(null, null, numTickets);

        MovieShow show =
                new MovieShow(
                        new Date(),
                        new Time(System.currentTimeMillis()),
                        0,
                        0.0,
                        0
                );

        Message msg = new Message();

        msg.setAction("CREATE_RESERVATION");
        msg.setUserID(userID);
        msg.setShowID(showID);
        msg.setReservation(reservation);
        msg.setShow(show);
        msg.setCcNumber(ccNumber);

        try {
            msg = sendMessage(msg);

            if (msg.getAction().equals("CREATE_RESERVATION_SUCCESS")) {

                JOptionPane.showMessageDialog(
                        this,
                        "Reservation created. Reservation ID: "
                                + msg.getReservationID()
                );

                reservationInfo.setText(
                        "Reservation created successfully.\n"
                        + "Reservation ID: " + msg.getReservationID() + "\n"
                        + "Show ID: " + showID + "\n"
                        + "Tickets: " + numTickets + "\n"
                );
            }
            else if (msg.getAction().equals("NO_SEATS_AVAILABLE")) {
                JOptionPane.showMessageDialog(
                        this,
                        "Not enough seats are available for that show."
                );
            }
            else if (msg.getAction().equals("CREATE_PAYMENT_FAILED")) {
                JOptionPane.showMessageDialog(
                        this,
                        "Payment failed. Reservation was not completed."
                );
            }
            else if (msg.getAction().equals("CREATE_RESERVATION_FAILED")) {
                JOptionPane.showMessageDialog(
                        this,
                        "Reservation could not be created. Check that the Show ID exists."
                );
            }
            else {
                JOptionPane.showMessageDialog(
                        this,
                        "Reservation failed. Please check your information and try again."
                );
            }
        }
        catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Reservation request failed. Please check the server connection.");
            ex.printStackTrace();
        }
    }

 // view customer reservations for a selected date
    private void viewReservationsButtonClicked() {

        // make sure user logged in
        if (userID <= 0) {
            JOptionPane.showMessageDialog(this, "You must login before viewing reservations.");
            return;
        }

        Date showDate = getDateValue();
        if (showDate == null) {
            return;
        }

        MovieShow show = new MovieShow();
        show.setShowDate(showDate);

        Message msg = new Message();

        msg.setAction("GET_RESERVATIONS_BY_USER_AND_DATE");
        msg.setUserID(userID);
        msg.setShow(show);

        try {
            msg = sendMessage(msg);

            if (msg.getData() != null && !msg.getData().trim().isEmpty()) {
                reservationInfo.setText(msg.getData());
            }
            else {
                reservationInfo.setText("No reservations found for that date.");
            }
        }
        catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Could not view reservations. Please check the server connection.");
            ex.printStackTrace();
        }
    }

    // cancel reservation
    private void cancelReservationButtonClicked() {

        int reservationID = getIntValue(txtReservationID, "Reservation ID");
        if (reservationID <= 0) {
            return;
        }

        int showID = getIntValue(txtShowID, "Show ID");
        if (showID <= 0) {
            return;
        }

        // confirm before canceling
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel this reservation?",
                "Confirm Cancel",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Message msg = new Message();

        msg.setAction("CANCEL_RESERVATION");
        msg.setReservationID(reservationID);
        msg.setShowID(showID);

        try {
            msg = sendMessage(msg);

            if (msg.getAction().equals("CANCEL_RESERVATION_SUCCESS")) {

                JOptionPane.showMessageDialog(this, "Reservation canceled.");
                reservationInfo.setText("Reservation canceled successfully.");
            }
            else if (msg.getAction().equals("CANCEL_TOO_LATE")) {

                JOptionPane.showMessageDialog(
                        this,
                        "Reservation cannot be canceled less than 1 hour before showtime."
                );
            }
            else if (msg.getAction().equals("CANCEL_RESERVATION_FAILED")) {

                JOptionPane.showMessageDialog(
                        this,
                        "Reservation could not be canceled. Check that the Reservation ID exists."
                );
            }
            else {

                JOptionPane.showMessageDialog(
                        this,
                        "Reservation could not be canceled. Please check the Reservation ID and Show ID."
                );
            }
        }
        catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Cancel request failed. Please check the server connection.");
            ex.printStackTrace();
        }
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

    // clear text fields
    private void clearButtonClicked() {

        txtShowDate.setText("");
        txtShowID.setText("");
        txtReservationID.setText("");
        txtNumTickets.setText("");
        txtCCNumber.setText("");

        showInfo.setText("");
        reservationInfo.setText("");
    }

    // close application
    private void closeButtonClicked() {
        System.exit(0);
    }

    // main method
    public static void main(String[] args) {
        new CustomerClient("localhost", 8000);
    }
}