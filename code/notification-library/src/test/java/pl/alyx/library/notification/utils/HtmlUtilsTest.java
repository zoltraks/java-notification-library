package pl.alyx.library.notification.utils;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class HtmlUtilsTest {

    @Test
    public void testMakeHtml() {
        String needle;
        String result;
        String expect;
        needle = null;
        result = HtmlUtils.makeHtml(needle);
        expect = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body></body></html>";
        assertEquals(result, expect);
        needle = "Hello, World.";
        result = HtmlUtils.makeHtml(needle);
        expect = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>Hello, World.</body></html>";
        assertEquals(result, expect);
    }

    @Test
    public void testMakeText() {
    }

}