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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;



public class SignUpClient extends JFrame implements ActionListener {

    // server connection info
    private String hostname;
    private int port;

    // streams for client/server communication
    private ObjectOutputStream clientOutputStream;
    private ObjectInputStream clientInputStream;
    private Socket conn;

    // GUI fields
    private JTextField txtUserName;
    private JPasswordField txtPassword;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtAddress;
    private JTextField txtEmail;
    private JTextField txtPhone;

    // buttons
    private JButton btnSignUp;
    private JButton btnBack;
    private JButton btnClose;

    // constructor
    public SignUpClient(String hostname, int port) {

        this.hostname = hostname;
        this.port = port;

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
            JOptionPane.showMessageDialog(this, "Connection failed.");
            return;
        }

        // build GUI
        initComponent();
        doTheLayout();

        // window settings
        setTitle("New User Sign Up");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // initialize GUI components
    private void initComponent() {

        txtUserName = new JTextField(15);
        txtPassword = new JPasswordField(15);
        txtFirstName = new JTextField(15);
        txtLastName = new JTextField(15);
        txtAddress = new JTextField(15);
        txtEmail = new JTextField(15);
        txtPhone = new JTextField(15);

        btnSignUp = new JButton("Sign Up");
        btnBack = new JButton("Back");
        btnClose = new JButton("Close");

        // button listeners
        btnSignUp.addActionListener(this);
        btnBack.addActionListener(this);
        btnClose.addActionListener(this);
    }

    // arrange GUI layout
    private void doTheLayout() {

        JPanel mainPanel = new JPanel(new GridLayout(7, 2, 5, 5));

        mainPanel.add(new JLabel("Username:"));
        mainPanel.add(txtUserName);

        mainPanel.add(new JLabel("Password:"));
        mainPanel.add(txtPassword);

        mainPanel.add(new JLabel("First Name:"));
        mainPanel.add(txtFirstName);

        mainPanel.add(new JLabel("Last Name:"));
        mainPanel.add(txtLastName);

        mainPanel.add(new JLabel("Address:"));
        mainPanel.add(txtAddress);

        mainPanel.add(new JLabel("Email:"));
        mainPanel.add(txtEmail);

        mainPanel.add(new JLabel("Phone:"));
        mainPanel.add(txtPhone);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(btnSignUp);
        buttonPanel.add(btnBack);
        buttonPanel.add(btnClose);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // button click handler
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnSignUp) {
            signUpButtonClicked();
        }
        else if (e.getSource() == btnBack) {
            backButtonClicked();
        }
        else if (e.getSource() == btnClose) {
            closeButtonClicked();
        }
    }

    // sign up button logic
    private void signUpButtonClicked() {

        // get input
        String username = txtUserName.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String address = txtAddress.getText().trim();
        String email = txtEmail.getText().trim();
        String phoneText = txtPhone.getText().trim();

        // validation checks
        if (username.isEmpty() || password.isEmpty() ||
                firstName.isEmpty() || lastName.isEmpty() ||
                address.isEmpty() || email.isEmpty() ||
                phoneText.isEmpty()) {

            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        if (username.length() < 4) {
            JOptionPane.showMessageDialog(
                    this,
                    "Username must be at least 4 characters."
            );
            return;
        }

        if (password.length() < 4) {
            JOptionPane.showMessageDialog(
                    this,
                    "Password must be at least 4 characters."
            );
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.");
            return;
        }

        if (!phoneText.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Phone number must be 10 digits."
            );
            return;
        }

        Long phone = Long.parseLong(phoneText);

        // create new customer user
        User user = new User(
                username,
                password,
                Role.CUSTOMER,
                firstName,
                lastName,
                address,
                email,
                phone
        );

        // create message
        Message msg = new Message();

        msg.setAction("CREATE_USER");
        msg.setUser(user);

        try {
            // send sign up request
            clientOutputStream.writeObject(msg);
            clientOutputStream.flush();

            // receive response
            msg = (Message) clientInputStream.readObject();

            if (msg.getAction().equals("CREATE_USER_SUCCESS")) {
                JOptionPane.showMessageDialog(
                        this,
                        "Account created successfully."
                );

                new LoginClient(hostname, port);
                dispose();
            }
            else {
                JOptionPane.showMessageDialog(
                        this,
                        "Account could not be created. Username may already exist."
                );
            }
        }
        catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Sign up failed.");
            ex.printStackTrace();
        }
    }

    // return to login window
    private void backButtonClicked() {

        new LoginClient(hostname, port);
        dispose();
    }

    // close application
    private void closeButtonClicked() {

        System.exit(0);
    }

    // main method
    public static void main(String[] args) {

        new SignUpClient("localhost", 8000);
    }
}