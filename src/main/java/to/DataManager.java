package to;

import to.model.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;

public class DataManager {

    public static boolean addToDatabase(Object o){
        if(o instanceof Order){
            return addOrderToDatabase((Order) o);
        }
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        boolean added;
        try{
            transaction = session.beginTransaction();
            session.save(o);
            transaction.commit();
            added = true;
        } catch (PersistenceException e) {
            if (transaction != null)
                transaction.rollback();
            added = false;
        } finally {
            session.close();
        }
        return added;
    }

    public static boolean updateInDatabase(Object o){
        if(o instanceof Order){
            return updateOrderInDatabase((Order) o);
        }
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        boolean updated;
        try{
            transaction = session.beginTransaction();
            session.update(o);
            transaction.commit();
            updated = true;
        } catch (PersistenceException e) {
            if (transaction != null)
                transaction.rollback();
            updated = false;
            e.printStackTrace();
        } finally {
            session.close();
        }
        return updated;
    }

    public static boolean deleteFromDatabase(Object o){
        if(o instanceof Order){
            return deleteOrderFromDatabase((Order) o);
        }
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        boolean deleted;
        try{
            transaction = session.beginTransaction();
            session.remove(o);
            transaction.commit();
            deleted = true;
        } catch (PersistenceException e) {
            if (transaction != null)
                transaction.rollback();
            deleted = false;
        } finally {
            session.close();
        }
        return deleted;
    }

    public static boolean addItemToOrder(Item i){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        boolean added;
        try{
            transaction = session.beginTransaction();
            i.getOrder().getItems().add(i);
            session.save(i);
            session.update(i.getOrder());
            transaction.commit();
            added = true;
        } catch (PersistenceException e) {
            if (transaction != null)
                transaction.rollback();
            added = false;
            e.printStackTrace();
        } finally {
            session.close();
        }
        return added;
    }

    public static boolean addOrderToDatabase(Order o){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        boolean added;
        try{
            transaction = session.beginTransaction();
            session.save(o);
            if(o.getShipment() != null)
                session.save(o.getShipment());
            transaction.commit();
            added = true;
        } catch (PersistenceException e) {
            if (transaction != null)
                transaction.rollback();
            added = false;
        } finally {
            session.close();
        }
        return added;
    }

    public static boolean updateOrderInDatabase(Order o){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        boolean updated;
        try{
            transaction = session.beginTransaction();
            session.save(o.getShipment());
            session.update(o);
            transaction.commit();
            updated = true;
        } catch (PersistenceException e) {
            if (transaction != null)
                transaction.rollback();
            updated = false;
        } finally {
            session.close();
        }
        return updated;
    }

    public static boolean deleteOrderFromDatabase(Order o){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        boolean deleted;
        try{
            transaction = session.beginTransaction();
            if(o.getShipment() != null)
                session.remove(o.getShipment());
            session.remove(o);
            transaction.commit();
            deleted = true;
        } catch (PersistenceException e) {
            if (transaction != null)
                transaction.rollback();
            e.printStackTrace();
            deleted = false;
        } finally {
            session.close();
        }
        return deleted;
    }

    public static <T>List<T> getDatabaseTableAsList(String tableName){
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<T> dList = session.createQuery("SELECT o FROM " + tableName + " o").list();
        session.close();
        return dList;
    }

    public static void generate() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        Random random = new Random();
        try {
            transaction = session.beginTransaction();
            List<Customer> customers = generateCustomers(session, random);
            List<Employee> employees = generateEmployees(session, random);
            List<Product> products = generateProducts(session, random);
            List<Order> orders = generateOrders(session, random, customers, employees);
            generateItems(session, random, orders, products);
            generateShipments(session, random, orders);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null)
                transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private static List<Customer> generateCustomers(Session session, Random random) {
        List<String> names = Arrays.asList("Adam", "Marta", "Karol", "Anna", "Rafał");
        List<String> surnames = Arrays.asList("Dąb", "Nowak", "Miłorząb", "Prus", "Brat");
        List<String> emailSites = Arrays.asList("gmail.com", "wp.pl", "onet.pl");
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Customer customer = new Customer();
            customer.setName(names.get(Math.abs(random.nextInt()) % names.size()));
            customer.setSurname(surnames.get(Math.abs(random.nextInt()) % surnames.size()));
            customer.setEmailAddress((customer.getName() + "." + customer.getSurname() + "@"
                    + emailSites.get(Math.abs(random.nextInt()) % emailSites.size())).toLowerCase());
            customers.add(customer);
            session.save(customer);
        }
        return customers;
    }

    private static List<Employee> generateEmployees(Session session, Random random) {
        List<String> names = Arrays.asList("Aleksandra", "Franciszek", "Katarzyna", "Olaf", "Bartłomiej");
        List<String> surnames = Arrays.asList("Mazurek", "Klon", "Frak", "Brzoza", "Łąka");
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Employee employee = new Employee();
            employee.setName(names.get(Math.abs(random.nextInt()) % names.size()));
            employee.setSurname(surnames.get(Math.abs(random.nextInt()) % surnames.size()));
            employee.setEmailAddress((employee.getName() + "." + employee.getSurname() + "@shop.com").toLowerCase());
            if (random.nextBoolean())
                employee.setPhoneNumber(String.valueOf((int) Math.abs(random.nextDouble() * 999999999)));
            employees.add(employee);
            session.save(employee);
        }
        return employees;
    }

    private static List<Item> generateItems(Session session, Random random, List<Order> orders,
            List<Product> products) {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < 80; i++) {
            Order order = orders.get(Math.abs(random.nextInt()) % orders.size());
            Product product = products.get(Math.abs(random.nextInt()) % products.size());
            Item item = new Item();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(Math.abs(random.nextInt()) % 99 + 1.00);
            items.add(item);
            Set<Item> orderItems = order.getItems();
            if (orderItems == null)
                orderItems = new HashSet<>();
            orderItems.add(item);
            order.setItems(orderItems);
            Set<Item> productItems = product.getItems();
            if (productItems == null)
                productItems = new HashSet<>();
            productItems.add(item);
            product.setItems(productItems);
            session.save(order);
            session.save(product);
            session.save(item);
        }
        return items;
    }

    private static List<Order> generateOrders(Session session, Random random, List<Customer> customers,
            List<Employee> employees) {
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            Customer customer = customers.get(Math.abs(random.nextInt()) % customers.size());
            Employee employee = employees.get(Math.abs(random.nextInt()) % employees.size());
            Order order = new Order();
            order.setCustomer(customer);
            order.setEmployee(employee);
            if (random.nextBoolean())
                order.setPlacingDate(
                        new Timestamp((new Date()).getTime() - ((Math.abs(random.nextInt()) % 30) * DAY_IN_MS)));
            orders.add(order);
            Set<Order> customerOrders = customer.getOrders();
            if (customerOrders == null)
                customerOrders = new HashSet<>();
            customerOrders.add(order);
            customer.setOrders(customerOrders);
            Set<Order> employeeOrders = customer.getOrders();
            if (employeeOrders == null)
                employeeOrders = new HashSet<>();
            employeeOrders.add(order);
            employee.setOrders(employeeOrders);
            session.save(customer);
            session.save(employee);
        }
        return orders;
    }

    private static List<Product> generateProducts(Session session, Random random) {
        List<String> baseNames = Arrays.asList("Mydło", "Chleb", "Masło", "Gąbka", "Szczoteczka");
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            Product product = new Product();
            product.setName(baseNames.get(Math.abs(random.nextInt()) % baseNames.size()) + " #"
                    + Math.abs(random.nextInt()) % 100);
            products.add(product);
            session.save(product);
        }
        return products;
    }

    private static List<Shipment> generateShipments(Session session, Random random, List<Order> orders) {
        List<Integer> orderIndexes = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            orderIndexes.add(i);
        }
        Collections.shuffle(orderIndexes);
        List<String> addresses = Arrays.asList("00-038 Warszawa", "30-015 Kraków", "80-053 Gdańsk", "90-007 Łódź",
                "87-128 Toruń");
        List<Shipment> shipments = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Order order = orders.get(orderIndexes.get(i));
            Shipment shipment = new Shipment();
            shipment.setOrder(order);
            shipment.setAddress(addresses.get(Math.abs(random.nextInt()) % addresses.size()));
            shipments.add(shipment);
            order.setShipment(shipment);
            session.save(order);
            session.save(shipment);
        }
        return shipments;
    }
}
