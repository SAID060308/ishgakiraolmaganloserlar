package org.example.petproject.modules.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.petproject.modules.base.BaseEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Category extends BaseEntity {

    private String name;

}
