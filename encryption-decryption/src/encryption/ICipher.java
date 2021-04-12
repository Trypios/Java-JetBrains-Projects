package encryption;

/**
 * Specifies the behavior of Cipher objects.
 */
public interface ICipher {

    /**
     * Encrypt or decrypt data.
     * @param mode "enc" for encoding, "dec" for decoding.
     * @param data a string to be processed.
     * @param key a number to be used as cipher key.
     * @return the processed data (encoded or decoded).
     */
    String process(String mode, String data, int key);

    /**
     * Encode data.
     * @param data The string to be encoded.
     * @param key a number to be used as cipher key.
     * @return the encoded data.
     */
    String encode(String data, int key);

    /**
     * Decode data.
     * @param data The string to be decoded.
     * @param key a number to be used as cipher key.
     * @return the decoded data.
     */
    String decode(String data, int key);
}
