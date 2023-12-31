import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;


public class TCPConnection {
    private final Socket socket;
    private final Thread thread;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final TCPConnectionListener listener;

    public TCPConnection(TCPConnectionListener listener, String ipAddress, int port) throws IOException {
        this(listener, new Socket(ipAddress,port));
    }

    public TCPConnection(TCPConnectionListener listener, Socket socket) throws IOException {
        this.listener = listener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),Charset.forName("UTF-8")));
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onConnectionReady(TCPConnection.this);
                    while (!thread.isInterrupted()) {
                        String message = in.readLine();
                        listener.onAcceptLine(TCPConnection.this, message);
                    }
                } catch (IOException e) {
                    listener.onException(TCPConnection.this, e);
                } finally {
                    listener.onDisconnect(TCPConnection.this);
                }

            }
        });
        thread.start();

    }

    public synchronized void sendMessage(String message) {
        try {
            out.write(message + "\r\n");
            out.flush();
        } catch (IOException e) {
            listener.onException(TCPConnection.this, e);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        thread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            listener.onException(TCPConnection.this, e);
        }
    }

    @Override
    public String toString() {
        return "TCPConnection: " + socket.getInetAddress() + ": " + socket.getPort() + " " + "\n";
    }
}
