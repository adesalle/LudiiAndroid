package androidUtils.awt.batik;

import java.io.InputStream;

public class TranscoderInput {
    private final InputStream inputStream;

    public TranscoderInput(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}