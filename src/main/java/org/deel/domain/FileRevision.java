package org.deel.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="File")
public class FileRevision {
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private Long id;
	
	@Column(name="fsPath")
	private String fsPath;
	
	@Column(name="date")
	private Date date;
	
	@ManyToOne()
	@JoinColumn(name="uploadedBy_id")
	private User uploadedBy;
	
	@ManyToOne()
	@JoinColumn(name="file_id")
	private File file;
	
	
}
