package com.utcn.ps.library.util;

import java.util.ArrayList;
import java.util.List;

import com.utcn.ps.library.dto.BookDto;
import com.utcn.ps.library.dto.BookFilterDto;
import com.utcn.ps.library.model.Book;

public class FilterUtil {

	public static List<BookDto> filterBooks(List<Book> books, BookFilterDto bookFilterDto) {
		List<BookDto> filteredBooks = new ArrayList<>();
		for (Book book : books) {
			if (!bookFilterDto.getSelectedTitles().isEmpty()
					&& !bookFilterDto.getSelectedTitles().contains(book.getTitle())) {
				continue;
			}
			if (!bookFilterDto.getSelectedAuthors().isEmpty()
					&& !bookFilterDto.getSelectedAuthors().contains(book.getAuthor())) {
				continue;
			}
			if (!bookFilterDto.getSelectedLanguages().isEmpty()
					&& !bookFilterDto.getSelectedLanguages().contains(book.getLanguage())) {
				continue;
			}
			if (book.getPublished().isBefore(bookFilterDto.getMinPublishingYear())
					|| book.getPublished().isAfter(bookFilterDto.getMaxPublishingYear())) {
				continue;
			}
			if (book.getNrOfPages() < bookFilterDto.getMinNrOfPages()
					|| book.getNrOfPages() > bookFilterDto.getMaxNrOfPages()) {
				continue;
			}
			BookDto bookDto = Mapper.mapBookToDto(book);
			filteredBooks.add(bookDto);
		}
		return filteredBooks;
	}

}
