package client

import server.FileToTransfer

class ClientFileDownloader extends Thread {

    protected DatagramSocket socket = null
    private FileToTransfer fileToTransfer
    private String fileName


    ClientFileDownloader(FileToTransfer fileToTransfer, String fileName) {
        this.fileName = fileName
        this.fileToTransfer = fileToTransfer
        this.socket = new DatagramSocket(10000)
    }


    void run() {
        byte[] texto = new byte[256]
        texto = "${fileToTransfer.fileDirectory} ${fileName}".getBytes()

        InetAddress endereco = InetAddress.getByName("127.0.0.1")

        DatagramPacket pacote = new DatagramPacket(texto, texto.length, endereco, 4700)
        socket.send(pacote)

        String diretorio = "/home/paulo/Downloads"

        File f1 = new File("$diretorio/$fileName")
        FileOutputStream bos = new FileOutputStream(f1)
        byte[] buf = new byte[63 * 1024]
        DatagramPacket pkg = new DatagramPacket(buf, buf.length)

        while (true) {
            System.out.println("\n Downloading do arquivo $fileName no diretório: $diretorio")
            socket.receive(pkg)
            if (new String(pkg.getData(), 0, pkg.getLength()).equals("end")) {
                System.out.println("\n ----------------- Arquivo recebido com sucesso -----------------")
                bos.close()
                socket.close()
                break
            }
            bos.write(pkg.getData(), 0, pkg.getLength());
            bos.flush()
        }
        bos.close()
    }
}