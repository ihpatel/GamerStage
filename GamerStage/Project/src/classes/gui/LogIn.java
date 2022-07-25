package classes.gui;

import client.NetworkClient;
import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogIn extends JComponent implements Runnable {

    private NetworkClient nc;
    private LogIn logIn;
    private JLabel usernameLabel;
    private JTextField username;
    private JLabel passwordLabel;
    private JTextField password;
    private JButton loginButton;
    private JButton mainMenu;
    public JFrame frame = new JFrame("Log In");

    public LogIn(NetworkClient nc) {
        this.nc = nc;
    }

    public synchronized void run() {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                nc.closeConnection();
            }
        });

        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        logIn = new LogIn(nc);
        content.add(logIn, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        Box box = Box.createVerticalBox();

        username = new JTextField();
        username.addActionListener(actionListener);
        panel.add(username);
        box.add(username);

        usernameLabel = new JLabel("Enter Username:");
        panel.add(username);
        panel.add(usernameLabel);
        box.add(usernameLabel);
        box.add(username);

        passwordLabel = new JLabel("Enter Password:");
        password = new JTextField();
        panel.add(password);
        box.add(passwordLabel);
        box.add(password);

        loginButton = new JButton("Login");
        panel.add(loginButton);
        box.add(loginButton);
        loginButton.addActionListener(actionListener);

        mainMenu = new JButton("Main Menu");
        panel.add(mainMenu);
        box.add(mainMenu);
        mainMenu.addActionListener(actionListener);

        content.add(panel, BorderLayout.CENTER);
        frame.add(box);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == loginButton) {

                if (username.getText().isBlank() || password.getText().isBlank()) {
                    JOptionPane.showMessageDialog(frame, "Please Enter All Your Information", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (username.getText().trim().contains(" ")) {
                    JOptionPane.showMessageDialog(frame, "Please Remove Spaces From Username", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (password.getText().trim().contains(" ")) {
                    JOptionPane.showMessageDialog(frame, "Please Remove Spaces From Password", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                nc.login(username.getText(), password.getText());

                MessagingGUI m = new MessagingGUI(nc);

                while (nc.awaitingResponse) {
                    if (nc.error)
                        break;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ee) {
                        ee.printStackTrace();
                    }
                }

                    if (nc.error) {
                    JOptionPane.showMessageDialog(frame, nc.errormsg, "Error", JOptionPane.ERROR_MESSAGE);
                    nc.error = false;
                } else {
                    m.run();
                    frame.dispose();


                }
            }
            if (e.getSource() == mainMenu) {
                MainMenu m = new MainMenu(nc);
                m.run();
                frame.dispose();
            }
        }

    };
}
