package com.bookstudio.models;

import java.time.LocalDate;

public class Author {
	private String authorId;
	private String name;
	private String nationality;
	private String literaryGenreId;
	private String literaryGenreName;
	private LocalDate birthDate;
	private String biography;
	private String status;
	private byte[] photo;
	private String photoBase64;

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getLiteraryGenreId() {
		return literaryGenreId;
	}

	public void setLiteraryGenreId(String literaryGenreId) {
		this.literaryGenreId = literaryGenreId;
	}

	public String getLiteraryGenreName() {
		return literaryGenreName;
	}

	public void setLiteraryGenreName(String literaryGenreName) {
		this.literaryGenreName = literaryGenreName;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPhotoBase64() {
		return photoBase64;
	}

	public void setPhotoBase64(String photoBase64) {
		this.photoBase64 = photoBase64;
	}
}
