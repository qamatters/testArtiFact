package screenRecord.videoUtil;

import java.util.EventObject;

public class VideoRecorderEventObject extends EventObject {
    private ScreenCapture screenCapture;
    public VideoRecorderEventObject(Object source, ScreenCapture screenCapture) {
        super(source);
        this.screenCapture = screenCapture;
    }
}
