package com.github.emilycares.loganaintelij;

import com.github.emilycares.loganaintelij.model.LoganaMessage;
import org.junit.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LoganaReportParserTest {

    private LoganaReportParser parser = new LoganaReportParser();

    @Test
    public void testParser() {
        List<String> log = List.of(
                "/home/emily/Documents/rust/logana/src/main.rs:16:5|type annotations needed",
                "C:\\home\\emily\\Documents\\rust\\logana\\src\\main.rs:16:5|type annotations needed"
        );

        List<LoganaMessage> result = parser.parse(log);

        assertEquals(result, List.of(
                new LoganaMessage(
                        "type annotations needed",
                        Path.of("/home/emily/Documents/rust/logana/src/main.rs"),
                        16,
                        5 ),
                new LoganaMessage(
                        "type annotations needed",
                        Path.of("C:\\home\\emily\\Documents\\rust\\logana\\src\\main.rs"),
                        16,
                        5)
        ));
    }
    @Test
    public void testParser2() {
        List<String> log = List.of(
                "/home/emily/test/src/main/java/Generator.java:118:51|';' expected",
                "",
                ""
        );

        List<LoganaMessage> result = parser.parse(log);

        assertEquals(result, List.of(
                new LoganaMessage(
                        "';' expected",
                        Path.of("/home/emily/test/src/main/java/Generator.java"),
                        118,
                        51)
        ));
    }
}
