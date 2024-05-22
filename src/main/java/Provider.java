import java.sql.*;
import java.util.Scanner;

public class Provider {
    static Scanner scanner = new Scanner(System.in);
    public static final String db_USERNAME  = "postgres";
    public static final String db_PASSWORD  = "1";
    public static final String db_URL  = "jdbc:postgresql://localhost:5432/postgres";
    Provider() throws SQLException {
        login();
    }
    public static void login() throws SQLException {
        System.out.println("Ввведите логин>>>");
        String login = scanner.next();
        System.out.println("Ввведите пароль>>>");
        String password = scanner.next();
        validateLogin(login,password);
        options();
    }
    private static boolean validateLogin(String username, String password) throws SQLException{
        try (Connection connection = DriverManager.getConnection(db_URL, db_USERNAME, db_PASSWORD)) {
            String sql = "SELECT * FROM provider WHERE name = ? AND password = ?";
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
        System.out.println("Приветствую дорогой, Поставщик!\n" +
                "Пожалуйста наберите номер меню для работы с программой.");
        System.out.println("1. Показать список товаров для поставки\n" +
                "2. Показать товар с самым большим количеством заказов для доставки\n" +
                "3. Показать товар с самым меньшим количеством заказов для доставки\n" +
                "4. Выход\n");
        int response = scanner.nextInt();
        switch (response){
            case 1:
                Statement option1 = connection.createStatement();
                String sql_show_all_products_to_provide = "select * from need_material";
                ResultSet resultSet1 = option1.executeQuery(sql_show_all_products_to_provide);
                while (resultSet1.next()){
                    System.out.println("Товар - " + resultSet1.getString("name") + ";" + "Количество - " +resultSet1.getInt("quantity"));
                }
                System.exit(0);
                break;
            case 2:
                String sql_max = "select max(quantity) as max_goods from need_material";
                PreparedStatement preparedStatement2 = connection.prepareStatement(sql_max);
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                if (resultSet2.next()){
                    System.out.println("Товар с наибольшим числом заказов - " + resultSet2.getInt("max_goods"));
                }
                break;
            case 3:
                String sql_min = "select min(quantity) as min_goods from need_material";
                PreparedStatement preparedStatement3 = connection.prepareStatement(sql_min);
                ResultSet resultSet3 = preparedStatement3.executeQuery();
                if (resultSet3.next()){
                    System.out.println("Товар с наименьшим числом заказов - " + resultSet3.getInt("min_goods"));
                }
                break;
            case 4:
                System.out.println("Программа завершена.");
                System.exit(0);
        }
    }
}
