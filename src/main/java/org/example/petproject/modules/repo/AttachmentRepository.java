package org.example.petproject.modules.repo;

import org.example.petproject.modules.entity.Attachment;
import org.example.petproject.modules.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByUser(User user);
}