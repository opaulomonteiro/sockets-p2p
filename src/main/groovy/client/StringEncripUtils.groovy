package client

import javax.xml.bind.DatatypeConverter
import java.security.MessageDigest

class StringEncripUtils {

    static String generateMD5FileName(String fileName) {
        MessageDigest md = MessageDigest.getInstance("MD5")
        md.update(fileName.getBytes());
        byte[] digest = md.digest();
        DatatypeConverter.printHexBinary(digest).toUpperCase();
    }
}