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

        //입력
        int LockerNumber=0;

        while(true){
            System.out.println("——————요금표—————— ");
            System.out.println("기본 4시간");
            System.out.println("S:2000원/M:3000원/L:4000원");
            System.out.println("시간당 추가요금");
            System.out.println("S:500원/M:800원/L:1000원");
            System.out.println("——————————————————\n\n");
            System.out.println("——물품보관함 사이즈 안내——");
            System.out.println("S : 01~08번 보관함 (총 8개) ");
            System.out.println("M : 09~12번 보관함 (총 4개) ");
            System.out.println("L : 13~16번 보관함 (총 4개) ");
            System.out.println("——————————————————————\n\n");
            System.out.println("———————————예약 서비스 이용 시 주의사항————————————");
            System.out.println("* 예약 시 4시간 선결제 후 보관함에 도착하여 물품을 보관해주세요");
            System.out.println("* 예약 후 2시간 내로 물품 보관이 완료되지 않을 경우, 예약이 자동 취소됩니다. (환불 불가)");
            System.out.println("* 기본 4시간 초과 시 수거하는 시점까지 자동으로 시간 당 추가 요금이 부과됩니다. (추가요금은 요금표 참고) ");
            System.out.println("* 추가 요금은 보관함 수거 시 부과됩니다. ");
            System.out.println("* 결제 수단은 카드만 사용 가능합니다. ");
            System.out.println("* 이용 중인 보관함은 예약이 불가합니다. ");
            System.out.println("* 예약은 보관함 한 개만 가능합니다. ");
            System.out.println("—————————————————————————————————————————————\n\n");
            System.out.println("이용 하실 보관함의 번호를 입력하세요.\n");
            System.out.println("* 이전 메뉴로 돌아가려면 Q 또는 q를 입력하세요.\n");
            System.out.println("—————————————————————————————————————————————");

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

                break;

            }catch(IllegalArgumentException e){ //나머지 입력 예외 처리
                System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");

            }catch(IllegalAccessException e){
                System.out.println("이용 중인 보관함입니다.\n");
            }

        }

        //사이즈 별 입력

        String payment;

        //사이즈 별
        while(true){
            if(LockerNumber>0 && LockerNumber<9){
                System.out.println("<"+LockerNumber+">"+"번 보관함을 선택하셨습니다.");
                System.out.println("해당 보관함의 사이즈는 S이며 기본 4시간 2000원, 시간당 추가 요금은 500원입니다.\n");
                System.out.print("결제 하시겠습니까? (Y/N) ");
            }
            else if(LockerNumber>8 && LockerNumber<13){
                System.out.println("<"+LockerNumber+">"+"번 보관함을 선택하셨습니다.");
                System.out.println("해당 보관함의 사이즈는 M이며 기본 4시간 3000원, 시간당 추가 요금은 800원입니다. \n");
                System.out.print("결제 하시겠습니까? (Y/N) ");
            }
            else{
                System.out.println("<"+LockerNumber+">"+"번 보관함을 선택하셨습니다.");
                System.out.println("해당 보관함의 사이즈는 L이며 기본 4시간 4000원, 시간당 추가 요금은 1000원입니다. \n");
                System.out.print("결제 하시겠습니까? (Y/N) ");
            }


            //입력
            payment = sc.next();

            try{
                if(!(payment.equals("Y")||payment.equals("N"))) throw new IllegalArgumentException();
                break;
            }catch(Exception e){
                System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
            }

        }
        //
        System.out.println(payment);

        if(payment.equals("Y")){
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

    //예약 확정 메소드
    public static void ConfirmBooking(){
        /**/

    }


}