import java.sql.*;
import java.util.Scanner;

public class Delivery {
    static Scanner scanner = new Scanner(System.in);
    public static final String db_USERNAME  = "postgres";
    public static final String db_PASSWORD  = "1";
    public static final String db_URL  = "jdbc:postgresql://localhost:5432/postgres";
    Delivery() throws SQLException {
        login();
        options();
    }
    public static void login() throws SQLException {
        System.out.println("Ввведите логин>>>");
        String login = scanner.next();
        System.out.println("Ввведите пароль>>>");
        String password = scanner.next();
        validateLogin(login,password);
    }
    private static boolean validateLogin(String username, String password) throws SQLException{
        try (Connection connection = DriverManager.getConnection(db_URL, db_USERNAME, db_PASSWORD)) {
            String sql = "SELECT * FROM delivery WHERE name = ? AND password = ?";
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
        System.out.println("Приветствую дорогой, Доставщик!\n" +
                "Пожалуйста наберите номер меню для работы с программой.");
        System.out.println("1.\tПоказать список товаров для доставки.\n" +
                "2.\tПоказать доставленные заказы.\n" +
                "3.\tДоставить заказ.\n" +
                "4.\tПоказать количество заказанных товаров.\n" +
                "5.\tПоказать мой заработок.\n" +
                "6.\tВыход.");
        int response = scanner.nextInt();
        switch (response){
            case 1:
                Statement option1 = connection.createStatement();
                String sql_show_all_sold_goods_to_deliver = "select * from sold";
                ResultSet resultSet1 = option1.executeQuery(sql_show_all_sold_goods_to_deliver);
                if (resultSet1.next()){
                    System.out.println("Товар - " + resultSet1.getString("name") + "; " + "Количество - " +resultSet1.getInt("quantity"));
                }
                break;
            case 2:
                Statement option2 = connection.createStatement();
                String sql_show_delivered = "select * from delivered";
                ResultSet resultSet2 = option2.executeQuery(sql_show_delivered);
                if (resultSet2.next()){
                    System.out.println("Товар - " + resultSet2.getString("name") + "; " + "Количество - " +resultSet2.getInt("quantity"));
                }
                break;
            case 3:
                System.out.println("Введите название товара, который хотите доставить>>>");
                String nameToDeliver = scanner.next();
                String sql_add_toDeliver = "insert into delivered(name) values (?)";
                String sql_delete_from_delivered = "delete from sold where name = ?";
                PreparedStatement statement31 = connection.prepareStatement(sql_add_toDeliver);
                PreparedStatement statement32 = connection.prepareStatement(sql_delete_from_delivered);
                statement31.setString(1,nameToDeliver);
                statement32.setString(1,nameToDeliver);
                int affectedRows31 = statement31.executeUpdate();
                int affectedRows32 = statement32.executeUpdate();
                if (affectedRows31 > 0 && affectedRows32 > 0){
                    System.out.println("Товар успешно доставлен.");
                }else{
                    System.out.println("Товар не найден в базе данных.");
                }
                break;
            case 4:
                Statement option4 = connection.createStatement();
                String sql_show_all_sold_goods = "select * from sale";
                ResultSet resultSet4 = option4.executeQuery(sql_show_all_sold_goods);
                while(resultSet4.next()){
                    System.out.println(resultSet4.getString("name") + " " +resultSet4.getInt("quantity"));
                }
                break;
            case 5:
                String sql_salary = "select sum(quantity) AS total_quantity from delivered";
                PreparedStatement preparedStatement5 = connection.prepareStatement(sql_salary);
                ResultSet resultSet5 = preparedStatement5.executeQuery();
                if (resultSet5.next()){
                    System.out.println("Вы заработали: " + resultSet5.getInt("total_quantity")*50 + " сом!");
                }
                break;
            case 6:
                System.out.println("Программа завершена!");
                System.exit(0);
                break;
        }
    }
}
