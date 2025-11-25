package org.example.petproject.modules.repo;

import org.example.petproject.modules.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}