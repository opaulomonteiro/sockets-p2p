package client

class ClientPingTimer extends TimerTask {

    protected DatagramSocket socket = null
    protected String service
    private String[] args
    private static final Integer serverPort = 5001

    ClientPingTimer(String name) {
        this.socket = new DatagramSocket(Integer.parseInt(name))
        this.service = service
    }

    void run() {
        byte[] texto = new byte[256]
        texto = "ping".getBytes()
        InetAddress endereco = InetAddress.getByName("127.0.0.1")
        DatagramPacket pacote = new DatagramPacket(texto, texto.length, endereco, serverPort)
        socket.send(pacote)
    }
}