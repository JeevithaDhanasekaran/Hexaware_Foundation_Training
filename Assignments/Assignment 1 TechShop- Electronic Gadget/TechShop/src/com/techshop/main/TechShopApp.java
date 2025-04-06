package com.techshop.main;
import com.techshop.service.ProductDaoImpl;
import com.techshop.service.OrderDaoImpl;
import com.techshop.service.CustomerDaoImpl;
import com.techshop.dao.ProductDao;
import com.techshop.dao.CustomerDao;
import com.techshop.entities.Customers;
import com.techshop.entities.Orders;
import com.techshop.entities.Products;
import com.techshop.exceptions.DatabaseConnectionException;
import com.techshop.dao.OrderDao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class TechShopApp {
	public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.println("==== Welcome to TechShop ====");

        boolean exit = false;
        while (!exit) {
            System.out.println("1. Admin Login");
            System.out.println("2. Customer Login");
            System.out.println("3. Customer Registration");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            switch(choice) {
                case 1: adminMenu(sc); break;
                case 2: customerMenu(sc); break;
                case 3: registerCustomer(sc); break;
                case 4: exit = true; break;
                default: System.out.println("Invalid choice! Try again.");
            }
        }

        System.out.println("Thank you for visiting TechShop!");
    }

	//register customer
	private static void registerCustomer(Scanner sc) throws DatabaseConnectionException {
		CustomerDao customerDao = new CustomerDaoImpl();

	    System.out.println("===============================");
	    System.out.println("    Register As New Customer   ");
	    System.out.println("===============================");

	    System.out.println("Enter Customer First Name: ");
	    String firstName = sc.next();
	    
	    System.out.println("Enter Customer Last Name: ");
	    String lastName = sc.next();

	    System.out.println("Enter Customer Email: ");
	    String email = sc.next();

	    System.out.println("Enter Customer Mobile: ");
	    String mobile = sc.next();

	    System.out.println("Enter Customer Address: ");
	    String address = sc.next();

	    Customers customer = new Customers();
	    customer.setFirstName(firstName);
	    customer.setLastName(lastName);
	    customer.setEmail(email);
	    customer.setPhoneNumber(mobile);
	    customer.setAddress(address);

	    if(customerDao.addCustomer(customer))
	        System.out.println("Customer Registered Successfully!");
	    else
	        System.out.println("Failed to Register Customer!");
	}
	
	
	

	//customer menu
	private static void customerMenu(Scanner sc) throws Exception {
		
		ProductDao productDao = new ProductDaoImpl();
	    OrderDao orderDao = new OrderDaoImpl();

	    while (true) {
	        System.out.println("===========================");
	        System.out.println("       Customer Menu       ");
	        System.out.println("===========================");
	        System.out.println("1. View All Products");
	        System.out.println("2. Search Product By ID");
	        System.out.println("3. Place Order");
	        System.out.println("4. View My Orders");
	        System.out.println("5. Exit");
	        System.out.println("===========================");
	        System.out.print("Enter choice: ");
	        int choice = sc.nextInt();

	        switch (choice) {
	            case 1:
	                List<Products> products=productDao.getAllProducts();
	                if(products.isEmpty()) {
	                    System.out.println("No products found.");
	                } else {
	                    System.out.println("Available Products:");
	                    for(Products p : products) {
	                        System.out.println(p);
	                    }
	                }
	                break;

	            case 2: {
	                System.out.println("Enter Product ID to Search: ");
	                int searchId = sc.nextInt();

	                Products prod = productDao.getProductById(searchId);

	                if (prod != null)
	                    System.out.println(prod);
	                else
	                    System.out.println("Product Not Found!");

	                break;
	            }

	            case 3: {
	                System.out.println("Enter Your Customer ID: ");
	                int customerId = sc.nextInt();

	                System.out.println("Enter Product ID to Order: ");
	                int productId = sc.nextInt();

	                System.out.println("Enter Quantity: ");
	                int quantity = sc.nextInt();

	                if (orderDao.placeOrder(customerId, productId, quantity))
	                    System.out.println("Order Placed Successfully!");
	                else
	                    System.out.println("Failed to Place Order!");

	                break;
	            }

	            case 4: {
	                System.out.println("Enter Your Customer ID to View Orders: ");
	                int customerId = sc.nextInt();

	                orderDao.getOrdersByCustomerId(customerId);
	                break;
	            }

	            case 5:
	                System.out.println("Thank you for visiting!");
	                return;

	            default:
	                System.out.println("Invalid choice! Try again.");
	        }
	    }
		
	}
	
	
	
	
	
// admin menu
	private static void adminMenu(Scanner sc) throws Exception {
        ProductDao productDao = new ProductDaoImpl();
        OrderDao orderDao = new OrderDaoImpl();

        while (true) {
            System.out.println("===========================");
            System.out.println("        Admin Menu         ");
            System.out.println("===========================");
            System.out.println("1. Add Product");
            System.out.println("2. View All Products");
            System.out.println("3. Search Product By ID");
            System.out.println("4. Update Product");
            System.out.println("5. Delete Product");
            System.out.println("6. View All Orders");
            System.out.println("7. Logout / Exit");
            System.out.println("===========================");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                {
                	System.out.print("Enter Product Name: ");
                	sc.nextLine();  // clear buffer after nextInt()
                	String name = sc.nextLine();

                	System.out.print("Enter Product Price: ");
                	while (!sc.hasNextDouble()) {
                		System.out.println("Invalid input. Enter valid price:");
                		sc.next();
                	}
                	double price = sc.nextDouble();

                	System.out.print("Enter Product Quantity: ");
                	while (!sc.hasNextInt()) {
                		System.out.println("Invalid input. Enter valid quantity:");
                		sc.next();
                	}
                	int quantity = sc.nextInt();
                	sc.nextLine();  // clear buffer

                	System.out.print("Enter Product Category/Description: ");
                	String category = sc.nextLine();
                	
                	System.out.println("Enter Product Line: ");
                    String productLine = sc.nextLine();

                	Products product = new Products();
                	product.setProductName(name);
                	product.setPrice(BigDecimal.valueOf(price));
                	product.setStockQuantity(quantity);
                	product.setDescription(category);
                	product.setProductLine(productLine);
                	
                	boolean status = productDao.addProduct(product);
                	
                	if(status)
                		System.out.println("Product added successfully!");
                	else
                		System.out.println("Failed to add Product.");
                	break;
                }
                case 2:
                {
                	List<Products> products = productDao.getAllProducts();
                	if(products.isEmpty())
                		System.out.println("No Products Available.");
                	else
                		products.forEach(System.out::println);
                }
                case 3:
                {
                	System.out.println("Enter Product ID to Search: \n");
                    int searchId = sc.nextInt();

                    Products prod = productDao.getProductById(searchId);

                    if(prod != null)
                        System.out.println(prod);
                    else
                        System.out.println("Product Not Found!");

                    break;
                }
                case 4:
                {
                	System.out.print("Enter Product ID: ");
                    int productId = sc.nextInt();
                    sc.nextLine();  // consume leftover newline

                    System.out.print("Enter New Product Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter New Product Price: ");
                    BigDecimal price;
                    try {
                        String priceInput = sc.nextLine().trim();
                        price = new BigDecimal(priceInput);
                        if (price.compareTo(BigDecimal.ZERO) <= 0) {
                            throw new IllegalArgumentException("Price must be greater than 0.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid price input! Please enter a valid number.");
                        return;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        return;
                    }

                    System.out.print("Enter New Product Quantity: ");
                    int qty = sc.nextInt();
                    sc.nextLine(); // consume leftover newline

                    Products product = productDao.getProductById(productId); // Assuming DAO pattern
                    if (product == null) {
                        System.out.println("Product not found!");
                        return;
                    }
                    product.setProductName(name);
                    product.setPrice(price);
                    product.setStockQuantity(qty);

                    productDao.updateProduct(product); // Update in DB or list

                    System.out.println("Product updated successfully!");
                    break;
                }
                case 5:
                {
                	System.out.println("Enter Product ID to Delete: \n");
                    int deleteId = sc.nextInt();

                    if(productDao.deleteProduct(deleteId))
                        System.out.println("Product Deleted Successfully!\n");
                    else
                        System.out.println("Product Not Found!\n");

                    break;
                }
                case 6:
                {
                	List<Orders> orders = orderDao.getAllOrders();
                	if(orders.isEmpty())
                		System.out.println("No Orders Available.\n");
                	else
                		orders.forEach(System.out::println);
                	break;
                }
                case 7:
                    System.out.println("Logged out successfully!\n");
                    return;
                default:
                    System.out.println("Invalid choice! Try again.\n");
            }
        }
	}
}