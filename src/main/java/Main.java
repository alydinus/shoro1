import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        chooseTypeOfAcc();
    }
    static void chooseTypeOfAcc(){
        System.out.println("Для запуска программы, пожалуйста введите тип аккаунта: >>>");
        System.out.println("1 - Продавец" +
                "\n2 - Доставщик" +
                "\n3 - Поставщик");
        Scanner scanner = new Scanner(System.in);
        try {
            int response = scanner.nextInt();
            switch (response) {
                case 1:
                    new Salesman();
                    break;
                case 2:
                    new Delivery();
                    break;
                case 3:
                    new Provider();
                    break;
                default:
                    System.err.println("Неверный ввод.");
                    break;
            }
        }catch (Exception e){
            System.err.println("Неверный ввод.");
        }
    }
}
