package io;

import io.xml.XMLElement;
import io.xml.XMLParser;
import io.xml.XMLTokenizer;

import java.nio.charset.Charset;

/**
 * @author Juyas
 * @version 11.12.2021
 * @since 11.12.2021
 */
public class FileFormats {

    /**
     * Simple XML format, based on {@link XMLParser} and {@link XMLTokenizer}.
     */
    public static final FileFormat<XMLElement> XML = new FileFormat<XMLElement>() {
        private final XMLTokenizer tokenizer = new XMLTokenizer();
        private final XMLParser parser = new XMLParser();

        @Override
        public XMLElement parse(String input) {
            return parser.parse(tokenizer.tokenize(input));
        }

        @Override
        public XMLElement parse(byte[] input, Charset charset) {
            return parser.parse(tokenizer.tokenize(input, charset));
        }

        @Override
        public XMLElement parse(byte[] input) {
            return parser.parse(tokenizer.tokenize(input));
        }
    };

}