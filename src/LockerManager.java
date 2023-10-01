import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;

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
        String menu = """
                ——MENU——\s
                1. 보관하기\s
                2. 수거하기\s
                3. 예약하기\s
                4. 예약 확인 및 예약 확정\s
                5. 로그아웃\s
                6. 종료
                 ———————\s
                               
                """;
        System.out.println(menu);
        System.out.print(">>");
        int number = sc.nextInt();
        sc.nextLine();

        System.out.println();
        switch(number){
            case 3:
                LockerManager.Booking();
                break;
            case 4:
                LockerManager.ConfirmBooking();
                break;
            default:
                break;
        }

    }

    //예약 메소드
    public static void Booking(){
        /* <해야할 것>
        * 예약 저장 - Y에서 파일에 예약 정보 저장 Locker methods 제작
        * 예약 확인 - 예약 확인 부분에서 Locker methods 제작
        * menu2.1로 돌아가기, 메인메뉴로 돌아가기 기능 상의 필요
        * */

        //처음 안내 출력
        locker.print(); //Locker class의 print 기능사용하여 사물함 프린트

        String tariff = """
                ——————요금표——————————————————————————\s
                기본 4시간\s
                S : 2000원 / M : 3000원 / L : 4000원\s
                시간당 추가요금\s
                S : 500원 / M : 800원 / L : 1000원\s
                —————————————————————————————————————\s
                                
                ——물품보관함 사이즈 안내————————\s
                S : 01~08번 보관함 (총 8개)\s
                M : 09~12번 보관함 (총 4개)\s
                L : 13~16번 보관함 (총 4개)\s
                ————————————————————————————\s
                                
                이용하실 보관함의 번호를 입력하세요.\s
                                
                * 이전 메뉴로 돌아가려면 Q 또는 q를 입력하세요.\s
                ————————————————————————————————————————\s
                                
                                
                """;

        //입력
        int LockerNumber=0;

        //흐름에 따라 flow 값 변경. 보관함 선택 (1) -> 사이즈 별 안내, 결제 확인 (2) -> 결제 (3)
        int flow = 1;

        while(true){
            System.out.println(tariff);

            //입력
            System.out.print(">>");
            String LockerNum = String.valueOf(sc.next());

            try{
                //Q,q 처리
                if(Objects.equals(LockerNum, "Q") || Objects.equals(LockerNum, "q")){
                    //!!!!!!!!!!!!!!아직 상의 안 된 부분 바뀔 수도 있음
                    System.out.println("\n\nmenu2.1로 돌아가기\n");
                    LockerManager.Menu();
                    break;

               }

                //형식 예외 처리(00, 01, 02 등으로 입력)
                if (LockerNum.length() != 2) throw new IllegalArgumentException();

                //범위 예외 처리(01~16)
                LockerNumber = Integer.parseInt(LockerNum);
                if (LockerNumber<1 || LockerNumber > 16) throw new IllegalArgumentException();

                //예약 확인 처리 !!!!!!!1아직 상의 안 된 부분 바뀔 수도 있음
                if(locker.BooingCheck(LockerNumber)) throw new IllegalAccessException();

                //아무 문제 없다면 사이즈 별 안내 창으로 이동
                flow = 2;
                break;

            }catch(IllegalArgumentException e){ //나머지 입력 예외 처리
                System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");

            }catch(IllegalAccessException e){
                System.out.println("이용 중인 보관함입니다.\n");
            }

        }

        //사이즈 별 입력

        String payment = " ";

        //사이즈 별
        if(flow==2){
            while(true){
                System.out.print(StoragePaymentPrompt(LockerNumber));
                payment = String.valueOf(sc.next());
                sc.nextLine();

                try{
                    if(!(payment.equals("Y")||payment.equals("N")||payment.equals("y")||payment.equals("n")))
                        throw new IllegalArgumentException();

                    //아무 문제 없다면 결제 창으로 이동
                    flow = 3;
                    break;
                }catch(Exception e){
                    System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
                }

            }

        }


        if(flow==3){
            if(payment.equals("Y")||payment.equals("y")){
                System.out.println("\n보관함 예약이 완료되었습니다. ");
                System.out.println("* 2시간 내로 물품 보관(예약 확정)을 완료해주시기 바랍니다.");
                System.out.println("(물품 보관이 완료 되지 않을 경우 예약 자동 취소 및 환불 불가)");

                System.out.println("");
                //Date/Time prompt로 돌아가기
                //!!!!!!!!!!UserManager의 날짜/시간 입력 부분으로 돌아감(여긴 UserManager에서 만들어야 함)
                //UserManager.date();

                //여기서 Locker 객체 불러와서 예약 여부 정해야 합니다. 혹은 여기서 파일 바로 저장.
                // 근데 Locker 객체에서 저장하는게 더 좋을듯
                locker.BooingFileInput(LockerNumber);

                return;
            }
            else{
                //!!!!!!!!!!!!!!아직 상의 안 된 부분 바뀔 수도 있음
                System.out.println("\n\nmenu2.1로 돌아가기\n");
                LockerManager.Menu();
                return;
            }
        }


    }

    //예약 확정 메소드
    public static void ConfirmBooking(){
        /**/

    }

    public static String StoragePaymentPrompt(int lockerNum) {
        /*
        storage_payment_prompt 를 출력하는 메서드
         */
        String strLockerNum = String.valueOf(lockerNum);
        if(lockerNum < 10) {
            strLockerNum = "0" + strLockerNum;
        }
        //보관함 크기에 따른 금액 차등
        int payment = 0;
        if(lockerNum <= 8)
            payment = 2000;
        else if (lockerNum <= 12) {
            payment = 3000;
        }
        else
            payment = 4000;

        String str = "<" + strLockerNum + ">번 보관함 선택하셨습니다. \n" +
                "기본(4시간) 금액 <" + payment + ">원 결제하시겠습니까? (y/n) \n" +
                "\n" +
                "* 초과 시간에 따른 요금은 수거 시 추가결제됩니다.\n" +
                "—————————————————————————— \n" +
                ">>";

        return str;
    }



}