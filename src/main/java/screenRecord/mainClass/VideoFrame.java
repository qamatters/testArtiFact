package screenRecord.mainClass;


import screenRecord.videoUtil.ScreenRecorder;

import javax.swing.*;
import java.awt.event.*;
import java.util.Date;


public class VideoFrame extends JFrame implements ActionListener {

    JButton startButton, stopButton;
    public VideoFrame() {
        super("Button Demo");

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");

        // Add action listeners to the buttons
        startButton.addActionListener(this);
        stopButton.addActionListener(this);

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);

        // Add the panel to the frame's content pane
        getContentPane().add(buttonPanel);

        // Set the frame's default close operation to exit on close
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack(); // Adjust frame size to fit its content
        setVisible(true);
        stopButton.setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            stopButton.setEnabled(true);
            try {
                startAction();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            startButton.setEnabled(false);
        } else if (e.getSource() == stopButton) {
            try {
                stopAction();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            startButton.setEnabled(true);
        }
    }

    private void startAction() {
        System.out.println("Video Recording started!");
        ScreenRecorder.start("Temp_" + new Date().getTime());
    }

    private void stopAction() throws Exception {
        System.out.println("Video Recording Ended!");
        ScreenRecorder.stop();
    }


}