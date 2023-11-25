import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;

public class AdminManager {
    static Scanner sc = new Scanner(System.in);

    public AdminManager() {
    }

    static int count = 0;

    UserManager u = new UserManager();
    LockerManager l = new LockerManager();


    public void menu() {
        if (count == 0)
            l.LockerFileInput();

        String menu = """
                ——MENU——\s
                1. 강제 수거\s
                2. 임시폐쇄\s
                3. 보관함 삭제\s
                4. 보관함 추가\s
                5. 보관함 수정\s
                6. 종료\s
                 ———————\s                         
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
                break;
            case 3:
                // 보관함 삭제 메소드
                break;
            case 4:
                // 보관함 추가 메소드
                break;
            case 5:
                // 보관함 수정 메소드
                break;
            case 6:
                // 종료 메소드
                ExitWrite();
                break;
            default:
                break;
        }

    }

    public void force_pickup() {
        String noticeFp = """
                --------------------------------------------------------------\n
                물품을 강제 수거할 보관함 번호를 입력해주세요.\n\n
                                
                * 이전 메뉴로 돌아가려면 Q 또는 q를 입력하세요.\n
                --------------------------------------------------------------\n
                """;

        int LockerNumber = 0;
        Locker targetLocker = null;
        int target = 0;
        while (true) {
            //noticeFp("강제수거 프롬프트") 출력
            printAdminLocker();
            System.out.println(noticeFp);

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
                LockerNumber = Integer.parseInt(LockerNum);
                if (LockerNumber < 1 || LockerNumber > 99)
                    throw new IllegalArgumentException();

                //존재하지 않는 보관함의 번호를 입력한 경우 처리]
                //일단 for문으로 찾고
                int i = 0;
                for (Locker lc : l.LockerList) {
                    i++;
                    if (Integer.parseInt(lc.locknum) == LockerNumber) {
                        targetLocker = lc;
                        target = i;
                        break;
                    }

                }
                if (targetLocker == null) {
                    System.out.println("해당 번호의 보관함이 존재하지 않습니다.");
                    throw new IllegalAccessException();
                }

                //사용 중이지 않은 보관함의 번호를 입력한 경우
                if (targetLocker.use == "0") {
                    System.out.println("보관 중인 물품이 없습니다.");
                    throw new IllegalAccessException();
                }

                //강제수거가 가능하지 않은 보관함을 선택했을 때 처리
                for (Locker lc : l.LockerList) {
                    if (Integer.parseInt(lc.locknum) == LockerNumber) {
                        if (lc.iscanFp == false) {
                            System.out.println("강제수거를 할 수 없는 보관함입니다. 강제수거 가능여부를 확인해주세요.");
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
        String FpCheckNotice = Integer.toString(LockerNumber) + "번 보관함을 선택하셨습니다.\n";
        FpCheckNotice += targetLocker.locknum + "번 / " + targetLocker.date + " / " + targetLocker.timediff + "시간 / ";
        if (targetLocker.iscanFp == true)
            FpCheckNotice += "강제수거 가능";
        else
            FpCheckNotice += "강제수거 불가능";
        FpCheckNotice += """
                강제수거 하시려면 Y또는 y를 입력해주세요.\n
                ------------------------------------------\n              
                >>                                        
                """;


        System.out.println(FpCheckNotice);
        String yn = String.valueOf(sc.next());
        sc.nextLine();

        if (!(Objects.equals(yn, "Y") || Objects.equals(yn, "y"))) {
            System.out.println("강제수거를 취소하셨습니다. \n");
            return;
        } else { //Y나 y를 입력한경우
            //mem과 nonmem에서 해당 보관함 찾기 (회원<User> 정보 저장구조)
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

            //강제수거 완료한 보관함 정보 삭제 & File Write
            l.LockerList.get(target).use = "0";
            l.LockerList.get(target).date = "-";
            l.LockerList.get(target).confirmbook = "0";
            l.LockerList.get(target).iscanFp = false;
            l.LockerList.get(target).timediff = 0;

            if (isMemLocker) {
                u.memMap.get(targetKey).locknum = "-";
                u.memMap.get(targetKey).lockPW = "-";
            } else {
                u.nonmemMap.remove(targetKey);
            }


            System.out.println("물품 강제 수거가 완료되었습니다. \n 이용해주셔서 감사합니다.\n");

            ExitWrite();


        }
    }

    public void printAdminLocker() {
        System.out.println("---------------------- 보관함 목록 ----------------------");
        int timeDiff = 0;
        String iscanforce = "강제수거 불가능";
        for (Locker lc : l.LockerList) {
            String size = "";
            iscanforce = "강제수거 불가능";
            if (lc.locksize.equals("0"))
                size = "S";
            else if (lc.locksize.equals("1"))
                size = "M";
            else if (lc.locksize.equals("2"))
                size = "L";

            if (!lc.use.equals("0")) {
                Date currentTime = l.StringToDate(Main.currentTimeString);
                Date startTime = l.StringToDate(lc.date);

                long timeDiffMillis = currentTime.getTime() - startTime.getTime();
                int timeDiffMinutes = (int) (timeDiffMillis / (60 * 1000));
                int timeDiffHours = (int) (Math.ceil((double) timeDiffMillis / (60 * 60 * 1000)));
                timeDiff = (int) Math.ceil((double) (currentTime.getTime() - startTime.getTime()) / 3600000);

                lc.timediff = timeDiff;

                if (timeDiff > 10) {
                    iscanforce = "강제수거 가능";
                    lc.iscanFp = true;
                }
                System.out.println(lc.locknum + "번 / " + size + " / " + lc.date + " / " +Math.abs(timeDiffHours)+ "시간"
                        +Integer.toString(Math.abs(timeDiffMinutes)%60)+"분째 사용중 / "+ iscanforce);
            } else{
                System.out.println(lc.locknum + "번 / " + size + " / - / " + iscanforce);

            }
        }
    }
//    }

    public void ExitWrite() {
        u.UserFileWrite();
        l.LockerFileWrite();
        System.exit(0);
    }

}