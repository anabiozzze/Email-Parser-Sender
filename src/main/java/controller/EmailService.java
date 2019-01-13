package controller;

import controller.dao.DAOImpl;
import controller.dao.HibernateSessionFactoryUtil;
import model.emails.classes4hibernate.Email;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailService extends DAOImpl{
    // класс проводит все необходимые операции с емайлами в БД; используется классом UrlParser

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class.getName());

    @Override
    // сохраняем в БД новую строку
    public void saveEntry(Email email) {
        super.saveEntry(email);
    }

    @Override
    // обновляем существующую строку в БД по адресу почты
    public void updateEntry(Email email) {
        super.updateEntry(email);
    }

    // обновляем существующую строку в БД по номеру записи
    public void updateEntry(int id) {
        Email email = HibernateSessionFactoryUtil.getSessionFactory().openSession().
                get(Email.class, id);
        super.updateEntry(email);
    }

    @Override
    // удаляем строку из БД по почтовому адресу
    public void delEntry(Email email) {
        super.delEntry(email);
    }

    @Override
    // удаляем строку из БД по номеру записи
    public void delEntry(int id) {
        super.delEntry(id);
    }

    @Override
    // получаем почту по номеру записи
    public String getEmail(int id) {
        return super.getEmail(id);
    }

    @Override
    // получаем статус записи по её номеру (новая, готовится к отправке, отправляется и тд)
    public String getStatus(int id) {
        return super.getStatus(id);
    }

    @Override
    // получаем статусы несколькуих записей по их номерам
    public Map<String, String> getStatuses(int... ids) {
        return super.getStatuses(ids);
    }

    @Override
    // получаем список почтовых адресов, взятых с общего url
    public List<String> getEmailsByUrl(String url) {
        return super.getEmailsByUrl(url);
    }

    @Override
    // получаем список всех почтовых адресов и их "родительских" url из БД
    public List<Email> getAll() {
        return super.getAll();
    }

    @Transactional
    // удаляем все записи в таблице БД
    public void delAll() {

        logger.debug("Method delAll() started;");

        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query query = session.createQuery("delete from Email");

        Transaction tx = session.beginTransaction();
        query.executeUpdate();
        tx.commit();

        logger.debug("Method delAll() finished;"  + "\n");
    }
}
