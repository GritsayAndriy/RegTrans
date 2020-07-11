package com.regtrans.service;

import com.regtrans.model.Driver;
import com.regtrans.model.TimeSheet;
import com.regtrans.model.TypeTransportsRate;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;

@Service
public class ReportService {

    private static final String FILE_NAME_TEMPLATE_TRUCK = "templateTruck";
    private static final String FILE_NAME_TEMPLATE_SPECIAL_MACHINERY = "templateSpec";
    private static final String LOCATION_TEMPLATE = "report/template";
    private static final String LOCATION_REPORTS = "report";
    private static final String NAME_TABLE_FOR_TIME_SHEET_TRUCK = "Завдання водія";
    private static final String NAME_TABLE_FOR_TIME_SHEET_SPECIAL = "Виконання";

    private Desktop desktop;
    private String fileFormat;
    private String dateReports;
    private String route;
    private String сustomer;
    private String address;
    private TypeTransportsRate typeTransports;


    private static final String[] parameters = new String[]{
            "withtrailer",
            "address",
            "customer",
            "finishing",
            "transport",
            "fullname",
            "trailer",
            "fuel",
            "usefuel",
            "departure",
            "arrival",
            "date",
            "route",
            "resultwork",
            "usemonth",
            "month",
            "year"

    };


    @Autowired
    public void setDesktop(Desktop desktop) {
        this.desktop = desktop;
    }

    public void setFormat(String format) {
        this.fileFormat = "." + format;
    }

    public void setDate(String date) {
        this.dateReports = date;
    }

    public void setСustomer(String сustomer) {
        this.сustomer = сustomer;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public void createFile() throws IOException {
        checkPackageWithTemplate();
    }

    private File createPackageReport(String date) throws IOException {
        File dirReports = new File(LOCATION_REPORTS + "/" + date);
        if (!dirReports.exists()) {
            boolean exist = dirReports.mkdir();
            if (!exist) {
                throw new IOException();
            }
        }
        return dirReports;
    }

    public void writeReport(Driver driver, String fileName, TypeTransportsRate typeTransport) throws IOException {
        String pathTemplateTruck = LOCATION_TEMPLATE + "/" + FILE_NAME_TEMPLATE_TRUCK + this.fileFormat;
        String pathTemplateSpec = LOCATION_TEMPLATE + "/" + FILE_NAME_TEMPLATE_SPECIAL_MACHINERY + this.fileFormat;
        List<TimeSheet> timeSheets = driver.getTimeSheets();
        Comparator<TimeSheet> comparator = Comparator.comparing(TimeSheet::getDayDate, (s1, s2) -> {
            LocalDate dateS1 = LocalDate.parse(s1);
            LocalDate dateS2 = LocalDate.parse(s2);
            return dateS1.compareTo(dateS2);
        });
        timeSheets.sort(comparator);
        String locationReports = createPackageReport(this.dateReports).getPath();
        this.typeTransports = typeTransport;
        if (TypeTransportsRate.TRUCK == typeTransport) {
            File fileTemplateGruz = new File(pathTemplateTruck);
            File fileReport = new File(locationReports + "/" + fileName + "Truck" + this.fileFormat);
            writeReport(driver, timeSheets, fileTemplateGruz, fileReport);
        } else {
            File fileTemplateSpec = new File(pathTemplateSpec);
            File fileReport = new File(locationReports + "/" + fileName + "Spec" + this.fileFormat);
            writeReport(driver, timeSheets, fileTemplateSpec, fileReport);
        }
    }

    private void writeReport(Driver driver, List<TimeSheet> timeSheetList, File fileTemplate, File fileReport)
            throws IOException {
        try (FileInputStream inputStreamTemplate = new FileInputStream(fileTemplate);
             FileOutputStream outputStreamTimeSheet = new FileOutputStream(fileReport)) {
            XWPFDocument documentTemplate = new XWPFDocument(inputStreamTemplate);
            replaceParagraph(documentTemplate, driver);
            replaceTables(documentTemplate, driver, timeSheetList);
            documentTemplate.write(outputStreamTimeSheet);
            documentTemplate.close();
        } catch (IOException e) {
            throw new IOException();
        }

    }

    private void replaceParagraph(XWPFDocument document, Driver driver) {
        for (XWPFParagraph p : document.getParagraphs()) {
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    r.setText(replaceValue(text, driver), 0);
                }
            }
        }
    }

    private void replaceTables(XWPFDocument document, Driver driver, List<TimeSheet> timeSheets) throws IOException {
        for (XWPFTable tbl : document.getTables()) {
            if (tbl.getText().contains(NAME_TABLE_FOR_TIME_SHEET_TRUCK) ||
                    tbl.getText().contains(NAME_TABLE_FOR_TIME_SHEET_SPECIAL)) {
                int templateRow = tbl.getNumberOfRows() - 1;
                XWPFTableRow oldRow = tbl.getRow(templateRow);
                int lastRow = 0;
                for (int i = 0; i < timeSheets.size(); ++i) {
                    CTRow ctrow = null;
                    try {
                        ctrow = CTRow.Factory.parse(oldRow.getCtRow().newInputStream());
                    } catch (XmlException e) {
                        e.printStackTrace();
                    }
                    XWPFTableRow newRow = new XWPFTableRow(ctrow, tbl);
                    for (XWPFTableCell cell : newRow.getTableCells()) {
                        for (XWPFParagraph p : cell.getParagraphs()) {
                            for (XWPFRun r : p.getRuns()) {
                                String text = r.getText(0);
                                r.setText(replaceValue(text, timeSheets.get(i)), 0);
                            }
                        }
                    }
                    lastRow = i + templateRow;
                    tbl.addRow(newRow, lastRow);
                }
                tbl.removeRow(lastRow + 1);
            } else {
                for (int j = 0; j < tbl.getRows().size(); j++) {
                    XWPFTableRow row = tbl.getRow(j);
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph p : cell.getParagraphs()) {
                            for (XWPFRun r : p.getRuns()) {
                                String text = r.getText(0);
                                r.setText(replaceValue(text, driver), 0);
                            }
                        }
                    }
                }
            }
        }
    }

    private String replaceValue(String text, TimeSheet timeSheet) {
        double value;
        for (String param : parameters) {
            if (text != null && text.contains(param)) {
                switch (param) {
                    case "withtrailer":
                        if (timeSheet.getTrailer() != null) {
                            text = text.replace(param, String.valueOf(timeSheet.getResultWork()));
                        } else {
                            text = text.replace(param, "");
                        }
                        break;
                    case "transport":
                        text = text.replace(param, timeSheet.getTransport().getBrand() + " "
                                + timeSheet.getTransport().getModel() + " "
                                + timeSheet.getTransport().getNumber() + " ");
                        break;
                    case "trailer":
                        if (timeSheet.getTrailer() != null) {
                            text = text.replace(param, timeSheet.getTrailer().getBrand()
                                    + " " + timeSheet.getTrailer().getNumber());
                        } else {
                            text = text.replace(param, "");
                        }
                        break;

                    case "usefuel":
                        value = Decimal.cleaningExtra(timeSheet.getUseFuel());
                        text = text.replace(param, String.valueOf(value));
                        value = 0;
                        break;
                    case "date":
                        text = text.replace(param, timeSheet.getDayDate());
                        break;
                    case "route":
                        if (this.route != null) {
                            text = text.replace(param, route);
                        } else {
                            text = text.replace(param, "");
                        }
                        break;
                    case "resultwork":
                        value = Decimal.cleaningExtra(timeSheet.getResultWork());
                        if (TypeTransportsRate.TRUCK == typeTransports) {
                            text = text.replace(param, String.valueOf(value));
                        } else {
                            String time = getResultTime(value);
                            text = text.replace(param, time);
                        }
                        value = 0;
                        break;
                    case "finishing":
                        String result = getFinishing(timeSheet);
                        text = text.replace(param, result);
                        break;

                }
            }
        }
        return text;
    }

    private String replaceValue(String text, Driver driver) {
        for (String param : parameters) {
            if (text != null && text.contains(param)) {
                switch (param) {
                    case "transport":
                        text = text.replace(param, driver.getTransport().getBrand() + " "
                                + driver.getTransport().getModel() + " "
                                + driver.getTransport().getNumber() + " ");
                        break;
                    case "fullname":
                        text = text.replace(param,
                                driver.getLastName()
                                        + " "
                                        + driver.getFirstName()
                                        + " "
                                        + driver.getFatherName());
                        break;
                    case "fuel":
                        text = text.replace(param, driver.getTransport().getTypeFuel().getName());
                        break;
                    case "usemonth":
                        double sumFuel = driver.getTimeSheets()
                                .stream()
                                .filter(timeSheet -> {
                                    return timeSheet.getTransport().getTypeFuel() == driver.getTransport().getTypeFuel();
                                })
                                .mapToDouble(TimeSheet::getUseFuel)
                                .sum();
                        sumFuel = Decimal.cleaningExtra(sumFuel);
                        text = text.replace(param, String.valueOf(sumFuel));
                        break;
                    case "month":
                        LocalDate dateMonth = LocalDate.parse(dateReports);
                        Month month = dateMonth.getMonth();
                        String stringMonth = com.regtrans.model.date.Month.UA.getMonth(month.getValue() - 1);
                        text = text.replace(param, stringMonth);
                        break;
                    case "year":
                        LocalDate dateYear = LocalDate.parse(dateReports);
                        int year = dateYear.getYear();
                        text = text.replace(param, String.valueOf(year));
                        break;
                    case "departure":
                        sumFuel = driver.getTimeSheets()
                                .stream()
                                .filter(timeSheet -> {
                                    return timeSheet.getTransport().getTypeFuel() == driver.getTransport().getTypeFuel();
                                })
                                .mapToDouble(TimeSheet::getUseFuel)
                                .sum();
                        sumFuel = Decimal.cleaningExtra(sumFuel);
                        text = text.replace(param, String.valueOf(sumFuel + 10));
                        break;
                    case "arrival":
                        text = text.replace(param, String.valueOf(10));
                        break;
                    case "customer":
                        if (this.route != null) {
                            text = text.replace(param, this.сustomer);
                        } else {
                            text = text.replace(param, "");
                        }
                        break;
                    case "address":
                        if (this.route != null) {
                            text = text.replace(param, this.address);
                        } else {
                            text = text.replace(param, "");
                        }
                        break;
                }
            }
        }
        return text;
    }


    public void printReport(String date) throws IOException {
        File[] files = new File(LOCATION_REPORTS + "/" + date).listFiles();
        if (files != null) {
            for (File file : files) {
                desktop.print(file);
            }
        }

    }

    public void checkPackageWithTemplate() throws IOException {
        File dirReports = new File(LOCATION_REPORTS);
        if (!dirReports.exists()) {
            boolean exist = dirReports.mkdir();
            if (!exist) {
                throw new IOException();
            }
        }

        File dirTemplate = new File(LOCATION_TEMPLATE);
        if (!dirTemplate.exists()) {
            boolean exist = dirTemplate.mkdir();
            if (!exist) {
                throw new IOException();
            }
        }

        File fileTemplateTruck = new File(LOCATION_TEMPLATE + "/" + FILE_NAME_TEMPLATE_TRUCK + this.fileFormat);
        File fileTemplateSpec = new File(LOCATION_TEMPLATE + "/" + FILE_NAME_TEMPLATE_SPECIAL_MACHINERY + this.fileFormat);
        if (!fileTemplateTruck.exists()) {
            createTemplate(fileTemplateTruck);
        }
        if (!fileTemplateSpec.exists()) {
            createTemplate(fileTemplateSpec);
        }
    }


    public void createTemplate(File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            XWPFDocument document = new XWPFDocument();
            XWPFParagraph tmpParagraph = document.createParagraph();
            XWPFRun tmpRun = tmpParagraph.createRun();
            String paramRun = "";
            for (String param : parameters) {
                paramRun += param + " ";
            }
            tmpRun.setText(paramRun);
            document.write(fos);
        } catch (IOException e) {
            throw new IOException();
        }
    }

    private String getResultTime(double time) {
        int hour = (int) time;
        double min = time >= 1 ? time % (double) hour : time;

        min = Decimal.cleaningExtra(min);

        min *= 100;
        min = min * 60 / 100;
        return hour + " год. " + (int) min + "хв.";
    }

    private String getFinishing(TimeSheet timeSheet) {
        double time;
        if (timeSheet.getResultWork() > 5) {
            time = Decimal.cleaningExtra(timeSheet.getResultWork() + 9);
        } else {
            time = Decimal.cleaningExtra(timeSheet.getResultWork() + 8);
        }
        int hour = (int) time;
        double min = time >= 1 ? time % (double) hour : time;

        min = Decimal.cleaningExtra(min);

        min *= 100;
        min = min * 60 / 100;
        String strMin = String.format("%02d", (int) min);
        return hour + ":" + strMin;
    }
}
