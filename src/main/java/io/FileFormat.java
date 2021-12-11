package io;

import java.nio.charset.Charset;

/**
 * @author Juyas
 * @version 11.12.2021
 * @since 11.12.2021
 */
public interface FileFormat<T> {

    T parse(String input);

    T parse(byte[] input, Charset charset);

    T parse(byte[] input);

}