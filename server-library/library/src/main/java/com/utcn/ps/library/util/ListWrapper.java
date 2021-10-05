package com.utcn.ps.library.util;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.utcn.ps.library.model.Book;
import com.utcn.ps.library.model.BookRequest;
import com.utcn.ps.library.model.Rental;
import com.utcn.ps.library.model.Review;
import com.utcn.ps.library.model.User;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@XmlRootElement(name = "list-wrapper")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(value = { User.class, Book.class, Rental.class, Review.class, BookRequest.class })
public class ListWrapper<T> {

	@XmlElementWrapper(name = "list")
	@XmlElement(name = "object")
	private List<T> list = null;

}
