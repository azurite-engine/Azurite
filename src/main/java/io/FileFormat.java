package io;

import java.nio.charset.Charset;

/**
 * Can contain all tools for parsing a specific file format from text or byte input to an object.
 *
 * @author Juyas
 * @version 11.12.2021
 * @see FileFormats
 * @since 11.12.2021
 */
public interface FileFormat<T> {

    /**
     * Parse all data in the given string. Usually an {@link java.nio.charset.StandardCharsets#UTF_8} encoding is expected.
     *
     * @param input the input string
     * @return the parsed object data
     */
    T parse(String input);

    /**
     * Parse all data in the given input bytes by using the defined encoding.
     *
     * @param input   the input bytes
     * @param charset the charset to read the bytes
     * @return the parsed object data
     */
    T parse(byte[] input, Charset charset);

    /**
     * Parse all data in the given input bytes by using an encoding
     * that is either defined within the bytes or might be a default one according to the file format.
     *
     * @param input the input bytes
     * @return the parsed object data
     * @see Tokenizer#detectCharset(byte[])
     */
    T parse(byte[] input);

}