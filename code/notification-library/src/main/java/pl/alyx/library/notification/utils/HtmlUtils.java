package pl.alyx.library.notification.utils;

import org.jsoup.Jsoup;

import java.util.Objects;

public final class HtmlUtils {

    public static String makeHtml(String text) {
        final String template = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>{}</body></html>";
        final String html = template.replace("{}", Objects.toString(text, ""));
        return html;
    }

    public static String makeText(String text) {
        return Jsoup.parse(text).wholeText();
    }

}
