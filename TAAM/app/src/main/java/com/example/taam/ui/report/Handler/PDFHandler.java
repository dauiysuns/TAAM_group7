package com.example.taam.ui.report.Handler;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.example.taam.database.DataModel;
import com.example.taam.database.DataView;
import com.example.taam.database.Item;
import com.example.taam.ui.report.Handler.generator.DownloadCompleteListener;
import com.example.taam.ui.report.Handler.generator.PDFGenerator;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import java.io.File;
import java.io.FileNotFoundException;

public class PDFHandler implements DataView, DownloadCompleteListener {
    public interface PDFCallback {
        void onPDFGenerated();
    }

    private final Context context;
    private final PDFCallback pdfCallback;
    private PdfWriter writer;
    private PdfDocument pdfDoc;
    private Document document;
    private File file;
    private PDFGenerator generator;

    public PDFHandler(Context context, PDFCallback pdfCallback) {
        this.context = context;
        this.pdfCallback = pdfCallback;
    }

    public void generatePdf(String category, String input, PDFGenerator pdfGenerator) {
        this.generator = pdfGenerator;

        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment
                        .DIRECTORY_DOWNLOADS)
                .toString() + "/TAAMReport" + category + input + ".pdf";

        file = new File(pdfPath);

        try {
            writer = new PdfWriter(file);
            pdfDoc = new PdfDocument(writer);
            document = new Document(pdfDoc);

            DataModel dm = new DataModel(this);
            generator.generate(document);
            loadDataIntoGenerator(dm, category, input);
            Log.v("Generate Report", "Successful");
        } catch (FileNotFoundException e) {
            Log.v("error", e.getMessage());
        }
    }

    private void loadDataIntoGenerator(DataModel dm, String category, String input) {
        if (category.equals("Category with Description and Picture only")) {
            dm.getItemsByCategory("category", input, context);
        } else if (category.equals("Period with Description and Picture only")) {
            dm.getItemsByCategory("period", input, context);
        } else if (category.equals("All Items")){
            dm.getAllItems();
        } else {
            dm.getItemsByCategory(category.toLowerCase(), input, context);
        }
    }

    public void viewPdf() {
        Uri pdfUri = FileProvider.getUriForFile(context,
                context.getPackageName() + ".provider", file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent chooser = Intent.createChooser(intent, "Open PDF");
        context.startActivity(chooser);
    }

    @Override
    public void updateView(Item item) {
        generator.populate(item);
    }

    private void onCompleted() {
        try {
            document.close();
        } catch (Exception e) {
            Log.v("error", e.getMessage());
        }
        try {
            pdfDoc.close();
        } catch (Exception e) {
            Log.v("error", e.getMessage());
        }
        try {
            writer.close();
        } catch (Exception e) {
            Log.v("error", e.getMessage());
        }
        viewPdf();
        pdfCallback.onPDFGenerated();
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void showError(String errorMessage) {
        // TODO: Handle error
    }

    @Override
    public void onDownloadComplete() {
        onCompleted();
    }
}
