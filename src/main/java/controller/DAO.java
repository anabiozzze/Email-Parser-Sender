package controller;

import model.emails.classes4hibernate.Email;

import java.util.List;
import java.util.Map;

public interface DAO {

    public void saveEntry(Email email);

    public void updateEntry(Email email);

    public void delEmail(Email email);

    public void delEmail(int id);

    public String getEmail(int id);

    public Map<String, String> getStatuses(int... ids);

    public List<String> getEmailsByUrl(String url);

    public List<Email> getAll();
}
