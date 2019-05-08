import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class Server extends Thread {
    static final HashMap<String, ClientDetails> DATABASE = new HashMap<>();
    static final HashSet<String> ACTIVE_CLIENTS = new HashSet<>();

    final int SEND = 1, REFRESH_ALL = 2, REFRESH_PERSONAL = 3;
    final int BROADCAST = 4, GET_ALL_IPs = 5, GET_CONN_IPs = 6;

    Socket clientSocket;
    String MAC_id;
    ClientDetails clientDetails;

    DataInputStream inputStream;
    DataOutputStream outputStream;

    Server(Socket client) throws Exception {
        clientSocket = client;
        MAC_id = getMAC_id(clientSocket.getRemoteSocketAddress().toString());

        DATABASE.putIfAbsent(MAC_id, new ClientDetails(MAC_id));
        clientDetails = DATABASE.get(MAC_id);

        inputStream = new DataInputStream(client.getInputStream());
        outputStream = new DataOutputStream(client.getOutputStream());
    }

    @Override
    public void run() {
        try {
            ACTIVE_CLIENTS.add(clientDetails.getMAC_id());

            while (clientSocket.isConnected()) {
                int choice = Integer.parseInt(inputStream.readUTF().trim());

                switch (choice) {
                    case SEND: sendMessage(); break;
                    case REFRESH_ALL: refreshAllChatHistory(); break;
                    case REFRESH_PERSONAL: refreshPersonalChatHistory(); break;
                    case BROADCAST: broadcastMessage(); break;
                    case GET_ALL_IPs: getRegisteredIPs(); break;
                    case GET_CONN_IPs: getConnectedIPs(); break;
                    default: System.out.println("Wrong Choice !!!");
                }

                System.out.println("\n" + DATABASE + "\n");
            }
            System.out.println("Device with MAC " + MAC_id + " has disconnected.");

            inputStream.close();
            outputStream.close();
            clientSocket.close();
        } catch (Exception e) {
        } finally {
            ACTIVE_CLIENTS.remove(clientDetails.getMAC_id());
        }
    }

    void sendMessage() throws Exception {
        String destMAC = getMAC_id(inputStream.readUTF());
        String timeStamp = ServerSide.DATE_FORMAT.format(new Date());
        String message = inputStream.readUTF();

        clientDetails.sendMessage(destMAC, timeStamp, message);

        DATABASE.putIfAbsent(destMAC, new ClientDetails(destMAC));
        DATABASE.get(destMAC).receiveMessage(MAC_id, timeStamp, message);
    }

    void refreshPersonalChatHistory() throws Exception {
        String MAC = getMAC_id(inputStream.readUTF());

        ArrayList<String> receivedMsgs = clientDetails.getReceivedMessages(MAC);
        outputStream.writeUTF(receivedMsgs.size() + "");
        for (String itr : receivedMsgs) outputStream.writeUTF(itr);

        ArrayList<String> sentMsgs = clientDetails.getSentMessages(MAC);
        outputStream.writeUTF(sentMsgs.size() + "");
        for (String itr : sentMsgs) outputStream.writeUTF(itr);
    }

    void refreshAllChatHistory() throws Exception {
        HashSet<String> allMACs = clientDetails.getAllMAC_IDs();
        outputStream.writeUTF(allMACs.size() + "");

        for (String mac : allMACs) {
            outputStream.writeUTF(mac);

            ArrayList<String> receivedMsgs = clientDetails.getReceivedMessages(mac);
            outputStream.writeUTF(receivedMsgs.size() + "");
            for (String itr : receivedMsgs) outputStream.writeUTF(itr);

            ArrayList<String> sentMsgs = clientDetails.getSentMessages(mac);
            outputStream.writeUTF(sentMsgs.size() + "");
            for (String itr : sentMsgs) outputStream.writeUTF(itr);
        }
    }

    void broadcastMessage() throws Exception {
        for (String destMAC : DATABASE.keySet()) {
            if (!destMAC.equals(clientDetails.getMAC_id())) {
                String timeStamp = ServerSide.DATE_FORMAT.format(new Date());
                String message = inputStream.readUTF();

                clientDetails.sendMessage(destMAC, timeStamp, message);

                DATABASE.putIfAbsent(destMAC, new ClientDetails(destMAC));
                DATABASE.get(destMAC).receiveMessage(MAC_id, timeStamp, message);
            }
        }
    }

    void getRegisteredIPs() throws Exception {
        for (String IPs : DATABASE.keySet()) outputStream.writeUTF(IPs);
    }

    void getConnectedIPs() throws Exception {
        for (String IPs : ACTIVE_CLIENTS) outputStream.writeUTF(IPs);
    }


    final static String getIP(String mac_id) throws Exception {
        return mac_id;
    }

    final static String getMAC_id(String ip) throws Exception {
        if (ip.contains("/")) ip = ip.substring(ip.indexOf("/") + 1);
        if (ip.contains(":")) ip = ip.substring(0, ip.indexOf(":"));
        return ip;
    }
}