package screenCapture.excelHelper;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Iterator;
import java.util.List;

public class excelUtil {
    private static Workbook my_workbook;
    private static Sheet my_sheet;
    private static int row = 0;

    public excelUtil() {
    }

    public static void addImageToExcel(String reportPath, List<String> screenshots, String results) throws IOException {
        openExcel(reportPath);
        Iterator var3 = screenshots.iterator();

        while (var3.hasNext()) {
            String image = (String) var3.next();
            pasteInExcel(image, reportPath, results);
        }

    }

    private static void pasteInExcel(String imgPath, String reportLocation, String reportName) throws IOException {
        InputStream my_banner_image = new FileInputStream(imgPath);
        byte[] bytes = IOUtils.toByteArray(my_banner_image);
        int my_picture_id = my_workbook.addPicture(bytes, 6);
        my_banner_image.close();
        XSSFDrawing drawing = (XSSFDrawing) my_sheet.createDrawingPatriarch();
        XSSFPicture my_picture = drawing.createPicture(getAnchorPoint(), my_picture_id);
        my_picture.resize();
        FileOutputStream fos = new FileOutputStream(reportName + "//" + reportLocation);
        my_workbook.write(fos);
        fos.close();
    }

    public static void openExcel(String excelPath) throws IOException {
        File f = new File(excelPath);
        if (!f.exists()) {
            my_workbook = new XSSFWorkbook();
            my_sheet = my_workbook.createSheet("Screenshots");
        } else {
            my_workbook = new XSSFWorkbook(new FileInputStream(excelPath));
            my_sheet = my_workbook.getSheet("Screenshots");
        }

    }

    public static XSSFClientAnchor getAnchorPoint() {
        System.out.println("Row is " + row);
        XSSFClientAnchor my_anchor = new XSSFClientAnchor();
        my_anchor.setCol1(2);
        my_anchor.setRow1(row);
        row += 60;
        return my_anchor;
    }
}
