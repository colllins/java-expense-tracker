package com.collins.expensetracker.cli;

import com.collins.expensetracker.db.ConnectionFactory;
import com.collins.expensetracker.db.DatabaseConfig;
import com.collins.expensetracker.model.*;
import com.collins.expensetracker.repository.*;
import com.collins.expensetracker.service.CategoryService;
import com.collins.expensetracker.service.TransactionService;
import com.collins.expensetracker.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ExpenseTrackerApp {

    static Scanner scanner = new Scanner(System.in);
    public static int currentUserId = 0;

    public static void main(String[] args) {

        DatabaseConfig config = new DatabaseConfig("jdbc:mysql://localhost:3306/expensetracker","root","Thisismanmad1@");
        ConnectionFactory connectionFactory = new ConnectionFactory(config);

        UserRepository userRepo = new JdbcUserRepository(connectionFactory);
        UserService userService = new UserService(userRepo);
        CategoryRepository categoryRepo= new JdbcCategoryRepository(connectionFactory);
        CategoryService categoryService = new CategoryService(categoryRepo);
        TransactionRepository transactionRepo =  new JdbcTransactionRepository(connectionFactory);
        TransactionService transactionService = new TransactionService(transactionRepo);
        int input = 0;

        do{
            showMainMenu();

            try{
                input = Integer.parseInt(scanner.nextLine());

                readOption(input, userService, categoryService, transactionService);
            }catch (NumberFormatException e){
                System.out.println("Input is Not a passable integer");
            }
        }while(input!=8);
    }

    public static void showMainMenu(){
        System.out.print("Enter 1 to Create User");
        System.out.println();
        System.out.print("Enter 2 to Select Active User");
        System.out.println();
        System.out.print("Enter 3 to Manage Categories for current User");
        System.out.println();
        System.out.print("Enter 4 to Add Transaction for current User");
        System.out.println();
        System.out.print("Enter 5 to List Transactions for current User");
        System.out.println();
        System.out.print("Enter 6 to Show Monthly Summary for current User");
        System.out.println();
        System.out.print("Enter 7 to Delete Transaction");
        System.out.println();
        System.out.print("Enter 8 to Exit: ");
    }

    public static void readOption(int input, UserService userService, CategoryService categoryService, TransactionService transactionService){

        if(input==1){
            createUser(userService);
        }else if(input==2){
            selectActiveUser(userService);
        }else if(input == 3){
            manageCategories(categoryService, userService);
        }else if(input == 4){
            addTransaction(transactionService, userService, categoryService);
        }else if(input == 5){
            listTransactions(transactionService, userService);
        }else if(input == 6){
            showMonthlySummary(userService, transactionService);
        }
        else if(input == 7){
            deleteTransaction(transactionService, userService);
        }else if(input == 8){
            System.out.println();
            System.out.println("GoodBye!!!");
            System.out.println();
        }else{
            System.out.println();
            System.out.println("Number entered is out of range! Try Again!");
            System.out.println();
        }
    }

    private static void deleteTransaction(TransactionService transactionService, UserService userService) {
        System.out.println();
        System.out.println("****** Delete transaction ******");
        System.out.println();

        if(currentUserId == 0){
            selectActiveUser(userService);
            if (currentUserId == 0) return;
        }
        System.out.print("Enter transaction id: ");
        try{
            int id = Integer.parseInt(scanner.nextLine());
            Transaction  transaction = transactionService.getTransactionById(id);
            System.out.println();
            if(transaction!=null && transaction.getUserId() == currentUserId){
                transactionService.deleteTransaction(id);
                System.out.println("Transaction deleted successfully! "+ transaction);
                System.out.println();
            }else{
                System.out.println("No Transaction exists with that id for the current user!");
                System.out.println();
            }
        }catch (NumberFormatException e){
            System.out.println("Input is not a passable integer!");
            System.out.println();
        }
    }

    private static void showMonthlySummary(UserService userService, TransactionService transactionService) {
        System.out.println();
        System.out.println("****** Show monthly summary ******");
        System.out.println();

        if(currentUserId == 0){
            selectActiveUser(userService);
            if (currentUserId == 0) return;
        }
        System.out.print("Enter transaction year: ");
        try{
            int year = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter transaction month: ");
            int month = Integer.parseInt(scanner.nextLine());

            MonthlySummary summary = transactionService.getMonthlySummary(currentUserId,year,month);
            System.out.println("Monthly summary for "+ userService.getUserById(currentUserId).getName()+": "+summary);
            System.out.println();

        }catch (NumberFormatException e){
            System.out.println("Input is not a passable integer!");
        }

    }

    private static void listTransactions(TransactionService transactionService, UserService userService) {
        System.out.println();
        System.out.println("****** list transactions ******");
        System.out.println();

        if(currentUserId == 0){
            selectActiveUser(userService);
            if (currentUserId == 0) return;
        }
        List<Transaction> transactions = transactionService.getTransactionForUser(currentUserId);
        if(!transactions.isEmpty()) {
            System.out.println("{ ");
            transactions.forEach(System.out::println);
            System.out.println(" }");
        }else{
            System.out.println("No Transactions for this user yet!");
            System.out.println();
        }
    }

    private static void addTransaction(TransactionService transactionService, UserService userService, CategoryService categoryService) {
        System.out.println();
        System.out.println("****** Add transaction ******");
        System.out.println();

        if(currentUserId == 0){
            selectActiveUser(userService);
            if (currentUserId == 0) return;
        }

        System.out.print("Enter category id: ");
        try{
            int categoryId = Integer.parseInt(scanner.nextLine());
            Category category = categoryService.getCategoryById(categoryId);
            if(category!=null && category.getUserId() == currentUserId){
                System.out.print("Enter transaction type (EXPENSE OR INCOME): ");
                try{
                    TransactionType type = TransactionType.valueOf(scanner.nextLine().toUpperCase());
                    System.out.print("Enter transaction amount: ");
                    BigDecimal amount = new BigDecimal(scanner.nextLine());
                    System.out.print("Enter transaction date (yyyy-MM-dd): ");
                    LocalDate date = LocalDate.parse(scanner.nextLine());
                    System.out.print("Enter transaction description: ");
                    String description = scanner.nextLine();

                    Transaction transaction = transactionService.addTransaction(currentUserId,categoryId,type,amount,date,description);
                    System.out.println("Transaction with id: "+ transaction.getId()+" created successfully");
                    System.out.println();
                }catch (IllegalArgumentException e){
                    System.out.println("Input is not passable transaction type!");
                }
            }else{
                System.out.println("No Category exist for this user with that id!");
            }

        }catch (NumberFormatException e){
            System.out.println("Input is not a passable integer!");
        }

    }

    private static void manageCategories(CategoryService categoryService, UserService userService) {
        System.out.println();
        System.out.println("****** Manage Categories for current user ******");

        String input = "";

        System.out.print("Enter a to Add Category");
        System.out.println();
        System.out.print("Enter b to List Categories");
        System.out.println();
        System.out.print("Enter c to Delete Category: ");

        input = scanner.nextLine().toLowerCase();

        if(input.equals("a")){
            addCategory(categoryService, userService);
        }else if(input.equals("b")){
            listCategories(categoryService, userService);
        }else if(input.equals("c")){
            deleteCategory(categoryService, userService);
        }else{
            System.out.println();
            System.out.println("Invalid Entry! Try Again!");
            System.out.println();
        }
    }

    private static void deleteCategory(CategoryService categoryService, UserService userService) {
        System.out.println();
        System.out.println("****** Delete category ******");
        System.out.println();

        if(currentUserId == 0){
            selectActiveUser(userService);
            if (currentUserId == 0) return;
        }
        System.out.print("Enter category id: ");
        try{
            int id = Integer.parseInt(scanner.nextLine());
            Category  category = categoryService.getCategoryById(id);
            if(category!=null && category.getUserId() == currentUserId){
                categoryService.deleteCategory(id);
                System.out.println("Category deleted successfully! "+ category);
                System.out.println();
            }else{
                System.out.println("No Category exists with that id for the current user!");
                System.out.println();
            }
        }catch (NumberFormatException e){
            System.out.println("Input is not a passable integer!");
            System.out.println();
        }
    }

    private static void listCategories(CategoryService categoryService, UserService userService) {
        System.out.println();
        System.out.println("****** List categories for current user ******");
        System.out.println();

        if(currentUserId == 0){
            selectActiveUser(userService);
            if (currentUserId == 0) return;
        }
        List<Category> categories = categoryService.getCategoriesForUserId(currentUserId);
        if(!categories.isEmpty()) {
            System.out.println("{ ");
            categories.forEach(System.out::println);
            System.out.println(" }");
        }else{
            System.out.println("No Categories for this user yet!");
            System.out.println();
        }
    }

    private static void addCategory(CategoryService categoryService, UserService userService) {
        System.out.println();
        System.out.println("****** Add Category ******");
        System.out.println();

        if(currentUserId == 0){
            selectActiveUser(userService);
            if (currentUserId == 0) return;
        }
        System.out.print("Enter Category Name: ");
        String catName = scanner.nextLine();
        System.out.println();

        Category category = categoryService.createCategory(currentUserId,catName);
        System.out.println("Category with id: "+ category.getId()+" created successfully");
        System.out.println();

    }

    private static void selectActiveUser(UserService userService) {
        System.out.println();
        System.out.println("****** Select active user *****");
        System.out.println();

        List<User> users  = userService.getAllUsers();
        if(!users.isEmpty()){
            System.out.println("{ ");
            users.forEach(System.out::println);
            System.out.println(" }");
            System.out.println();

            System.out.print("Select user id from the list of users: ");
            try{
                int id = Integer.parseInt(scanner.nextLine());
                User user  = userService.getUserById(id);
                if(user == null){
                    System.out.println("User does not exist!");
                    System.out.println();
                    return;
                }else{
                    currentUserId = user.getId();
                    System.out.println("Current Active user is now: "+user);
                    System.out.println();
                }

            }catch (NumberFormatException e){
                System.out.println("Input is not a passable integer");
            }
        }else{
            System.out.println("No users found!");
            System.out.println();
        }
    }

    private static void createUser(UserService userService) {
        System.out.println();
        System.out.println("****** Create User ******");
        System.out.println();

        System.out.print("Enter user name: ");
        String name = scanner.nextLine();
        System.out.print("Enter user email: ");
        String email = scanner.nextLine();
        System.out.println();

        User user = userService.createUser(name,email);
        currentUserId = user.getId();
        System.out.println("User with id: "+currentUserId+ " successfully created");
        System.out.println();
    }
}
