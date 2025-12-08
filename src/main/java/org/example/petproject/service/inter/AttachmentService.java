package org.example.petproject.service.inter;

import org.example.petproject.modules.entity.Attachment;
import org.example.petproject.modules.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AttachmentService {
    void saveAndClassify(MultipartFile file, User user) throws IOException;

    List<Attachment> getAttachmentsByUser(User user);
}
