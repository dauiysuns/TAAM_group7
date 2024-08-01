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

public class ReportForAllFields implements PDFGenerator {
    protected FirebaseStorage storage = FirebaseStorage.getInstance();
    protected StorageReference storageRef = storage.getReference();
    protected Document document;
    protected Table table;
    //private Cell cell;
    protected DownloadCompleteListener downloadCompleteListener;
    protected int pendingDownloads = 0;

    public void setDownloadCompleteListener(DownloadCompleteListener downloadCompleteListener) {
        this.downloadCompleteListener = downloadCompleteListener;
    }

    public void downloadFile(String urlPath, Cell cell) {
        //pendingDownloads++;
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

    protected void checkPendingDownloads() {
        pendingDownloads--;
        if (pendingDownloads == 0) {
            //table.addCell(cell);
            applyChanges();
            if (downloadCompleteListener != null) {
                downloadCompleteListener.onDownloadComplete();
            }
        }
    }

    public void generate(Document document) {
        PdfFont bold;
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

        Style headerStyle = new Style()
                .setFont(bold)
                .setFontSize(12)
                .setBackgroundColor(new DeviceRgb(200, 200, 200))
                .setPadding(5);

        this.document = document;
        document.add(title);

        generateTable(headerStyle);
    }

    public void generateTable(Style headerStyle){
        float[] columnWidths = {1, 3, 3, 2, 4, 4};

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

            Style cellStyle = new Style()
                    .setFont(normal)
                    .setFontSize(10)
                    .setPadding(5);

            table.addCell(new Cell().add(new Paragraph(item.getLot())));

            // add required field(s)
            addField(item, cellStyle);

            // add the mediaUrls
            Field field = item.getClass().getDeclaredField("mediaUrls");
            ArrayList<HashMap<String, String>> mediaUrls = (ArrayList<HashMap<String, String>>) field.get(item);
            Cell cell = new Cell().addStyle(cellStyle);
            table.addCell(cell);
            for (HashMap<String, String> media : mediaUrls) {
                pendingDownloads++;
                String image = media.get("image");
                String video = media.get("video");
                //table.addCell(new Cell().addStyle(cellStyle));
                if (image != null) {
                    downloadFile(media.get("image"), cell);
                }
                if (video != null) {
                    cell.add(new Paragraph(video));
                    checkPendingDownloads();
                }
            }
            if (mediaUrls.isEmpty()) {
                cell.add(new Paragraph("None"));
                pendingDownloads++;
                checkPendingDownloads();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            Log.v("error", Objects.requireNonNull(e.getMessage()));
        } catch (NoSuchFieldException e) {
            Log.v("error", "Cannot access field");
        }
    }

    @Override
    public void applyChanges() {
        document.add(table);
    }

    public void addField(Item item, Style cellStyle) throws NoSuchFieldException, IllegalAccessException {
        // set up desired order for fields other than lot
        String[] fieldOrder = {"name", "category", "period", "description"};

        for (String fieldName : fieldOrder) {
            Field field = item.getClass().getDeclaredField(fieldName);
            table.addCell(new Cell()
                            .add(new Paragraph(Objects
                                    .requireNonNull(field.get(item))
                                    .toString())))
                    .addStyle(cellStyle);
        }
    }
}

