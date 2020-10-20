package to;

import org.hibernate.Session;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import to.model.*;

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
      List orders = session.createQuery("SELECT o FROM Order o").list();
      for (Iterator iterator = orders.iterator(); iterator.hasNext();) {
        Order order = (Order) iterator.next();
        System.out.println("-----");
        System.out.println("Customer: " + order.getCustomer().getName() + " " + order.getCustomer().getSurname() + " "
            + order.getCustomer().getEmailAddress());
        System.out.print("Employee: " + order.getEmployee().getName() + " " + order.getEmployee().getSurname() + " "
            + order.getEmployee().getEmailAddress());
        if (order.getEmployee().getPhoneNumber() != null)
          System.out.println(" " + order.getEmployee().getPhoneNumber());
        else
          System.out.println();
        System.out.print("Items: ");
        for (Item item : order.getItems())
          System.out.print(item.getQuantity() + "x \"" + item.getProduct().getName() + "\" ");
        System.out.println();
        if (order.getPlacingDate() != null)
          System.out.println("Placing date: " + order.getPlacingDate().toString());
        if (order.getShipment() != null)
          System.out.println("Shipment: " + order.getShipment().getAddress());
      }
      System.out.println("-----");
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
