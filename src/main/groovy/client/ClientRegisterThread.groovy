package client

class ClientRegisterThread extends Thread {

    protected DatagramSocket socket = null
    protected String service
    private String[] args
    private static final Integer serverPort = 5001


    ClientRegisterThread(String name, String[] args) throws IOException {
        super(name)
        this.service = name
        this.args = args
        this.socket = new DatagramSocket(Integer.parseInt(name))
    }

    void run() {
        byte[] texto = new byte[256]
        String fileName = StringEncripUtils.generateMD5FileName(args[1])
        texto = "register $fileName ${args[2]} ${args[3]}".getBytes()
        InetAddress endereco = InetAddress.getByName(args[0])
        DatagramPacket pacote = new DatagramPacket(texto, texto.length, endereco, serverPort)
        socket.send(pacote)

        // obtem a resposta
        pacote = new DatagramPacket(texto, texto.length)
        socket.receive(pacote)

        // mostra a resposta
        String resposta = new String(pacote.getData(), 0, pacote.getLength())
        System.out.println("\n Server Response: " + resposta)

        // fecha o socket
        socket.close()
        System.out.println("\n Cliente Registrado, finalizando thread de registro")

        System.out.println("\n Iniciando Thread para lookup files")

        new ClientLookUpThread("4900", endereco).start()

        this.interrupt()
    }
}