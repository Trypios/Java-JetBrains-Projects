package encryption;

/**
 * Class responsible for ciphering text by
 * monoalphabetic substitution (Caesar-like),
 */
public class ShiftCipher extends Cipher {

    /**
     *  {@inheritdoc}
     */
    @Override
    public String encode(String data, int key) {

        StringBuilder cipherText = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            char currentLetter = data.charAt(i);
            if (currentLetter >= 97 && currentLetter <= 122) {  // for lowercase letters:
                char editedLetter = (char) ((int) currentLetter + key);
                if (editedLetter > 122) {
                    editedLetter -= 26;
                } else if (editedLetter < 97) {
                    editedLetter += 26;
                }
                cipherText.append(editedLetter);
            } else if (currentLetter >= 65 && currentLetter <= 90) {  // for uppercase letters:
                char editedLetter = (char) ((int) currentLetter + key);
                if (editedLetter > 90) {
                    editedLetter -=26;
                } else if (editedLetter < 65) {
                    editedLetter += 26;
                }
                cipherText.append(editedLetter);
            } else {  // for all other characters:
                cipherText.append(currentLetter);
            }
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
