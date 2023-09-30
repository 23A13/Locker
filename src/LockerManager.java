import java.util.Scanner;

public class LockerManager {
    //class 선언
    static Scanner sc = new Scanner(System.in);
    static Locker locker = new Locker();


    //Constructor
    public LockerManager() {

    }
    public LockerManager(String userId, String userPassword) {

    }

    public static void Menu(){
        System.out.println("——MENU—— ");
        System.out.println("1. 보관하기");
        System.out.println("2. 수거하기");
        System.out.println("3. 예약하기");
        System.out.println("4. 예약 확인 및 예약 확정");
        System.out.println(("5. 로그아웃"));
        System.out.println("6. 종료");
        System.out.println(" ——————— ");
        System.out.print(">>");
        int number = sc.nextInt();

        System.out.println();
        switch(number){
            case 3:
                //LockerManager.Booking();
                break;
            case 4:
               //LockerManager.ConfirmBooking();
                break;
            default:
                break;
        }

    }
}
