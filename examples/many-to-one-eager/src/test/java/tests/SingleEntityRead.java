package tests;

import modeltoone.Breed;
import modeltoone.Dog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class SingleEntityRead {

  public static void main(String[] args) {
    Tools.setPrefix("EL> ");
    run("demo-el");
    Tools.setPrefix("HIB> ");
    run("demo-hib");
    Tools.printResult();
  }

  private static void run(String persistenceUnitName) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();
      prepareData(em);
      emf.getCache().evictAll();

      System.out.println("\nfind");
      Dog dog = em.find(Dog.class, 1);
      Tools.println();
    } finally {
      emf.close();
    }
  }

  private static void prepareData(EntityManager em) {
    em.getTransaction().begin();
    Breed wolf = new Breed();
    wolf.setName("wolf");
    em.persist(wolf);

    Breed germanShepherd = new Breed();
    germanShepherd.setName("german shepherd");
    germanShepherd.setDerivedFrom(wolf);
    em.persist(germanShepherd);

    Breed collie = new Breed();
    collie.setName("collie");
    collie.setDerivedFrom(germanShepherd);
    em.persist(collie);

    Dog lassie = new Dog();
    lassie.setName("Lassie");
    lassie.setBreed(collie);
    em.persist(lassie);

    em.getTransaction().commit();
    em.clear();
  }
}