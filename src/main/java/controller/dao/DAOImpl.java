package controller.dao;

import model.emails.classes4hibernate.Email;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// класс отвечает за всё взаимодействие с БД
public class DAOImpl implements DAO {

    // сохраняем в БД новую строку
    public void saveEntry(Email email) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(email);

        transaction.commit();
        session.close();
    }

    // обновляем существующую строку в БД
    public void updateEntry(Email email) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(email);

        transaction.commit();
        session.close();
    }

    // удаляем строку из БД по почтовому адресу
    public void delEntry(Email email) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(email);

        transaction.commit();
        session.close();
    }

    // удаляем строку из БД по номеру записи
    public void delEntry(int id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(session.get(Email.class, id));

        transaction.commit();
        session.close();
    }

    // получаем почту по номеру записи
    public String getEmail(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().
                get(Email.class, id).getAddress();
    }

    // получаем статус записи по её номеру (новая, готовится к отправке, отправляется и тд)
    public String getStatus(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().
                get(Email.class, id).getStatus().name();
    }

    // получаем статусы несколькуих записей по их номерам
    public Map<String, String> getStatuses(int... ids) {
        Map<String, String> statuses = new HashMap<String, String>();

        for (int id : ids) {
            String address = getEmail(id);
            String status = getStatus(id);
            statuses.put(address, status);
        }

        return statuses;
    }

    // получаем список почтовых адресов, взятых с общего url
    public List<String> getEmailsByUrl(String url) {
        ArrayList<String> mailsByUrl = new ArrayList<String>();

        List<Email> mails = (List<Email>) HibernateSessionFactoryUtil.getSessionFactory().
                openSession().createQuery("From Email").list();

        for (Email eml : mails) {
          if (eml.getUrl().equals(url)) {
              mailsByUrl.add(eml.getAddress());
          }
        }

        return mailsByUrl;
    }

    // получаем список всех почтовых адресов и их "родительских" url из БД
    public List<Email> getAll() {
        return (List<Email>) HibernateSessionFactoryUtil.getSessionFactory().
                openSession().createQuery("From Email").list();
    }
}
