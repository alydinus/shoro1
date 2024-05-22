import java.sql.*;
import java.util.Scanner;

public class Salesman {
    static Scanner scanner = new Scanner(System.in);
    public static final String db_USERNAME  = "postgres";
    public static final String db_PASSWORD  = "1";
    public static final String db_URL  = "jdbc:postgresql://localhost:5432/postgres";
    Salesman() throws SQLException {
        login();
    }
    static void login() throws SQLException {
        scanner = new Scanner(System.in);
        System.out.println("Ввведите логин>>>");
        String login = scanner.next();
        System.out.println("Ввведите пароль>>>");
        String password = scanner.next();
        validateLogin(login,password);
        options();
    }
    private static boolean validateLogin(String username, String password) {
        try (Connection connection = DriverManager.getConnection(db_URL, db_USERNAME, db_PASSWORD)) {
            String sql = "SELECT * FROM salesman WHERE name = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                System.err.println("Неверный логин и(или) пароль.");
                System.exit(0);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    static void options() throws SQLException{
        Connection connection = DriverManager.getConnection(db_URL,db_USERNAME,db_PASSWORD);
        System.out.println("\nПриветствую дорогой, Продавец!"+" Пожалуйста наберите номер меню для работы с программой.");
        System.out.println("1.\tПоказать весь список товаров для продажи.\n" +
                "2.\tИскать товар по названию.\n" +
                "3.\tПоказать отчет по продаже.\n" +
                "4.\tСделать заказ отсутствующего товара.\n" +
                "5.\tУдалить заказ.\n" +
                "6.\tВыход.\n");
        int option = scanner.nextInt();
        switch (option){
            case 1:
                Statement option1 = connection.createStatement();
                String sql_show_all_goods = "select * from sale";
                ResultSet resultSet1 = option1.executeQuery(sql_show_all_goods);
                while(resultSet1.next()){
                    System.out.println("Товар - " + resultSet1.getString("name") + "; " + "Количество - " +resultSet1.getInt("quantity"));
                }
                break;
            case 2:
                System.out.println("Введите название товара.");
                String nameToSearch = scanner.next();
                String sql_search_by_name = "SELECT * FROM sale WHERE name LIKE ?";
                PreparedStatement statement2 = connection.prepareStatement(sql_search_by_name);
                statement2.setString(1, "%" + nameToSearch + "%");
                ResultSet resultSet2 = statement2.executeQuery();
                while (resultSet2.next()) {
                    System.out.println(resultSet2.getString("name") + " " + resultSet2.getInt("quantity"));
                }
                break;
            case 3:
                Statement option3 = connection.createStatement();
                String sql_show_all_sold_goods = "select * from sale";
                ResultSet resultSet3 = option3.executeQuery(sql_show_all_sold_goods);
                while(resultSet3.next()){
                    System.out.println("Товар - " + resultSet3.getString("name") + "; " + "Количество - " +resultSet3.getInt("quantity"));
                }
                break;
            case 4:
                System.out.println("Введите название товара, который хотите добавить.");
                String name = scanner.next();
                System.out.println("Введите количество товара, который хотите добавить.");
                int quantity = scanner.nextInt();

                String option4 = "insert into order_new_goods (name, quantity) VALUES (?, ?)";
                PreparedStatement statement4 = connection.prepareStatement(option4);
                statement4.setString(1, name);
                statement4.setInt(2, quantity);
                int affectedRows4 = statement4.executeUpdate();
                if (affectedRows4 > 0){
                    System.out.println("Товар успешно добавлен!");
                }else{
                    System.out.println("Ошибка при добавлении товара.");
                }
                break;
            case 5:
                System.out.println("Какой заказ вы бы хотели удалить?>>>");
                Statement option51 = connection.createStatement();
                String sql_delete = "select * from sold";
                ResultSet resultSet51 = option51.executeQuery(sql_delete);
                while(resultSet51.next()){
                    System.out.println("Товар - " + resultSet51.getString("name") + "; " + "Количество - " +resultSet51.getInt("quantity"));
                }
                String nameToDelete = scanner.next();
                String option5 = "DELETE FROM sold WHERE name = ?";
                PreparedStatement statement5 = connection.prepareStatement(option5);
                statement5.setString(1, nameToDelete);
                int affectedRows = statement5.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Товар " + nameToDelete + " успешно удален из базы данных.");
                } else {
                    System.out.println("Товар " + nameToDelete + " не найден в базе данных.");
                }
                break;
            case 6:
                System.out.println("Программа завершена.");
                System.exit(0);
                break;
        }

    }



}
