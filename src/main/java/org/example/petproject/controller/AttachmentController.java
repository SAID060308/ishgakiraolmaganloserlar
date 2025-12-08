package org.example.petproject.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.example.petproject.modules.entity.Attachment;
import org.example.petproject.modules.entity.AttachmentContent;
import org.example.petproject.modules.entity.User;
import org.example.petproject.modules.repo.AttachmentContentRepository;
import org.example.petproject.modules.repo.AttachmentRepository;
import org.example.petproject.modules.repo.UserRepository;
import org.example.petproject.service.inter.AttachmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*") // Frontend bilan bog'lanish uchun
public class AttachmentController {

    private final UserRepository userRepository;
    private final AttachmentService attachmentService;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentContentRepository attachmentContentRepository;

    public AttachmentController(UserRepository userRepository, AttachmentService attachmentService, AttachmentRepository attachmentRepository, AttachmentContentRepository attachmentContentRepository) {
        this.userRepository = userRepository;
        this.attachmentService = attachmentService;
        this.attachmentRepository = attachmentRepository;
        this.attachmentContentRepository = attachmentContentRepository;
    }

    @PostMapping("/attachments/upload")
    public ResponseEntity<?> uploadFiles(@RequestParam("file") MultipartFile file) throws IOException {
        attachmentService.saveAndClassify(file,userRepository.getReferenceById(1L));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/attachments")
    public ResponseEntity<?> getUserAttachments() {
        try {

            User user = userRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("User topilmadi"));
            System.out.println("=================================================="+user.getName());

            List<Attachment> attachments = attachmentService.getAttachmentsByUser(user);
            return ResponseEntity.ok(attachments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"Xatolik yuz berdi: \" + e.getMessage()}");
        }
    }

    @GetMapping("/attachments/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {

        
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));

        AttachmentContent content = attachmentContentRepository.findById(attachment.getContent().getId())
                .orElseThrow(() -> new RuntimeException("Content not found"));


        return ResponseEntity.ok()
                .header("Content-Type", attachment.getFileType())
                .header("Content-Disposition", "attachment; filename=\"" + attachment.getFileName() + "\"")
                .body(content.getContent());
    }




}