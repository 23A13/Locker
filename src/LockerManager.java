import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;

import static java.lang.Integer.*;
import static java.lang.System.exit;

public class LockerManager {

    //class 선언
    static Scanner sc = new Scanner(System.in);
    static Locker locker = new Locker();
    UserManager u = new UserManager();

    static ArrayList<Locker> LockerList = new ArrayList<>(); //Locker정보 저장 구조

    //로그인한 사용자 아이디
    String loguser;
    User memUser; //유저 아이디로 해당되는 user 찾기
    static int count = 0;
    static int Icount = 0;

    //Constructor
    public LockerManager() {

    }

    public LockerManager(String loguser) {
        this.loguser = loguser;
        memUser = u.memMap.get(loguser);
    }


    //안내문구들
    static String tariff = """
            ———————————————요금표—————————————————\s
            기본 4시간\s
            S : 2000원 / M : 3000원 / L : 4000원\s
            시간당 추가요금\s
            회원 - S : 500원 / M : 800원 / L : 1000원\s
            비회원 - S : 1000원 / M : 1600원 / L : 2000원\s
            —————————————————————————————————————\s
                            
            ———————————예약 서비스 이용 시 주의사항——————
            * 예약 시 4시간 선결제 후 보관함에 도착하여 물품을 보관해주세요.\s
            * 예약 후 2시간 내로 물품 보관이 완료되지 않을 경우, 예약이 자동 취소됩니다. (환불 불가)
            * 기본 4시간 초과 시 수거하는 시점까지 자동으로 시간 당 추가 요금이 부과됩니다. (추가요금은 요금표 참고)
            * 추가 요금은 보관함 수거 시 부과됩니다.\s
            * 결제 수단은 카드만 사용 가능합니다.\s
            * 이용 중인 보관함은 예약이 불가합니다.\s
            * 예약은 보관함 한 개만 가능합니다.\s
            * 이미 보관함을 이용 중이라면 예약이 불가합니다.\s
            * 이전 이용에서 회원이 기본 4시간 초과 후 수거를 했다면, 초과된 시간의 2배 동안 예약이 불가합니다.\s
            ————————————————————————————————————\s
            이용하실 보관함의 번호를 입력하세요.\s
                            
            * 이전 메뉴로 돌아가려면 Q 또는 q를 입력하세요.\s
            ————————————————————————————————————————\s """;

    //pwd_prompt1
    String pwd_prompt1 = "\n사용하실 보관함 비밀번호(PIN) 네자리를 입력하세요.";

    //프로그램 최초 시작 시 locker 데이터 txt파일로부터 불러오는 함수
    public void LockerFileInput() {
        if(Icount == 0) {
            String filename = "../Locker/Locker.txt";
            try (Scanner scan = new Scanner(new File(filename))) {
                while (scan.hasNextLine()) {
                    String str = scan.nextLine();
                    String[] temp = str.split(" ");
                    LockerList.add(new Locker(temp[0].trim(), temp[1].trim(), temp[2].trim(), temp[3].trim(), temp[4].trim()
                            , temp[5].trim(), temp[6].trim()));//최초로 저장구조에 locker정보 저장
                }
            } catch (FileNotFoundException e) {
                System.out.println("파일 입력이 잘못되었습니다.");
            }
        }
    }

    //프로그램 종료 시 locker 데이터 txt파일에 저장하는 함수
    public void LockerFileWrite() {
        try {
            File file = new File("../Locker/Locker.txt");
            if (!file.exists()) {
                System.out.println("파일경로를 다시 확인하세요.");
            } else {
                FileWriter writer = new FileWriter(file, false);//기존 내용 없애고 쓰려면 false
                for (int i = 0; i < LockerList.size(); i++) {
                    writer.write(LockerList.get(i).locknum + " " + LockerList.get(i).locksize + " " + LockerList.get(i).use + " " + LockerList.get(i).date
                                 + " " + LockerList.get(i).confirmbook + " "
                                 + LockerList.get(i).closeddatestart + " " + LockerList.get(i).closeddatefinish + "\n");
                    writer.flush();
                }
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //날짜시간 입력 시 무효 예약 내역 수정용
    public void dateLockerFileWrite(ArrayList<Locker> List) {
        try {
            File file = new File("../Locker/Locker.txt");
            if (!file.exists()) {
                System.out.println("파일경로를 다시 확인하세요.");
            } else {
                FileWriter writer = new FileWriter(file, false);//기존 내용 없애고 쓰려면 false
                for (int i = 0; i < List.size(); i++) {
                    writer.write(List.get(i).locknum + " " + List.get(i).locksize + " " + List.get(i).use + " " + List.get(i).date + " "
                            + List.get(i).confirmbook + " "
                            + List.get(i).closeddatestart + " " + List.get(i).closeddatefinish + "\n");
                    writer.flush();
                }
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //회원 메뉴
    public void Menu_Mem() {

        //LockerFileInput();
        if (count == 0) LockerFileInput();
        count++;
        Icount++; //@

        String menu = """
                \n——MENU——\s
                1. 보관하기\s
                2. 수거하기\s
                3. 예약하기\s
                4. 예약 확인 및 예약 확정\s
                5. 로그아웃\s
                6. 종료
                ———————                             
                """;

        System.out.print(menu);

        int number = 0;

        while (true) {

            try {
                System.out.print(">> ");
                number = sc.nextInt();
                sc.nextLine();

                if (number < 1 || number > 6) throw new InputMismatchException();

                //올바른 입력시
                break;

            } catch (InputMismatchException e) {
                System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
                sc.nextLine();
            }

        }

        System.out.println();

        switch (number) {
            case 1:
                Storage(true);
                break;
            case 2:
                Pickup(true);
                break;
            case 3:
                Booking();
                break;
            case 4:
                ConfirmBooking();
                //ExitWrite();
                break;
            case 5:
                logout();
                break;
            case 6:
                Exit(true);
                break;
            default:
                break;
        }

    }

    //비회원메뉴
    public void Menu_Nonmem() {
        if (count == 0) LockerFileInput();
        //LockerFileInput();
        count++; //@
        Icount++; //@

        String menu = """
                \n——MENU——\s
                1. 보관하기\s
                2. 수거하기\s
                3. 이전 메뉴로 돌아가기\s
                4. 종료
                ———————
                """;

        System.out.print(menu);

        int number = 0;

        while (true) {

            try {
                System.out.print(">> ");
                number = sc.nextInt();
                sc.nextLine();

                if (number < 1 || number > 4) throw new InputMismatchException();

                //올바른 입력시
                break;

            } catch (InputMismatchException e) {
                System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
                sc.nextLine();
            }

        }


        switch (number) {
            case 1:
                Storage(false);
                break;
            case 2:
                Pickup(false);
                break;
            case 3:
                logout();
                break;
            case 4:
                Exit(false);
                break;
            default:
                break;
        }
    }

    private void Pickup(boolean isLogin) {
        //보관함
        Locker.print();


        String notice2 = """
                이용하신 보관함의 번호를 입력하세요. (ex: 01)\s
                \s
                * 이전 메뉴로 돌아가려면 Q 또는 q를 입력하세요.\s
                ——————————————————————————————————————————————""";

        //보관함 번호 입력받기
        int LockerNumber = 0;
        Locker targetLocker = null;
        int target = 0;
        while (true) {
            //notice 2 출력
            System.out.println(notice2);

            //보관함 번호 입력받기
            System.out.print(">> ");

            try {
                String LockerNum = String.valueOf(sc.next());
                sc.nextLine();

                //Q,q 처리
                if (Objects.equals(LockerNum, "Q") || Objects.equals(LockerNum, "q")) {
//                    count++;
                    if (isLogin) Menu_Mem();
                    else Menu_Nonmem();
                    //ExitWrite();
                    return;
                }

                //형식 예외 처리(00, 01, 02 등으로 입력해야함)
                if (LockerNum.length() != 2)
                    throw new IllegalArgumentException();

                //범위 예외 처리(01~99)
                LockerNumber = Integer.parseInt(LockerNum);
                if (LockerNumber < 1 || LockerNumber > 99)
                    throw new IllegalArgumentException();

                //LockerList에서 보관함 찾기
                int i=0;
                for (Locker l : LockerList) {
                    if (Integer.parseInt(l.locknum) == LockerNumber) {
                        targetLocker = l;
                        target = i;
                    }
                    i++;
                }

                //존재하지 않는 보관함의 번호를 입력한 경우 처리
                if (targetLocker == null) {
                    System.out.println("해당 번호의 보관함이 존재하지 않습니다.\n");
                    throw new IllegalAccessException();
                }

                //사용중이지 않은 보관함을 선택했을 때 처리
                if (Integer.parseInt(targetLocker.use) != 1) {
                    System.out.println("이용 중이지 않은 보관함입니다.");
                    System.out.println("보관함 번호를 확인해주세요.\n");
                    throw new IllegalAccessException();
                }
                break;

            } catch (IllegalArgumentException e) { //나머지 입력 예외 처리
                System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");

            } catch (IllegalAccessException e) {

            }

        }
        //test확인용출력-나중에삭제
//        System.out.print("선택한 보관함 번호 : ");
//        System.out.println(LockerNumber);


        //해당 번호(LockerNum)의 보관함 정보 찾기

        //mem과 nonmem에서 찾기 (회원<User> 정보 저장구조)
        String targetKey = null;
        boolean isMemLocker = false;

        for (String id : u.memMap.keySet()) {
            if (!u.memMap.get(id).locknum.equals("-")) {
                if (LockerNumber == Integer.parseInt(u.memMap.get(id).locknum)) {
                    targetKey = id;

                    //test확인용출력-나중에삭제
//                    System.out.println("mem map에서 찾기 성공");
//                    System.out.println(u.memMap.get(id));
                    break;
                }
            }
        }
        if (targetKey != null) {
            isMemLocker = true;
        } else {
            isMemLocker = false;
            for (String lnum : u.nonmemMap.keySet()) {
                if (Integer.parseInt(u.nonmemMap.get(lnum).locknum) == LockerNumber) {
                    targetKey = lnum;

                    //test확인용출력-나중에삭제
//                    System.out.println("nonmem map에서 찾기 성공");
//                    System.out.println(u.nonmemMap.get(lnum));
                    break;
                }
            }
        }


        //보관함 비밀번호 입력
        String pwd_prompt2 = "사용하신 보관함의 비밀번호(PIN) 네자리를 입력하세요.";
        boolean pwdCheck2 = pwdCheck(pwd_prompt2, isMemLocker, targetKey, 0);
        if (!pwdCheck2) { //보관함 비밀번호 입력 3회 실패시
//            count++;
            if (isLogin) Menu_Mem(); //이전 메뉴로 돌아가기
            else Menu_Nonmem();
            //System.exit(0);
        }


        //보관함 비밀번호가 올바른 경우

        //시간 차이 구하기
        Date currentTime = StringToDate(Main.currentTimeString);
        Date startTime = StringToDate(LockerList.get(target).date);
        long timeDiffMillis = currentTime.getTime() - startTime.getTime();
        int timeDiffMinutes = (int) (timeDiffMillis / (60 * 1000));
        int timeDiffHours = (int) (Math.ceil((double) timeDiffMillis / (60 * 60 * 1000)));

        //추가 결제가 필요한 경우 : 현재시간 = 예약시간+4시간 초과인 경우
        if (timeDiffMinutes > 4 * 60) {
            while (true) {
                String timeShowPrompt = "\n기본 이용 시간에서 " + Integer.toString(timeDiffHours - 4) + "시간이 초과됐습니다.\n";
                if (isMemLocker) { //"회원"이 사용했던 보관함인경우
                    timeShowPrompt += "수거 완료시, " + u.memMap.get(targetKey).memberID + "님은 " + Integer.toString((timeDiffHours - 4) * 2) + "시간 동안 예약이 불가합니다.\n";
                } else {
                    timeShowPrompt += "비회원용 추가 금액이 적용됩니다.\n";
                }
                System.out.println(timeShowPrompt);

                Print_AddPayPrompt(timeDiffHours, target, isMemLocker);
                String yn = String.valueOf(sc.next());
                sc.nextLine();
                try {
                    // Y or y 말고 다른 것을 입력한 경우
                    if (!(Objects.equals(yn, "Y") || Objects.equals(yn, "y")))
                        throw new IllegalArgumentException();
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
                }
            }
            System.out.println("결제가 완료되었습니다.");


            if (isMemLocker) { //"회원"이 사용했던 보관함인경우 - user객체에 cannotUntil 날짜 저장
                Date limitDate = new Date(currentTime.getTime() + 2 * (timeDiffHours - 4) * 3600000);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                u.memMap.get(targetKey).cannotUntil = sdf.format(limitDate);
            }
        }


        //수거완료
        String pwd_prompt3 = "물품 수거가 가능합니다. \n" +
                             "\n* 보관하신 물품 수거 후에 수거완료를 위해 반드시 보관함 비밀번호(PIN) 4자리를 입력해주세요.\n" +
                             "수거완료 처리가 되지 않은 이용 건은 자동 초과 요금이 부과됩니다. \n";

        pwdCheck(pwd_prompt3, isMemLocker, targetKey, 0);


        //수거완료한 보관함 정보 삭제 & File Write
        LockerList.get(target).use = "0";
        LockerList.get(target).date = "-";
        LockerList.get(target).confirmbook = "0";
        if (isMemLocker) {
            u.memMap.get(targetKey).locknum = "-";
            u.memMap.get(targetKey).lockPW = "-";
        } else {
            u.nonmemMap.remove(targetKey);
        }


        System.out.println("물품 수거가 완료되었습니다.");
        System.out.println("이용해주셔서 감사합니다.");


        ExitWrite();


    }

    //예약 메소드
    public void Booking() {


        //기존 예약 내역 확인
        if (!u.memMap.get(loguser).locknum.equals("-")) {
            System.out.println("이미 예약 내역이 존재합니다.\n\n");
//            count++;
            Menu_Mem();
            //System.exit(0);
        }

        //이전 이용 기본 4시간 이산 초과했을 경우 확인
        if (!(u.memMap.get(loguser).cannotUntil).equals("-")) {
            int cannotUntil = Integer.parseInt(u.memMap.get(loguser).cannotUntil);
            String cantUntil = u.memMap.get(loguser).cannotUntil;
            int currentTime = Integer.parseInt(Main.currentTimeString);
            if (cannotUntil >= currentTime) {
                System.out.println("지난 예약 건 초과 보관으로 " + u.memMap.get(loguser).cannotUntil + "까지 보관함 이용이 불가합니다.\n\n");
                Menu_Mem();
                return;
            }

        }


        Locker.print();

        //입력
        int LockerNumber = 0;
        String LockerNum = " ";

        //흐름에 따라 flow 값 변경. 보관함 선택 (1) -> 사이즈 별 안내, 결제 확인 (2) -> 결제 (3)
        int flow = 1;
        //보관함 번호 입력
        while (true) {
            System.out.println(tariff);

            //입력
            System.out.print(">> ");
            LockerNum = String.valueOf(sc.next());

            try {
                //Q,q 처리
                if (Objects.equals(LockerNum, "Q") || Objects.equals(LockerNum, "q")) {
//                    count++;
                    Menu_Mem();
                    break;
                }

                //형식 예외 처리(00, 01, 02 등으로 입력)
                if (LockerNum.length() != 2) throw new IllegalArgumentException();

                //범위 예외 처리(01~16)
                LockerNumber = parseInt(LockerNum);
                if (LockerNumber < 1 || LockerNumber > 16) throw new IllegalArgumentException();

                //예약 확인 처리
                boolean bookingcheck = false;
                for (int i = 0; i < LockerList.size(); i++) {
                    if (parseInt(LockerList.get(i).locknum) == LockerNumber) {
                        if (parseInt(LockerList.get(i).use) == 1 || parseInt(LockerList.get(i).use) == 2) {
                            throw new IllegalAccessException();
                        }
                    }
                }
                //임시폐쇄
                for (int i = 0; i < LockerList.size(); i++) {
                    if (parseInt(LockerList.get(i).locknum) == LockerNumber) {
                        //임시 폐쇄 중
                        if (parseInt(LockerList.get(i).use) == 4)
                            throw new IllegalStateException();

                        //임시폐쇄 예정
                        if (parseInt(LockerList.get(i).use) == 3) {
                            Date currentTime = LockerManager.StringToDate(Main.currentTimeString);
                            Date startTime = LockerManager.StringToDate(LockerManager.LockerList.get(i).closeddatestart);
                            long timeDiffMillis = startTime.getTime() - currentTime.getTime();
                            int timeDiffMinutes = (int) (timeDiffMillis / (60 * 1000));

                            if(timeDiffMinutes < 12 * 60) //12시간보다 작을때 예약불가
                                throw new IllegalStateException();
                        }
                    }
                }


                //아무 문제 없다면 비민번호 창으로 이동
                flow = 2;
                break;

            } catch (IllegalArgumentException e) { //나머지 입력 예외 처리
                System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");

            } catch (IllegalAccessException e) {
                System.out.println("이용 중인 보관함입니다.\n");
            } catch (IllegalStateException e) {
                System.out.println("임시폐쇄 예정이거나 임시폐쇄중인 보관함이므로 사용하실 수 없습니다.\n");
            }
        }
        //비밀번호 입력
        String LockerPwd = " ";
        if (flow == 2) {
            while (true) {
                //비밀번호 입력하시오 프롬프트 출력
                System.out.println(pwd_prompt1);

                //비밀번호 입력
                System.out.print(">> ");
                LockerPwd = String.valueOf(sc.next());
                sc.nextLine();

                try {
                    //숫자 입력이 아닌 경우
                    if (!isNumeric(LockerPwd))
                        throw new IllegalArgumentException();

                    //4자리가 아닌 경우
                    if (LockerPwd.length() != 4)
                        throw new IllegalArgumentException();

                    //아무 문제 없는 경우 결제 확인 창으로 이동
                    flow = 3;
                    break;

                } catch (IllegalArgumentException e) {
                    System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
                }
            }
        }

        //결제 확인 입력
        String payment = "";
        if (flow == 3) {
            while (true) {
                System.out.print(StoragePaymentPrompt(LockerNumber));
                payment = String.valueOf(sc.next());
                sc.nextLine();

                try {
                    if (!(payment.equals("Y") || payment.equals("N") || payment.equals("y") || payment.equals("n")))
                        throw new IllegalArgumentException();

                    //아무 문제 없다면 결제 알림 창으로 이동
                    flow = 4;
                    break;
                } catch (Exception e) {
                    System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
                }

            }

        }
        //마지막
        if (flow == 4) {
            if (payment.equals("Y") || payment.equals("y")) {
                System.out.println("\n보관함 예약이 완료되었습니다. ");
                System.out.println("* 2시간 내로 물품 보관(예약 확정)을 완료해주시기 바랍니다.");
                System.out.println("(물품 보관이 완료 되지 않을 경우 예약 자동 취소 및 환불 불가)");

                System.out.println("");


                //Locker 사용여부 예약중(2), 보관함 크기 저장
                String lockersize;
                if (LockerNumber <= 8) lockersize = "0";
                else if (LockerNumber <= 12) lockersize = "1";
                else lockersize = "2";

                for (int i = 0; i < LockerList.size(); i++) {
                    if (LockerList.get(i).locknum.equals(LockerNum)) {
                        LockerList.get(i).use = "2";
                        LockerList.get(i).locksize = lockersize;
                        LockerList.get(i).date = Main.currentTimeString;
                    }
                }

                //User 회원 lockernumber 저장
                u.memMap.get(loguser).locknum = LockerNum;
                u.memMap.get(loguser).lockPW = LockerPwd;
                ExitWrite();
            } else {
//                count++;
                Menu_Mem();
                //System.exit(0);
            }
        }


    }


    public static String StoragePaymentPrompt(int lockerNum) {
        /*
        storage_payment_prompt 를 출력하는 메서드
         */
        String strLockerNum = String.valueOf(lockerNum);
        if (lockerNum < 10) {
            strLockerNum = "0" + strLockerNum;
        }
        //보관함 크기에 따른 금액 차등
        int payment = 0;
        if (lockerNum <= 8)
            payment = 2000;
        else if (lockerNum <= 12) {
            payment = 3000;
        } else
            payment = 4000;

        String str = "\n<" + strLockerNum + ">번 보관함 선택하셨습니다. \n" +
                     "기본(4시간) 금액 <" + payment + ">원 결제하시겠습니까? (y/n) \n" +
                     "\n" +
                     "* 초과 시간에 따른 요금은 수거 시 추가결제됩니다.\n" +
                     "—————————————————————————— \n" +
                     ">> ";

        return str;
    }

    private void Storage(boolean isLogin) {

        //입력받은 보관함 번호
        int LockerNumber = 0;   //정수형
        String LockerNum = null;    //스트링
        //보관함 비밀번호
        String LockerPwd = null;
        //흐름에 따라 flow 값 변경. 보관함 선택 (1) -> 비밀번호 입력 (2) -> 결제 (3)
        int flow = 1;


        //보관함 출력
        Locker.print();

        while (true) {
            //요금표 출력
            System.out.println(tariff);

            //보관함 번호 입력
            System.out.print(">> ");
            LockerNum = String.valueOf(sc.next());
            sc.nextLine();

            try {
                //Q or q 입력 시 menu2.1로 돌아가기
                if (Objects.equals(LockerNum, "Q") || Objects.equals(LockerNum, "q")) {
                    //수정
                    //회원 유무에 따라 돌아갈 메뉴 결정
                    if (isLogin) {
                        //System.out.println("\n\nmenu2.1로 돌아가기\n");
//                        count++;
                        Menu_Mem();
                    } else {
                        //System.out.println("\n\nmenu2.2로 돌아가기\n");
//                        count++;
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
                        if (locker.getUse().equals("1") || locker.getUse().equals("2")) {
                            System.out.println("이용 중인 보관함입니다. 다른 보관함을 선택해주세요.");
                            throw new IllegalAccessException();
                        }

                        //임시폐쇄중 or 임시폐쇄 예정 이라면 catch
                        if(!locker.closeddatestart.equals("-")) { //@
                            Date currentTime = LockerManager.StringToDate(Main.currentTimeString);
                            Date startTime = LockerManager.StringToDate(locker.closeddatestart);
                            long timeDiffMillis = currentTime.getTime() - startTime.getTime();
                            int timeDiffMinutes = (int) (timeDiffMillis / (60 * 1000));

                            if (timeDiffMinutes < 10 * 60) {//10시간보다 작을때 예약불가
                                System.out.println("임시폐쇄 예정이거나 임시폐쇄중인 보관함이므로 사용하실 수 없습니다.");
                                throw new IllegalStateException();
                            }
                        }
                    }
                }

                //아무 문제 없다면 비밀번호 변경 프롬프트로 이동
                flow = 2;
                break;

            } catch (IllegalArgumentException e) { //나머지 입력 예외 처리
                System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
            } catch (IllegalAccessException e) {
                System.out.println();
            }
        }

        if (flow == 2) {
            while (true) {
                //비밀번호 입력하시오 프롬프트 출력
                System.out.println(pwd_prompt1);

                //비밀번호 입력
                System.out.print(">> ");
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
                        if (isLogin) {
//                            System.out.println("\n\nmenu2.1로 돌아가기\n");
//                            count++;
                            Menu_Mem();
                        } else {
//                            System.out.println("\n\nmenu2.2로 돌아가기\n");
//                            count++;
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
                                locker.setConfirmbook("1");
                            }
                        }

                        //수정
                        //보관함 비밀번호 저장도 해야한다
                        if (isLogin) {
                            memUser.setLocknum(LockerNum);
                            memUser.setLockPW(LockerPwd);
                            u.memMap.put(loguser, memUser);
                        } else {
                            u.nonmemMap.put(LockerNum, new User(LockerNum, LockerPwd));
                        }
                        //locker.setLockpwd(LockerPwd);
                        //현재 date UserManager로부터 받아와야 함
                        //locker.setDate();

                        System.out.println("결제가 완료되었습니다.");
                        System.out.println("보관함 이용 등록이 완료되었습니다.\n");

                        //locker 데이터 Locker.txt 파일에 저장
                        //LockerFileWrite();

                        //break;
                        ExitWrite();
                    }

                } catch (IllegalArgumentException e) {
                    System.out.println("올바른 입력이 아닙니다.");
                    System.out.println("다시 한번 입력해주세요.\n");
                }
            }
        }


    }

    // 오늘 날짜 입력 시 Locker 데이터 수정 메소드 (날짜는 지났는데 예약 확정이 안된 이용 내역 삭제)
    public void deleteLockerBeforeDate(Date newDate) {

        String filename = "../Locker/Locker.txt";

        ArrayList<String> lockersToDelete = new ArrayList<>();
        ArrayList<Locker> tmpLockerList = new ArrayList<>();

        try (Scanner scan = new Scanner(new File(filename))) {
            while (scan.hasNextLine()) {
                String str = scan.nextLine();

                String[] temp = str.split(" ");

                // 임시 폐쇄 중인 보관함 확인
                if(temp[2].equals("4"))
                {
                    // 임시 폐쇄 중인 보관함의 임시 폐쇄 종료 날짜 확인
                    String tmpEnd = temp[6].trim();
                    Date closureEndTime = StringToDate(tmpEnd);

                    // 임시 폐쇄 종료 날짜가 새로 입력 받은 날짜보다 이후일 경우
                    if (closureEndTime.after(newDate))
                    {
                        tmpLockerList.add(new Locker(temp[0].trim(), temp[1].trim(), temp[2].trim(), temp[3].trim(),
                                temp[4].trim(), temp[5].trim(), temp[6].trim()));
                    } else if(closureEndTime.equals(newDate)){
                        tmpLockerList.add(new Locker(temp[0].trim(), temp[1].trim(), temp[2].trim(), temp[3].trim(),
                                temp[4].trim(), temp[5].trim(), temp[6].trim()));
                    } else // 임시 폐쇄 종료 날짜가 새로 입력 받은 날짜보다 전일 경우 (임시 폐쇄 끝남)
                    {
                        tmpLockerList.add(new Locker(temp[0].trim(), temp[1].trim(), "0", temp[3].trim(),
                                temp[4].trim()));
                    }
                    continue;
                }

                // 임시 폐쇄 예정인 보관함 확인
                if(temp[2].equals("3"))
                {
                    // 임시 폐쇄 예정인 보관함의 임시 폐쇄 시작 & 종료 날짜 확인
                    String tmpStart = temp[5].trim();
                    String tmpEnd = temp[6].trim();

                    Date closureStartTime = StringToDate(tmpStart);
                    Date closureEndTime = StringToDate(tmpEnd);

                    // 임시 폐쇄 종료 날짜가 새로 입력 받은 날짜보다 이후일 경우
                    if (closureEndTime.after(newDate))
                    {
                        //  임시 폐쇄 시작 날짜가 새로 입력 받은 날짜보다 이후일 경우
                        if (closureStartTime.after(newDate))
                        {
                            tmpLockerList.add(new Locker(temp[0].trim(), temp[1].trim(), temp[2].trim(), temp[3].trim(),
                                    temp[4].trim(), temp[5].trim(), temp[6].trim()));
                        } else if(closureStartTime.equals(newDate))
                        {
                            tmpLockerList.add(new Locker(temp[0].trim(), temp[1].trim(), temp[2].trim(), temp[3].trim(),
                                    temp[4].trim(), temp[5].trim(), temp[6].trim()));
                        } else  //  임시 폐쇄 시작 날짜가 새로 입력 받은 날짜보다 전일 경우 (임시 폐쇄 시작)
                        {
                            tmpLockerList.add(new Locker(temp[0].trim(), temp[1].trim(), "4", temp[3].trim(),
                                    temp[4].trim(), temp[5].trim(), temp[6].trim()));
                        }
                    } else if(closureEndTime.equals(newDate)){
                        // 임시 폐쇄 종료 날짜가 새로 입력 받은 날짜와 같음 -> 임시 폐쇄 중으로 판단
                        tmpLockerList.add(new Locker(temp[0].trim(), temp[1].trim(), "4", temp[3].trim(),
                                temp[4].trim(), temp[5].trim(), temp[6].trim()));
                    } else // 임시 폐쇄 종료 날짜가 새로 입력 받은 날짜보다 전일 경우 (임시 폐쇄 끝남)
                    {
                        tmpLockerList.add(new Locker(temp[0].trim(), temp[1].trim(), "0", temp[3].trim(),
                                temp[4].trim()));
                    }

                    continue;
                }

                //이용 중인 보관함이 아니라서 날짜 부분이 "-" 일 경우
                if (temp[3].length() <= 1) {
                    tmpLockerList.add(new Locker(temp[0].trim(), temp[1].trim(), temp[2].trim(), temp[3].trim(), temp[4].trim()));//저장구조에 기존 locker정보 저장
                    continue;
                }

                //예약 확정된 and 사용 중인 보관 내역의 경우
                if (temp[2].equals("1")) {
                    tmpLockerList.add(new Locker(temp[0].trim(), temp[1].trim(), temp[2].trim(), temp[3].trim(), temp[4].trim()));//저장구조에 기존 locker정보 저장
                    continue;
                }

                // 날짜만 가져와 비교
                Date lockerDate;
                String dateStr = temp[3];
                String[] dateStrTemp = new String[5];
                int num = 0;

                // 년, 월, 일, 시간, 분 5구간으로 잘라서 배열에 저장
                for (int i = 0; i < 5; i++) {
                    if (i == 0) {
                        dateStrTemp[i] = dateStr.substring(num, num + 4);
                        num += 4;
                    } else {
                        dateStrTemp[i] = dateStr.substring(num, num + 2);
                        num += 2;
                    }
                }

                // 배열에 저장한 숫자 이용해서 날짜 객체 생성
                Calendar oldCalendar = new GregorianCalendar(Integer.parseInt(dateStrTemp[0]),
                        Integer.parseInt(dateStrTemp[1]) - 1, Integer.parseInt(dateStrTemp[2]));

                //예약 내역이라서 2시간을 더한 값으로 날짜 비교를 해야함
                oldCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dateStrTemp[3]));
                oldCalendar.set(Calendar.MINUTE, (Integer.parseInt(dateStrTemp[4], 10)));
                oldCalendar.set(Calendar.SECOND, 0);
                oldCalendar.set(Calendar.MILLISECOND, 0);
                oldCalendar.add(Calendar.HOUR_OF_DAY, 2);

                // 만약 다음 날로 넘어가면 날짜를 1일 증가시킴
                if (oldCalendar.get(Calendar.HOUR_OF_DAY) < 2) {
                    oldCalendar.add(Calendar.DAY_OF_MONTH, 1);
                }

                lockerDate = oldCalendar.getTime();

                // 예약 날짜가 입력 받은 날보다 앞이거나 같을 경우에만 저장
                if ((lockerDate.after(newDate))) {
                    tmpLockerList.add(new Locker(temp[0].trim(), temp[1].trim(), temp[2].trim(), temp[3].trim(), temp[4].trim()));//저장구조에 기존 locker정보 저장
                } else if (lockerDate.equals(newDate)) {
                    tmpLockerList.add(new Locker(temp[0].trim(), temp[1].trim(), temp[2].trim(), temp[3].trim(), temp[4].trim()));//저장구조에 기존 locker정보 저장
                } else {
                    tmpLockerList.add(new Locker(temp[0].trim(), temp[1].trim(), "0", "-", "0"));//저장구조에 보관/예약 정보를 수정해서 locker정보 저장
                    lockersToDelete.add(temp[0]);
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("파일 입력이 잘못되었습니다.");
        }

        UserManager tmpUserManager = new UserManager(true);
        tmpUserManager.deleteUserBeforeDate(lockersToDelete, newDate);
        dateLockerFileWrite(tmpLockerList);
    }

    public static Date StringToDate(String str) {

        // 년, 월, 일, 시간, 분 5구간으로 잘라서 배열에 저장
        String[] temp = new String[5];
        int num = 0;
        for (int i = 0; i < 5; i++) {
            if (i == 0) //yyyy
            {
                temp[i] = str.substring(num, num + 4);
                num += 4;
            } else //mm, dd, hh, mm
            {
                temp[i] = str.substring(num, num + 2);
                num += 2;
            }
        }

        Calendar cal = new GregorianCalendar(Integer.parseInt(temp[0]),
                Integer.parseInt(temp[1]) - 1, Integer.parseInt(temp[2]));
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(temp[3]));
        cal.set(Calendar.MINUTE, (Integer.parseInt(temp[4], 10)));
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

    public void Exit(boolean isLogin) {
        Scanner sc = new Scanner(System.in);
        System.out.print("종료하시려면 Y 또는 y를 입력해주세요 >> ");
        String exit = sc.next();
        if (Objects.equals(exit, "Y") || Objects.equals(exit, "y")) {
            System.out.println("프로그램을 종료합니다.");
            System.exit(0);
        } else {
//            count++;
            if (isLogin) Menu_Mem(); //이전 메뉴로 돌아가기
            else Menu_Nonmem();
            System.exit(0);
        }
    }

    public void ExitWrite() {
        u.UserFileWrite();
        LockerFileWrite();
        System.exit(0);
    }

    //보관함 비밀번호 입력받기
    /*
    return type : boolean : 비번입력 성공여부
    prompt : 출력할 prompt
    ismemLocker : 해당 Locker가 회원이용인지
    targetKey : mem/nonmem map에서 찾은 해당 사용내역 key값
    chacne : 기회 몇번, 0 : 무한루프로 (맞을때까지-수거완료에서 사용)
     */

    public boolean pwdCheck(String prompt, boolean ismemLocker, String targetKey, int chance) {
        int th = 0;
        String LockerPwd;

        while (true) {
            if (chance != 0) {
                if (th > chance) {
                    System.out.println("보관함 비밀번호를 " + chance + "회 틀리셨습니다. 메뉴로 돌아갑니다.");
                    return false;
                    //break;
                }
            }

            //보관함 비밀번호 입력
            System.out.println(prompt);
            System.out.print(">> ");
            th++;

            //보관함 비밀번호 입력받기
            LockerPwd = String.valueOf(sc.next());
            sc.nextLine();

            try {
                //숫자 입력이 아닌 경우
                if (!isNumeric(LockerPwd))
                    throw new IllegalArgumentException();

                //4자리가 아닌 경우
                if (LockerPwd.length() != 4)
                    throw new IllegalArgumentException();


                //보관함 비밀번호가 올바르지 않을 경우
                if (ismemLocker) {
                    if (!LockerPwd.equals(u.memMap.get(targetKey).lockPW))
                        throw new IllegalAccessException();
                } else {
                    if (!LockerPwd.equals(u.nonmemMap.get(targetKey).lockPW))
                        throw new IllegalAccessException();
                }

                return true;
                //break;

            } catch (IllegalArgumentException e) {
                System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
            } catch (IllegalAccessException e) {
                System.out.println("보관함 비밀번호가 올바르지 않습니다.");


            }
        }
    }


    private void Print_AddPayPrompt(int timeDiff, int target, boolean isMemLocker) {
        /*
        addtional_payment_prompt를 출력하는 메서드
         */
        int additional = 0;
        switch (Integer.parseInt(LockerList.get(target).locksize)) {
            case 0:
                additional = 500;
                break;
            case 1:
                additional = 800;
                break;
            case 2:
                additional = 1000;
                break;
        }
        if (!isMemLocker) //비회원일 경우 시간당 요금이 2배
            additional = additional * 2;


        String str = "초과 시간에 따른 추가 금액 결제를 진행합니다. \n" +
                     (timeDiff - 4) * additional + "원 \n" +
                     "결제하시려면 Y 또는 y를 입력해주세요. \n" +
                     "———————————————————————————\n" +
                     ">> ";

        System.out.print(str);

    }

    public void ConfirmBooking() {
        String str;//메뉴 입력(1.예약확정/ Q,q

        String password; //비밀번호 입력받는 변수

        String u_locknum = u.memMap.get(loguser).locknum;//로그인한 회원이 예약한 보관함 번호
        String u_payment = "0";//회원이 지불해야하는 금액
        String u_password = u.memMap.get(loguser).lockPW; //회원이 저장한 보관함 비밀번호
        Locker temL = null;

        if (u_locknum.equals("-")) {//예약한 보관함이 없다면
            System.out.println("예약 내역이 존재하지 않습니다.");// 출력 후 함수 종료-->변경: 이전 메뉴로 돌아가기
//            count++;
            Menu_Mem();
        } else {//예약된 내역이 있다면 함수 계속 진행

            //보관함 정보 가져옴
            int index = 0;
            for (int i = 0; i < LockerList.size(); i++) {//해당 락커 정보 저장
                if (LockerList.get(i).locknum.equals((u_locknum))) {
                    temL = new Locker(u_locknum, LockerList.get(i).locksize, LockerList.get(i).use, Main.currentTimeString, LockerList.get(i).confirmbook);
                    index = i;
                }
            }

            if (temL.confirmbook.equals("1")) { // 이미 확정된 예약건 처리+보관함 이미 이용중
                System.out.println("이미 예약이 확정되었거나 이용중인 보관함이 존재합니다.");  // 출력 후 함수 종료
//                count++;
                Menu_Mem();

            } else {
                //예외에 해당 사항 없다면 예약확정 처리 시작

                //가격측정
                int payment = 0;
                if (temL.locksize.equals("0")) {
                    payment = 2000;
                } else if (temL.locksize.equals("1")) {
                    payment = 3000;
                } else {
                    payment = 4000;
                }
                u_payment = Integer.toString(payment);//이거 4빼야되나????

                //예약확정
                int flag = 0; //flag==0이면 올바르지 않은 입력
                while (flag == 0) {
                    try {
                        System.out.println("-----예약 내역------");
                        System.out.println("보관함 번호 : " + u_locknum + "번"); //사용자의 사물함 객체 정보 받아야함
                        System.out.println("결제 금액: " + u_payment + "원"); //사용자의 사물함 객체 정보 받아야함
                        System.out.println("------------------");
                        System.out.println("1. 예약 확정");
                        System.out.print("\n\n* 이전 메뉴로 돌아가려면 Q 또는 q를 입력하세요.\n>> ");
                        //메인메뉴에 nextInt()하고 나서 추가하기@@@@@@@ github 에 push!!
                        str = sc.nextLine();
                        if (str.equals("Q") || str.equals("q")) {
                            flag = 1;
                        } else if (str.equals("1")) {
                            flag = 2;
                        } else {
                            //flag=0;
                            //System.out.println("1"); 확인용. 나중에 지우기
                            System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
                        }

                    } catch (InputMismatchException e) {
                        //flag=0;
                        //System.out.println("2"); 확인용. 나중에 지우기
                        System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
                    }
                }
                int flag_ = 5;//flag_ !=1 이면 올바르지 않은 입력
                if (flag == 1) {//이전 메뉴로 돌아감
//                    count++;
                    Menu_Mem();
                }
                if (flag == 2) {//예약확정하기->종료
                    while (flag_ != 1) {
                        try {
                            System.out.print("비밀번호(PIN) 4자리를 입력하세요 >> ");
                            password = sc.nextLine();
                            if (password.equals(u_password)) {
                                flag_ = 1;//예약확정 성공
                            } else {
                                flag_ = 2;//비밀번호가 올바르지 않은 경우
                                System.out.println("비밀번호(PIN)가 올바르지 않습니다.\n");
                            }

                        } catch (InputMismatchException e) {
                            flag_ = 0;//잘못된 입력을 받은 경우(문법적 오류)
                            sc.nextLine();
                            System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
                        }
                    }
                    System.out.println("예약이 확정되었습니다.");
                    LockerList.set(index, new Locker(temL.locknum, temL.locksize, "1", temL.date, "1"));
                    //System.out.println(LockerList.get(index).confirmbook);
                    //LockerList의 사용여부-2,예약확정-1로 변경

                    ExitWrite();
                }
                //flag==2작업마치면 프로그램 종료로
            }

        }

    }


    public static void logout() {
        return;
    }

}