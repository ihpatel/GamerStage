package classes.gui;

import client.NetworkClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;

public class MainMenu extends JComponent implements Runnable {

    private NetworkClient nc;
    private MainMenu mainMenu;
    private JButton createAccount;
    private JButton logIn;
    public JFrame frame = new JFrame("Sign In");

    public MainMenu(NetworkClient nc) {this.nc = nc;}

    public static int exit() {
        String[] options = {"Yes", "No"};
        int num = JOptionPane.showOptionDialog(null, "Would You to Continue?", "Social Network",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        return num;
        //add log out button in MessagingGUI
        //set receiveServerInput in the client to false
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
        mainMenu = new MainMenu(nc);
        content.add(mainMenu, BorderLayout.CENTER);

        JPanel panel = new JPanel();

        createAccount = new JButton("Create Account");
        createAccount.setLocation(300, 100);
        createAccount.addActionListener(actionListener);
        panel.add(createAccount, BorderLayout.CENTER);

        logIn = new JButton("Log In");
        logIn.addActionListener(actionListener);
        panel.add(logIn, BorderLayout.CENTER);

        content.setLayout(new BorderLayout());
        content.add(panel, BorderLayout.CENTER);

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == logIn) {
                LogIn l = new LogIn(nc);
                l.run();
                frame.dispose();
            }
            if (e.getSource() == createAccount) {
                CreateAccount c = new CreateAccount(nc);
                c.run();
                frame.dispose();
            }
        }
    };


}
