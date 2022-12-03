package com.file.test;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.file.test.model.JsonData;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipFile;

import static com.codeborne.pdftest.PDF.containsText;
import static com.file.test.utils.FileUtils.getFile;
import static org.assertj.core.api.Assertions.assertThat;


public class FileTest {

    ClassLoader cl = FileTest.class.getClassLoader();
    ZipFile file = new ZipFile("src/test/resources/test.zip");

    public FileTest() throws IOException {
    }

    @Test
    public void jsonTest() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try (
                InputStream resource = cl.getResourceAsStream("test.json");
                InputStreamReader reader = new InputStreamReader(resource)
        ) {
            JsonData jsonData = objectMapper.readValue(reader, JsonData.class);
            assertThat(jsonData.id).isEqualTo(1);
            assertThat(jsonData.name).isEqualTo("Any Restaurant");
            assertThat(jsonData.kitchen).isEqualTo("Italian");
            assertThat(jsonData.work).isTrue();
            assertThat(jsonData.topics.get(0)).isEqualTo("Pizza");
            assertThat(jsonData.topics.get(1)).isEqualTo("Pasta");
            assertThat(jsonData.topics.get(2)).isEqualTo("Coffee");
            assertThat(jsonData.topics.get(3)).isEqualTo("Wine");
            assertThat(jsonData.address.city).isEqualTo("Karaganda");
            assertThat(jsonData.address.country).isEqualTo("Kazakhstan");
        }
    }

    @Test
    public void pdfTest() throws IOException {
        PDF pdf = new PDF(getFile(file, "test.pdf"));
        MatcherAssert.assertThat(pdf, containsText("Тестовый pdf файл"));
    }

    @Test
    public void xlsxTest() throws IOException {
        XLS xlsx = new XLS(getFile(file, "test.xlsx"));
        assertThat(xlsx.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue()).isEqualTo("Номер");
        assertThat(xlsx.excel.getSheetAt(0).getRow(0).getCell(1).getStringCellValue()).isEqualTo("Название");
        assertThat(xlsx.excel.getSheetAt(0).getRow(1).getCell(0).getNumericCellValue()).isEqualTo(1);
        assertThat(xlsx.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue()).isEqualTo("Pizza");
        assertThat(xlsx.excel.getSheetAt(0).getRow(2).getCell(0).getNumericCellValue()).isEqualTo(2);
        assertThat(xlsx.excel.getSheetAt(0).getRow(2).getCell(1).getStringCellValue()).isEqualTo("Pasta");
        assertThat(xlsx.excel.getSheetAt(0).getRow(3).getCell(0).getNumericCellValue()).isEqualTo(3);
        assertThat(xlsx.excel.getSheetAt(0).getRow(3).getCell(1).getStringCellValue()).isEqualTo("Coffee");
        assertThat(xlsx.excel.getSheetAt(0).getRow(4).getCell(0).getNumericCellValue()).isEqualTo(4);
        assertThat(xlsx.excel.getSheetAt(0).getRow(4).getCell(1).getStringCellValue()).isEqualTo("Wine");
    }

    @Test
    public void csvTest() throws IOException, CsvException {
        CSVReader csvReader = new CSVReader(
                new InputStreamReader(getFile(file, "test.csv"),
                                              StandardCharsets.UTF_8));
        List<String[]> content = csvReader.readAll();
        assertThat(content.get(0)[0]).isEqualTo("Номер");
        assertThat(content.get(0)[1]).isEqualTo("Название");
        assertThat(content.get(1)[0]).isEqualTo("1");
        assertThat(content.get(1)[1]).isEqualTo("Pizza");
        assertThat(content.get(2)[0]).isEqualTo("2");
        assertThat(content.get(2)[1]).isEqualTo("Pasta");
        assertThat(content.get(3)[0]).isEqualTo("3");
        assertThat(content.get(3)[1]).isEqualTo("Coffee");
        assertThat(content.get(4)[0]).isEqualTo("4");
        assertThat(content.get(4)[1]).isEqualTo("Wine");
    }
}
