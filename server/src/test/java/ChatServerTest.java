import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;


public class ChatServerTest {
    @Mock
    private BufferedReader reader;

    @BeforeEach
    void init() {
        openMocks(this);
    }

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    void setSettingsTest() {
        String filePath = "C:\\Users\\seper\\IdeaProjects\\Network\\server\\settings.txt";
        int testPort = 8080;

        try {
            when(reader.readLine()).thenReturn(String.valueOf(filePath));
        } catch (IOException e) {
         e.printStackTrace();
        }

        ChatServer chatServer = new ChatServer();
        ServerSocket serverSocket = chatServer.setSettings();

        assertEquals(testPort, serverSocket.getLocalPort());
    }

    @Mock
    private TCPConnection connection1;
    @Mock
    private TCPConnection connection2;
    @Test
    void testSendToAllConnection() {

        openMocks(this);

        ChatServer chatServer = new ChatServer();
        chatServer.onConnectionReady(connection1);
        chatServer.onConnectionReady(connection2);

        String message = "Test message";

        doNothing().when(connection1).sendMessage(message);
        doNothing().when(connection2).sendMessage(message);

        chatServer.sendToAllConnection(message);

        verify(connection1).sendMessage(message);
        verify(connection2).sendMessage(message);
    }
}
