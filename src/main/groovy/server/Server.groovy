package server

import com.google.gson.Gson

class Server extends Thread {

    protected DatagramSocket socket = null
    private Map<String, List<FileToTransfer>> fileManager
    private Gson gson
    private ServerCache serverCache

    Server(ServerCache serverCache, Map<String, List<FileToTransfer>> fileManager) {
        this.gson = new Gson()
        this.serverCache = serverCache
        this.fileManager = fileManager
        socket = new DatagramSocket(5001)
    }

    void run() {
        while (true) {
            try {
                byte[] texto = new byte[256]
                // recebe datagrama
                DatagramPacket pacote = new DatagramPacket(texto, texto.length)
                socket.receive(pacote)

                // processa o que foi recebido
                String recebido = new String(pacote.getData(), 0, pacote.getLength())

                InetAddress endereco = pacote.getAddress()
                int porta = pacote.getPort()

                if (recebido.equalsIgnoreCase("ping")) {
                    System.out.println("\n ### PING ### Atualiza chave no cache")

                    serverCache.put(endereco.getHostAddress(), 1)
                }

                if (recebido.contains("register")) {
                    System.out.print("\n #### REGISTER ####")

                    addInfosToCache(recebido, pacote.getAddress())
                    serverCache.put(endereco.getHostAddress(), 1)
                    texto = "Registro finalziado com sucesso !!".getBytes()
                    pacote = new DatagramPacket(texto, texto.length, endereco, porta)
                    socket.send(pacote)
                }

                if (recebido.contains("lookup")) {
                    System.out.print("\n #### LOOKUP ####")

                    List<FileToTransfer> files = fileManager.get(recebido.split(" ")[1])
                    byte[] filesByte = new byte[2024]
                    filesByte = gson.toJson(files).getBytes()
                    socket.send(new DatagramPacket(filesByte, filesByte.length, endereco, porta))
                }
            } catch (IOException e) {
                e.printStackTrace()
                break
            }
        }
        socket.close()
        System.out.println("Servidor encerrado...")
    }


    private void addInfosToCache(String recebido, InetAddress address) {
        String[] infosFromClient = recebido.split(" ")
        String fileName = infosFromClient[1]
        String fileDirectory = infosFromClient[2]
        Integer porta = Integer.valueOf(infosFromClient[3])

        if (fileManager.findAll { it.key == fileName }.isEmpty()) {
            fileManager.put(fileName, [
                    new FileToTransfer(
                            ip: address.getHostAddress(),
                            fileDirectory: fileDirectory,
                            port: porta)
            ])
        } else {
            List<FileToTransfer> files = fileManager.get(fileName)
            files.add(new FileToTransfer(ip: address.getHostAddress(), fileDirectory: fileDirectory, port: porta))
            fileManager.put(fileName, files)
        }
    }
}