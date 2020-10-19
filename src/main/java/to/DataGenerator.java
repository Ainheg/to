package to;

import to.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

public class DataGenerator {
    public static void generate() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        Random random = new Random(1000);
        try {
            transaction = session.beginTransaction();
            List<Item> items = generateItems(session, random);
            List<Shipment> shipments = generateShipments(session, random);
            List<Order> orders = generateOrders(session, random, shipments, items);
            generateCustomers(session, random, orders);
            generateEmployees(session, random, orders);
            generateProducts(session, random, items);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null)
                transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private static List<Customer> generateCustomers(Session session, Random random, List<Order> orders) {
        List<Customer> customers = new ArrayList<Customer>();
        for (int i = 0; i < 15; i++) {
            Customer customer = new Customer();
            // TODO
            customers.add(customer);
            session.save(customer);
        }
        return customers;
    }

    private static List<Employee> generateEmployees(Session session, Random random, List<Order> orders) {
        List<Employee> employees = new ArrayList<Employee>();
        for (int i = 0; i < 5; i++) {
            Employee employee = new Employee();
            // TODO
            employees.add(employee);
            session.save(employee);
        }
        return employees;
    }

    private static List<Item> generateItems(Session session, Random random) {
        List<Item> items = new ArrayList<Item>();
        for (int i = 0; i < 80; i++) {
            Item item = new Item();
            item.setQuantity(random.nextDouble() * 99 + 1);
            items.add(item);
            session.save(item);
        }
        return items;
    }

    private static List<Order> generateOrders(Session session, Random random, List<Shipment> shipments,
            List<Item> items) {
        List<Order> orders = new ArrayList<Order>();
        for (int i = 0; i < 25; i++) {
            Order order = new Order();
            // TODO
            orders.add(order);
            session.save(order);
        }
        return orders;
    }

    private static List<Product> generateProducts(Session session, Random random, List<Item> items) {
        List<Product> products = new ArrayList<Product>();
        for (int i = 0; i < 40; i++) {
            Product product = new Product();
            // TODO
            products.add(product);
        }
        return products;
    }

    private static List<Shipment> generateShipments(Session session, Random random) {
        List<Shipment> shipments = new ArrayList<Shipment>();
        for (int i = 0; i < 20; i++) {
            Shipment shipment = new Shipment();
            // TODO
            shipments.add(shipment);
            session.save(shipment);
        }
        return shipments;
    }
}
