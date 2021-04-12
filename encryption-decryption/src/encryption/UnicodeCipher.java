package encryption;

/**
 * Class responsible for ciphering text by
 * substitution beyond basic latin characters,
 * within Unicode range.
 */
public class UnicodeCipher extends Cipher {

    /**
     *  {@inheritdoc}
     */
    @Override
    public String encode(String data, int key) {

        StringBuilder cipherText = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            char editedLetter = (char) ((int) data.charAt(i) + key);
            cipherText.append(editedLetter);
        }
        return cipherText.toString();
    }

    /**
     *  {@inheritdoc}
     */
    @Override
    public String decode(String data, int key) {

        return encode(data, -key);
    }
}
