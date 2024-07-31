package com.example.taam.ui.report.generator;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.taam.database.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
import com.google.firebase.storage.FileDownloadTask;

import java.io.File;
import java.io.IOException;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Reports implements PDFGenerator {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private Document document;
    private Table table;
    private Cell cell;
    private DownloadCompleteListener downloadCompleteListener;
    private int pendingDownloads = 0;

    public void setDownloadCompleteListener(DownloadCompleteListener downloadCompleteListener) {
        this.downloadCompleteListener = downloadCompleteListener;
    }

    public void downloadFile(String urlPath) {
        pendingDownloads++;
        StorageReference fileRef = storage.getReferenceFromUrl(urlPath);
        try {
            File localFile = File.createTempFile("image", "jpg");
            fileRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                String absolutePath = localFile.getAbsolutePath();
                Log.d("Firebase", "File downloaded to: " + absolutePath);

                try {
                    Image img = new Image(ImageDataFactory.create(absolutePath));
                    cell.add(img.scaleAbsolute(100f, 100f));
                    checkPendingDownloads();
                } catch (MalformedURLException e) {
                    Log.e("Firebase", "Error creating URL", e);
                    checkPendingDownloads();
                }
            }).addOnFailureListener(exception -> {
                Log.e("Firebase", "Error downloading file", exception);
                checkPendingDownloads();
            });
        } catch (IOException e) {
            Log.e("Firebase", "Error creating local file", e);
            checkPendingDownloads();
        }
    }

    private void checkPendingDownloads() {
        pendingDownloads--;
        if (pendingDownloads == 0) {
            table.addCell(cell);
            applyChanges();
            if (downloadCompleteListener != null) {
                downloadCompleteListener.onDownloadComplete();
            }
        }
    }

    public void generate(Document document) {
        PdfFont bold;
        cell = new Cell();
        try {
            bold = PdfFontFactory.createFont("Helvetica-Bold");
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
    public void populate(Item item) {
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

        Field[] fields = item.getClass().getFields();
        table.addCell(new Cell().add(new Paragraph(item.getLot())));
        for (Field field : fields) {
            try {
                if (field.getName().equals("mediaUrls")) {
                    ArrayList<HashMap<String, String>> mediaUrls = (ArrayList<HashMap<String, String>>) field.get(item);
                    for (HashMap<String, String> media : mediaUrls) {
                        //table.addCell(new Cell().addStyle(cellStyle));
                        downloadFile(media.get("image"));
                    }
                } else {
                    table.addCell(new Cell()
                                    .add(new Paragraph(Objects
                                            .requireNonNull(field.get(item))
                                            .toString())))
                            .addStyle(cellStyle);
                }
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

