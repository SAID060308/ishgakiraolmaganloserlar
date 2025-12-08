package org.example.petproject.modules.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.petproject.modules.base.BaseEntity;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Attachment extends BaseEntity {

    private String fileName;
    private String fileType;
    @OneToOne
    private AttachmentContent content;
    @ManyToOne
    private Category category;
    @ManyToOne
    private User user;
}
