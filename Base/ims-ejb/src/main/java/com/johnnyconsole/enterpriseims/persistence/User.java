package com.johnnyconsole.enterpriseims.persistence;

import javax.persistence.*;

@Entity
@Table(name="ims_users")
@NamedQueries({
        @NamedQuery(name="User.FindByUsername", query="SELECT u FROM User u WHERE u.username = :username"),
        @NamedQuery(name="User.FindAllExcept", query="SELECT u FROM User u WHERE u.username != :username"),
        @NamedQuery(name="User.Count", query="SELECT COUNT(u) AS count FROM User u")
})
public class User {
    @Id private String username;
    private String name, password;
    private boolean isAdministrator;

    public User(){}

    public User(String username, String name, String password) {
        this(username, name, password, false);
    }

    public User(String username, String name, String password, boolean isAdministrator) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.isAdministrator = isAdministrator;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdministrator() {
        return isAdministrator;
    }

    public void setAdministrator(boolean administrator) {
        isAdministrator = administrator;
    }

    @Override
    public String toString(){
        return "User{" + "username=" + username + ", name=" + name +  ", isAdministrator=" + (isAdministrator ? "yes" : "no") + '}';

    }

}
