package solver;

public class Row {

    private final ComplexNum[] row;
    private final int size;


    public Row(ComplexNum[] row) {
        this.row = row;
        this.size = row.length;
    }

    public ComplexNum[] getArray(boolean deepCopy) {
        /*
        * returns the ComplexNum[] row array
        * as a reference or deep copy
        * */

        if (!deepCopy) {
            return row;
        }

        ComplexNum[] newRowArr = new ComplexNum[size];
        for (int i = 0; i < size; i++) {
            newRowArr[i] = row[i].copy();
        }
        return newRowArr;
    }

    public ComplexNum[] getArray() {
        return getArray(false);
    }

    public int getSize() {
        return size;
    }

    public ComplexNum getConstant() {
        /*
         * returns the last element of the row
         * */

        return row[size - 1];
    }

    public ComplexNum getElement(int index) {
        /*
         * returns a complex number by specified index
         * */

        return row[index];
    }

    public int getZeroCount() {
        /*
         * returns the count of zeros in row
         * */

        int counter = 0;
        for (ComplexNum el : row) {
            if (el.isZero()) {
                counter++;
            }
        }
        return counter;
    }

    public boolean isZeroed() {
        /*
         * returns true if the whole row consists of zeros
         * */

        return getZeroCount() == size;
    }

    public void add(Row otherRow) {
        /*
         * row addition, in place
         * */

        for (int i = 0; i < row.length; i++) {
            row[i].add(otherRow.getArray()[i]);
        }
    }

    public void subtract(Row otherRow) {
        /*
         * row subtraction, in place
         * */

        for (int i = 0; i < row.length; i++) {
            row[i].subtract(otherRow.getArray()[i]);
        }
    }

    public static Row multiply(Row row, ComplexNum scalar) {
        /*
         * returns the row multiplied by a scalar
         * as a new row (not in place)
         * */

        Row newRow = new Row(row.getArray(true));
        for (int i = 0; i < newRow.getSize(); i++) {
            newRow.getArray()[i].multiply(scalar);
        }
        return newRow;
    }

    public int getPivotIndex() {
        /*
         * Returns the index of the pivot.
         * (Pivot: Number == 1, where everything before it == 0)
         * Returns -1 if a pivot is not found
         * */

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

    public void setPivot() {
        /*
         * Multiplies every element in row
         * by the first non-zero element's reciprocal.
         * This sets a pivot in the row.
         * (Pivot: Number == 1, where everything before it == 0)
         */

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

    public static Row roundAllValues(Row row, int dp) {
        /*
         * Rounds all values of the given row
         * to the specified decimal places (dp) <-- cannot be less than 1
         * Returns a new row (not in place)
         * */

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