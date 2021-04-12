package solver;

/**
 * Class responsible for a complex number and
 * some of its basic mathematical operations.
 */
public class ComplexNum {

    private double realPart;
    private double imaginaryPart;

    public ComplexNum(double realPart, double imaginaryPart) {
        this.realPart = realPart;
        this.imaginaryPart = imaginaryPart;
    }

    /**
     * Getter
     */
    public double getRealPart() {
        return realPart;
    }

    /**
     * Getter
     */
    public double getImaginaryPart() {
        return imaginaryPart;
    }

    /**
     * Check if this complex number equals zero.
     * @return true if this complex number equals zero, else false.
     */
    public boolean isZero() {

        return realPart == 0d && imaginaryPart == 0d;
    }

    /**
     * Check if this complex number equals one.
     * @return true if this complex number equals one, else false.
     */
    public boolean isOne() {

        return realPart == 1d && imaginaryPart == 0d;
    }

    /**
     * Add a specified complex number to this complex number (this + other), in place.
     * @param otherComplexNum a ComplexNum object
     */
    public void add(ComplexNum otherComplexNum) {

        this.realPart += otherComplexNum.getRealPart();
        this.imaginaryPart += otherComplexNum.getImaginaryPart();
    }

    /**
     * Subtract a specified complex number from this complex number (this - other), in place.
     * @param otherComplexNum a ComplexNum object
     */
    public void subtract(ComplexNum otherComplexNum) {

        this.realPart -= otherComplexNum.getRealPart();
        this.imaginaryPart -= otherComplexNum.getImaginaryPart();
    }

    /**
     * Multiply a specified complex number by this complex number (this * other), in place.
     * @param otherComplexNum a ComplexNum object
     */
    public void multiply(ComplexNum otherComplexNum) {

        double newRealPart = realPart * otherComplexNum.getRealPart() - imaginaryPart * otherComplexNum.getImaginaryPart();
        double newImaginaryPart = realPart * otherComplexNum.getImaginaryPart() + imaginaryPart * otherComplexNum.getRealPart();
        this.realPart = newRealPart;
        this.imaginaryPart = newImaginaryPart;
    }

    /**
     * Divide this complex number by a specified complex number (this / other), in place.
     * @param otherComplexNum a ComplexNum object
     */
    public void divide(ComplexNum otherComplexNum) {

        double mod = Math.pow(otherComplexNum.getRealPart(), 2) + Math.pow(otherComplexNum.getImaginaryPart(), 2);
        double newRealPart = realPart * otherComplexNum.getRealPart() + imaginaryPart * otherComplexNum.getImaginaryPart();
        newRealPart /= mod;
        double newImaginaryPart = imaginaryPart * otherComplexNum.getRealPart() - realPart * otherComplexNum.getImaginaryPart();
        newImaginaryPart /= mod;
        this.realPart = newRealPart;
        this.imaginaryPart = newImaginaryPart;
    }

    /**
     * Copy this complex number.
     * @return a deep copy of this complex number.
     */
    public ComplexNum copy() {
        /*
         * returns a deep copy of this complex number
         * */

        return new ComplexNum(realPart, imaginaryPart);
    }

    /**
     * Round real and imaginary parts to the specified decimal places (dp).
     * @param dp count of decimal places.
     */
    public void roundValues(int dp) {
        /*
         * rounds values to the specified decimal places (dp)
         * in place
         * */

        if (dp < 1) {
            return;
        }

        dp *= 10;
        realPart = Math.round(realPart * dp) / (double) dp;
        imaginaryPart = Math.round(imaginaryPart * dp) / (double) dp;
    }

    @Override
    public String toString() {
        if (isZero()) {
            return "0";
        } else if (realPart == 0) {
            return String.format("%fi", imaginaryPart);
        } else if (imaginaryPart == 0) {
            return String.format("%f", realPart);
        } else {
            if (imaginaryPart > 0) {
                return String.format("%f+%fi", realPart, imaginaryPart);
            }
            return String.format("%f%fi", realPart, imaginaryPart);
        }
    }
}
