package io.bin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Juyas
 * @version 13.12.2021
 * @since 13.12.2021
 */
public class BinaryIO {

    public static ByteBuffer readData(URL url, int blockSize) throws IOException {
        if (blockSize < 10) blockSize = 10;
        //open the url connection
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        List<byte[]> readData = new LinkedList<>();
        List<Integer> length = new LinkedList<>();
        //read the data until there is nothing left
        int r;
        do {
            byte[] data = new byte[blockSize];
            r = inputStream.read(data);
            if (r == -1) break;
            readData.add(data);
            length.add(r);
        }
        while (r > 0);
        //combine all data segments
        ByteBuffer buffer = ByteBuffer.allocate(length.stream().reduce(Integer::sum).get());
        for (int i = 0; i < readData.size(); i++) {
            byte[] d = readData.get(i);
            buffer.put(d, 0, length.get(i));
        }
        return buffer;
    }

    public static ByteBuffer readData(File file) throws IOException {
        return ByteBuffer.wrap(Files.readAllBytes(file.toPath()));
    }

    public static void writeData(File file, byte[] data) throws IOException {
        FileOutputStream stream = new FileOutputStream(file);
        stream.write(data);
        stream.flush();
        stream.close();
    }

}