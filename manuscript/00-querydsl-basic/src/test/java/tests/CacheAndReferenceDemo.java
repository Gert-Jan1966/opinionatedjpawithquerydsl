package tests;

import model00.Dog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class CacheAndReferenceDemo {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("sqldemo");
    try {
      EntityManager em = emf.createEntityManager();
      DogQueryDemo.prepareData(em);

      System.out.println("\ngetReference");
      Dog reference = em.getReference(Dog.class, 1);
      System.out.println("\nfind");
      Map<String, Object> props = new HashMap<String, Object>();
      // TODO any hint to load "references" (objects with id only) instead of cascading?
      Dog dog = em.find(Dog.class, 1, props);
      System.out.println();

//      referenceAndCacheExperiments(emf, em);
    } finally {
      emf.close();
    }
  }

  private static void referenceAndCacheExperiments(EntityManagerFactory emf, EntityManager em) {
    System.out.println("\nBEFORE cache evict");
    em.clear();
    Dog dog = em.find(Dog.class, 1);
    System.out.println("dog = " + dog);

    System.out.println("\nAFTER cache evict");
    emf.getCache().evictAll();
    em.clear();
    // EclipseLink: 2 selects, dog+breed
    // Hibernate: 1 select, clever enough to JOIN
    em.find(Dog.class, 1);
    dog = em.find(Dog.class, 1);
    System.out.println("dog = " + dog);

    System.out.println("\nREFERENCE");
    emf.getCache().evictAll();
    em.clear();
    // EclipseLink: This does not work lazily, but maybe with weaving it would? (2 selects)
    // Hibernate: Not lazy either out-of-the-box (1 select)
    Dog reference = em.getReference(Dog.class, 1);
    System.out.println("===");
    System.out.println(reference.getId());
    System.out.println("===");
    System.out.println(reference);
  }
}
