package androidUtils.imageio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class FileImageOutputStream implements ImageOutputStream {
    private final FileOutputStream fos;
    private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;

    public FileImageOutputStream(File file) throws IOException {
        this.fos = new FileOutputStream(file);
    }

    @Override
    public void write(int b) throws IOException {
        fos.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        fos.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        fos.write(b, off, len);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        fos.write(v ? 1 : 0);
    }

    @Override
    public void writeByte(int v) throws IOException {
        fos.write(v);
    }

    @Override
    public void writeShort(int v) throws IOException {
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            fos.write((v >>> 8) & 0xFF);
            fos.write(v & 0xFF);
        } else {
            fos.write(v & 0xFF);
            fos.write((v >>> 8) & 0xFF);
        }
    }

    @Override
    public void writeChar(int v) throws IOException {
        fos.write(v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            fos.write((v >>> 24) & 0xFF);
            fos.write((v >>> 16) & 0xFF);
            fos.write((v >>> 8) & 0xFF);
            fos.write(v & 0xFF);
        } else {
            fos.write(v & 0xFF);
            fos.write((v >>> 8) & 0xFF);
            fos.write((v >>> 16) & 0xFF);
            fos.write((v >>> 24) & 0xFF);
        }
    }

    @Override
    public void writeLong(long v) throws IOException {
        byte[] bytes = new byte[8];
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int i = 0; i < 8; i++) {
                bytes[i] = (byte)(v >>> (56 - i * 8));
            }
        } else {
            for (int i = 0; i < 8; i++) {
                bytes[i] = (byte)(v >>> (i * 8));
            }
        }
        fos.write(bytes);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        writeInt(Float.floatToIntBits(v));
    }

    @Override
    public void writeDouble(double v) throws IOException {
        writeLong(Double.doubleToLongBits(v));
    }

    @Override
    public void writeBytes(String s) throws IOException {
        fos.write(s.getBytes());
    }

    @Override
    public void writeChars(String s) throws IOException {
        for (char c : s.toCharArray()) {
            writeChar(c);
        }
    }

    @Override
    public void writeUTF(String s) throws IOException {
        byte[] bytes = s.getBytes("UTF-8");
        writeShort(bytes.length);
        write(bytes);
    }

    @Override
    public void setByteOrder(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
    }

    @Override
    public ByteOrder getByteOrder() {
        return byteOrder;
    }

    @Override
    public void flush() throws IOException {
        fos.flush();
    }

    @Override
    public void close() throws IOException {
        fos.close();
    }
}