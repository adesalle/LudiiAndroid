package androidUtils.imageio.exceptions;

import java.io.IOException;

public class IIOException extends IOException {
    public IIOException(String noGifImageWritersExist) {
        super(noGifImageWritersExist);
    }
}
