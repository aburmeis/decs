package diergo.csv;

import org.junit.Test;

import java.io.StringWriter;
import java.util.stream.Stream;

import static diergo.csv.Writers.consumeTo;
import static diergo.csv.Writers.toWriter;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class WritersTest {

    @Test
    public void emptyStreamCollectsToAnUnchangedWriter() {
        StringWriter out = Stream.<String>empty().collect(toWriter(new StringWriter()));

        assertThat(out.toString(), is(""));
    }

    @Test
    public void eachStringIsCollectedToALine() {
        StringWriter out = Stream.of("one", "two").collect(toWriter(new StringWriter()));

        assertThat(out.toString(), is("one\ntwo\n"));
    }

    @Test
    public void eachStringIsConsumedToALine() {
        StringWriter out = new StringWriter();
        Stream.of("one", "two").forEach(consumeTo(out));

        assertThat(out.toString(), is("one\ntwo\n"));
    }
}