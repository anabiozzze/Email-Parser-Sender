package controller.dao;

import model.emails.classes4hibernate.Email;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import javax.security.auth.login.AppConfigurationEntry;


// в этом классе создаем фабрику сессий подключения к БД
public class HibernateSessionFactoryUtil {

    private static SessionFactory sessionFactory;

    public HibernateSessionFactoryUtil() {
    }

    public static SessionFactory getSessionFactory() {

        if (sessionFactory == null) {
            // создаем шаблон настройки подключения и добавляем в него нужный класс
            Configuration configuration = new Configuration().configure();
            configuration.addAnnotatedClass(Email.class);

            // создаем шаблон самого подключения с новыми настройками
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                    applySettings(configuration.getProperties());

            // знакомим фабрику подключений с новым шаблоном подключения
            sessionFactory = configuration.buildSessionFactory(builder.build());

        }

        return sessionFactory;
    }
}