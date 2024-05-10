package screenCapture.UI;

import screenCapture.excelHelper.excelUtil;
import screenCapture.util.Helper;
import screenCapture.wordHelper.CreateWordPicturesInSinglePages;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;


public class uiHelper {
    static boolean alertShown = false;
    public static void launchUI(String screenShotsPath, String tempFilePath, String results, String reportName) {

        JFrame frame = new JFrame("Test Artifacts Generator");
        frame.setAlwaysOnTop(true);
        JButton capture = new JButton("Take Screenshot");
        capture.setBackground(Color.green);
        capture.setForeground(Color.BLACK);
        capture.setBounds(500, 180, 150, 50);


        JButton generatePDFReports = new JButton("Generate PDF Report");
        generatePDFReports.setBackground(Color.green);
        generatePDFReports.setForeground(Color.BLACK);
        generatePDFReports.setBounds(800, 180, 180, 50);

        JButton generateDocReport = new JButton("Generate Doc Report");
        generateDocReport.setBackground(Color.green);
        generateDocReport.setForeground(Color.BLACK);
        generateDocReport.setBounds(1000, 180, 180, 50);

        JButton generateExcelReport = new JButton("Generate Excel Report");
        generateExcelReport.setBackground(Color.green);
        generateExcelReport.setForeground(Color.BLACK);
        generateExcelReport.setBounds(1200, 180, 180, 50);

        JButton deleteTempFile = new JButton("Delete Screenshots");
        deleteTempFile.setBackground(Color.RED);
        deleteTempFile.setForeground(Color.BLACK);
        deleteTempFile.setBounds(1400, 180, 180, 50);


        frame.add(generateExcelReport);
        frame.add(generateDocReport);
        frame.add(deleteTempFile);
        frame.add(generatePDFReports);
        frame.add(capture);
        frame.pack();
        frame.setResizable(true);
        frame.setEnabled(true);
        frame.setLayout((LayoutManager)null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(3);

        generateExcelReport.addActionListener((e) -> {
            String docReportName = reportName + ".xlsx";
            List<String> allScreenShotPaths = CreateWordPicturesInSinglePages.getScreenshotPaths(screenShotsPath);

            try {
                excelUtil.addImageToExcel(docReportName, allScreenShotPaths, results);
            } catch (IOException var7) {
                throw new RuntimeException(var7);
            }

            System.exit(0);
        });

        generateDocReport.addActionListener((e) -> {
            String docReportName = reportName + ".docx";
            List<String> allScreenShotPaths = CreateWordPicturesInSinglePages.getScreenshotPaths(screenShotsPath);
            CreateWordPicturesInSinglePages.addImagesToWord(allScreenShotPaths, docReportName, results);
            System.exit(0);
        });

        deleteTempFile.addActionListener((e) -> {
            try {
                Helper.deleteTempFiles(screenShotsPath);
            } catch (IOException var3) {
                throw new RuntimeException(var3);
            }
        });

        capture.addActionListener((e) -> {
            try {
                if(!alertShown) {
                    alertShown = true;
                    int choice = JOptionPane.showConfirmDialog(null, "Do you want to delete previous screenshots?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        Helper.deleteTempFiles(screenShotsPath);
                        JOptionPane.showMessageDialog(null, "All previous screenshots have been deleted.");
                    }
                }
                frame.setVisible(false);
                Helper.getScreenshot(screenShotsPath);
                frame.setVisible(true);
            } catch (IOException | AWTException var3) {
                throw new RuntimeException(var3);
            } catch (InterruptedException var4) {
                throw new RuntimeException(var4);
            }
        });

        generatePDFReports.addActionListener((e) -> {
            try {
                String pdfReportName = reportName + ".pdf";
                Helper.generatePDFReport(screenShotsPath, tempFilePath, results, pdfReportName);
                Helper.deleteTempFiles(tempFilePath);
                System.exit(0);
            } catch (IOException var6) {
                throw new RuntimeException(var6);
            }
        });
    }
}

