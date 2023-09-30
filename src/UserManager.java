import java.io.IOException;
import java.util.Scanner;
import java.util.InputMismatchException;

public class UserManager {

    static Scanner scan = new Scanner(System.in);
    public void menu1() {
        // TODO Auto-generated method stub

        String choice=null;

        while(true) {

            if(choice != null) {
                if(choice.equals("4"))
                    break;
            }

            System.out.println("1.회원가입\n2.회원\n3.비회원\n4.종료");

            try {
                choice = scan.nextLine().trim();

                if(choice.equals("1"))
                    signup();

                else if(choice.equals("2"))
                    login();

                else if(choice.equals("3"))
                    break;

                else if(choice.equals("4")) {
                    System.out.println("프로그램을 종료합니다");
                    System.exit(0);
                }

                else {
                    System.out.println("올바른 입력이 아닙니다");
                    continue;
                }
            }
            catch(InputMismatchException e)
            {
                System.out.println("올바른 입력이 아닙니다");
                scan.nextLine();
            }
        }
    }



    //회원가입 메소드
    private void signup() {
    }

    private void login() {
    }
}
