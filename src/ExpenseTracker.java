import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

class Expense {
	
	String date;
	String day;
    String description;
    double amount;

    public Expense(String date, String day, String description, double amount) {
    	this.date = date;
    	this.day = day;
        this.description = description;
        this.amount = amount;
    }
}
public class ExpenseTracker {
	
	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/expensestracker";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "jithesh@1998";
	
	private static ArrayList<Expense> expenses = new ArrayList<>();

	public static void main(String[] args) {
		
		try {Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD); 
        System.out.println("Database connected");
        System.out.println();
		
		Scanner scanner = new Scanner(System.in);
		
		while (true) {
            System.out.println("Expense Tracker Menu:");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Select Expenses");
            System.out.println("4. Update Expenses");
            System.out.println("5. Delete Expenses");
            System.out.println("6. Exit");
            System.out.println();
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            System.out.println();

            switch (choice) {
                case 1:
                    addExpense(connection, scanner);
                    break;
                case 2:
                    viewExpenses();
                    break;
                case 3:
                    selectExpenses(connection);
                    break;
                case 4:
                    updateExpenses(connection, scanner);
                    break;
                case 5:
                    deleteExpenses(connection, scanner);
                    break;
                case 6:
                    System.out.println("Exiting Expense Tracker. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
		} catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addExpense(Connection connection, Scanner scanner) throws SQLException {
    	System.out.print("Enter expense date: ");
        String Date = scanner.next();
        
        System.out.print("Enter expense day: ");
        String Day = scanner.next();
        
        System.out.print("Enter expense description: ");
        String Description = scanner.next();

        System.out.print("Enter expense amount: ");
        double Amount = scanner.nextDouble();

        Expense expense = new Expense(Date, Day, Description, Amount);
        expenses.add(expense);
        
        String insertSQL = "INSERT INTO expenses (Date, Day, Description, Amount) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL); {

        preparedStatement.setString(1, Date);
    	preparedStatement.setString(2, Day);
    	preparedStatement.setString(3, Description);
        preparedStatement.setDouble(4, Amount);
        preparedStatement.executeUpdate();
        System.out.println();
        System.out.println("Expense added successfully!");
        System.out.println();
        }
        
    }

    private static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
        } else {
            System.out.println("Expense List:");
            for (Expense expense : expenses) {
            	System.out.println();
            	System.out.println("Date: " + expense.date);
            	System.out.println("Day: " + expense.day);
                System.out.println("Description: " + expense.description);
                System.out.println("Description: " + expense.amount);
                System.out.println();
            }
        }
		

	}
    
    private static void selectExpenses(Connection connection) throws SQLException {
    	
    	String selectSQL = "select * from expenses";
    	PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
    	ResultSet resultset = preparedStatement.executeQuery();
    	while(resultset.next())
    	{
    		String Date = resultset.getString(1);
    		String Day = resultset.getString(2);
    		String Description = resultset.getString(3);
    		double Amount = resultset.getDouble(4);
    		
    		System.out.println(Date + " " + Day + " " + Description + " " + Amount);
 
    	}
    	System.out.println();
    	
    }
    
    private static void updateExpenses(Connection connection, Scanner scanner) throws SQLException {
    	
    	System.out.print("Enter expense description: ");
        String Description = scanner.next();
        System.out.print("Enter expense amount: ");
        double Amount= scanner.nextDouble();
        
        String updateSQL = "update expenses SET Amount = ? where Description = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
        preparedStatement.setString(1, Description);
        preparedStatement.setDouble(2, Amount);
        preparedStatement.executeUpdate();
        System.out.println();
        System.out.println("Expense updated successfully!");
        System.out.println();
        
   }
    
    private static void deleteExpenses(Connection connection, Scanner scanner) throws SQLException {
    	
    	
    	System.out.print("Enter expense date: ");
        String Date = scanner.next();
        System.out.print("Enter expense description: ");
        String Description = scanner.next();
        
        String deleteSQL = "delete from expenses where Date = ? && Description = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
        preparedStatement.setString(1, Date);
        preparedStatement.setString(2, Description);
        preparedStatement.executeUpdate();
        System.out.println();
        System.out.println("Expense deleted successfully!");
        System.out.println();
    	
    }

}
