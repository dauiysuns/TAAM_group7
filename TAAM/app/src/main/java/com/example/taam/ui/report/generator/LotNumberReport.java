package com.example.taam.ui.report.generator;

import com.example.taam.database.DataModel;
import com.example.taam.database.DataView;
import com.example.taam.database.Item;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

public class LotNumberReport implements PDFGenerator, DataView {
    Document document;
    Table table;
    DataModel dm = new DataModel(this);

    @Override
    public void generate(Document document) {
        Table table = new Table(1);

        table.setBorder(Border.NO_BORDER);
        table.addCell(new Cell().add(new Paragraph("Lot Number")));

        document.add(new Paragraph("Lot!"));
        this.document = document;
        this.table = table;

        dm.displayAllItems();

        document.add(table);
    }

    @Override
    public void updateView(Item item) {
        table.addCell(new Cell().add(new Paragraph(item.getLot())));
    }

    @Override
    public void showError(String errorMessage) {

    }
}
