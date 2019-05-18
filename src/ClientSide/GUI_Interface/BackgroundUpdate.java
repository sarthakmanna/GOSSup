package ClientSide.GUI_Interface;

import ClientSide.ClientAPI;
import ClientSide.Message;

import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class BackgroundUpdate extends Thread {
    JComboBox cbOnline, cbAll, cbFriends;
    JTextField tfSelected;
    JTextArea taChat;
    JCheckBox cbAutoScroll;
    ClientAPI CLIENT_API;

    public BackgroundUpdate (JComboBox cbOnline, JComboBox cbAll, JComboBox cbFriends,
                             JTextField tfSelected, JTextArea taChat, JCheckBox cbAutoScroll, ClientAPI CLIENT_API) {
        this.cbOnline = cbOnline;
        this.cbAll = cbAll;
        this.cbFriends = cbFriends;
        this.tfSelected = tfSelected;
        this.taChat = taChat;
        this.cbAutoScroll = cbAutoScroll;
        this.CLIENT_API = CLIENT_API;
    }

    public void run() {
        while (true) {
            try {
                refreshOnlinePeers();
                refreshAllPeers();
                refreshFriendlyPeers();
                refreshChat();

                Thread.sleep(50);
            } catch (Exception e) {
            }
        }
    }

    void refreshOnlinePeers() throws Exception {
        if (cbOnline.isPopupVisible()) return;
        int selectedIndex = cbOnline.getSelectedIndex();

        ArrayList<String> online = CLIENT_API.getOnlineUsernames();
        String[] onlinePeers = new String[online.size()];

        for (int i = 0; i < onlinePeers.length; ++i) {
            onlinePeers[i] = online.get(i);
        }

        DefaultComboBoxModel<String> onlineModel = new DefaultComboBoxModel(onlinePeers);
        cbOnline.setModel(onlineModel);

        cbOnline.setSelectedIndex(selectedIndex);

        if (cbOnline.isEnabled()) {
            tfSelected.setText(cbOnline.getSelectedItem().toString());
        }
    }

    void refreshAllPeers() throws Exception {
        if (cbAll.isPopupVisible()) return;
        int selectedIndex = cbAll.getSelectedIndex();

        ArrayList<String> all = CLIENT_API.getAllUsernames();
        String[] allPeers = new String[all.size()];

        for (int i = 0; i < allPeers.length; ++i) {
            allPeers[i] = all.get(i);
        }

        DefaultComboBoxModel<String> allModel = new DefaultComboBoxModel(allPeers);
        cbAll.setModel(allModel);

        cbAll.setSelectedIndex(selectedIndex);

        if (cbAll.isEnabled()) {
            tfSelected.setText(cbAll.getSelectedItem().toString());
        }
    }

    void refreshFriendlyPeers() throws Exception {
        if (cbFriends.isPopupVisible()) return;
        int selectedIndex = cbFriends.getSelectedIndex();

        ArrayList<String> friends = CLIENT_API.getRecentUsernames();
        String[] allPeers = new String[friends.size()];

        for (int i = 0; i < allPeers.length; ++i) {
            allPeers[i] = friends.get(i);
        }

        DefaultComboBoxModel<String> allModel = new DefaultComboBoxModel(allPeers);
        cbFriends.setModel(allModel);

        cbFriends.setSelectedIndex(selectedIndex);

        if (cbFriends.isEnabled()) {
            tfSelected.setText(cbFriends.getSelectedItem().toString());
        }
    }

    void refreshChat() throws Exception {
        if (tfSelected.getText().equals("")) {
            taChat.setText("");
        } else {
            taChat.setText(formatToText(CLIENT_API.getPersonalChatHistory(
                    tfSelected.getText())));

            if (cbAutoScroll.isSelected()) {
                taChat.setCaretPosition(taChat.getDocument().getLength());
            }
        }
    }

    public String formatToText(ArrayList<Message> messageList) {
        StringBuilder sb = new StringBuilder();

        for (Message message : messageList) {
            boolean shift = message.isSent();

            //sb.append(allign(message.getUserInvolved(), shift)).append("\n");
            sb.append(allign("[" + CLIENT_API.DATE_FORMAT.format(message.getTimeStamp()) + "]",
                    shift)).append("\n");
            sb.append(allign(message.getMessage(), shift)).append("\n");
            sb.append("\n");
        }

        return sb.toString();
    }

    final int lineCapacity = 30, shiftAmount = 25;

    String allign(String str, boolean toShift) {
        StringBuilder sb = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(str);

        if (toShift) {
            for (int i = 0; i < shiftAmount; ++i) {
                sb.append(" ");
            }
        }

        int lenSoFar = 0;
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();

            if (lenSoFar > 0 && lenSoFar + word.length() + 1 > lineCapacity) {
                sb.deleteCharAt(sb.length() - 1);
                sb.append("\n");
                lenSoFar = 0;

                if (toShift) {
                    for (int i = 0; i < shiftAmount; ++i) {
                        sb.append(" ");
                    }
                }
            }

            sb.append(word).append(" ");
            lenSoFar += word.length() + 1;
        }

        return sb.toString();
    }
}