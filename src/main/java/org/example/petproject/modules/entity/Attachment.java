package org.example.petproject.modules.entity;

import jakarta.persistence.Entity;
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


}
