import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class Client implements TCPConnectionListener {
    static String PI_ADDRR;
    static int PORT;
    private TCPConnection connection;
    private String nickname;
    private static final String SETTINGS = "client/settings.txt";

    public static void main(String[] args) {
        setSettings();
        new Client();
    }

    Client() {
        try {
            connection = new TCPConnection(this, PI_ADDRR, PORT);
            setNickname();
            consoleInput();
        } catch (IOException e) {
            printMsg("Connection exception" + e);
        }
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMsg("Connection ready");
    }

    @Override
    public void onAcceptLine(TCPConnection tcpConnection, String line) {
        printMsg(line);
        writeToLogFile(line);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMsg("Connection close");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        printMsg("Connection exception" + e);
    }

    synchronized void printMsg(String msg) {
        System.out.println(msg);
    }

    static void setSettings() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SETTINGS))) {
            PI_ADDRR = reader.readLine();
            PORT = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   private void consoleInput() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                    while (true) {
                        String input = reader.readLine();
                        if (input.equalsIgnoreCase("exit")) {
                            connection.sendMessage("Дата: " + LocalDate.now() + " Время: " + LocalTime.now().withSecond(0).withNano(0) + " " + nickname + ": покинул чат");
                            connection.disconnect();
                            break;
                        }
                        connection.sendMessage("Дата: " + LocalDate.now() + " Время: " + LocalTime.now().withSecond(0).withNano(0) + " " + nickname + ": " + input);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void setNickname() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your nickname: " + "\n");
        this.nickname = scanner.nextLine();
    }

    void writeToLogFile(String message) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("file.log", true)))) {
            out.println(message); //
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

