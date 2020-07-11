package com.regtrans.controller;

import javafx.print.PrinterJob;
import javafx.stage.Window;
import org.hibernate.jpa.internal.util.PersistenceUtilHelper;

import javax.print.*;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class PrintFileController {


    public static void printDocument(String fileName){
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.print(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
