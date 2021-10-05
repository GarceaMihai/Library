package com.utcn.ps.library.dto;

import java.time.Year;
import java.util.List;

import javax.validation.constraints.PastOrPresent;

import lombok.Data;

@Data
public class BookFilterDto {
	private List<String> selectedTitles;
	private List<String> selectedAuthors;
	private List<String> selectedLanguages;

	@PastOrPresent
	private Year minPublishingYear;

	@PastOrPresent
	private Year maxPublishingYear;

	private int minNrOfPages;
	private int maxNrOfPages;
}
