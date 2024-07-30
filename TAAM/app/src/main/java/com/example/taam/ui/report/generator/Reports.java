package com.example.taam.ui.report.generator;

import android.net.Uri;
import android.util.Log;

import com.example.taam.database.Item;
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

import java.io.IOException;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Reports implements PDFGenerator{
    static PdfFont bold;
    static PdfFont normal;
    Document document;
    Table table;

    static {
        try {
            bold = PdfFontFactory.createFont("Helvetica-Bold");
            normal = PdfFontFactory.createFont("Helvetica");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Title
    Paragraph title = new Paragraph("Toronto Asian Art Museum Collection Management")
            .setFont(bold)
            .setFontSize(20)
            .setBold()
            .setMarginBottom(20);


    // Table Column Widths
    float[] columnWidths = {1, 3, 3, 2, 4, 4};

    // Header style
    static Style headerStyle = new Style()
            .setFont(bold)
            .setFontSize(12)
            .setBackgroundColor(new DeviceRgb(200, 200, 200))
            .setPadding(5);

    // Cell style
    static Style cellStyle = new Style()
            .setFont(normal)
            .setFontSize(10)
            .setPadding(5);

    public void generate(Document document) {
        this.document = document;
        document.add(title);

        table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell(new Cell().add(new Paragraph("Lot#")).addStyle(headerStyle));
        table.addHeaderCell(new Cell().add(new Paragraph("Name")).addStyle(headerStyle));
        table.addHeaderCell(new Cell().add(new Paragraph("Category")).addStyle(headerStyle));
        table.addHeaderCell(new Cell().add(new Paragraph("Period")).addStyle(headerStyle));
        table.addHeaderCell(new Cell().add(new Paragraph("Description")).addStyle(headerStyle));
        table.addHeaderCell(new Cell().add(new Paragraph("Picture/Video")).addStyle(headerStyle));
    }
    @Override
    public void populate(Item item){
        Field [] fields = item.getClass().getFields();
        table.addCell(new Cell().add(new Paragraph(item.getLot())));
        for (Field field: fields) {
            try {
                if (field.getName().equals("mediaUrls")) {
                    Cell cell = new Cell();
                    //cell.add(new Image(ImageDataFactory.create(item.getMediaUrls().get(0).getUri())));
                    table.addCell(cell).addStyle(cellStyle);
                } else
                    table.addCell(new Cell()
                            .add(new Paragraph(Objects
                                    .requireNonNull(field.get(item))
                                    .toString())))
                            .addStyle(cellStyle);
            } catch (IllegalAccessException e) {
                Log.v("error", Objects.requireNonNull(e.getMessage()));
            }
        }
    }

    @Override
    public void applyChanges() {
        document.add(table);
    }
}
