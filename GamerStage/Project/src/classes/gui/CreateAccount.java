package classes.gui;

import classes.*;
import client.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateAccount extends JComponent implements Runnable {

    private NetworkClient nc;
    private CreateAccount account;
    private JLabel usernameLabel;
    private JTextField username;
    private JLabel nameLabel;
    private JTextField name;
    private JLabel passwordLabel;
    private JTextField password;
    private JLabel ageLabel;
    private JButton mainMenu;
    public JFrame frame = new JFrame("Create Account");
    private JButton create;
    private int ageGet;
    private JComboBox<String> age;


    public CreateAccount(NetworkClient nc) {
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
        account = new CreateAccount(nc);
        content.add(account, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        Box box = Box.createVerticalBox();

        username = new JTextField();
        panel.add(username);
        box.add(username);

        usernameLabel = new JLabel("Enter Username:");
        panel.add(username);
        panel.add(usernameLabel);
        box.add(usernameLabel);
        box.add(username);

        name = new JTextField();
        panel.add(name);
        box.add(name);

        nameLabel = new JLabel("Enter Full Name:");
        panel.add(name);
        panel.add(nameLabel);
        box.add(nameLabel);
        box.add(name);

        passwordLabel = new JLabel("Enter Password:");
        password = new JTextField();
        panel.add(password);
        box.add(passwordLabel);
        box.add(password);

        String[] ages = new String[87];
        for (int i = 0; i < ages.length; i++) {
            ages[i] = Integer.toString(13 + i);
        }

        ageLabel = new JLabel("Select Age:");
        age = new JComboBox<>(ages);
        panel.add(age);
        box.add(ageLabel);
        box.add(age);

        create = new JButton("Create Account");
        create.addActionListener(actionListener);
        panel.add(create);
        box.add(create);

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
            if (e.getSource() == create) {

                if (username.getText().isBlank() || name.getText().isBlank() ||
                password.getText().isBlank()) {
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

                ageGet = Integer.parseInt(String.valueOf(age.getSelectedItem()));

                nc.signUp(username.getText().trim(), name.getText(), ageGet, password.getText().trim());

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
