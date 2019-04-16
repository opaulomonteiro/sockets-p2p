package server

class ServerClientController extends TimerTask {

    private ServerCache serverCache
    private Map<String, List<FileToTransfer>> fileManager

    ServerClientController(ServerCache serverCache, Map<String, List<FileToTransfer>> fileManager) {
        this.serverCache = serverCache
        this.fileManager = fileManager
    }

    @Override
    void run() {
        List<FileToTransfer> clientsToRemove = fileManager.findAll {
            it.value.findAll {
                System.out.print("\n IP DO USER $it.ip")
                serverCache.get(it.ip) == 0
            }
        }.collect { it.value } as List<FileToTransfer>

        if (clientsToRemove && !clientsToRemove.isEmpty()) {
            System.out.print("\n Removendo cliente do servidor $clientsToRemove")

            fileManager.values().removeAll(clientsToRemove)

            if (fileManager.isEmpty()) fileManager = [:]

            System.out.print("\n Lista de Arquivos apos remocao dos cliente offLine $fileManager")
        }
    }
}