package to;

import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

public class App {
  public static void main(String[] args) {
    DataGenerator.generate();
    exampleQuery();
    HibernateUtil.shutdown();
  }

  public static void exampleQuery() {
    Session session = HibernateUtil.getSessionFactory().openSession();
    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();
      // TODO
      /*
       * List employees = session.createQuery("FROM Employee").list(); for (Iterator
       * iterator = employees.iterator(); iterator.hasNext();) { Employee employee =
       * (Employee) iterator.next(); System.out.println("First Name: " +
       * employee.getFirstName()); System.out.println("Last Name: " +
       * employee.getLastName()); }
       */
      transaction.commit();
    } catch (HibernateException e) {
      if (transaction != null)
        transaction.rollback();
      e.printStackTrace();
    } finally {
      session.close();
    }
  }
}
