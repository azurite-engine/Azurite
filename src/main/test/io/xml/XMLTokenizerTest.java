package io.xml;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Juyas
 * @version 11.12.2021
 * @since 11.12.2021
 */
public class XMLTokenizerTest {

    private static String headerless1 = "<root>\n" +
            "\t<wangtile tileid=\"6\" wangid=\"0,1,0,1,0,1,0,2\"/>\n" +
            "\t<wangtile tileid=\"7\" wangid=\"0,2,0,1,0,1,0,1\"/>\n" +
            "\t<!--wangtile tileid=\"7\" wangid=\"0,2,0,1,0,1,0,1\"/-->\n" +
            "\t<wangtile tileid=\"8\" wangid=\"0,2,0,2,0,2,0,1\"/>\n" +
            "</root>";
    private static String header1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + headerless1;
    private static String header2 = "<?xml version=\"1.0\" encoding=\"us-ascii\"?>\n" + headerless1;

    private XMLTokenizer tokenizer;

    @Before
    public void setUp() {
        tokenizer = new XMLTokenizer();
    }

    @Test
    public void detectCharset() {
        Charset charset = tokenizer.detectCharset(header1.getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals(StandardCharsets.UTF_8, charset);
        Charset charset2 = tokenizer.detectCharset(header2.getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals(StandardCharsets.US_ASCII, charset2);
    }

}