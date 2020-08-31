package solver;


public class ComplexNum {

    private double realPart;
    private double imaginaryPart;

    public ComplexNum(double realPart, double imaginaryPart) {
        this.realPart = realPart;
        this.imaginaryPart = imaginaryPart;
    }

    public double getRealPart() {
        return realPart;
    }

    public double getImaginaryPart() {
        return imaginaryPart;
    }

    public boolean isZero() {
        /*
        * returns true if this complex number equals 0
        * */

        return realPart == 0d && imaginaryPart == 0d;
    }

    public boolean isOne() {
        /*
         * returns true if this complex number equals 1
         * */

        return realPart == 1d && imaginaryPart == 0d;
    }

    public void add(ComplexNum otherComplexNum) {
        /*
         * adds an other complex number to this one
         * in place
         * */

        this.realPart += otherComplexNum.getRealPart();
        this.imaginaryPart += otherComplexNum.getImaginaryPart();
    }

    public void subtract(ComplexNum otherComplexNum) {
        /*
         * subtracts an other complex number from this one
         * in place
         * */

        this.realPart -= otherComplexNum.getRealPart();
        this.imaginaryPart -= otherComplexNum.getImaginaryPart();
    }

    public void multiply(ComplexNum otherComplexNum) {
        /*
         * multiplies with other complex number, in place
         * */

        double newRealPart = realPart * otherComplexNum.getRealPart() - imaginaryPart * otherComplexNum.getImaginaryPart();
        double newImaginaryPart = realPart * otherComplexNum.getImaginaryPart() + imaginaryPart * otherComplexNum.getRealPart();
        this.realPart = newRealPart;
        this.imaginaryPart = newImaginaryPart;
    }

    public void divide(ComplexNum otherComplexNum) {
        /*
         * divided by an other complex number, in place
         * */

        double mod = Math.pow(otherComplexNum.getRealPart(), 2) + Math.pow(otherComplexNum.getImaginaryPart(), 2);
        double newRealPart = realPart * otherComplexNum.getRealPart() + imaginaryPart * otherComplexNum.getImaginaryPart();
        newRealPart /= mod;
        double newImaginaryPart = imaginaryPart * otherComplexNum.getRealPart() - realPart * otherComplexNum.getImaginaryPart();
        newImaginaryPart /= mod;
        this.realPart = newRealPart;
        this.imaginaryPart = newImaginaryPart;
    }

    public ComplexNum copy() {
        /*
         * returns a deep copy of this complex number
         * */

        return new ComplexNum(realPart, imaginaryPart);
    }

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
