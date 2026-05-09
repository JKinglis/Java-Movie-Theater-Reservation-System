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
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class LoginClient extends JFrame implements ActionListener {

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

    // buttons
    private JButton btnLogin;
    private JButton btnSignUp;
    private JButton btnClose;

    // constructor
    public LoginClient(String hostname, int port) {

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
        setTitle("Login or Sign Up");
        setSize(450, 130);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // initialize GUI components
    private void initComponent() {

        txtUserName = new JTextField(15);
        txtPassword = new JPasswordField(15);

        btnLogin = new JButton("Login");
        btnSignUp = new JButton("Sign Up");
        btnClose = new JButton("Close");

        // button listeners
        btnLogin.addActionListener(this);
        btnSignUp.addActionListener(this);
        btnClose.addActionListener(this);
    }

    // arrange GUI layout
    private void doTheLayout() {

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));

        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(txtUserName);

        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(txtPassword);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnSignUp);
        buttonPanel.add(btnClose);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // button click handler
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnLogin) {
            loginButtonClicked();
        }
        else if (e.getSource() == btnSignUp) {
            signUpButtonClicked();
        }
        else if (e.getSource() == btnClose) {
            closeButtonClicked();
        }
    }

    // login button logic
    private void loginButtonClicked() {

        // get user input
        String username = txtUserName.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        // required field checks
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username is required.");
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password is required.");
            return;
        }

        // minimum length checks
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

        // create user object for login request
        User user = new User(
                username,
                password,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // create message object
        Message msg = new Message();

        msg.setAction("LOGIN");
        msg.setUser(user);

        try {
            // send login request to server
            clientOutputStream.writeObject(msg);
            clientOutputStream.flush();

            // receive response from server
            msg = (Message) clientInputStream.readObject();

            // open correct GUI based on role
            if (msg.getUser() != null &&
                    msg.getUser().getRole() == Role.DBA) {

                JOptionPane.showMessageDialog(this, "DBA login successful.");

                new DBAClient(hostname, port);
                dispose();
            }
            else if (msg.getUser() != null &&
                    msg.getUser().getRole() == Role.CUSTOMER) {

                JOptionPane.showMessageDialog(this, "Customer login successful.");

                new CustomerClient(hostname, port, msg.getUserID());
                dispose();
            }
            else {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password."
                );
            }
        }
        catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Login failed.");
            ex.printStackTrace();
        }
    }

    // open sign up window
    private void signUpButtonClicked() {

        new SignUpClient(hostname, port);
        dispose();
    }

    // close application
    private void closeButtonClicked() {

        System.exit(0);
    }

    // main method
    public static void main(String[] args) {

        new LoginClient("localhost", 8000);
    }
}