package io.xml;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Juyas
 * @version 10.12.2021
 * @since 10.12.2021
 */
public class XMLParserTest {

    private static String headerless1 = "<root>\n" +
            "\t<wangtile tileid=\"6\" wangid=\"0,1,0,1,0,1,0,2\"/>\n" +
            "\t<wangtile tileid=\"7\" wangid=\"0,2,0,1,0,1,0,1\"/>\n" +
            "\t<!--wangtile tileid=\"7\" wangid=\"0,2,0,1,0,1,0,1\"/-->\n" +
            "\t<wangtile tileid=\"8\" wangid=\"0,2,0,2,0,2,0,1\"/>\n" +
            "</root>";
    private static String header1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + headerless1;
    private static String header2 = "<?xml version=\"1.0\" encoding=\"us-ascii\"?>\n" + headerless1;

    @Test
    public void parse() {
        headerless1 = strip(headerless1);
        XMLElement element = XML.parse(headerless1);
        Assert.assertEquals(headerless1, strip(element.toString(true)));
        element = XML.parse(header1);
        Assert.assertEquals(headerless1, strip(element.toString(true)));
        element = XML.parse(header2);
        Assert.assertEquals(headerless1, strip(element.toString(true)));

        element = XML.parse(headerless1.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        Assert.assertEquals(headerless1, strip(element.toString(true)));
        element = XML.parse(header1.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        Assert.assertEquals(headerless1, strip(element.toString(true)));
        element = XML.parse(header2.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        Assert.assertEquals(headerless1, strip(element.toString(true)));

        element = XML.parse(headerless1.getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals(headerless1, strip(element.toString(true)));
        element = XML.parse(header1.getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals(headerless1, strip(element.toString(true)));
        element = XML.parse(header2.getBytes(StandardCharsets.US_ASCII));
        Assert.assertEquals(headerless1, strip(element.toString(true)));

    }

    private static String strip(String s) {
        return s.replaceAll("\n", "").replaceAll("\t", "");
    }

    @Test
    public void detectCharset() {
        Charset charset = XML.readHeader(header1.getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals(StandardCharsets.UTF_8, charset);
        Charset charset2 = XML.readHeader(header2.getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals(StandardCharsets.US_ASCII, charset2);
    }

    @Test
    public void transformValue() {
        String value = "Hello :> How are you & and your friends?";
        String raw = XML.transformValue(value, false);
        String fromRaw = XML.transformValue(raw, true);
        Assert.assertEquals("Hello :&gt; How are you &amp; and your friends?", raw);
        Assert.assertEquals(value, fromRaw);
    }

}