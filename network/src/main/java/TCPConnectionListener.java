public interface TCPConnectionListener {

    void onConnectionReady(TCPConnection tcpConnection);

    void onAcceptLine(TCPConnection tcpConnection, String line);

    void onDisconnect(TCPConnection tcpConnection);

    void onException(TCPConnection tcpConnection, Exception e);
}
