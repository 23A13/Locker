import java.util.Scanner;
import java.util.Objects;

public class LockerManager {
    //class 선언
    static Scanner sc = new Scanner(System.in);
    static Locker locker = new Locker();


    //Constructor
    public LockerManager() {

    }

    public LockerManager(String userId, String userPassword) {

    }

    public static void Menu() {
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
        switch (number) {
            case 2:
                LockerManager.Pickup();
                break;
            default:
                break;
        }

    }

    private static void Pickup() {
        //보관함
        locker.print();

        String tariff = """
                이용하신 보관함의 번호를 입력하세요. (ex: 01)\s
                \s
                * 이전 메뉴로 돌아가려면 Q 또는 q를 입력하세요.\s
                ——————————————————————————————————————————————\s
                """;

        int LockerNumber = 0;
        String pwd_prompt2 = "비밀번호 4자리를 입력하세요. ";

        while (true) {
            //notice 2 출력
            System.out.println(tariff);

            //보관함 번호 입력받기
            System.out.println(">>");
            String LockerNum = String.valueOf(sc.next());
            sc.nextLine();

            try {
                //Q,q 처리
                if (Objects.equals(LockerNum, "Q") || Objects.equals(LockerNum, "q")) {
                    //!!!!!!!!!!!!!!아직 상의 안 된 부분 바뀔 수도 있음
                    System.out.println("\n\nmenu2.1로 돌아가기\n");
                    LockerManager.Menu();
                    break;

                }

                //형식 예외 처리(00, 01, 02 등으로 입력해야함)
                if (LockerNum.length() != 2)
                    throw new IllegalArgumentException();

                //범위 예외 처리(01~16)
                LockerNumber = Integer.parseInt(LockerNum);
                if (LockerNumber < 1 || LockerNumber > 16)
                    throw new IllegalArgumentException();

                //예약 확인 처리
                if (!locker.BooingCheck(LockerNumber))
                    throw new IllegalAccessException();

                break;

            } catch (IllegalArgumentException e) { //나머지 입력 예외 처리
                System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");

            } catch (IllegalAccessException e) {
                System.out.println("이용 중이지 않은 보관함입니다.\n");
                System.out.println("보관함 번호를 확인해주세요.\n");
            }


            //보관함 비밀번호 입력
            System.out.println(pwd_prompt2);
            System.out.println(">>");

            //비밀번호 입력
            String LockerPwd = String.valueOf(sc.next());
            sc.nextLine();

            try {
                //숫자 입력이 아닌 경우
                if (!isNumeric(LockerPwd))
                    throw new IllegalArgumentException();

                //4자리가 아닌 경우
                if (LockerPwd.length() != 4)
                    throw new IllegalArgumentException();

                break;

            }catch(IllegalArgumentException e){
                    System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");

            }

            //비밀번호가 올바르지 않을 경우
            if(!Objects.equals(LockerPwd, locker.getpwd())){
                System.out.println("비밀번호가 올바르지 않습니다.\n");
                System.out.println("\n\nmenu2.1로 돌아가기 \n");
                LockerManager.Menu();
                break;
            }

            //추가 결제가 필요한 경우 = 예약시간+4시간 초과인 경우




        }
    }
    public static boolean isNumeric(String strNum) {
        /*
        문자열이 숫자인지 확인하는 메서드
         */
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}

