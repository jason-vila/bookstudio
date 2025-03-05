package com.bookstudio.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.bookstudio.dao.LiteraryGenreDao;
import com.bookstudio.dao.PublisherDao;
import com.bookstudio.dao.impl.LiteraryGenreDaoImpl;
import com.bookstudio.dao.impl.PublisherDaoImpl;
import com.bookstudio.models.LiteraryGenre;
import com.bookstudio.models.Publisher;
import com.bookstudio.utils.SelectOptions;

public class PublisherService {
	private PublisherDao publisherDao = new PublisherDaoImpl();
	private LiteraryGenreDao literaryGenreDao = new LiteraryGenreDaoImpl();

	public List<Publisher> listPublishers() throws Exception {
		List<Publisher> publisherData = publisherDao.listPublishers();
		
		for (Publisher publisher : publisherData) {
			byte[] photo = publisher.getPhoto();
			if (photo != null) {
				String photoBase64 = Base64.getEncoder().encodeToString(photo);
				publisher.setPhotoBase64("data:image/jpeg;base64," + photoBase64);
			}
		}
		
		return publisherData;
	}

	public Publisher getPublisher(String publisherId) throws Exception {
		Publisher publisher = publisherDao.getPublisher(publisherId);
		byte[] photo = publisher.getPhoto();
		
		if (photo != null) {
			String photoBase64 = Base64.getEncoder().encodeToString(photo);
			publisher.setPhotoBase64("data:image/jpeg;base64," + photoBase64);
		}
		
		return publisher;
	}

	public Publisher createPublisher(HttpServletRequest request) throws Exception {
		String name = getUtf8Parameter(request, "addPublisherName");
		String nationality = getUtf8Parameter(request, "addPublisherNationality");
		String literaryGenreId = getUtf8Parameter(request, "addLiteraryGenre");
		int foundationYear = Integer.parseInt(getUtf8Parameter(request, "addFoundationYear"));
		String website = getUtf8Parameter(request, "addPublisherWebsite");
		String address = getUtf8Parameter(request, "addPublisherAddress");
		String status = getUtf8Parameter(request, "addPublisherStatus");

		byte[] photo = null;
		try {
			InputStream inputStream = request.getPart("addPublisherPhoto").getInputStream();
			if (inputStream.available() > 0) {
				photo = inputStream.readAllBytes();
			}
		} catch (Exception e) {
		}

		Publisher publisher = new Publisher();
		publisher.setName(name);
		publisher.setNationality(nationality);
		publisher.setLiteraryGenreId(literaryGenreId);
		publisher.setFoundationYear(foundationYear);
		publisher.setWebsite(website);
		publisher.setAddress(address);
		publisher.setStatus(status);
		publisher.setPhoto(photo);

		Publisher createdPublisher = publisherDao.createPublisher(publisher);
		if (createdPublisher.getPhoto() != null) {
			String photoBase64 = Base64.getEncoder().encodeToString(createdPublisher.getPhoto());
			createdPublisher.setPhotoBase64("data:image/jpeg;base64," + photoBase64);
		}
		return createdPublisher;
	}

	public Publisher updatePublisher(HttpServletRequest request) throws Exception {
		String publisherId = getUtf8Parameter(request, "publisherId");
		String name = getUtf8Parameter(request, "editPublisherName");
		String nationality = getUtf8Parameter(request, "editPublisherNationality");
		String literaryGenreId = getUtf8Parameter(request, "editLiteraryGenre");
		int foundationYear = Integer.parseInt(getUtf8Parameter(request, "editFoundationYear"));
		String website = getUtf8Parameter(request, "editPublisherWebsite");
		String address = getUtf8Parameter(request, "editPublisherAddress");
		String status = getUtf8Parameter(request, "editPublisherStatus");

		byte[] photo = null;
		try {
			InputStream inputStream = request.getPart("editPublisherPhoto").getInputStream();
			if (inputStream.available() > 0) {
				photo = inputStream.readAllBytes();
			}
		} catch (Exception e) {
		}
		if (photo == null) {
			Publisher currentPublisher = publisherDao.getPublisher(publisherId);
			photo = currentPublisher.getPhoto();
		}

		Publisher publisher = new Publisher();
		publisher.setPublisherId(publisherId);
		publisher.setName(name);
		publisher.setNationality(nationality);
		publisher.setLiteraryGenreId(literaryGenreId);
		publisher.setFoundationYear(foundationYear);
		publisher.setWebsite(website);
		publisher.setAddress(address);
		publisher.setStatus(status);
		publisher.setPhoto(photo);

		return publisherDao.updatePublisher(publisher);
	}

	public SelectOptions populateSelects() throws Exception {
		SelectOptions selectOptions = new SelectOptions();

		List<LiteraryGenre> literayGenres = literaryGenreDao.populateLiteraryGenreSelect();
		selectOptions.setLiteraryGenres(literayGenres);

		return selectOptions;
	}

	private String getUtf8Parameter(HttpServletRequest request, String fieldName) throws IOException, ServletException {
		Part part = request.getPart(fieldName);
		if (part != null) {
			try (InputStream is = part.getInputStream()) {
				return new String(is.readAllBytes(), "UTF-8");
			}
		}
		
		return "";
	}
}
