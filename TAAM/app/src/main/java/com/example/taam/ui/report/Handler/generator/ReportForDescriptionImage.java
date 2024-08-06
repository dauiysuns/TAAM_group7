package com.example.taam.ui.report.Handler.generator;

import com.example.taam.database.Item;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import java.lang.reflect.Field;
import java.util.Objects;

public class ReportForDescriptionImage extends ReportForAllFields implements PDFGenerator {

    // set up table properties that are specific to Description and Image only reports
    @Override
    public void generateTable(Style headerStyle) {
        float[] columnWidths = {1, 4, 4};

        table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell(new Cell().add(new Paragraph("Lot#")).addStyle(headerStyle));
        table.addHeaderCell(new Cell().add(new Paragraph("Description")).addStyle(headerStyle));
        table.addHeaderCell(new Cell().add(new Paragraph("Picture/Video")).addStyle(headerStyle));
    }

    // adds Description cell to table
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

