import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class ClientTest {

    @Test
    void writeToLogFileTest()  {
        Client client =  new Client();
        String message = "test message";
        client.writeToLogFile(message);
        try ( BufferedReader reader = new BufferedReader(new FileReader("file.log"))) {
            assertEquals(message, reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Mock
    private BufferedReader reader;

    @Test
    void setSettingsTest() throws IOException {

        openMocks(this);

        String addr = "localhost";
        int port = 8080;
        Client client = new Client();

        when(reader.readLine()).thenReturn(addr, String.valueOf(port));
        client.setSettings();

        assertEquals(addr, client.PI_ADDRR);
        assertEquals(port, client.PORT);
    }

    @Test
    void printMsgTest() {
        Client client = new Client();
        String message = "Test message";

        PrintStream mockPrintStream = mock(PrintStream.class);
        System.setOut(mockPrintStream);

        client.printMsg(message);
        verify(mockPrintStream).println(message);
    }
}
