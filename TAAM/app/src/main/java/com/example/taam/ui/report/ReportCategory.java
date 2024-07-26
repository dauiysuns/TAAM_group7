package com.example.taam.ui.report;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class ReportCategory implements PDFGenerator{

    @Override
    public void generate(Document document) {
        document.add(new Paragraph("category!"));

    }
}
