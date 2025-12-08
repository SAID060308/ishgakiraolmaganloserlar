package org.example.petproject.service.impl;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.example.petproject.modules.entity.Attachment;
import org.example.petproject.modules.entity.AttachmentContent;
import org.example.petproject.modules.entity.User;
import org.example.petproject.modules.repo.AttachmentContentRepository;
import org.example.petproject.modules.repo.AttachmentRepository;
import org.example.petproject.modules.repo.CategoryRepository;
import org.example.petproject.service.inter.AttachmentService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final CategoryRepository categoryRepository;
    private final AttachmentContentRepository attachmentContentRepository;

    @Override
    public void saveAndClassify(MultipartFile file, User user) throws IOException {
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        Attachment attachment = new Attachment();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFileType(file.getContentType());
        attachment.setUser(user);
        AttachmentContent attachmentContent = new AttachmentContent();
        attachmentContent.setContent(file.getBytes());
        attachmentContentRepository.save(attachmentContent);
        attachment.setContent(attachmentContent);
        String extractedText = "";

        try {
            if (extension.equals(".pdf")) {
                extractedText = extractTextFromPdf(file);
            } else if (extension.equals(".docx")) {
                extractedText = extractTextFromDocx(file);
            } else {
                throw new RuntimeException("File type not supported: " + extension);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse file: " + e.getMessage());
        }
        attachment.setCategory(categoryRepository.findByName(classify(extractedText)));
        attachmentRepository.save(attachment);

    }

    @Override
    public List<Attachment> getAttachmentsByUser(User user) {
        return attachmentRepository.findByUser(user);
    }


    public String extractTextFromPdf(MultipartFile file) throws IOException {
        StringBuilder text = new StringBuilder();

        PdfDocument pdfDocument = new PdfDocument(new PdfReader(file.getInputStream()));
        int pages = pdfDocument.getNumberOfPages();

        for (int i = 1; i <= pages; i++) {
            text.append(PdfTextExtractor.getTextFromPage(pdfDocument.getPage(i)));
        }

        pdfDocument.close();

        return text.toString();
    }
    private String extractTextFromDocx(MultipartFile file) throws Exception {
        StringBuilder sb = new StringBuilder();

        XWPFDocument doc = new XWPFDocument(file.getInputStream());

        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            sb.append(paragraph.getText()).append("\n");
        }

        doc.close();

        return sb.toString();
    }
    public static String classify(String text) {

        if (text == null || text.isEmpty()) return "Boshqa";

        // ---------------------
        // 1) Keywords MAP – ichkarida
        // ---------------------
        Map<String, List<String>> keywords = new LinkedHashMap<>();

        keywords.put("Prezident", Arrays.asList(
                "o'zbekiston respublikasi prezidenti", "ijtimoiy", "qaror", "vazir", "muvofiq",
                 "hokim", "farmon", "prezident",
                "rivojlantirish", "davlat", "iqtisodiyot", "korrupsiya"
        ));

        keywords.put("Vazirlik", Arrays.asList(
                "buyruq", "buyrug'", "professor o‘qituvchilar", "oliy ta'lim",
                "ilova", "axborot texnologiyalari rivojl"
        ));

        keywords.put("Rektorat", Arrays.asList(
                "rektor", "prorektor", "fakultet", "dekan", "talabalar"
        ));

        keywords.put("Kafedra", Arrays.asList(
                "kafedra", "bayonnoma", "majlis", "mudir", "ish reja", "kun tartibi",
                "so'zga chiqdi", "tasdiqlayman", "qarori", "o‘quv dasturi"
        ));

        // ---------------------
        // 2) Normalize input text
        // ---------------------
        String normText = text.toLowerCase().replaceAll("\\s+", " ");

        // ---------------------
        // 3) Score map
        // ---------------------
        Map<String, Double> scores = new LinkedHashMap<>();
        for (String cat : keywords.keySet()) scores.put(cat, 0.0);

        // ---------------------
        // 4) Matching and scoring
        // ---------------------
        for (var entry : keywords.entrySet()) {
            String category = entry.getKey();
            double score = 0;

            for (String kw : entry.getValue()) {
                String normKw = kw.toLowerCase().trim();
                double boost = normKw.contains(" ") ? 1.25 : 1.0;

                Pattern p = Pattern.compile("\\b" + Pattern.quote(normKw) + "\\b",
                        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

                var matcher = p.matcher(normText);
                int count = 0;
                while (matcher.find()) count++;

                score += count * boost;
            }

            scores.put(category, score);
        }

        // ---------------------
        // 5) Best category
        // ---------------------
        String best = "Boshqa";
        double max = 0;

        for (var e : scores.entrySet()) {
            if (e.getValue() > max) {
                max = e.getValue();
                best = e.getKey();
            }
        }

        return max == 0 ? "Boshqa" : best;
    }

}
