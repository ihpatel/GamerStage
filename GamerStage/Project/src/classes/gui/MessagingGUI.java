package classes.gui;

import classes.Chat;
import classes.DirectMessage;
import classes.Group;
import classes.Message;
import client.NetworkClient;
import server.errors.NotFoundException;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;

public class MessagingGUI extends JComponent implements Runnable {

    private NetworkClient nc;
    public static Message currentMessage;
    public static Chat currentChat;
    private JFrame frame;
    private static JButton chatIcon;
    private JPanel chats;
    private JScrollPane scroll;
    private JScrollPane scroller;
    private JButton msg;
    private Container content;
    private Border blueOutline;
    private JPanel messenger;
    private JPanel messages;
    private JPanel top;
    private JPanel controlPanel;
    // private HashMap<JTextArea, UUID> currentMessages;
    private JPanel type;
    private JButton send;
    private JButton signout;
    private JButton createGroup;
    private JButton createDM;
    private JButton inviteToGroup;
    private JButton leaveGroup;
    private JButton viewAccount;
    private JTextField text;
    private Timer timer;

    public MessagingGUI(NetworkClient nc) {
        this.nc = nc;
    }

    public void refreshDisplay() {
        refreshChats();
        if (currentChat != null) {
            currentChat = nc.currentUser.getChats().get(currentChat.getId());
            refreshMessages();
        } else {
            if (messenger != null)
                content.remove(messenger);
            messenger = new JPanel(new BorderLayout());
            messenger.add(top, BorderLayout.NORTH);
            content.add(messenger);
            frame.repaint();
            frame.setVisible(true);
        }
    }

    public void refreshChats() {
        chats = new JPanel();
        chats.setLayout(new BoxLayout(chats, BoxLayout.Y_AXIS));

        if (nc.currentUser.getChats().size() != 0) {
            for (Chat chat : nc.currentUser.getChats().values()) {
                if (chat instanceof Group) {
                    Group g = (Group) chat;
                    chatIcon = new JButton(g.getGroupName());
                    chatIcon.setActionCommand(g.getId().toString());
                } else {
                    DirectMessage dm = (DirectMessage) chat;
                    if (dm.getParticipants().getFirst().equals(nc.currentUser.getUsername())) {
                        chatIcon = new JButton(dm.getParticipants().getLast());
                        chatIcon.setActionCommand(dm.getId().toString());
                    } else {
                        chatIcon = new JButton(dm.getParticipants().getFirst());
                        chatIcon.setActionCommand(dm.getId().toString());
                    }
                }
                chatIcon.setMaximumSize(new Dimension(200, 200));
                chatIcon.setPreferredSize(new Dimension(200, 200));
                chatIcon.addActionListener(newChat);

                chats.add(chatIcon);
            }
        }

        if (scroll != null) {
            content.remove(scroll);
        }

        scroll = new JScrollPane(chats, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setMaximumSize(new Dimension(200, content.HEIGHT));

        content.add(scroll, BorderLayout.WEST);

        if (currentChat != null) {

            content.add(type, BorderLayout.SOUTH);
        }

        frame.repaint();
        frame.setVisible(true);
    }

    public void refreshMessages() {

        JPanel messages = new JPanel();
        JPanel messages1 = new JPanel();
        JPanel messages2 = new JPanel();
        messages.setLayout(new GridLayout(0, 2));
        messages1.setLayout(new BoxLayout(messages1, BoxLayout.Y_AXIS));
        messages2.setLayout(new BoxLayout(messages2, BoxLayout.Y_AXIS));

        LinkedList<Message> sorted = new LinkedList<>(currentChat.getMessages().values());
        Collections.sort(sorted, Comparator.comparing(Message::getTimeStamp));

        for (Message message : sorted) {
            msg = new JButton("<html>" + message.getMessage() + "</html>");
            
            Border name;
            if (!currentChat.getParticipants().contains(message.getSenderUsername()) &&
                !message.getSenderUsername().equals("SERVER")) {
                name = BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), message.getSenderUsername() + " |LEFT|");
            } else {
                name = BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), message.getSenderUsername());
                if (!message.getSenderUsername().equals("SERVER")) {
                    msg.setActionCommand(message.getId().toString());
                    msg.addActionListener(messageEdit);
                }
            }

            msg.setBorder(name);
            msg.setContentAreaFilled(false);
            msg.setPreferredSize(new Dimension(350, 100));
            msg.setMaximumSize(new Dimension(350, 100));

            if (message.getSenderUsername().equals(nc.currentUser.getUsername())) {
                messages2.add(msg);
                messages1.add(Box.createRigidArea(new Dimension(0, 100)));
            } else {
                messages1.add(msg);
                messages2.add(Box.createRigidArea(new Dimension(0, 100)));
            }

            messages1.add(Box.createRigidArea(new Dimension(0, 30)));
            messages2.add(Box.createRigidArea(new Dimension(0, 30)));

        }

        if (messenger != null) {
            content.remove(messenger);
            messenger = new JPanel(new BorderLayout());
        }

        messages.add(messages1);
        messages.add(messages2);

        scroller = new JScrollPane(messages, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setMaximumSize(new Dimension(200, content.HEIGHT));

        messenger.add(top, BorderLayout.NORTH);
        messenger.add(scroller, BorderLayout.CENTER);
        messenger.add(type, BorderLayout.SOUTH);

        content.add(messenger, BorderLayout.CENTER);
        frame.repaint();
        frame.setVisible(true);

    }

    @Override
    public void run() {
        frame = new JFrame();
        frame.setTitle(nc.currentUser.getUsername() + " LOGGED IN");

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                nc.signOut();
                nc.closeConnection();
            }
        });
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        content = frame.getContentPane();

        // get the messages from here
        type = new JPanel();
        top = new JPanel();
        text = new JTextField(40);
        send = new JButton("Send");
        signout = new JButton("Signout");
        createGroup = new JButton("Create A Group");
        createDM = new JButton("Create A DM");
        inviteToGroup = new JButton("Invite To Group");
        leaveGroup = new JButton("Leave Chat");
        viewAccount = new JButton("Check Account");
        messenger = new JPanel(new BorderLayout());

        send.addActionListener(buttonListener);
        createGroup.addActionListener(buttonListener);
        createDM.addActionListener(buttonListener);
        inviteToGroup.addActionListener(buttonListener);
        leaveGroup.addActionListener(buttonListener);
        viewAccount.addActionListener(buttonListener);
        signout.addActionListener(buttonListener);

        top.add(createGroup);
        top.add(createDM);
        top.add(inviteToGroup);
        top.add(leaveGroup);
        top.add(viewAccount);
        top.add(signout);
        type.add(text);
        type.add(send);

        messenger.add(top);
        this.refreshDisplay();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (nc.newChat) {
                    refreshDisplay();
                    nc.newChat = false;
                }
                if (nc.closeGUI) {
                    frame.dispose();
                    timer.cancel();
                }
            }
        }, 0, 1000);
    }

    ActionListener buttonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            nc.error = false;

            if (e.getSource() == send) {
                String msgToSend = text.getText();
                if (msgToSend.isBlank()) {
                    JOptionPane.showMessageDialog(frame, "Please type a message", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                text.setText("");
                nc.sendMessage(msgToSend);
                while (nc.awaitingResponse) {
                    if (nc.error)
                        break;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ee) {
                        ee.printStackTrace();
                    }
                }
                if (!nc.error)
                    refreshDisplay();
                else
                    JOptionPane.showMessageDialog(frame, nc.errormsg, "Error", JOptionPane.ERROR_MESSAGE);

            } else if (e.getSource() == createGroup) {
                String name = JOptionPane.showInputDialog(frame, "Please Enter The Group Name");
                if (name == null)
                    return;
                if (name.isBlank()) {
                    JOptionPane.showMessageDialog(frame, "Please Enter A Name", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (name.contains(" ")) {
                    JOptionPane.showMessageDialog(frame, "Please Remove Spaces", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                nc.createGroup(name.trim());
                while (nc.awaitingResponse) {
                    if (nc.error)
                        break;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ee) {
                        ee.printStackTrace();
                    }
                }
                if (!nc.error)
                    refreshDisplay();
                else
                    JOptionPane.showMessageDialog(frame, nc.errormsg, "Error", JOptionPane.ERROR_MESSAGE);
            } else if (e.getSource() == createDM) {
                String name = JOptionPane.showInputDialog(frame, "Please Enter The Username Of The Other User");
                if (name == null)
                    return;
                if (name.isBlank()) {
                    JOptionPane.showMessageDialog(frame, "Please Enter A Name", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (name.contains(" ")) {
                    JOptionPane.showMessageDialog(frame, "Please Remove Spaces", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (name.equals(nc.currentUser.getUsername())) {
                    JOptionPane.showMessageDialog(frame, "You Can't Message Yourself!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                nc.createDm(name.trim());
                while (nc.awaitingResponse) {
                    if (nc.error)
                        break;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ee) {
                        ee.printStackTrace();
                    }
                }
                if (!nc.error)
                    refreshDisplay();
                else
                    JOptionPane.showMessageDialog(frame, nc.errormsg, "Error", JOptionPane.ERROR_MESSAGE);
            } else if (e.getSource() == viewAccount) {
                InfoGUI ig = new InfoGUI(nc);
                ig.run();
            } else if (e.getSource() == inviteToGroup) {
                if (!(currentChat instanceof Group)) {
                    JOptionPane.showMessageDialog(frame, "Please Select A Group", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String name = JOptionPane.showInputDialog(frame, "Please Enter The Username Of The Other User");
                if (name == null)
                    return;

                if (name.isBlank()) {
                    JOptionPane.showMessageDialog(frame, "Please Enter A Name", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (name.contains(" ")) {
                    JOptionPane.showMessageDialog(frame, "Please Remove Spaces", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (name.equals(nc.currentUser.getUsername())) {
                    JOptionPane.showMessageDialog(frame, "You Can't Add Yourself!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                nc.addUser(name);
                while (nc.awaitingResponse) {
                    if (nc.error)
                        break;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ee) {
                        ee.printStackTrace();
                    }
                }
                if (nc.error)
                    JOptionPane.showMessageDialog(frame, nc.errormsg, "Error", JOptionPane.ERROR_MESSAGE);

            } else if (e.getSource() == leaveGroup) {
                if (currentChat == null) {
                    JOptionPane.showMessageDialog(frame, "Please Select A Chat", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                nc.removeChat(currentChat.getId());
                currentChat = null;
                while (nc.awaitingResponse) {
                    if (nc.error)
                        break;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ee) {
                        ee.printStackTrace();
                    }
                }
                if (nc.error)
                    JOptionPane.showMessageDialog(frame, nc.errormsg, "Error", JOptionPane.ERROR_MESSAGE);
                else
                    refreshDisplay();

            } else if (e.getSource() == signout) {
                nc.signOut();
                MainMenu m = new MainMenu(nc);
                m.run();
                frame.dispose();
            }
        }
    };

    ActionListener messageEdit = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            UUID messageID = UUID.fromString(e.getActionCommand());
            InfoGUI ig = new InfoGUI(nc, currentChat, messageID.toString());
            ig.run();
        }
    };

    ActionListener newChat = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String chatName = e.getActionCommand();
            UUID id = UUID.fromString(chatName);
            currentChat = nc.currentUser.getChats().get(id);
            while (nc.awaitingResponse) {
                if (nc.error)
                    break;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ee) {
                    ee.printStackTrace();
                }
            }
            if (!nc.error)
                refreshDisplay();
            else
                JOptionPane.showMessageDialog(frame, nc.errormsg, "Error", JOptionPane.ERROR_MESSAGE);

        }
    };
}
