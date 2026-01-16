package com.johnnyconsole.enterpriseims.persistence.implementations;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.johnnyconsole.enterpriseims.persistence.User;
import com.johnnyconsole.enterpriseims.persistence.interfaces.UserDao;

import javax.ejb.Stateful;
import javax.enterprise.inject.Alternative;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateful
@Alternative
public class UserDaoImpl implements UserDao {

    @PersistenceContext(unitName="user")
    private EntityManager manager;


    @Override
    public User getUser(String username) {
        try {
            return (User) (manager.createNamedQuery("User.FindByUsername")
                    .setParameter("username", username)
                    .getSingleResult());
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public boolean userExists(String username) {
        return getUser(username) != null;
    }

    @Override
    public long count() {
        try {
            return (long) (manager.createNamedQuery("User.Count").getSingleResult());
        } catch(Exception e) {
            return 0;
        }
    }

    @Override
    public List<User> getUsersExcept(String username) {
        try {
            return (List<User>)(manager.createNamedQuery("User.FindAllExcept")
                    .setParameter("username", username)
                    .getResultList());
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public boolean addUser(User user) {
        try {
            manager.persist(user);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateUser(User user) {
        try {
            manager.merge(user);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteUser(User user, String myUsername) {
        try {
            if(user.getUsername().equals(myUsername) || !manager.contains(user)) {
                return false;
            }
            manager.remove(user);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    @Override
    public boolean verifyUserPassword(User user, String password) {
        try {
            return BCrypt.verifyer()
                    .verify(password.getBytes(), user.getPassword().getBytes())
                    .verified;
        } catch(Exception e) {
            return false;
        }
    }

}
