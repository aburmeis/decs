package diergo.csv;

import java.io.Reader;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static diergo.csv.ErrorHandlers.throwingError;
import static diergo.csv.Row.DEFAULT_QUOTE;

/**
 * Configure and build a CSV parser. Typically this is used as a mapper for a stream of lines:
 * <p>
 * {@link Readers#asLines(Reader) asLines}(in)
 * .{@link java.util.stream.Stream#map(Function) map}({@link #csvParser()}.{@link #build()})
 * .{@link java.util.stream.Stream#flatMap(Function) flatMap}({@link Collection#stream() Collection::stream})
 * <p>
 * The parser will return one row for most lines, zero rows if the end of line is part of a quoted cell
 * and any number of rows created by an {@link #handlingErrors(BiFunction) error handler} on invalid data.
 * <p>
 * Don't forget the {@code flatMap(Collection::stream)} call as the parser may return zero to multiple rows per line!
 *
 * @see Readers#asLines(Reader)
 * @see java.util.stream.Stream#map(Function)
 * @see java.util.stream.Stream#flatMap(Function)
 */
public class CsvParserBuilder {

    public static final String DEFAULT_SEPARATORS = ",;\t";

    /**
     * Creates a builder for a new parser.
     *
     * @see #build()
     */
    public static CsvParserBuilder csvParser() {
        return new CsvParserBuilder();
    }

    private CharSequence separators = DEFAULT_SEPARATORS;
    private char quote = DEFAULT_QUOTE;
    private String commentStart = null;
    private boolean laxMode = false;
    private BiFunction<String, RuntimeException, List<Row>> errorHandler = throwingError();

    private CsvParserBuilder() {
    }

    /**
     * Configure the quoting character for data containing separator or multiple lines or a quote itself.
     * The default is {@link Row#DEFAULT_QUOTE}.
     */
    public CsvParserBuilder quotedWith(char quote) {
        this.quote = quote;
        return this;
    }

    public CsvParserBuilder commentsStartWith(String commentStart) {
        this.commentStart = commentStart;
        return this;
    }

    /**
     * Enables a lax parsing mode allowing quotes in cell data.
     */
    public CsvParserBuilder inLaxMode() {
        this.laxMode = true;
        return this;
    }

    /**
     * Configures the fixed separator between data columns in the line.
     * By default a couple of separators are possible.
     *
     * @see #separatedByAnyOf(CharSequence)
     */
    public CsvParserBuilder separatedBy(char separator) {
        return separatedByAnyOf(String.valueOf(separator));
    }

    /**
     * Configures any of multiple possible separators between data columns in the line.
     *
     * @see #DEFAULT_SEPARATORS
     */
    public CsvParserBuilder separatedByAnyOf(CharSequence possibleSeparators) {
        this.separators = possibleSeparators;
        return this;
    }

    /**
     * Configures the error handler for input format problems.
     * By default illegal lines create an error.
     *
     * @see ErrorHandlers#throwingError()
     */
    public CsvParserBuilder handlingErrors(BiFunction<String, RuntimeException, List<Row>> errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    /**
     * Created a new configured parser.
     *
     * @see java.util.stream.Stream#map(Function)
     */
    public Function<String, List<Row>> build() {
        return new RowParser(separators, quote, commentStart, laxMode, errorHandler);
    }
}
