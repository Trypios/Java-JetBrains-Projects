package encryption;

/**
 * Class responsible for ciphering text.
 */
public abstract class Cipher implements ICipher {

    /**
     *  {@inheritdoc}
     */
    @Override
    public String process(String mode, String data, int key) {

        switch (mode) {
            case "enc":
                return encode(data, key);
            case "dec":
                return decode(data, key);
            default:
                return null;
        }
    }

    /**
     * Cipher factory.
     * @param algorithmType determines what kind of algorithm
     *                     to use for ciphering, "shift" or "unicode"
     * @return a Cipher object.
     */
    public static Cipher getAlgorithm(String algorithmType) {

        switch (algorithmType) {
            case "shift":
                return new ShiftCipher();
            case "unicode":
                return new UnicodeCipher();
            default:
                return null;
        }
    }
}
