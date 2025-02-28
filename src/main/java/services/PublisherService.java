package services;

import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import dao.PublisherDao;
import dao.PublisherDaoImpl;
import dao.LiteraryGenreDao;
import dao.LiteraryGenreDaoImpl;
import models.LiteraryGenre;
import models.Publisher;
import utils.SelectOptions;

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
        String name = request.getParameter("addPublisherName");
        String nationality = request.getParameter("addPublisherNationality");
        String literaryGenreId = request.getParameter("addLiteraryGenre");        
        int foundationYear = Integer.parseInt(request.getParameter("addFoundationYear"));
        String website = request.getParameter("addPublisherWebsite");
        String address = request.getParameter("addPublisherAddress");
        String status = request.getParameter("addPublisherStatus");
        
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
    	String publisherId = request.getParameter("publisherId");    
        String name = request.getParameter("editPublisherName");
        String nationality = request.getParameter("editPublisherNationality");
        String literaryGenreId = request.getParameter("editLiteraryGenre");        
        int foundationYear = Integer.parseInt(request.getParameter("editFoundationYear"));
        String website = request.getParameter("editPublisherWebsite");
        String address = request.getParameter("editPublisherAddress");
        String status = request.getParameter("editPublisherStatus");
        
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
}
