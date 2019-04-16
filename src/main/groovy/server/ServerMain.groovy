package server

class ServerMain {

    static void main(String[] args) throws IOException {
        ServerCache serverCache = new ServerCache()
        Map<String, List<FileToTransfer>> fileManager = new HashMap<>()
        new Server(serverCache, fileManager).start()
        new Timer().scheduleAtFixedRate(new ServerClientController(serverCache, fileManager), 5000, 5000)
    }
}