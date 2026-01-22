package com.johnnyconsole.enterpriseims.persistence.interfaces;

import com.johnnyconsole.enterpriseims.persistence.User;

import javax.ejb.Local;
import java.util.List;

@Local
public interface UserDao {

    User getUser(String username);
    boolean userExists(String username);
    long count();
    List<User> getUsersExcept(String username);
    boolean addUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(User user, String myUsername);
    boolean verifyUserPassword(User user, String password);

}
