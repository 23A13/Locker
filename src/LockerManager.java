import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Lock;

import static java.lang.Integer.*;
import static java.lang.System.exit;

public class LockerManager {


    static ArrayList<Locker> LockerList = new ArrayList<>(); //Locker정보 저장 구조
    Map<String, User> mem = new HashMap<>(); //회원 정보 저장 구조
    Map<String, User> nonmem = new HashMap<>(); //비회원 정보 저장 구조

    UserManager usermanager = new UserManager();

    //로그인한 사용자 아이디
    String loguser;
    User memUser = mem.get(loguser);  //유저 아이디로 해당되는 user 찾기

    //constructor


    //Constructor
    public LockerManager(){

    }
    public LockerManager(String loguser)
    {
        this.loguser = loguser;
    }



    //class 선언
    static Scanner sc = new Scanner(System.in);
    static Locker locker = new Locker();
    UserManager userManager = new UserManager();

    //constructor



    //안내문구들
    static String table1 = """
                ---------------------------------------------------------------------------------------
                | 01        | 02        | 03        | 04        | 13               | 14               |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                |-----------------------------------------------|                  |                  |
                | 05        | 06        | 07        | 08        |                  |                  |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                |-----------|-----------|-----------|-----------|------------------|------------------|
                | 09        | 10        | 11        | 12        | 15               | 16               |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                ---------------------------------------------------------------------------------------

                """;
    static String tariff = """
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

    //프로그램 최초 시작 시 locker 데이터 txt파일로부터 불러오는 함수
    public void LockerFileInput(){
        String filename="Locker.txt";
        try(Scanner scan=new Scanner(new File(filename))){
            while(scan.hasNextLine()) {
                String str=scan.nextLine();
                String[] temp=str.split(" ");
                LockerList.add(new Locker(temp[0].trim(),temp[1].trim(),temp[2].trim(),temp[3].trim(),temp[4].trim()));//최초로 저장구조에 locker정보 저장
            }
        }catch(FileNotFoundException e){
            System.out.println("파일 입력이 잘못되었습니다.");
        }
    }

    //프로그램 종료 시 locker 데이터 txt파일에 저장하는 함수
    public void LockerFileWrite(){
        try{
            File file = new File("Locker.txt");
            if(!file.exists()){
                System.out.println("파일경로를 다시 확인하세요.");
            }else{
                FileWriter writer =new FileWriter(file, false);//기존 내용 없애고 쓰려면 false
                for(int i=0;i< LockerList.size();i++){
                    writer.write(LockerList.get(i).locknum+" "+LockerList.get(i).locksize+" "+LockerList.get(i).use+" "+LockerList.get(i).date+" "+LockerList.get(i).confirmbook+"\n");
                    writer.flush();
                }
                writer.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //날짜시간 입력 시 무효 예약 내역 수정용
    public void dateLockerFileInput(ArrayList<Locker> List){
        String filename="Locker.txt";
        try(Scanner scan=new Scanner(new File(filename))){
            while(scan.hasNextLine()) {
                String str=scan.nextLine();
                String[] temp=str.split(" ");
                List.add(new Locker(temp[0].trim(),temp[1].trim(),temp[2].trim(),temp[3].trim(),temp[4].trim()));//최초로 저장구조에 locker정보 저장
            }
        }catch(FileNotFoundException e){
            System.out.println("파일 입력이 잘못되었습니다.");
        }
    }

    //날짜시간 입력 시 무효 예약 내역 수정용
    public void dateLockerFileWrite(ArrayList<Locker> List){
        try{
            File file = new File("Locker.txt");
            if(!file.exists()){
                System.out.println("파일경로를 다시 확인하세요.");
            }else{
                FileWriter writer =new FileWriter(file, false);//기존 내용 없애고 쓰려면 false
                for(int i=0;i< List.size();i++){
                    writer.write(List.get(i).locknum+" "+List.get(i).locksize+" "+List.get(i).use+" "+List.get(i).date+" "+List.get(i).confirmbook+"\n");
                    writer.flush();
                }
                writer.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //회원 메뉴
    public void Menu_Mem(){
        LockerFileInput();
        userManager.UserFileInput();

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

        int number=0;

        while(true){

            try{
                System.out.print(">>");
                number = sc.nextInt();

                if(number<1||number>6) throw new InputMismatchException();

                //올바른 입력시
                break;

            }catch(InputMismatchException e){
                System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
                sc.nextLine();
            }

        }

        System.out.println();

        switch(number){
            case 1:
                Storage(true);
                break;
            case 2:
                break;
            case 3:
                Booking();
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                exit(0);
                break;
            default:
                break;
        }

        LockerFileWrite();
        userManager.UserFileWrite();

    }
    //비회원메뉴
    public void Menu_Nonmem() {

        LockerFileInput();

        String menu = """
                --MENU--\s
                1. 보관하기\s
                2. 수거하기\s
                3. 이전 메뉴로 돌아가기\s
                4. 종료
                ---------\s

                """;

        System.out.println(menu);
        System.out.print(">>");
        int number = sc.nextInt();
        sc.nextLine();

        switch (number) {
            case 1:
                Storage(false);
                break;
            case 2:
                Pickup();
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                break;
        }
        LockerFileWrite();
    }


    //예약 메소드
    public void Booking(){

        //기존 예약 내역 확인
        if(!mem.get(loguser).locknum.equals("-")){
            System.out.println("이미 예약 내역이 존재합니다.\n\n");
            Menu_Mem();
            exit(0);
        }

        System.out.println(table1);

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
                    Menu_Mem();
                    break;

                }

                //형식 예외 처리(00, 01, 02 등으로 입력)
                if (LockerNum.length() != 2) throw new IllegalArgumentException();

                //범위 예외 처리(01~16)
                LockerNumber = parseInt(LockerNum);
                if (LockerNumber<1 || LockerNumber > 16) throw new IllegalArgumentException();

                //예약 확인 처리
                boolean bookingcheck=false;
                for(int i=0; i<LockerList.size(); i++){
                    if(parseInt(LockerList.get(i).locknum) == LockerNumber){
                        if(parseInt(LockerList.get(i).use) != 0){
                            throw new IllegalAccessException();
                        }
                    }
                }

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
                exit(0);


                //Locker 사용여부 예약중(2), 보관함 크기 저장
                String lockersize;
                if(LockerNumber <= 8) lockersize="0";
                else if (LockerNumber <= 12) lockersize="1";
                else lockersize="2";

                for(int i=0; i<LockerList.size(); i++){
                    if(parseInt(LockerList.get(i).locknum) == LockerNumber){
                        LockerList.get(i).use = "2";
                        LockerList.get(i).locksize = lockersize;
                        LockerList.get(i).date = Main.currentTimeString;
                    }
                }

                //User 회원 lockernumber 저장
                mem.get(loguser).locknum = String.valueOf(LockerNumber);

                exit(0);
            }
            else{
                Menu_Mem();
                exit(0);
            }
        }


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


    private void Pickup() {
    }

    private void Storage(boolean isMembership) {

        //전역으로 빠질 애들
        //보관함
        //수정
        String table1 = """
                ---------------------------------------------------------------------------------------
                | 01        | 02        | 03        | 04        | 13               | 14               |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                |-----------------------------------------------|                  |                  |
                | 05        | 06        | 07        | 08        |                  |                  |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                |-----------|-----------|-----------|-----------|------------------|------------------|
                | 09        | 10        | 11        | 12        | 15               | 16               |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                |           |           |           |           |                  |                  |
                ---------------------------------------------------------------------------------------

                """;

        //요금표
        String tariff = """
                ——————요금표——————\s
                기본 4시간\s
                S : 2000원 / M : 3000원 / L : 4000원\s
                시간당 추가요금\s
                S : 500원 / M : 800원 / L : 1000원\s
                ———————————————\s

                ——물품보관함 사이즈 안내——\s
                S : 01~08번 보관함 (총 8개)\s
                M : 09~12번 보관함 (총 4개)\s
                L : 13~16번 보관함 (총 4개)\s
                ———————————————\s

                이용하실 보관함의 번호를 입력하세요.\s

                * 이전 메뉴로 돌아가려면 Q 또는 q를 입력하세요.
                """;
        //pwd_prompt1
        String pwd_prompt1 = "사용하실 비밀번호 네자리를 입력하세요.\n";


        //입력받은 보관함 번호
        int LockerNumber = 0;   //정수형
        String LockerNum = null;    //스트링
        //보관함 비밀번호
        String LockerPwd = null;
        //흐름에 따라 flow 값 변경. 보관함 선택 (1) -> 비밀번호 입력 (2) -> 결제 (3)
        int flow = 1;


        //보관함 출력
        System.out.println(table1);

        while (true) {
            //요금표 출력
            System.out.println(tariff);

            //보관함 번호 입력
            System.out.print(">>");
            LockerNum = String.valueOf(sc.next());
            sc.nextLine();

            try {
                //Q or q 입력 시 menu2.1로 돌아가기
                if (Objects.equals(LockerNum, "Q") || Objects.equals(LockerNum, "q")) {
                    //수정
                    //회원 유무에 따라 돌아갈 메뉴 결정
                    if(isMembership) {
                        System.out.println("\n\nmenu2.1로 돌아가기\n");
                        Menu_Mem();
                    } else {
                        System.out.println("\n\nmenu2.2로 돌아가기\n");
                        Menu_Nonmem();
                    }
                    break;
                }

                //범위 예외 처리(01~16)
                LockerNumber = Integer.parseInt(LockerNum);
                if (LockerNumber < 1 || LockerNumber > 16)
                    throw new IllegalArgumentException();

                //형식 예외 처리 (01, 02 등으로 입력하지 않고 1, 2 등으로 입력함)
                if (LockerNum.length() != 2)
                    throw new IllegalArgumentException();

                //이용중인 보관함 선택
                //수정
                Iterator<Locker> iterator = LockerList.iterator();
                while (iterator.hasNext()) {
                    Locker locker = iterator.next();
                    //선택한 보관함과 번호가 같은 보관함 찾기
                    if (locker.getLocknum().equals(LockerNum)) {
                        //이용중 or 예약중인 보관함이라면 catch
                        if (!locker.getUse().equals("0"))
                            throw new IllegalAccessException();
                    }
                }

                //아무 문제 없다면 비밀번호 변경 프롬프트로 이동
                flow = 2;
                break;


            } catch (IllegalArgumentException e) { //나머지 입력 예외 처리
                System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
            } catch (IllegalAccessException e) {
                System.out.println("이용 중인 보관함입니다. 다른 보관함을 선택해주세요.\n\n");
            }

        }

        if (flow == 2) {
            while (true) {
                //비밀번호 입력하시오 프롬프트 출력
                System.out.println(pwd_prompt1);

                //비밀번호 입력
                System.out.print(">>");
                LockerPwd = String.valueOf(sc.next());
                sc.nextLine();

                try {
                    //숫자 입력이 아닌 경우
                    if (!isNumeric(LockerPwd))
                        throw new IllegalArgumentException();

                    //4자리가 아닌 경우
                    if (LockerPwd.length() != 4)
                        throw new IllegalArgumentException();

                    //아무 문제 없는 경우 결제 창으로 이동
                    flow = 3;
                    break;

                } catch (IllegalArgumentException e) {
                    System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
                }
            }
        }

        if (flow == 3) {
            while (true) {
                //storage_payment_prompt 출력
                System.out.print(StoragePaymentPrompt(LockerNumber));
                String yn = String.valueOf(sc.next());
                sc.nextLine();

                try {
                    // Y or y or N or n 말고 다른 것을 입력한 경우
                    if (!(Objects.equals(yn, "Y") || Objects.equals(yn, "y") ||
                          Objects.equals(yn, "N") || Objects.equals(yn, "n")))
                        throw new IllegalArgumentException();

                    //N or n 입력한 경우
                    if (Objects.equals(yn, "N") || Objects.equals(yn, "n")) {
                        System.out.println("결제를 취소하셨습니다.");
                        flow = 1; //flow 값 초기화

                        //수정
                        //회원 유무에 따라 돌아갈 메뉴 결정
                        if(isMembership) {
                            System.out.println("\n\nmenu2.1로 돌아가기\n");
                            Menu_Mem();
                        } else {
                            System.out.println("\n\nmenu2.2로 돌아가기\n");
                            Menu_Nonmem();
                        }
                        break;
                    }

                    if (Objects.equals(yn, "Y") || Objects.equals(yn, "y")) {

                        //locker 설정값 변경하고 Storage 종료
                        Iterator<Locker> iterator = LockerList.iterator();
                        while (iterator.hasNext()) {
                            Locker locker = iterator.next();
                            if (locker.getLocknum().equals(LockerNum)) {
                                locker.setUse("1");
                                locker.setDate(Main.currentTimeString);
                            }
                        }

                        //수정
                        //보관함 비밀번호 저장도 해야한다
                        if(isMembership) {
                            memUser.setLocknum(LockerNum);
                            memUser.setLockPW(LockerPwd);
                            mem.put(loguser, memUser);
                        }else {
                            nonmem.put(LockerNum, new User(LockerNum, LockerPwd));
                        }
                        //locker.setLockpwd(LockerPwd);
                        //현재 date UserManager로부터 받아와야 함
                        //locker.setDate();

                        System.out.println("결제가 완료되었습니다.");
                        System.out.println("보관함 이용 등록이 완료되었습니다.\n");

                        //locker 데이터 Locker.txt 파일에 저장
                        LockerFileWrite();

                        break;
                    }

                } catch (IllegalArgumentException e) {
                    System.out.println("올바른 입력이 아닙니다.");
                    System.out.println("다시 한번 입력해주세요.\n");
                }
            }
        }


    }

    public static void logout() {
        return;
    }

    // 오늘 날짜 입력 시 Locker 데이터 수정 메소드 (날짜는 지났는데 예약 확정이 안된 이용 내역 삭제)
    public void deleteLockerBeforeDate(Date newDate) {

        String filename = "Locker.txt";

        ArrayList<String> lockersToDelete = new ArrayList<>();
        ArrayList<Locker> tmpLockerList = new ArrayList<>();

        try (Scanner scan = new Scanner(new File(filename))) {
            while (scan.hasNextLine()) {
                String str = scan.nextLine();

                String[] temp = str.split(" ");

                //이용 중인 보관함이 아니라서 날짜 부분이 "-" 일 경우
                if(temp[3].length() <= 1)
                {
                    tmpLockerList.add(new Locker(temp[0].trim(), temp[1].trim(), temp[2].trim(), temp[3].trim(), temp[4].trim()));//저장구조에 기존 locker정보 저장
                    continue;
                }

                //예약 확정된 and 사용중인 보관내역의 경우
                if(temp[2].equals("1"))
                {
                    tmpLockerList.add(new Locker(temp[0].trim(), temp[1].trim(), temp[2].trim(), temp[3].trim(), temp[4].trim()));//저장구조에 기존 locker정보 저장
                    continue;
                }

                // 날짜만 가져와 비교
                Date lockerDate;
                String dateStr = temp[3];
                System.out.println(dateStr);
                String[] dateStrTemp = new String[4];
                int num = 0;

                // 년, 월, 일, 시간 4구간으로 잘라서 배열에 저장
                for(int i=0; i<4; i++) {
                    if(i==0)
                    {
                        dateStrTemp[i] = dateStr.substring(num, num+4);
                        num += 4;
                    }
                    else
                    {
                        dateStrTemp[i] = dateStr.substring(num, num+2);
                        num += 2;
                    }
                }

                // 배열에 저장한 숫자 이용해서 날짜 객체 생성
                Calendar oldCalendar = new GregorianCalendar(Integer.parseInt(dateStrTemp[0]),
                        Integer.parseInt(dateStrTemp[1])-1, Integer.parseInt(dateStrTemp[2]));

                //예약 내역이라서 2시간을 더한 값으로 날짜 비교를 해야함
                oldCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dateStrTemp[3])+2);
                oldCalendar.set(Calendar.MINUTE, 0);
                oldCalendar.set(Calendar.SECOND, 0);
                lockerDate = oldCalendar.getTime();

                // 예약 날짜가 입력 받은 날보다 앞일 경우에만 저장
                if(!(lockerDate.before(newDate))){
                    tmpLockerList.add(new Locker(temp[0].trim(), temp[1].trim(), temp[2].trim(), temp[3].trim(), temp[4].trim()));//저장구조에 기존 locker정보 저장
                } else
                {
                    tmpLockerList.add(new Locker(temp[0].trim(), temp[1].trim(), "0", "-", "0"));//저장구조에 보관/예약 정보를 수정해서 locker정보 저장
                    lockersToDelete.add(temp[0]);

                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("파일 입력이 잘못되었습니다.");
        }

        UserManager tmpUserManager = new UserManager(true);
        tmpUserManager.deleteUserBeforeDate(lockersToDelete);
        dateLockerFileWrite(tmpLockerList);
    }

    public static Date StringToDate(String str){

        // 년, 월, 일, 시간 4구간으로 잘라서 배열에 저장
        String[] temp = new String[4];
        int num = 0;
        for(int i=0; i<4; i++) {
            if(i==0) //yyyy
            {
                temp[i] = str.substring(num, num+4);
                num += 4;
            }
            else //mm, dd, tt
            {
                temp[i] = str.substring(num, num+2);
                num += 2;
            }
        }

        Calendar cal = new GregorianCalendar(Integer.parseInt(temp[0]),
                Integer.parseInt(temp[1])-1, Integer.parseInt(temp[2]));
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(temp[3]));
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date date = cal.getTime();
        return date;
    }

    //문자열이 숫자인지 확인하는 메서드
    public boolean isNumeric(String strNum) {

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