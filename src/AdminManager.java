import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Lock;

public class AdminManager {
    static Scanner sc = new Scanner(System.in);

    private String pw = "admin1234";

    public AdminManager() {
    }

    static int count = 0;

    UserManager u = new UserManager();
    LockerManager l = new LockerManager();


    public void menu() {
        if (count == 0){
            l.LockerFileInput();
            u.UserFileInput(); //얘도 넣어야 할듯용
        }
        count++;

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
                break;
            case 2:
                // 임시폐쇄 메소드
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
                System.out.println(lc.locknum + "번 / " + size + " / " + lc.date + " / " + Math.abs(timeDiffHours) + "시간"
                        + Integer.toString(Math.abs(timeDiffMinutes) % 60) + "분째 사용중 / " + iscanforce);
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
                LockerNumber = Integer.parseInt(LockerNum);
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
                            Date currentTime = LockerManager.StringToDate(Main.currentTimeString);
                            Date startTime = LockerManager.StringToDate(locker.date);
                            long timeDiffMillis = currentTime.getTime() - startTime.getTime();
                            int timeDiffMinutes = (int) (timeDiffMillis / (60 * 1000));
                            int timeDiffHours = (int) (Math.ceil((double) timeDiffMillis / (60 * 60 * 1000)));
                            int timeDiff = (int) (currentTime.getTime() - startTime.getTime()) / 3600000;

                            //수정
                            if (timeDiffMinutes <= 6 * 60) { //예약시간 + 6시간 초과했는지 확인
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
                LockerNumber = Integer.parseInt(LockerNum);
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
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
            } catch (IllegalAccessException e) {}
        }

        if (flow == 2) {
            while (true) {
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

                    if (Objects.equals(sizevalue, "S") || Objects.equals(sizevalue, "s")) if (totalsize > 48) {
                        System.out.println("용량 초과 문제로 보관함을 추가할 수 없습니다.");
                        throw new IllegalArgumentException();
                    } else if (Objects.equals(sizevalue, "M") || Objects.equals(sizevalue, "m")) if (totalsize > 47) {
                        System.out.println("용량 초과 문제로 보관함을 추가할 수 없습니다.");
                        throw new IllegalArgumentException();
                    } else if (Objects.equals(sizevalue, "L") || Objects.equals(sizevalue, "l")) if (totalsize > 46) {
                        System.out.println("용량 초과 문제로 보관함을 추가할 수 없습니다.");
                        throw new IllegalArgumentException();
                    }

                    flow = 3; //보관함 번호 크기 재확인 프롬프트로 이동
                    break;
                } catch (IllegalArgumentException e) {
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
                    ">>\s");
            String yn = String.valueOf(sc.next());
            sc.nextLine();

            if (Objects.equals(yn, "Y") || Objects.equals(yn, "y")) {
                //수정
                //파일처리

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
        String sel;
        int num=100;//보관함 번호 저장할 변수
        int capacity=0;//현재 보관함 총 용량--->나중에 받아오는 걸로 고치기!!

        //근데 생각해보니 용량 계산 함수 누가 만들지?
        int flag=0; //flag==0이면 올바르지 않은 입력
        boolean check=true;//수정가능한 보관함인지 확인-->받아오기!!!
        boolean capacity_check=true;//사이즈 수정했을 때 용량 초과하는지 아닌지 확인--->계산하는 함수 만들기

        while(flag==0){
            try{
                System.out.println("---------보관함 목록---------");
                //보관함 리스트 출력
                System.out.println("현재 보관함 총량: <"+capacity+"/50>");
                System.out.println("수정할 보관함을 선택해주세요.\n");
                System.out.println("*이전 메뉴로 돌아가려면 Q 또는 q를 입력하세요.\n--------------------------------");
                System.out.print(">>");
                sel=sc.nextLine();

                boolean isNum=sel.matches("[+-]?\\d*(\\.\\d+)?");
                if(isNum){//sel에서 입력받은 문자열이 숫자면 정수형으로 변환
                    try{
                        num= Integer.parseInt(sel);
                    }
                    catch (NumberFormatException ex){
                        ex.printStackTrace();
                    }
                }
                if(sel.equals("q")||sel.equals("Q")){//이전 메뉴로 돌아가기
                    flag=1;
                }
                else if(isNum&&(num>=1||num<=99)){//01~99 사이의 정수를 입력한 경우
                    if(check){//수정가능한 보관함인 경우
                        String str;
                        //보관함 수정
                        try{
                            System.out.println("수정하려는 보관함은\n-----------------------");
                            System.out.println("보관함 번호: <"+num+">\n가(이) 맞습니까?\n");
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
            String str;
            while(flag_==0){
                try{

                    System.out.print("보관함의 크기를 입력해주세요.\n>>");
                    str=sc.nextLine();
                    if(str.equals("S")|| str.equals("s")||str.equals("M")||str.equals("m")||str.equals("L")||str.equals("l")){
                        //올바른 크기 문자열 입력
                        //****여기에 용량 계산 함수 추가***
                        if(capacity_check){//용량 초과x
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
            if(flag_==1){
                //*****보관함 정보 수정 함수 여기에 넣기
                System.out.println("보관함 정보가 수정되었습니다.");
            }
            if(flag_==2){//보관함 입력받는 처음부분으로 돌아가기__재귀함수 주의
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
