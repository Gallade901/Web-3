package spo;




import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Named
@SessionScoped
public class EntriesBean implements Serializable {
    private static final String persistenceUnit = "StudsPU";

    private Entry entry;
    private List<Entry> entries;

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction transaction;

    public EntriesBean() {
        entry = new Entry();
        entries = new ArrayList<>();
        System.out.println("konstruct");
        connection();
        loadEntries();
    }

    private void connection() {
        System.out.println("connect");
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
        entityManager = entityManagerFactory.createEntityManager();
        transaction = entityManager.getTransaction();
    }

    private void loadEntries() {
        try {
            System.out.println("loadEntr");
            transaction.begin();
            Query query = entityManager.createQuery("SELECT e FROM Entry e");
            entries = query.getResultList();
            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
    }

    public String addEntry() {
        try {
            transaction.begin();
            entry.checkHit();
            if (entityManager.isOpen()) {
                System.out.println("EntityManager is open");
            } else {
                System.out.println("EntityManager is closed");
            }
            entityManager.persist(entry);
            if (entityManager.contains(entry)) {
                System.out.println("Entity is managed");
            } else {
                System.out.println("Entity is not managed");
            }
            entries.add(entry);
            entry = new Entry();
            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
        return "redirect";
    }

    public String clearEntries() {
        try {
            transaction.begin();
            Query query = entityManager.createQuery("DELETE FROM Entry");
            query.executeUpdate();
            entries.clear();
            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
        return "redirect";
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }
}