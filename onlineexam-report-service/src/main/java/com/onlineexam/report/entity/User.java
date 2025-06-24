package com.onlineexam.report.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

// Minimal representation for Report entity's relationship
@Data
@Entity
@NoArgsConstructor
@Table(name = "users") // Ensure this maps to the same table in your database
public class User {
    @Id
    private Integer userId;
    // No other fields are strictly necessary for the Report service's direct use
    // if only userId is referenced.
}
