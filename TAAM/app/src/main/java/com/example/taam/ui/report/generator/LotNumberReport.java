package com.example.taam.ui.report.generator;

import com.example.taam.database.DataModel;
import com.example.taam.database.DataView;
import com.example.taam.database.Item;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

public class LotNumberReport implements PDFGenerator{
    Document document;

    @Override
    public void generate(Document document) {
        this.document = document;
    }

    public void populate(Item item) {
        document.add(new Paragraph(item.getLot()));
    }
}
