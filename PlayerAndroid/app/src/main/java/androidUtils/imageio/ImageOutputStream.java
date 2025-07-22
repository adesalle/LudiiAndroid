package androidUtils.imageio;

import java.io.Closeable;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteOrder;

public interface ImageOutputStream extends DataOutput, Closeable {
    void setByteOrder(ByteOrder byteOrder);
    ByteOrder getByteOrder();
    void write(int b) throws IOException;
    void write(byte[] b) throws IOException;
    void write(byte[] b, int off, int len) throws IOException;
    void flush() throws IOException;
    // ... autres méthodes de DataOutput
}