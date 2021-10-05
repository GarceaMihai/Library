package com.utcn.ps.library.model;

import java.time.Year;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@XmlRootElement(name = "book")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@XmlAttribute(name = "book-id")
	private Long id;

	private String title;
	private String author;
	private String language;
	private Year published;
	private int nrOfPages;
	private int nrOfCopies;
	private String isbn13;

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
	@XmlTransient
	@ToString.Exclude
	private List<Rental> rentals;

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
	@XmlTransient
	@ToString.Exclude
	private List<Review> reviews;

	@ManyToMany(mappedBy = "bookWishlist")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@XmlTransient
	@ToString.Exclude
	private List<User> wishedBy;

}
