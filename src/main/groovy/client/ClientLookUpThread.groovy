package client

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import server.FileToTransfer

import java.lang.reflect.Type

class ClientLookUpThread extends Thread {

    protected DatagramSocket socket = null
    protected String service
    private String fileName
    private InetAddress serverAddress
    private Gson gson
    private static final Integer serverPort = 5001


    ClientLookUpThread(String name, InetAddress serverAddress) throws IOException {
        super(name)
        this.service = name
        this.serverAddress = serverAddress
        this.gson = new Gson()
        this.socket = new DatagramSocket(Integer.parseInt(name))
    }


    void run() {
        /* Permite leitura de dados do teclado */
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        /* Lê uma mensagem digitada pelo usuário */
        System.out.print("\n Digite o nome arquivo com a extensão , exemplo arquivo.txt, que deseja baixar: ")
        fileName = inFromUser.readLine()
        String fileNameEncripted = StringEncripUtils.generateMD5FileName(fileName)

        byte[] texto = new byte[2024]
        texto = "lookup $fileNameEncripted".getBytes()
        DatagramPacket pacote = new DatagramPacket(texto, texto.length, serverAddress, serverPort)
        socket.send(pacote)

        // obtem a resposta
        byte[] textoReceived = new byte[256]
        pacote = new DatagramPacket(textoReceived, textoReceived.length)
        socket.receive(pacote)

        // mostra a resposta
        String resposta = new String(pacote.getData(), 0, pacote.getLength())

        Type listType = new TypeToken<List<FileToTransfer>>() {}.getType()

        List<FileToTransfer> files = gson.fromJson(resposta, listType)

        if (!files) {
            System.out.println("\n O arquivo $fileName não encontrado no servidor")
            socket.close()
            System.out.println("\n Encerrando thread e socket de lookpUp ...")
            this.interrupt()
        } else {
            System.out.println("\n Arquivo encontrado nos seguintes IP's: ${files.collect { it.ip }})")

            //fecha o socket
            socket.close()
            System.out.println("\n Encerrando thread e socket de lookpUp ...")

            System.out.println("\n Inicializando thread de File Downloader")

            new ClientFileDownloader(files.first(), fileName).run()
        }
    }
}