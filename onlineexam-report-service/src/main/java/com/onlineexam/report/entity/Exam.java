package com.onlineexam.report.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

// This is a minimal representation needed for the Report entity's relationship
// It doesn't need to be a full JPA entity with @GeneratedValue if it's just for FK mapping
// However, to satisfy JPA @ManyToOne, it needs to be an entity.
@Data
@Entity
@NoArgsConstructor
@Table(name = "exams") // Ensure this maps to the same table in your database
public class Exam {
    @Id
    private Integer examId;
    // No other fields are strictly necessary for the Report service's direct use
    // if only examId is referenced.
}
