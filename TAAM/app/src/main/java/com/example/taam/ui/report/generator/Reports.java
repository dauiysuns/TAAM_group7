package com.example.taam.ui.report.generator;

import android.net.Uri;
import android.util.Log;

import com.example.taam.database.Item;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.pdfa.PdfADocument;

import java.io.File;
import java.io.IOException;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Reports implements PDFGenerator{
    Document document;
    Table table;

    // Title



    // Table Column Widths

    // Header style


    // Cell style


    public void generate(Document document) {
        PdfFont bold;
        PdfFont normal;
        try {
            bold = PdfFontFactory.createFont("Helvetica-Bold");
            normal = PdfFontFactory.createFont("Helvetica");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Paragraph title = new Paragraph("Toronto Asian Art Museum Collection Management")
                .setFont(bold)
                .setFontSize(20)
                .setBold()
                .setMarginBottom(20);

        float[] columnWidths = {1, 3, 3, 2, 4, 4};

        Style headerStyle = new Style()
                .setFont(bold)
                .setFontSize(12)
                .setBackgroundColor(new DeviceRgb(200, 200, 200))
                .setPadding(5);

        this.document = null;
        this.table = null;

        this.document = document;
        document.add(title);

        this.table = new Table(UnitValue.createPercentArray(columnWidths));
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
        PdfFont normal;
        try {
            normal = PdfFontFactory.createFont("Helvetica");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Style cellStyle = new Style()
                .setFont(normal)
                .setFontSize(10)
                .setPadding(5);


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
        this.document.add(this.table);
    }
}
