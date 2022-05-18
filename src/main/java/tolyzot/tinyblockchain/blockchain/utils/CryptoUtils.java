package tolyzot.tinyblockchain.blockchain.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class CryptoUtils {
    public static String SHA256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuffer hexHashString = new StringBuffer();
            for (int i = 0; i < hash.length; i++){
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1){
                    hexHashString.append('0');
                }
                hexHashString.append(hex);
            }
            return hexHashString.toString();
        }catch (Exception e){
            throw new RuntimeException("Error while calculating SHA-256 hash string.");
        }
    }
}
