package org.example.petproject.modules.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.petproject.modules.base.BaseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AttachmentContent extends BaseEntity {

    private byte[] content;

}
