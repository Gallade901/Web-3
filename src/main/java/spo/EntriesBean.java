package spo;

import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Named;
import jakarta.persistence.*;

//import javax.annotation.ManagedBean;
//import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Named
@ApplicationScoped
@ManagedBean
public class EntriesBean implements Serializable, EntriesMXBean {
    private static final String persistenceUnit = "StudsPU";

    private Entry entry;
    private List<Entry> entries;
    private Integer missRow = 0;

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction transaction;
    private Integer allCount = 0;
    private Integer missCount = 0;
    private String alert = "";
    private Double area = 1.535;
    public void init(@Observes @Initialized(ApplicationScoped.class) Object unused) {
        MBeanRegistryUtil.registerBean(this, "main");
    }

    public void destroy(@Observes @Destroyed(ApplicationScoped.class) Object unused) {
        MBeanRegistryUtil.unregisterBean(this);
    }


    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public EntriesBean() {
        entry = new Entry();
        entries = new ArrayList<>();
        System.out.println("konstruct");
        connection();
        loadEntries();
        countEntry();
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
            if (entry.getHitResult().equals("Промах")) {
                missRow++;
                if (missRow == 4) {
                    missRow = 0;
                    alert = "ВЫ ПРОМАЗАЛИ 4 РАЗА ПОДРЯД!";
                } else {
                    alert = "";
                }
            } else {
                missRow = 0;
                alert = "";
            }
            entry = new Entry();
            transaction.commit();
            countEntry();
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
            countEntry();
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
        return "redirect";
    }
    public void countEntry() {
        missCount = 0;
        for (int i = 0; i < entries.size(); i++) {
            Entry e = entries.get(i);
            if (e.getHitResult().equals("Промах")) {
                missCount++;
            }
        }
        allCount = entries.size();
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
    public Integer getAllCount () {
        return allCount;
    }
    public Integer getMissCount() {
        return missCount;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    @Override
    public int getAllCountPoints() {
        return allCount;
    }

    @Override
    public int getMissPoints() {
        return missCount;
    }
}