package client

import server.FileToTransfer

class ClientFileTransferThread extends Thread {

    protected DatagramSocket socket = null
    private FileToTransfer fileToTransfer
    private String fileName

    ClientFileTransferThread(String name) throws IOException {
        super(name)
        this.fileName = fileName
        this.fileToTransfer = fileToTransfer
        this.socket = new DatagramSocket(Integer.parseInt(name))
    }

    void run() {
        byte[] texto = new byte[256]
        // recebe datagrama
        DatagramPacket pacote = new DatagramPacket(texto, texto.length)
        socket.receive(pacote)

        // processa o que foi recebido
        String recebido = new String(pacote.getData(), 0, pacote.getLength())
        String[] fileInfos = recebido.split(" ")

        InetAddress endereco = pacote.getAddress()
        int porta = pacote.getPort()

        File f2 = new File("${fileInfos[0]}/${fileInfos[1]}")
        FileInputStream bis = new FileInputStream(f2)
        byte[] buf = new byte[63 * 1024];
        int len;

        DatagramPacket pkg = new DatagramPacket(buf, buf.length, endereco, porta)
        System.out.println("\n Iniciando tranferencia do arquivo ${fileInfos[1]} no diretorio: ${fileInfos[0]}")
        while ((len = bis.read(buf)) != -1) {
            socket.send(pkg);
        }
        buf = "end".getBytes();
        DatagramPacket endpkg = new DatagramPacket(buf, buf.length, endereco, porta)
        System.out.println("\n ----------------- Arquivo enviado com sucesso -----------------")
        socket.send(endpkg)
        bis.close()
    }
}