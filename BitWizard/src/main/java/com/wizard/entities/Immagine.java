package com.wizard.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Immagine {
	
	@Id
	@Column(name = "id_img")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idImg;
	
    @Lob
    @Column(name = "img", columnDefinition = "LONGBLOB")
    private byte[] img;

	public int getIdImg() {
		return idImg;
	}

	public void setIdImg(int idImg) {
		this.idImg = idImg;
	}

	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
	}

	public boolean isEmpty() {
	    return img == null || img.length == 0;
	}
	
}