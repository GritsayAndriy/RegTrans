package com.regtrans.service;

import com.regtrans.model.TimeSheet;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class ReportService {

    private String fileNameTemplate = "template";
    private String fileFormat;
    private String locationTemplate = "report/template";
    private String locationReports = "report";

    private static final String[] parameters = new String[]{
            "fullname",
            "data",
            "transport",
            "usefuel",
            "resultwork"
    };

    public void setFormat(String format) {
        this.fileFormat = "." + format;
    }

    public void createFile() throws IOException {
        checkPackageWithTemplate();
    }

    public void writeReport(List<TimeSheet> timeSheetList, String fileNameWithMonth) throws IOException {
        String pathTemplate = locationTemplate + "/" + fileNameTemplate + this.fileFormat;
        File fileTemplate = new File(pathTemplate);
        File fileTimeSheet = new File(locationReports + "/" + fileNameWithMonth + this.fileFormat);


        try (FileInputStream inputStreamTemplate = new FileInputStream(fileTemplate);
             FileOutputStream outputStreamTimeSheet = new FileOutputStream(fileTimeSheet)) {
            XWPFDocument documentTemplate = new XWPFDocument(inputStreamTemplate);
            XWPFDocument documentReport = new XWPFDocument();

            XWPFParagraph copyP;
            documentReport.createParagraph();

            int i=0;

            for (TimeSheet timeSheet : timeSheetList) {
                for (XWPFParagraph p : documentTemplate.getParagraphs()) {
                    copyP = new XWPFParagraph((CTP)p.getCTP().copy(), documentTemplate);
                    List<XWPFRun> runs = copyP.getRuns();
                    if (runs != null) {
                        for (XWPFRun r : runs) {
                            String text = r.getText(0);
                            for (String param : parameters) {
                                if (text != null && text.contains(param)) {
                                    switch (param) {
                                        case "fullname":
                                            text = text.replace(param,
                                                    timeSheet.getDriver().getLastName()
                                                            +" "
                                                            + timeSheet.getDriver().getFirstName()
                                                            + " "
                                                            + timeSheet.getDriver().getFatherName());
                                            break;
                                        case "data":
                                            text = text.replace(param, timeSheet.getDayDate());
                                            break;
                                        case "transport":
                                            text = text.replace(param, timeSheet.getTransport().getBrand()
                                                    + timeSheet.getTransport().getModel()
                                                    + timeSheet.getTransport().getNumber()
                                                    + timeSheet.getTransport().getStaticUseFuel());
                                            break;
                                        case "usefuel":
                                            text = text.replace(param, String.valueOf(timeSheet.getUseFuel()));
                                            break;
                                        case "resultwork":
                                            text = text.replace(param, String.valueOf(timeSheet.getResultWork()));
                                            break;
                                    }
                                    r.setText(text, 0);
                                }
                            }
                        }
                    }
                    documentReport.setParagraph(copyP,i++);
                    documentReport.createParagraph();
                }
            }
            documentReport.write(outputStreamTimeSheet);
            documentReport.close();
        } catch (IOException e) {
            throw new IOException();
        }

    }

    private XmlCursor getNextCursor(XmlObject object){
        XmlCursor xmlCursor =  object.newCursor();
        xmlCursor.toEndToken();
        return xmlCursor;
    }


    public void clearReport() {

    }

    public void printReport() throws IOException {
        String templatePath = locationTemplate + "/" + fileNameTemplate + this.fileFormat;
        File file = new File(templatePath);
        try (FileInputStream fileInp = new FileInputStream(file)) {
            XWPFDocument document = new XWPFDocument(fileInp);
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            System.out.println(extractor.getText());
        } catch (IOException e) {
            throw new IOException();
        }
    }

    public void checkPackageWithTemplate() throws IOException {
        File dirReports = new File(locationReports);
        if (!dirReports.exists()) {
            boolean exist = dirReports.mkdir();
            if (!exist) {
                throw new IOException();
            }
        }

        File dirTemplate = new File(locationTemplate);
        if (!dirTemplate.exists()) {
            boolean exist = dirTemplate.mkdir();
            if (!exist) {
                throw new IOException();
            }
        }

        File fileTemplate = new File(locationTemplate + "/" + fileNameTemplate + this.fileFormat);
        if (!fileTemplate.exists()) {
            createTemplate();
        }
    }


    public void createTemplate() throws IOException {
        String pathTemplate = locationTemplate + "/" + fileNameTemplate + this.fileFormat;
        File file = new File(pathTemplate);
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
}
