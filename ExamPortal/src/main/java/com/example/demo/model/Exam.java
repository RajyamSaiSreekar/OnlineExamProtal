package com.example.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Exam {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long examId;
	
	private String title;
	private String description;
	private int duration;
	private int totalMarks;
	
	@OneToMany(mappedBy="exam",cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Question> questions;
}
