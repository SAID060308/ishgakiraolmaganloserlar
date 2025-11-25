package org.example.petproject.modules.repo;

import org.example.petproject.modules.entity.AttachmentContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentContentRepository extends JpaRepository<AttachmentContent, Long> {
}