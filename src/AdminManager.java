import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Lock;

import static java.lang.Integer.parseInt;

public class AdminManager {
    static Scanner sc = new Scanner(System.in);

    private final String pw = "admin1234";

    public AdminManager() {
    }

    static int count = 0;

    UserManager u = new UserManager();
    LockerManager l = new LockerManager();

    //보관함 용량 계산(근데 사이즈별 용량 S:2 M:3 L:4 맞나영이?---->(지은)이거 바꾸면 용량 관련 다 바꿔야함.
    int Capacity(String locksize){//보관함 용량을 더했을 때
        int capacity=0;
        for(int i=0;i<l.LockerList.size();i++){
            if(l.LockerList.get(i).locksize.equals("0")){//사이즈가 S
                capacity=capacity+2;
            }else if(l.LockerList.get(i).locksize.equals("1")){//사이즈 M
                capacity=capacity+3;
            }else{//사이즈 L
                capacity=capacity+4;
            }
        }
        if(locksize.equals("s")||locksize.equals("S")){
            capacity=capacity+2;
        }else if(locksize.equals("m")||locksize.equals("M")){
            capacity=capacity+3;
        }else if(locksize.equals("l")||locksize.equals("L")){
            capacity=capacity+4;
        }else{//그 외의 입력 무시(계산 안함)--->   기존의 보관함 총 용량만 계산!!!!

        }
        return capacity;
    }


    public void menu() {
        if (count == 0)
            l.LockerFileInput();
        count++;

        String menu = """
                ——MENU——\s
                1. 강제 수거\s
                2. 임시폐쇄\s
                3. 보관함 삭제\s
                4. 보관함 추가\s
                5. 보관함 수정\s
                6. 종료\s
                 ———————                         
                """;

        System.out.print(menu);

        int number = 0;

        while (true) {

            try {
                System.out.print(">>");
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
                // 강제수거 메소드
                force_pickup();
                break;
            case 2:
                // 임시폐쇄 메소드
                temporary_closure();
                break;
            case 3:
                // 보관함 삭제 메소드
                DeleteLocker();
                break;
            case 4:
                // 보관함 추가 메소드
                AddLocker();
                break;
            case 5:
                // 보관함 수정 메소드
                modifyingLocker();
                break;
            case 6:
                // 종료 메소드
                Exit();
                break;
            default:
                break;
        }

    }

    public void force_pickup() {
        String noticeFp = """
                --------------------------------------------------------------
                물품을 강제 수거할 보관함 번호를 입력해주세요.
                                
                * 이전 메뉴로 돌아가려면 Q 또는 q를 입력하세요.
                --------------------------------------------------------------
                """;

        int LockerNumber = 0;
        Locker targetLocker = null;
        int target = 0;
        while (true) {
            //noticeFp("강제수거 프롬프트") 출력
            printAdminLocker();
            System.out.print(noticeFp);

            //보관함 번호 입력받기
            System.out.print(">>");


            try {
                String LockerNum = String.valueOf(sc.next());
                sc.nextLine();

                //Q,q 처리
                if (Objects.equals(LockerNum, "Q") || Objects.equals(LockerNum, "q")) {
                    count++; //@지은 이거 무한안걸리게 하는거였는데 어떤 원리인지 설명듣고 수정해야할듯
                    menu();
                    return;
                }

                //형식 예외 처리(00, 01, 02 등으로 입력해야함)
                if (LockerNum.length() != 2)
                    throw new IllegalArgumentException();

                //범위 예외 처리(01~99)
                LockerNumber = parseInt(LockerNum);
                if (LockerNumber < 1 || LockerNumber > 99)
                    throw new IllegalArgumentException();

                //존재하지 않는 보관함의 번호를 입력한 경우 처리]
                //일단 for문으로 찾고
                int i = 0;
                targetLocker = null;
                for (Locker lc : LockerManager.LockerList) {
                    if (parseInt(lc.locknum) == LockerNumber) {
                        targetLocker = lc;
                        target = i;
                        break;
                    }
                    i++;
                }
                if (targetLocker == null) {
                    System.out.println("해당 번호의 보관함이 존재하지 않습니다.\n");
                    throw new IllegalAccessException();
                }

                //사용 중이지 않은 보관함의 번호를 입력한 경우
                if (targetLocker.use.equals("0")) {
                    System.out.println("보관 중인 물품이 없습니다. \n");
                    throw new IllegalAccessException();
                }

                //강제수거가 가능하지 않은 보관함을 선택했을 때 처리
                for (Locker lc : LockerManager.LockerList) {
                    if (parseInt(lc.locknum) == LockerNumber) {
                        if (!lc.iscanFp) {
                            System.out.println("강제수거를 할 수 없는 보관함입니다. 강제수거 가능여부를 확인해주세요.\n");
                            throw new IllegalAccessException();
                        }
                    }
                }
                break;
            } catch (IllegalArgumentException e) { //나머지 입력 예외 처리
                System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");

            } catch (IllegalAccessException e) {

            }
        }

        //정상 결과
        System.out.println();
        String FpCheckNotice = targetLocker.locknum + "번 보관함을 선택하셨습니다.\n";
        FpCheckNotice += targetLocker.locknum + "번 / " + targetLocker.date + " / " + (int) (Math.ceil((double) targetLocker.timediffMinutes / 60)) + "시간 / ";
        if (targetLocker.iscanFp)
            FpCheckNotice += "강제수거 가능";
        else
            FpCheckNotice += "강제수거 불가능";
        FpCheckNotice += """
                \n
                강제수거 하시려면 Y또는 y를 입력해주세요.
                ------------------------------------------             
                >> """;


        System.out.print(FpCheckNotice);
        String yn = String.valueOf(sc.next());
        sc.nextLine();

        if (!(Objects.equals(yn, "Y") || Objects.equals(yn, "y"))) {
            System.out.println("강제수거를 취소하셨습니다. \n");
            //menu3으로 돌아감
        } else { //Y나 y를 입력한경우
            //mem과 nonmem에서 해당 보관함 찾기 (회원<User> 정보 저장구조)
            String targetKey = null;
            boolean isMemLocker = false;

            for (String id : UserManager.memMap.keySet()) {
                if (!UserManager.memMap.get(id).locknum.equals("-")) {
                    if (LockerNumber == parseInt(UserManager.memMap.get(id).locknum)) {
                        targetKey = id;
                        break;
                    }
                }
            }
            if (targetKey != null) {
                isMemLocker = true;
            } else {
                isMemLocker = false;
                for (String lnum : UserManager.nonmemMap.keySet()) {
                    if (parseInt(UserManager.nonmemMap.get(lnum).locknum) == LockerNumber) {
                        targetKey = lnum;
                        break;
                    }
                }
            }

            //강제수거 완료한 보관함 정보 삭제 & File Write
            LockerManager.LockerList.get(target).use = "0";
            LockerManager.LockerList.get(target).date = "-";
            LockerManager.LockerList.get(target).confirmbook = "0";
            LockerManager.LockerList.get(target).iscanFp = false;
            LockerManager.LockerList.get(target).timediffMinutes = 0;

            if (isMemLocker) {
                UserManager.memMap.get(targetKey).locknum = "-";
                UserManager.memMap.get(targetKey).lockPW = "-";
            } else {
                UserManager.nonmemMap.remove(targetKey);
            }

            System.out.println("물품 강제 수거가 완료되었습니다. \n이용해주셔서 감사합니다.\n");

            ExitWrite();


        }
    }

    public void temporary_closure() {

        //임시폐쇄 함수에서 필요한 프롬포트들
        String temporary_closure_prompt1 = """
                 --------------------------------------------------------------
                 물품을 임시폐쇄 시작할 날짜와 시각을 12자리의 수로 공백 없이 입력해주세요. (ex. 202309151730)
                                 
                 * 이전 메뉴로 돌아가려면 Q 또는 q를 입력하세요.
                 --------------------------------------------------------------
                """;
        String temporary_closure_prompt2 = """
                 --------------------------------------------------------------
                 물품을 임시폐쇄 종료할 날짜와 시각을 12자리의 수로 공백 없이 입력해주세요. (ex. 202309151730)
                                 
                 * 이전 메뉴로 돌아가려면 Q 또는 q를 입력하세요.
                 --------------------------------------------------------------
                """;
        String temporary_closure_select_prompt = """
                --------------------------------------------------------------
                임시폐쇄할 보관함 번호를 입력해주세요.
                --------------------------------------------------------------
                """;
        String temporary_clousure_check_prompt = """
                삭제하려는 보관함은
                ------------------------------------------
                보관함 번호: <""";

        //임시폐쇄 함수에서 필요한 변수들
        long closurestartdate = 0L; //임시 폐쇄 시작 날짜시간
        long closureenddate = 0L; //임시 폐쇄 종료 날짜시간
        int closureLockerNum = 0; //임시 폐쇄 보관함
        String strclosureLockerNum; //임시 폐쇄 보관함 string

        int flow = 11; //흐름에 따라 값 변경 11. 임시 폐쇄 시작 날짜 입력 12. 임시 폐쇄 끝나는 날짜 입력2. 임시 폐쇠 보관함 선택 3. 보관함 번호 재확인 4. 완료


        //11. 임시 폐쇄 시작 기간 입력
        if (flow == 11) {
            while (true) {
                System.out.print(temporary_closure_prompt1);
                System.out.print(">>");
                String strclosurestartdate = sc.nextLine();

                //Q,q 처리
                if (Objects.equals(strclosurestartdate, "Q") || Objects.equals(strclosurestartdate, "q")) {
                    menu();
                    break;
                }

                //예외1. 과거 예외2. 형식 처리
                if (closure_DateCheck(0L, strclosurestartdate)) {
                    closurestartdate = Long.parseLong(strclosurestartdate);
                    flow = 12;
                    break;
                }
            }
        }

        //2. 임시 폐쇄 종료 기간 입력
        if (flow == 12) {
            while (true) {
                System.out.print(temporary_closure_prompt2);
                System.out.print(">>");
                String strclosuresenddate = sc.nextLine();

                //과거, 형식 예외처리
                if (closure_DateCheck(closurestartdate, strclosuresenddate)) {
                    closureenddate = Long.parseLong(strclosuresenddate);
                    flow = 2;
                    break;
                }

            }
        }


        //2. 임시 폐쇠 보관함 선택
        if (flow == 2) {
            while (true) {

                printAdminLocker();
                System.out.print(temporary_closure_select_prompt);
                System.out.print(">>");

                strclosureLockerNum = sc.nextLine();
                //sc.nextLine();

                //예외처리
                try {
                    //형식예외
                    if (strclosureLockerNum.length() != 2) throw new IllegalArgumentException();

                    //범위 예외 처리(01~16)

                    closureLockerNum = parseInt(strclosureLockerNum);
                    if (closureLockerNum < 1 || closureLockerNum > 99) throw new IllegalArgumentException();

                    //예외1. 존재하지 않는 보관함일 경우
                    boolean isexist = false;
                    for (int i = 0; i < LockerManager.LockerList.size(); i++) {
                        if (parseInt(LockerManager.LockerList.get(i).locknum) == closureLockerNum) {
                            isexist = true;
                        }
                    }
                    if (!isexist)
                        throw new IllegalStateException();

                    //예외2. 임시폐쇄를 할 수 없는 보관함일 경우
                    //보관함의 보관내역이 존재하며 보관 시작 시간으로부터 기본 보관 시간 + 6시간이 지나지 않는 보관함
                    for (int i = 0; i < LockerManager.LockerList.size(); i++) {
                        if (parseInt(LockerManager.LockerList.get(i).locknum) == closureLockerNum) {
                            if (parseInt(LockerManager.LockerList.get(i).use) == 1) { //사용중일때

                                //시간 차이 구하기
                                Date currentTime = LockerManager.StringToDate(Main.currentTimeString);
                                Date startTime = LockerManager.StringToDate(LockerManager.LockerList.get(i).date);
                                long timeDiffMillis = currentTime.getTime() - startTime.getTime();
                                int timeDiffMinutes = (int) (timeDiffMillis / (60 * 1000));
                                int timeDiffHours = (int) (Math.ceil((double) timeDiffMillis / (60 * 60 * 1000)));
                                int timeDiff = (int) (currentTime.getTime() - startTime.getTime()) / 3600000;

                                if (timeDiffMinutes <= 6 * 60) //예약시간 + 6시간 초과했는지 확인
                                    throw new IllegalAccessException();

                            }
                        }
                    }

                    //예약 중, 임시 패쇄 중, 임시 폐쇄 예정인 보관함일 경우
                    for (int i = 0; i < LockerManager.LockerList.size(); i++) {
                        if (parseInt(LockerManager.LockerList.get(i).locknum) == closureLockerNum) {
                            if (parseInt(LockerManager.LockerList.get(i).use) == 2 || parseInt(LockerManager.LockerList.get(i).use) == 3 ||
                                parseInt(LockerManager.LockerList.get(i).use) == 4) {
                                throw new IllegalAccessException();
                            }
                        }
                    }

                    //아무 문제 없다면 폐쇄 기간 저장한 후 3. 보관함 번호 재확인으로 이동
                    flow = 3;
                    break;

                } catch (IllegalArgumentException e) { //형식 예외
                    System.out.println("올바른 입력이 아닙니다.");
                } catch (IllegalAccessException e) { //예외2. 임시폐쇄 할 수 없는 보관함.
                    System.out.println("임시폐쇄할 수 없는 보관함 번호입니다.\n");
                } catch (IllegalStateException e) { //예외1. 존재하지 않는 보관함
                    System.out.println("존재하지 않는 보관함 번호입니다.\n");
                }
            }

            if (flow == 3) {
                temporary_clousure_check_prompt += strclosureLockerNum + ">\n";
                temporary_clousure_check_prompt += """
                        ------------------------------------------
                        가(이) 맞습니까?
                                                
                        *맞다면 Y또는 y를 입력해주세요.
                        ------------------------------------------
                        """;
                while (true) {

                    System.out.println();
                    System.out.print(temporary_clousure_check_prompt);
                    System.out.print(">>");

                    String checking;
                    checking = String.valueOf(sc.next());
                    sc.nextLine();

                    try {
                        if (!(checking.equals("Y") || checking.equals("y")))
                            throw new IllegalArgumentException();

                        //아무 문제 없다면 결제 알림 창으로 이동
                        flow = 4;
                        break;
                    } catch (Exception e) {
                        System.out.println("올바른 입력이 아닙니다.\n");
                    }

                }
            }

            if (flow == 4) {
                //Locker 설정
                for (int i = 0; i < LockerManager.LockerList.size(); i++) {
                    if (parseInt(LockerManager.LockerList.get(i).locknum) == closureLockerNum) {
                        LockerManager.LockerList.get(i).use = "3";
                        LockerManager.LockerList.get(i).closeddatestart = String.valueOf(closurestartdate);
                        LockerManager.LockerList.get(i).closeddatefinish = String.valueOf(closureenddate);
                    }
                }

                ExitWrite();
                System.out.println("임시 폐쇄 기간이 설정되었습니다.");
                System.out.println();
            }


        }


    }

    private static boolean closure_DateCheck(Long start, String inputdate) {

        boolean flag = false;
        Date oldDate = Main.currentTimeDate;    // 현재 날짜
        Date newDate = null;    // 새로 입력 받은 날짜

        String oldD = Main.currentTimeString;    // 현재 날짜
        String dTrim = inputdate.trim();    //입력 받은 string의 공백 제거


        // 글자 수가 12가 아닐 경우 false 반환
        if (dTrim.length() != 12) {
            System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.");
            System.out.println();
            return flag;
        }

        // 2050년 이상인지 아닌지 확인
        int testYear = parseInt(dTrim.substring(0, 4));
        if (testYear > 2050) {
            System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.");
            System.out.println();
            return flag;
        }

        //새로 입력한 날짜 형식 확인
        try {
            int num = 0;
            String[] temp = new String[5];

            for (int i = 0; i < 5; i++) {
                if (i == 0) {
                    temp[i] = dTrim.substring(num, num + 4);
                    num += 4;
                } else {
                    temp[i] = dTrim.substring(num, num + 2);
                    num += 2;
                }

            }

            // 달력에 존재하는 날짜인지 확인
            Calendar newCalendar = new GregorianCalendar();
            newCalendar.setLenient(false); // set leniency mode to strict
            newCalendar.set(Calendar.YEAR, parseInt(temp[0]));
            newCalendar.set(Calendar.MONTH, parseInt(temp[1]) - 1); // subtract 1 from month because Calendar class starts at 0
            newCalendar.set(Calendar.DAY_OF_MONTH, parseInt(temp[2]));
            newCalendar.set(Calendar.HOUR_OF_DAY, parseInt(temp[3]));
            newCalendar.set(Calendar.MINUTE, (parseInt(temp[4], 10)));
            newCalendar.set(Calendar.SECOND, 0);
            newCalendar.set(Calendar.MILLISECOND, 0);
            newDate = newCalendar.getTime();

        } catch (Exception e) {
            System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.");
            System.out.println();
            return flag;
        }


        //시작날짜입력일때
        if (start == 0) {
            // 현재 날짜가 입력 받은 날보다 이전일 경우 true
            if (oldDate.before(newDate)) {
                flag = true;
            } else if (oldDate.equals(newDate)) { // 같은 날일 경우
                flag = false;
            } else { // 이외의 모든 경우
                flag = false;
            }

            if (!flag) {
                System.out.println("지난 날짜입니다. 기간을 확인해주세요.");
                System.out.println();
                return flag;
            }

        }
        //종료날짜입력일때
        else if (start != 0) {
            long endclosuretime = Long.parseLong(inputdate);

            if (start >= endclosuretime) {
                flag = false;
                System.out.println("올바르지 않은 기간입니다. 기간을 확인해주세요.");
                System.out.println();
                return flag;
            } else flag = true;
        }

        System.out.println();

        // flag를 이용해 반복 조절
        return flag;


    }

    public void timediffUpdate(Locker lc) {
        Date currentTime = LockerManager.StringToDate(Main.currentTimeString);
        Date startTime = LockerManager.StringToDate(lc.date);

        long timeDiffMillis = currentTime.getTime() - startTime.getTime();
        int timeDiffMinutes = (int) (timeDiffMillis / (60 * 1000));
        int timeDiffHours = (int) (Math.ceil((double) timeDiffMillis / (60 * 60 * 1000)));
        int timeDiff = (int) Math.ceil((double) (currentTime.getTime() - startTime.getTime()) / 3600000);

        lc.timediffMinutes = timeDiffMinutes;
    }

    public void printAdminLocker() {
        System.out.println("---------------------- 보관함 목록 ----------------------");
        int timeDiff = 0;
        String iscanforce = "강제수거 불가능";
        for (Locker lc : LockerManager.LockerList) {
            String size = "";
            iscanforce = "강제수거 불가능";
            if (lc.locksize.equals("0"))
                size = "S";
            else if (lc.locksize.equals("1"))
                size = "M";
            else if (lc.locksize.equals("2"))
                size = "L";

            if (!lc.date.equals("-")) { //date가 비어있지 않으면 {use가 사용중(1)이거나 임시폐쇄예정(3)일때만} @수정필요
                timediffUpdate(lc);

                if (lc.timediffMinutes / 60 > 10) {
                    iscanforce = "강제수거 가능";
                    lc.iscanFp = true;
                }
                System.out.println(lc.locknum + "번 / " + size + " / " + lc.date + " / " + Math.abs(lc.timediffMinutes / 60) + "시간"
                                   + (int) Math.abs(lc.timediffMinutes) % 60 + "분째 사용중 / " + iscanforce);
            } else {
                System.out.println(lc.locknum + "번 / " + size + " / - / " + iscanforce);

            }
        }
    }

    public void ExitWrite() {
        u.UserFileWrite();
        l.LockerFileWrite();
        System.exit(0);
    }


    public String getPW() {
        return pw;
    }

    private void DeleteLocker() {

        //입력받은 보관함 번호
        int LockerNumber = 0;   //정수형
        String LockerNum = null;    //스트링
        //보관함 비밀번호
        String LockerPwd = null;
        //삭제할 보관함
        Locker lc = null;

        //수정
        int flow = 1;

        while (true) {
            printAdminLocker();
            System.out.print("""
                    삭제할 보관함의 번호를 입력해주세요.

                    * 이전 메뉴로 돌아가려면 Q 또는 q를 입력하세요.
                    --------------------------------------------------------------
                    >>\s""");
            LockerNum = String.valueOf(sc.next());
            sc.nextLine();


            try {
                //Q 입력
                if (Objects.equals(LockerNum, "Q") || Objects.equals(LockerNum, "q")) {
                    //menu3으로 복귀
                    menu();
                    break;
                }

                //잘못됩 입력 (0~99 입력 안함)
                LockerNumber = parseInt(LockerNum);
                if (LockerNumber < 1 || LockerNumber > 16)
                    throw new IllegalArgumentException();

                //형식 예외 처리 (01, 02 등으로 입력하지 않고 1, 2 등으로 입력함)
                if (LockerNum.length() != 2)
                    throw new IllegalArgumentException();

                //존재하지 않는 보관함인 경우
                //수정 -> lockerlist 호출을 어케하지?
                Iterator<Locker> iterator = LockerManager.LockerList.iterator();
                while (iterator.hasNext()) {
                    Locker locker = iterator.next();
                    if (locker.getLocknum().equals(LockerNum)) {
                        //보관중
                        if (locker.getUse().equals("1")) {
                            //삭제 불가인 경우 (10시간 보관 안지남)
                            //시간 차이 구하기
                            timediffUpdate(locker);
                            //수정
                            if (locker.timediffMinutes <= 10 * 60) { //기본 보관 시간 + 6시간 초과했는지 확인
                                System.out.println("삭제할 수 없는 보관함 번호입니다.\n");
                                throw new IllegalAccessException();
                            }
                        }
                        //삭제 불가인 경우 (예약중인 보관함)
                        else if (locker.getUse().equals("2")) {
                            System.out.println("삭제할 수 없는 보관함 번호입니다.\n");
                            throw new IllegalAccessException();
                        }
                        flow = 2;   //삭제 가능이므로 flow값 2
                        lc = locker;
                        break;
                    }
                }
                //iterator를 다 돌고 나옴
                //존재하지 않는 보관함인 경우
                if (flow != 2) {
                    System.out.println("존재하지 않는 보관함 번호입니다.\n");
                    throw new IllegalAccessException();
                }

            } catch (IllegalArgumentException e) {
                System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
            } catch (IllegalAccessException e) {
            }

            //삭제 가능인 경우
            if (flow == 2) {
                System.out.print("삭제하려는 보관함은\n" +
                                 "------------------------------------------\n" +
                                 "보관함 번호: <" + LockerNum + ">\n" +
                                 "------------------------------------------\n" +
                                 "가(이) 맞습니까?\n" +
                                 "\n" +
                                 "*맞다면 Y또는 y를 입력해주세요.\n" +
                                 "------------------------------------------\n" +
                                 ">> ");
                String yn = String.valueOf(sc.next());
                sc.nextLine();

                try {
                    // Y or y 말고 다른 것을 입력한 경우
                    if (!(Objects.equals(yn, "Y") || Objects.equals(yn, "y")))
                        throw new IllegalArgumentException();
                        // Y or y 입력
                    else {
                        //수정!!
                        //저장구조 변경어케함
                        LockerManager.LockerList.remove(lc);
                        System.out.println("보관함이 삭제되었습니다.");
                        ExitWrite();
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("올바른 입력이 아닙니다.\n");
                }
            }
        }
    }

    private void AddLocker() {

        //입력받은 보관함 번호
        int LockerNumber = 0;   //정수형
        String LockerNum = null;    //스트링
        //보관함 사이즈
        String sizevalue = null;
        //현재 보관함 총 용량
        int totalsize = 0;

        Iterator<Locker> iterator = LockerManager.LockerList.iterator();
        while (iterator.hasNext()) {
            Locker locker = iterator.next();
            if (locker.getLocksize().equals("0")) {
                totalsize += 2;
            } else if (locker.getLocksize().equals("1")) {
                totalsize += 3;
            } else {
                totalsize += 4;
            }
        }

        //수정
        int flow = 1; // 보관함 번호 입력 (1) -> 보관함 크기 입력 (2) -> 보관함 번호 크기 재확인(3)

        while (true) {
            printAdminLocker();
            //수정
            System.out.print("(현재 보관함 총 용량: " + totalsize + "/50)\n\n" +
                             "추가할 보관함의 번호를 입력해주세요.\n\n" +
                             "* 이전 메뉴로 돌아가려면 Q 또는 q를 입력하세요.\n" +
                             "--------------------------------------------------------------\n" +
                             ">> ");

            LockerNum = String.valueOf(sc.next());
            sc.nextLine();

            try {
                //Q 입력
                if (Objects.equals(LockerNum, "Q") || Objects.equals(LockerNum, "q")) {
                    //menu3으로 복귀
                    menu();
                    break;
                }

                //잘못됩 입력 (0~99 입력 안함)
                LockerNumber = parseInt(LockerNum);
                if (LockerNumber < 1 || LockerNumber > 99) {
                    throw new IllegalArgumentException();
                }

                //형식 예외 처리 (01, 02 등으로 입력하지 않고 1, 2 등으로 입력함)
                if (LockerNum.length() != 2) {
                    throw new IllegalArgumentException();
                }

                //이미 존재하는 보관함 번호를 입력한 경우
                Iterator<Locker> iterator1 = LockerManager.LockerList.iterator();
                while (iterator1.hasNext()) {
                    Locker locker = iterator1.next();
                    //이미 존재하는 보관함 번호를 입력한 경우
                    if (locker.getLocknum().equals(LockerNum)) {
                        System.out.println("이미 존재하는 보관함 번호입니다.\n");
                        throw new IllegalAccessException();
                    }
                }
                flow = 2; // 보관함 크기 입력 프롬프트로 넘어가기

            } catch (IllegalArgumentException e) {
                System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
            } catch (IllegalAccessException e) {
            }


            if (flow == 2) {
                System.out.print("""
                        추가할 보관함의 크기를 입력해주세요.
                        >>\s""");

                sizevalue = String.valueOf(sc.next());
                sc.nextLine();

                try {
                    // S or s or M or m or L or l 말고 다른 것을 입력한 경우
                    if (!(Objects.equals(sizevalue, "S") || Objects.equals(sizevalue, "s") ||
                          Objects.equals(sizevalue, "M") || Objects.equals(sizevalue, "m") ||
                          Objects.equals(sizevalue, "L") || Objects.equals(sizevalue, "l"))) {
                        System.out.println("올바른 입력이 아닙니다.");
                        System.out.println("다시 한번 입력해주세요.\n");
                        throw new IllegalArgumentException();
                    }

                    if (Objects.equals(sizevalue, "S") || Objects.equals(sizevalue, "s")) {
                        if (totalsize > 48) {
                            System.out.println("용량 초과 문제로 보관함을 추가할 수 없습니다.");
                            throw new IllegalArgumentException();
                        }
                    } else if (Objects.equals(sizevalue, "M") || Objects.equals(sizevalue, "m")) {
                        if (totalsize > 47) {
                            System.out.println("용량 초과 문제로 보관함을 추가할 수 없습니다.");
                            throw new IllegalArgumentException();
                        }
                    } else if (Objects.equals(sizevalue, "L") || Objects.equals(sizevalue, "l")) {
                        if (totalsize > 46) {
                            System.out.println("용량 초과 문제로 보관함을 추가할 수 없습니다.");
                            throw new IllegalArgumentException();
                        }
                    }

                    flow = 3; //보관함 번호 크기 재확인 프롬프트로 이동
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("\n");
                }
            }
        }

        if (flow == 3) {
            System.out.print("추가하려는 보관함은\n" +
                             "------------------------------------------\n" +
                             "보관함 번호: " + LockerNum + "\n" +
                             "보관함 크기: " + sizevalue + "\n" +
                             "------------------------------------------\n" +
                             "가(이) 맞습니까?\n" +
                             "\n" +
                             "*맞다면 Y또는 y를 입력해주세요.\n" +
                             "------------------------------------------\n" +
                             ">> ");
            String yn = String.valueOf(sc.next());
            sc.nextLine();

            if (Objects.equals(yn, "Y") || Objects.equals(yn, "y")) {
                //수정
                //파일처리
                //보관함 크기(0/1/2-S/M/L)
                String sizenum = null;
                if (Objects.equals(sizevalue, "S") || Objects.equals(sizevalue, "s")) sizenum = "0";
                else if (Objects.equals(sizevalue, "M") || Objects.equals(sizevalue, "m")) sizenum = "1";
                else sizenum = "2";

                Locker lc = new Locker(LockerNum, sizenum, "-");
                LockerManager.LockerList.add(lc);
                Collections.sort(LockerManager.LockerList, new LockerSortComparator());
                System.out.println("보관함이 추가됐습니다.\n");
                ExitWrite();
            } else {
                flow = 1;
                //수정
                AddLocker();
            }
        }
    }

    //보관함 수정
    public void modifyingLocker(){
        String sel="";
        int num=100;//보관함 번호 저장할 변수
        int capacity=Capacity("");//현재 보관함 총 용량--->나중에 받아오는 걸로 고치기!!-->고치긴했는데 확인필요

        int flag=0; //flag==0이면 올바르지 않은 입력


        boolean check=true;//수정가능한 보관함인지 확인-->받아오기+디폴트 false로 바꾸기!!!
        boolean exist=false;//존재여부 확인

        //boolean capacity_check=true;//사이즈 수정했을 때 용량 초과하는지 아닌지 확인--->계산하는 함수 만들기--->필요없는듯??? @삭제예정

        while(flag==0){
            try{
                System.out.println("---------보관함 목록---------");

                //보관함 리스트 출력 수정!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                printAdminLocker();

                System.out.println("현재 보관함 총량: <"+capacity+"/50>");
                System.out.println("수정할 보관함을 선택해주세요.\n");
                System.out.println("*이전 메뉴로 돌아가려면 Q 또는 q를 입력하세요.\n--------------------------------");
                System.out.print(">>");
                sel=sc.nextLine();

                boolean isNum=sel.matches("[+-]?\\d*(\\.\\d+)?");
                boolean numscope=false;//01~99사이의 정수인지 판단
                int strlen=0;
                if(isNum){//sel에서 입력받은 문자열이 숫자면 정수형으로 변환
                    try{
                        strlen=sel.length();
                        num= Integer.parseInt(sel);
                    }
                    catch (NumberFormatException ex){
                        ex.printStackTrace();
                    }
                }
                if(num>=1&&num<=99){
                    numscope=true;
                }
                if(sel.equals("q")||sel.equals("Q")){//이전 메뉴로 돌아가기
                    flag=1;
                }
                else if((strlen==2)&&isNum&&numscope){//01~99 사이의 정수를 입력한 경우
                    //존재하는 보관함 번호인지 확인
                    for(int i=0;i<l.LockerList.size();i++) {//입력받은 보관함 번호로 탐색
                        if (l.LockerList.get(i).locknum.equals(sel)){
                            exist=true;//존재하는 보관함 번호
                            if(l.LockerList.get(i).use.equals("0")){//사용중이지 않은 보관함
                                check=true;
                            }else{//그 외의 경우
                                check=l.LockerList.get(i).iscanFp;//강제수거 가능한지 아닌지 저장
                            }
                        }
                    }

                    //여기에서 강제수거가능 여부저장@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

                    if(check&&exist){//수정가능한 보관함인 경우(보관함 번호 존재+강제수거가능)
                        String str;
                        //보관함 수정
                        try{
                            System.out.println("수정하려는 보관함은\n-----------------------");
                            System.out.println("보관함 번호: <"+sel+">\n가(이) 맞습니까?\n");
                            System.out.println("*맞다면 Y또는 y를 입력해주세요.");
                            System.out.print("----------------------\n>>");
                            str=sc.nextLine();
                            if(str.equals("Y")||str.equals("y")){//크기 입력으로 넘어감.
                                flag=2;//01~99 사이 정수이고, 수정가능+확인 받음
                            }
                        }catch(InputMismatchException e){
                            System.out.println("올바르지 않은 입력입니다.\n");//나중에 바꾸기 그냥 예외처리 한 것.
                        }

                        //Y또는 y가 아닐 시 다시 while문 돌음
                    }else if(exist==false){
                        System.out.println("존재하지 않는 보관함입니다.");//------->기획서 수정 필요!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    }else{//수정 불가능한 보관함인 경우
                        System.out.println("수정할 수 없는 보관함입니다.");
                    }
                }
                else{//Q 또는 q, 01~99가 아닌 입력, 수정할 수 없는 보관함
                    System.out.println("올바르지 않은 입력입니다.\n");
                }

            }catch(InputMismatchException e){
                System.out.println("올바르지 않은 입력입니다.\n");
            }
        }

        if(flag==1){//뒤로 돌아가기(Q 혹은 q)입력
            menu();
        }

        if(flag==2){//크기 입력 받기
            int flag_=0;
            int size=0;
            String str="";
            int cap=0;
            while(flag_==0){
                try{

                    System.out.print("보관함의 크기를 입력해주세요.\n>>");
                    str=sc.nextLine();
                    if(str.equals("S")|| str.equals("s")||str.equals("M")||str.equals("m")||str.equals("L")||str.equals("l")){
                        //올바른 크기 문자열 입력

                        //수정했을 때 용량 계산--->용량 기준 바뀌면 바꾸기!!!!!!!!!!!

                        for(int i=0;i<l.LockerList.size();i++){//수정전 용량 확인
                            if(l.LockerList.get(i).locknum.equals(sel)){
                                if(l.LockerList.get(i).locksize.equals("0")){
                                    size=2;
                                }else if(l.LockerList.get(i).locksize.equals("1")){
                                    size=3;
                                }else{
                                    size=4;
                                }
                            }
                        }
                        cap=Capacity(str)-size;//Capacity{기존용량(수정전 보관함 용량 포함) + 수정후 보관함 용량} - 수정전 보관함 용량
                        if(cap<=50){//용량 초과x
                            flag_=1;
                        }
                        else{//용량초과ㅇ
                            flag_=2;
                        }
                    }
                    else{
                        System.out.println("올바르지 않은 입력입니다.\n");
                    }

                }catch(InputMismatchException e){
                    System.out.println("올바르지 않은 입력입니다.\n");
                }
            }
            int index=-1;
            if(flag_==1){
                //*****보관함 정보 수정 함수 여기에 넣기-->수정완료,확인 필요
                for(int i=0;i<l.LockerList.size();i++){//입력받은 보관함 번호로 탐색
                    if(l.LockerList.get(i).locknum.equals(sel)){//수정하려는 보관함 번호면

                        //용량 기준 바뀌면 toString(size-머시기) 바꿔야함!!!!!!!!!
                        index=i;

                    }
                }

                String size_="";
                if(str.equals("S")||str.equals("s")){
                    size_="0";
                }else if(str.equals("M")||str.equals("m")){
                    size_="1";
                }else{
                    size_="2";
                }
                //보관함 크기 수정
                l.LockerList.set(index,new Locker(l.LockerList.get(index).locknum,size_,l.LockerList.get(index).use
                        ,l.LockerList.get(index).date,l.LockerList.get(index).confirmbook
                        ,l.LockerList.get(index).closeddatestart,l.LockerList.get(index).closeddatefinish));
                System.out.println("보관함 정보가 수정되었습니다.");
            }
            if(flag_==2){//보관함 입력받는 처음부분으로 돌아가기__재귀함수 주의
                System.out.println("용량 초과 문제로 보관함을 추가할 수 없습니다.");
                modifyingLocker();
            }
        }

        ExitWrite();//종료
    }

    //종료
    public void Exit(){
        try{
            String str;
            System.out.print("종료하시려면 Y또는 y를 입력해주세요.\n>>");
            str= sc.nextLine();
            if(str.equals("Y")||str.equals("y")){
                System.out.println("프로그램을 종료합니다.");
                ExitWrite();//종료
            }
            else{//다시 menu3으로 돌아감
                menu();
            }
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
    }


}

class LockerSortComparator implements Comparator<Locker> {
    @Override
    public int compare(Locker o1, Locker o2) {
        if (Integer.parseInt(o1.locknum) > Integer.parseInt(o2.locknum)) return 1;
        else if (Integer.parseInt(o1.locknum) < Integer.parseInt(o2.locknum)) return -1;
        return 0;
    }
}
