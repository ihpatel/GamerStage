package classes.gui;

import classes.*;
import client.*;

import java.util.UUID;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InfoGUI extends JComponent implements Runnable {

	// ^ name age
	private NetworkClient nc;
	private MessagingGUI msg;
	private UUID id;
	private Message message;
	private Chat chat;
	private User user;
	private int option;
	private InfoGUI info;
	private JButton delete;
	private JButton commit;

	private JLabel theMessageLabel;
	private JTextField theMessage;
	private JLabel nameLabel;
	private JTextField name;
	private JLabel ageLabel;
	private JLabel passwordLabel;
	private JTextField password;

	public JFrame frame = new JFrame("Info");

	private int ageGet;
	private JComboBox<String> agef;

	// InfoGUI ig = new InfoGUI(nc, currentChat, e.getActionCommand);

	public InfoGUI(NetworkClient nc, Chat chat, String uuid) {
		this.nc = nc;
		this.chat = chat;
		this.message = nc.currentUser.getChats().get(chat.getId()).getMessages().get(UUID.fromString(uuid));
		this.option = 1;
	}

	public InfoGUI(NetworkClient nc) {
		this.nc = nc;
		this.user = nc.currentUser;
		this.option = 2;
	}

	public synchronized void run() {

		// frame.addWindowListener(new WindowAdapter() {
		// 	@Override
		// 	public void windowClosing(WindowEvent e) {
		// 		nc.closeConnection();
		// 	}
		// });

		Container content = frame.getContentPane();
		content.setLayout(new BorderLayout());
		info = new InfoGUI(nc);
		content.add(info, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		Box box = Box.createVerticalBox();

		if (option == 1 || this.user == null) {
			nc.requestInfo(this.message.getSenderUsername());
			
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
			}

			JLabel usernameLabel = new JLabel("USERNAME: " + this.message.getSenderUsername());
			JLabel nameLabel = new JLabel("NAME: "+ nc.requestedName);
			JLabel age = new JLabel("AGE: " + nc.requestedAge);
		    panel.add(nameLabel);
		    box.add(nameLabel);
		    panel.add(usernameLabel);
            box.add(usernameLabel);
		    panel.add(age);
		    box.add(age);

			if (message.getSenderUsername().equals(nc.currentUser.getUsername())) {
			theMessageLabel = new JLabel("Editing Message...");
			theMessage = new JTextField();
			theMessage.addActionListener(actionListener);
			panel.add(theMessage);
			box.add(theMessage);

			panel.add(theMessage);
			panel.add(theMessageLabel);
			box.add(theMessageLabel);
			box.add(theMessage);

			theMessage.setText(message.getMessage());

			commit = new JButton("Commit Changes");
			panel.add(commit);
			box.add(commit);
			commit.addActionListener(actionListener);

			delete = new JButton("Delete Message");
			panel.add(delete);
			box.add(delete);
			delete.addActionListener(actionListener);
			} else {
				JLabel messagel = new JLabel("MESSAGE: " + message.getMessage());
				panel.add(messagel);
				box.add(messagel);
			}
		} else {
			JLabel username = new JLabel(user.getUsername() + ": ");
			panel.add(username);
			box.add(username);

			name = new JTextField(user.getName());
			name.setText(nc.currentUser.getName());
			

			name.addActionListener(actionListener);
			panel.add(name);
			box.add(name);

			nameLabel = new JLabel("Name: ");
			panel.add(name);
			panel.add(nameLabel);
			box.add(nameLabel);
			box.add(name);

			String[] ages = new String[87];
            for (int i = 0; i < ages.length; i++) {
                ages[i] = Integer.toString(13 + i);
            }

			ageLabel = new JLabel("Age: ");
			agef = new JComboBox<>(ages);
			agef.setSelectedItem(Integer.toString(nc.currentUser.getAge()));
			panel.add(agef);
			box.add(ageLabel);
			box.add(agef);

			passwordLabel = new JLabel("Password: ");
			password = new JTextField();
			password.setText(nc.currentUser.getPassword());
			password.addActionListener(actionListener);
			panel.add(password);
			box.add(passwordLabel);
			box.add(password);

			commit = new JButton("Commit Changes");
			commit.addActionListener(actionListener);
			panel.add(commit);
			box.add(commit);

			delete = new JButton("Remove Account");
			panel.add(delete);
			box.add(delete);
			delete.addActionListener(actionListener);
		}

		content.add(panel, BorderLayout.CENTER);
		frame.add(box);
		frame.setSize(600, 400);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == commit) {
                    if (option == 1) {
                        	nc.editMessage(chat.getId().toString(), message.getId().toString(),
                            theMessage.getText());

                        if (nc.error) {
                            JOptionPane.showMessageDialog(frame, nc.errormsg, "Error", JOptionPane.ERROR_MESSAGE);
                            nc.error = false;
                        } else {
                            frame.dispose();
                        }
                    } else {
                        
                        if (password.getText().contains(" ")) {
                            JOptionPane.showMessageDialog(frame, "Please Do Not Include Any Spaces", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        ageGet = Integer.parseInt(String.valueOf(agef.getSelectedItem()));

                        nc.editName(name.getText());
                        nc.currentUser.setName(name.getText());

                        nc.editAge(Integer.toString(ageGet));
                        nc.currentUser.setAge(ageGet);

                        nc.editPassword(nc.currentUser.getPassword(), password.getText());
                        nc.currentUser.setPassword(password.getText());
                        frame.dispose();
                    }
    
                }
    
                if (e.getSource() == delete) {
                    if (option == 1) {
                        nc.removeMessage( chat.getId().toString(), message.getId().toString());
                        frame.dispose();
                    } else {
                        String[] options = {"Yes", "No"};
                        int num = JOptionPane.showOptionDialog(null, "Are you sure?", "Social Network",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                                null, options, options[1]);
                        if (num == 0) {
							nc.deleteAccount();
							frame.dispose();
                        }

                    }
                }


            }
        };
    }