package core.models.agregador;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class HandlerRecientes {

    public static HandlerRecientes instance;

    public static HandlerRecientes getInstance() {
        if (instance == null) instance = new HandlerRecientes();

        return instance;
    }

    public static String generarHash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes());
            return HexFormat.of().formatHex(hashBytes); // devuelve un string en hex
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean esMismoHash (String hash1, String hash2){
        if (hash1 == null || hash2 == null) {
            return false;}
        return hash1.equals(hash2);
    }

}
