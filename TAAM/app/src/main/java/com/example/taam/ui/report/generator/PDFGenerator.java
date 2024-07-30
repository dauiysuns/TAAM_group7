package com.example.taam.ui.report.generator;

import com.example.taam.database.Item;
import com.itextpdf.layout.Document;

public interface PDFGenerator{
    public void generate(Document document);
    public void populate(Item item);
    public void applyChanges();
}
