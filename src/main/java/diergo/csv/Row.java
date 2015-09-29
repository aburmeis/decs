package diergo.csv;

/**
 * The internal representation of a CSV data row.
 */
public interface Row extends Iterable<String>
{

    /**
     * The default quote for data containing separator or multiple lines or a quote itself.
     */
    char DEFAULT_QUOTE = '"';

    /**
     * Is the row a comment?
     */
    boolean isComment();

    /**
     * The number of data columns in this row. Equal to the size of the iterable.
     * @see #iterator() 
     */
    int getLength();
}
