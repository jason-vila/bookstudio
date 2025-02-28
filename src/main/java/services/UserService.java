package services;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import dao.SessionDaoImpl;
import dao.UserDao;
import dao.UserDaoImpl;
import models.User;
import utils.LoginConstants;

public class UserService {
    
    private UserDao userDao = new UserDaoImpl();
    
    public List<User> listUsers() throws SQLException {
        List<User> userData = userDao.listUsers();
        for (User user : userData) {
            byte[] photo = user.getProfilePhoto();
            if (photo != null) {
                String photoBase64 = Base64.getEncoder().encodeToString(photo);
                user.setProfilePhotoBase64("data:image/jpeg;base64," + photoBase64);
            }
        }
        return userData;
    }
    
    public User getUser(String userId) throws SQLException {
        User user = userDao.getUser(userId);
        byte[] photo = user.getProfilePhoto();
        if (photo != null) {
            String photoBase64 = Base64.getEncoder().encodeToString(photo);
            user.setProfilePhotoBase64("data:image/jpeg;base64," + photoBase64);
        }
        return user;
    }
    
    public User createUser(HttpServletRequest request) throws Exception {
        String username = request.getParameter("addUserUsername");
        String email = request.getParameter("addUserEmail");
        String firstName = request.getParameter("addUserFirstName");
        String lastName = request.getParameter("addUserLastName");
        String password = request.getParameter("addUserPassword");
        String role = request.getParameter("addUserRole");
        
        Part photoPart = request.getPart("addUserProfilePhoto");
        byte[] profilePhoto = null;
        if (photoPart != null && photoPart.getSize() > 0) {
            try (InputStream inputStream = photoPart.getInputStream()) {
            	profilePhoto = inputStream.readAllBytes();
            }
        }
        
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setRole(role);
        user.setProfilePhoto(profilePhoto);
        
        User createdUser = userDao.createUser(user);
        
        if (createdUser.getProfilePhoto() != null) {
            String photoBase64 = Base64.getEncoder().encodeToString(createdUser.getProfilePhoto());
            createdUser.setProfilePhotoBase64("data:image/jpeg;base64," + photoBase64);
        }
        
        return createdUser;
    }
    
    public User updateUser(HttpServletRequest request) throws Exception {
        String userId = request.getParameter("userId");
        String firstName = request.getParameter("editUserFirstName");
        String lastName = request.getParameter("editUserLastName");
        String password = request.getParameter("editUserPassword");
        String role = request.getParameter("editUserRole");
        
        Part photoPart = request.getPart("editUserProfilePhoto");
        byte[] profilePhoto = null;
        if (photoPart != null && photoPart.getSize() > 0) {
            try (InputStream inputStream = photoPart.getInputStream()){
            	profilePhoto = inputStream.readAllBytes();
            }
        }
        
        User user = new User();
        user.setUserId(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setRole(role);
        
        if (profilePhoto == null) {
            User currentUser = userDao.getUser(userId);
            profilePhoto = currentUser.getProfilePhoto();
        }
        user.setProfilePhoto(profilePhoto);
        
        return userDao.updateUser(user);
    }
    
    public boolean deleteUser(String userId) throws SQLException {
        return userDao.deleteUser(userId);
    }
    
    public void updateSession(HttpServletRequest request, User updatedUser) {
        String userId = updatedUser.getUserId();
        String sessionUserId = (String) request.getSession().getAttribute(LoginConstants.ID);
        if (sessionUserId != null && sessionUserId.equals(userId)) {
            SessionDaoImpl sessionProject = new SessionDaoImpl();
            sessionProject.saveSessionString(request, LoginConstants.FIRSTNAME, updatedUser.getFirstName());
            sessionProject.saveSessionString(request, LoginConstants.LASTNAME, updatedUser.getLastName());
            sessionProject.saveSessionString(request, LoginConstants.PASSWORD, updatedUser.getPassword());
            sessionProject.saveSessionString(request, LoginConstants.ROLE, updatedUser.getRole());
            byte[] profilePhoto = updatedUser.getProfilePhoto();
            if (profilePhoto != null) {
                sessionProject.saveSessionString(request, LoginConstants.USER_PROFILE_IMAGE, 
                    "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(profilePhoto));
            }
        }
    }
}
