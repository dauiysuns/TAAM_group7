package com.example.taam.ui.report.generator;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class CategoryReport implements PDFGenerator{

    @Override
    public void generate(Document document) {
        document.add(new Paragraph("category!"));

    }
}
