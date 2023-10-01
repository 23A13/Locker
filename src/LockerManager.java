import java.util.*;
import java.util.concurrent.locks.Lock;

public class LockerManager {
    //class 선언
    static Scanner sc = new Scanner(System.in);
    static Locker locker = new Locker();
    static List<Locker> LockerList;

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
            System.out.print(">>");

            try {
                String LockerNum = String.valueOf(sc.next());
                sc.nextLine();
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

                //사용중이지 않은 보관함을 선택했을 때 처리
                if (!locker.BooingCheck(LockerNumber))
                    throw new IllegalAccessException();

                break;

            } catch (IllegalArgumentException e) { //나머지 입력 예외 처리
                System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");

            } catch (IllegalAccessException e) {
                System.out.println("이용 중이지 않은 보관함입니다.");
                System.out.println("보관함 번호를 확인해주세요.\n");
            }

        }

        String LockerPwd;
        while (true) {
            //보관함 비밀번호 입력
            System.out.println(pwd_prompt2);
            System.out.print(">>");

            //보관함 비밀번호 입력
            LockerPwd = String.valueOf(sc.next());
            sc.nextLine();

            try {
                //숫자 입력이 아닌 경우
                if (!isNumeric(LockerPwd))
                    throw new IllegalArgumentException();

                //4자리가 아닌 경우
                if (LockerPwd.length() != 4)
                    throw new IllegalArgumentException();

                break;

            } catch (IllegalArgumentException e) {
                System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");

            }
        }
        //보관함 비밀번호가 올바르지 않을 경우
        if (!Objects.equals(LockerPwd, locker.getpwd())) {
            System.out.println("비밀번호가 올바르지 않습니다.");
            System.out.println("menu2.1로 돌아가기 \n");
            LockerManager.Menu();
        }


        //보관함 비밀번호가 올바른 경우

        //파일에서 현재 시간 불러오기 -> Main.todayDateDate사용
        //Locker.txt에서 저장해온 LockerList에서 해당 보관함 사용 정보 불러와서 시간 비교

        int target=0;
        for (int i=0; i<=LockerList.size(); i++){
            if(LockerList.get(i).LockerNumber == LockerNumber){ //보관함 번호가 맞고
                //if(Integer.parseInt(LockerList.get(i).StartTime) <= Integer.parseInt(Main.todayDateString)){ //날짜가 오늘보다 이전에 시작된거라면
                   target = i;
                //}
            }
        }


        //시간 차이 구하기
        long timeDiff = (Main.currentTimeDate.getTime() - LockerList.get(target).StartTime.getTime())/3600000;

        //추가 결제가 필요한 경우 = 예약시간+4시간 초과인 경우
        if (timeDiff > 4){
            while(true){
                Print_AddPayPrompt((int) timeDiff);
                String yn = String.valueOf(sc.next());
                sc.nextLine();
                try{
                     // Y or y 말고 다른 것을 입력한 경우
                    if(!(Objects.equals(yn, "Y") || Objects.equals(yn, "y")))
                        throw new IllegalArgumentException();

                }catch  (IllegalArgumentException e){
                    System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
                }
                break;
            }
            System.out.println("결제가 완료되었습니다.");
        }

        String pwd_prompt3 = "물품 수거가 가능합니다. \n"+
                "\n* 보관하신 물품 수거 후에 수거완료를 위해 반드시 비밀번호 4자리를 입력해주세요.\n"+
                "수거완료 처리가 되지 않은 이용 건은 자동 초과 요금이 부과됩니다. \n";

        while(true){
            System.out.println(pwd_prompt2);
            System.out.print(">>");

            //보관함 비밀번호 입력
            LockerPwd = String.valueOf(sc.next());
            sc.nextLine();

            try {
                //숫자 입력이 아닌 경우
                if (!isNumeric(LockerPwd))
                    throw new IllegalArgumentException();

                //4자리가 아닌 경우
                if (LockerPwd.length() != 4)
                    throw new IllegalArgumentException();

            } catch (IllegalArgumentException e) {
                System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");

            }
            if (!Objects.equals(LockerPwd, locker.getpwd())) {
                System.out.println("비밀번호가 올바르지 않습니다. 다시 한 번 입력해주세요.");
            }
            break;
        }


        //수거완료한 보관함 정보(?) 삭제
        LockerList.remove(target);

        System.out.println("물품 수거가 완료되었습니다.");
        System.out.println("이용해주셔서 감사합니다.");
        System.exit(0);

         //*/



    }

    private static void Print_AddPayPrompt(int timeDiff){
        /*
        addtional_payment_prompt를 출력하는 메서드
         */
        int additional=0;
        switch (locker.size) {
            case 1:
                additional = 500;
                break;
            case 2:
                additional = 800;
                break;
            case 3:
                additional = 1000;
                break;
        }

        String str = "초과 시간에 따른 추가 금액 결제를 진행합니다. \n"+
                timeDiff*additional + "원 \n"+
                "결제하시려면 Y 또는 y를 입력해주세요. \n"+
                "———————————————————————————\n"+
                ">>";

        System.out.println(str);

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

