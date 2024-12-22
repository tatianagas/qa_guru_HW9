package model;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
public class ZipParsingTest {

    private ClassLoader cl = ZipParsingTest.class.getClassLoader();

    @Test
    void readPdfFromZipTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("test.zip")
        )) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("aml-form_legal-230356_2024-12-20.pdf")) {
                    PDF pdf = new PDF(zis);
                    Assertions.assertEquals("Анкета клиента - юридического лица", pdf.title);
                    break;
                }
            }
        }

    }

    @Test void readXlsxFromZipTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("test.zip")
        )) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("non_resident_report_physical_011224-0000__101224-2359.xlsx")) {
                    XLS xls = new XLS(zis);

                    String actualValue = xls.excel.getSheetAt(0).getRow(2).getCell(1).getStringCellValue();

                    Assertions.assertTrue(actualValue.contains("Гладков Евгений Симферополевич"));
                    break;
                }
            }
        }
    }

    @Test void readCsvFromZipTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("test.zip")
        )) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("ADDRESS.csv")) {
                    CSVReader csvReader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> list = csvReader.readAll();
                    Assertions.assertEquals(8, list.size());
                };
                break;
            }
        }
    }
}

