package comMain;

import screenCapture.UI.uiHelper;
import screenCapture.util.Helper;
import screenRecord.mainClass.VideoFrame;

import javax.swing.*;

public class MainFrame extends JFrame {
    static String tempFilesPath = "Files//Temp//";
    static String results = "Files//ScreenshotReport//";
    static String screenshots = "Files//Screenshots";
    static String reportName = "TestArtifacts_" + Helper.timeStamp();

    public MainFrame() {
        super("Media Capture Options");

        JButton screenshotButton = new JButton("Take Screenshot");
        screenshotButton.addActionListener(event -> {
            setVisible(false);
            uiHelper.launchUI(screenshots, tempFilesPath, results, reportName);
        });

        JButton videoButton = new JButton("Take Video");
        videoButton.addActionListener(event -> {
            setVisible(false);
            new VideoFrame().setVisible(true);
        });

        // Layout the buttons using a layout manager
        JPanel panel = new JPanel();
        panel.add(screenshotButton);
        panel.add(videoButton);
        add(panel);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new MainFrame().setVisible(true);
    }
}
