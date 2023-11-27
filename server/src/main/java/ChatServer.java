import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPConnectionListener {
   private static final String SETTINGS = "C:\\Users\\seper\\IdeaProjects\\Network\\server\\settings.txt";

    public static void main(String[] args) {
        new ChatServer();
    }

    private final ArrayList<TCPConnection> connection = new ArrayList<>();

    ChatServer() {
        System.out.println("Server running");
        try (ServerSocket serverSocket = setSettings()) {
            while (true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("TCPConntection exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connection.add(tcpConnection);
        sendToAllConnection("Client connected: " + tcpConnection);
    }

    @Override
    public synchronized void onAcceptLine(TCPConnection tcpConnection, String line) {
        sendToAllConnection(line);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connection.remove(tcpConnection);
        sendToAllConnection("Client disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConntection exception: " + e);
    }

    void sendToAllConnection(String message) {
        System.out.println(message);
        int size = connection.size();
        for (int i = 0; i < size; i++) {
            connection.get(i).sendMessage(message);
        }
    }

    static ServerSocket setSettings() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SETTINGS))) {
            int port = Integer.parseInt(reader.readLine());
            return new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
