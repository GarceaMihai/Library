package com.utcn.ps.library.model;

import static javax.persistence.FetchType.LAZY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@XmlRootElement(name = "book-request")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class BookRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@XmlAttribute(name = "book-request-id")
	private Long id;

	private String bookTitle;
	private String isbn13;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User user;

}
