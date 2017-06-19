package batch.batch.service;

import batch.model.User;
import org.springframework.batch.item.ItemReader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author okoybaev
 */
public class ItemCsvReader implements ItemReader<User> {

    private List<User> users;

    @Override
    public User read() throws Exception {
        if (users.size() == 0) return null;
        return users.remove(0);
    }

    public List<User> getUsers() {
        if (users == null) {
            users = new ArrayList<>();
        }
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
