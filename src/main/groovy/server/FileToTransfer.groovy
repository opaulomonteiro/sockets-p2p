package server

import groovy.transform.Canonical
import groovy.transform.ToString

@Canonical
@ToString
class FileToTransfer {
    String ip
    String fileDirectory
    Integer port

    String getIp() {
        return ip
    }

    void setIp(String ip) {
        this.ip = ip
    }

    String getFileDirectory() {
        return fileDirectory
    }

    void setFileDirectory(String fileDirectory) {
        this.fileDirectory = fileDirectory
    }

    Integer getPort() {
        return port
    }

    void setPort(Integer port) {
        this.port = port
    }
}