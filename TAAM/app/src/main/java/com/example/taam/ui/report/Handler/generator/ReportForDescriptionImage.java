package com.example.taam.ui.report.Handler.generator;

import android.util.Log;

import com.example.taam.database.Item;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.kernel.colors.DeviceRgb;

import java.io.File;
import java.io.IOException;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ReportForDescriptionImage extends ReportForAllFields implements PDFGenerator {

    @Override
    public void generateTable(Style headerStyle) {
        float[] columnWidths = {1, 4, 4};

        table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell(new Cell().add(new Paragraph("Lot#")).addStyle(headerStyle));
        table.addHeaderCell(new Cell().add(new Paragraph("Description")).addStyle(headerStyle));
        table.addHeaderCell(new Cell().add(new Paragraph("Picture/Video")).addStyle(headerStyle));
    }

    @Override
    public void addField(Item item, Style cellStyle) throws NoSuchFieldException, IllegalAccessException{
        Field field = item.getClass().getDeclaredField("description");
        table.addCell(new Cell()
                        .add(new Paragraph(Objects
                                .requireNonNull(field.get(item))
                                .toString())))
                .addStyle(cellStyle);
    }
}

