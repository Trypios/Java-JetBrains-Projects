package solver;

public class Row {

    private final ComplexNum[] row;
    private final int size;


    public Row(ComplexNum[] row) {
        this.row = row;
        this.size = row.length;
    }

    /**
     * Getter of row.
     * @param deepCopy set to true for a row deep copy, object reference otherwise.
     * @return a ComplexNum[] row array
     */
    public ComplexNum[] getArray(boolean deepCopy) {

        if (!deepCopy) {
            return row;
        }

        ComplexNum[] newRowArr = new ComplexNum[size];
        for (int i = 0; i < size; i++) {
            newRowArr[i] = row[i].copy();
        }
        return newRowArr;
    }

    /**
     * Getter of row.
     * @return a reference of the row ComplexNum[] array object.
     */
    public ComplexNum[] getArray() {
        return getArray(false);
    }

    /**
     * Getter of row's size.
     * @return the length of the row.
     */
    public int getSize() {
        return size;
    }

    /**
     * Getter of the row's constant (last element in the row).
     * @return the row's constant.
     */
    public ComplexNum getConstant() {

        return row[size - 1];
    }

    /**
     * Fetch an complex number from the row by its index.
     * @param index place in row array
     * @return a ComplexNum object
     */
    public ComplexNum getElement(int index) {
        /*
         * returns a complex number by specified index
         * */

        return row[index];
    }

    /**
     * Calculate the count of zeros in the row.
     * @return the count of zeros in the row.
     */
    public int getZeroCount() {

        int counter = 0;
        for (ComplexNum el : row) {
            if (el.isZero()) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Check if the whole row consists of zeros
     * @return true if the whole row consists of zeros, else false.
     */
    public boolean isZeroed() {

        return getZeroCount() == size;
    }

    /**
     * Add an other row to this row (this + other), in place.
     * @param otherRow a Row object
     */
    public void add(Row otherRow) {
        /*
         * row addition, in place
         * */

        for (int i = 0; i < row.length; i++) {
            row[i].add(otherRow.getArray()[i]);
        }
    }

    /**
     * Subtract an other row from this row (this - other), in place.
     * @param otherRow a Row object
     */
    public void subtract(Row otherRow) {
        /*
         * row subtraction, in place
         * */

        for (int i = 0; i < row.length; i++) {
            row[i].subtract(otherRow.getArray()[i]);
        }
    }

    /**
     * Multiply some specified row by a scalar (row * scalar).
     * @param row a Row object
     * @param scalar a ComplexNum object
     * @return a new Row object
     */
    public static Row multiply(Row row, ComplexNum scalar) {

        Row newRow = new Row(row.getArray(true));
        for (int i = 0; i < newRow.getSize(); i++) {
            newRow.getArray()[i].multiply(scalar);
        }
        return newRow;
    }

    /**
     * Find the row's pivot index.
     * (Pivot: Number == 1, where everything before it == 0)
     * @return the row index of the pivot, or -1 if pivot not found
     */
    public int getPivotIndex() {

        for (int i = 0; i < size - 1; i++) {

            if (row[i].isZero()) {
                continue;
            }
            if (row[i].isOne()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Multiply each element in row by its first non-zero reciprocal, in place.
     * This operation sets a pivot in the row.
     * (Pivot: Number == 1, where everything before it == 0)
     */
    public void setPivot() {

        // for every element in row except last one
        for (int i = 0; i < size - 1; i++) {

            ComplexNum currentElem = row[i].copy();

            if (currentElem.isZero()) {
                continue;
            }
            if (currentElem.isOne()) {
                return;
            } else {
                row[i] = new ComplexNum(1d, 0d);
                i++;
                for (; i < size; i++) {
                    row[i].divide(currentElem);
                }
            }
        }
    }

    /**
     * Round all elements of a specified row, to the specified count of decimal points.
     * @param row a Row object.
     * @param dp the count of decimal points [cannot be less than 1].
     * @return a new Row object.
     */
    public static Row roundAllValues(Row row, int dp) {

        if (dp < 1) {
            return null;
        }

        Row newRow = new Row(row.getArray(true));
        for (int i = 0; i < newRow.getSize(); i++) {
            newRow.getArray()[i].roundValues(dp);
        }
        return newRow;

    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder("| ");
        for (ComplexNum element : row) {
            text.append(element.toString()).append(" ");
        }
        return text.toString().trim();
    }
}