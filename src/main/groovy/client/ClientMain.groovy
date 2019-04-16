package client


class ClientMain {

    static void main(String[] args) throws IOException {
        new ClientRegisterThread("4800", args).start()
        new Timer().scheduleAtFixedRate(new ClientPingTimer("4000"), 5000, 5000)
        new ClientFileTransferThread("4700").start()
    }
}