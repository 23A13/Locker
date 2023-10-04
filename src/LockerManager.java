import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class LockerManager {
    ArrayList<Locker> LockerList=new ArrayList<>(); //Locker정보 저장 구조
    Map<String,User> mem=new HashMap<>(); //회원 정보 저장 구조
    Map<String,User> nonmem=new HashMap<>(); //비회원 정보 저장 구조


    UserManager u=new UserManager();

    //class 선언
    static Scanner sc = new Scanner(System.in);
    static Locker locker = new Locker();


    //Constructor
    public LockerManager() {
        mem = u.memMap;
        nonmem = u.nonmemMap;
    }

    public LockerManager(String userId, String userPassword) {

    }
    //프로그램 최초 시작 시 locker 데이터 txt파일로부터 불러오는 함수
    public void LockerFileInput(ArrayList<Locker> List){
        String filename="Locker.txt";
        try(Scanner scan=new Scanner(new File(filename))){
            while(scan.hasNextLine()) {
                String str=scan.nextLine();
                String[] temp=str.split(" ");
                /*int[] temp=new int[temp1.length-1];//string을 int형으로 바꿈
                for(int i=0;i< temp1.length-1;i++){
                    temp[i]=Integer.parseInt(temp1[i+1].trim());
                }*/
                List.add(new Locker(temp[0].trim(),temp[1].trim(),temp[2].trim(),temp[3].trim(),temp[4].trim()));//최초로 저장구조에 locker정보 저장
            }
        }catch(FileNotFoundException e){
            System.out.println("파일 입력이 잘못되었습니다.");
        }
    }

    //프로그램 종료 시 locker 데이터 txt파일에 저장하는 함수
    public void LockerFileWrite(ArrayList<Locker> List){
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

    public void Menu_1(){ //회원메뉴
        //파일 읽어오기
        LockerFileInput(LockerList);

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

        switch(number){
            case 1:
                //Storage();
                break;
            case 2:
                Pickup(true);
                break;
            case 3:
                /*if () {
                    Booking();
                }
                else {
                    Menu_1();
                }*/
                break;
            case 4:
                //ComfirmBooking();
                break;
            case 5:
                /* if(loggedIn) { //!@ ? ? ? ? ? loggedIn변수 형식지정,선언
                    logout();
                }*/
                break;
            case 6:
                Exit(true);
                break;
            default:
                break;
        }
        //locker 데이터 Locker.txt 파일에 저장
        LockerFileWrite(LockerList);
    }
    public void Menu_2() { //비회원메뉴

        LockerFileInput(LockerList);

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
                //Storage();
                break;
            case 2:
                Pickup(false);
                break;
            case 3:
                u.menu1();
                break;
            case 4:
                Exit(false);
                break;
            default:
                break;
        }
        LockerFileWrite(LockerList);

    }

    public static void logout() {
        //!오류! loggedIn = false;
        System.out.println("로그아웃되었습니다.");
    }

    public void Exit(boolean isLogin) {
        Scanner sc = new Scanner(System.in);
        System.out.println("종료하시려면 Y 또는 y를 입력해주세요: ");
        String exit = sc.next();
        if (!(Objects.equals(exit, "Y") || Objects.equals(exit, "y"))) {
            System.out.println("프로그램을 종료합니다.");
            System.exit(0);
            Main.date_check();
        } else {
            if(isLogin) Menu_1(); //이전 메뉴로 돌아가기
            else Menu_2();
            System.exit(0);
        }
    }

    private void Pickup(boolean isLogin) {
        //보관함
        locker.print();

        String notice2 = """
                이용하신 보관함의 번호를 입력하세요. (ex: 01)\s
                \s
                * 이전 메뉴로 돌아가려면 Q 또는 q를 입력하세요.\s
                ——————————————————————————————————————————————\s
                """;

        //보관함 번호 입력받기
        int LockerNumber =  0;
        while (true) {
            //notice 2 출력
            System.out.println(notice2);

            //보관함 번호 입력받기
            System.out.print(">>");

            try {
                String LockerNum = String.valueOf(sc.next());
                sc.nextLine();

                //Q,q 처리
                if (Objects.equals(LockerNum, "Q") || Objects.equals(LockerNum, "q")){
                    if(isLogin) Menu_1();
                    else Menu_2();
                    System.exit(0);
                }

                //형식 예외 처리(00, 01, 02 등으로 입력해야함)
                if (LockerNum.length() != 2)
                    throw new IllegalArgumentException();

                //범위 예외 처리(01~16)
                LockerNumber = Integer.parseInt(LockerNum);
                if (LockerNumber < 1 || LockerNumber > 16)
                    throw new IllegalArgumentException();

                //사용중이지 않은 보관함을 선택했을 때 처리
                for(Locker l : LockerList){
                    if(Integer.parseInt(l.locknum)==LockerNumber) {
                        if (Integer.parseInt(l.use) == 0)
                            throw new IllegalAccessException();
                    }
                }

                break;

            } catch (IllegalArgumentException e) { //나머지 입력 예외 처리
                System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");

            } catch (IllegalAccessException e) {
                System.out.println("이용 중이지 않은 보관함입니다.");
                System.out.println("보관함 번호를 확인해주세요.\n");
            }

        }
        //test확인용출력
        System.out.print("선택한 보관함 번호 : ");
        System.out.println(LockerNumber);


        //해당 번호(LockerNum)의 보관함 정보 찾기
            //LockerList에서 찾기
        int target=0;
        for (int i=0; i<LockerList.size(); i++){
            if(LockerNumber == Integer.parseInt(LockerList.get(i).locknum) ){ //보관함 번호가 맞으면
                target = i;
                //test확인용출력
                //System.out.println("LockerList에서 찾기 성공 "+LockerList.get(target).locknum);
            }
        }

            //mem과 nonmem에서 찾기 (회원<User> 정보 저장구조)
        String targetKey=null;
        boolean isMemLocker = false;

        for(String id : mem.keySet()) {
            if(!mem.get(id).locknum.equals("-")) {
                if (LockerNumber == Integer.parseInt(mem.get(id).locknum)) {
                    targetKey = id;

                    //test확인용출력
                    System.out.println("mem map에서 찾기 성공");
                    System.out.println(mem.get(id));
                    break;
                }
            }
        }
        if(targetKey!=null) {
            isMemLocker = true;
        }else{
            isMemLocker = false;
            for (String lnum : nonmem.keySet()) {
                if (Integer.parseInt(nonmem.get(lnum).locknum) == LockerNumber) {
                    targetKey = lnum;

                    //test확인용출력
                    System.out.println("nonmem map에서 찾기 성공");
                    System.out.println(nonmem.get(lnum));
                    break;
                }
            }
        }


        //보관함 비밀번호 입력
        String pwd_prompt2 = "비밀번호 4자리를 입력하세요. ";
        boolean pwdCheck2 = pwdCheck(pwd_prompt2, isMemLocker, targetKey, 3);
        if (!pwdCheck2) { //보관함 비밀번호 입력 3회 실패시
            if(isLogin) Menu_1(); //이전 메뉴로 돌아가기
            else Menu_2();
            System.exit(0);
        }



        //보관함 비밀번호가 올바른 경우

        //시간 차이 구하기
        Date currentTime = StringToDate(Main.currentTimeString);
        Date startTime = StringToDate(LockerList.get(target).date);
        int timeDiff = (int) (currentTime.getTime() - startTime.getTime())/3600000;

        //추가 결제가 필요한 경우 : 현재시간 = 예약시간+4시간 초과인 경우
        if (timeDiff > 4){
            while(true){
                Print_AddPayPrompt(timeDiff, target);
                String yn = String.valueOf(sc.next());
                sc.nextLine();
                try{
                     // Y or y 말고 다른 것을 입력한 경우
                    if(!(Objects.equals(yn, "Y") || Objects.equals(yn, "y")))
                        throw new IllegalArgumentException();
                    break;
                }catch  (IllegalArgumentException e){
                    System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
                }
            }
            System.out.println("결제가 완료되었습니다.");
        }


       //수거완료
        String pwd_prompt3 = "물품 수거가 가능합니다. \n"+
                "\n* 보관하신 물품 수거 후에 수거완료를 위해 반드시 비밀번호 4자리를 입력해주세요.\n"+
                "수거완료 처리가 되지 않은 이용 건은 자동 초과 요금이 부과됩니다. \n";

        pwdCheck(pwd_prompt3, isMemLocker, targetKey, 0);



        //수거완료한 보관함 정보 삭제 & File Write
        LockerList.get(target).use = "0";
        LockerList.get(target).date = "-";
        if(isMemLocker){
            mem.get(targetKey).locknum = "-";
            mem.get(targetKey).lockPW = "-";
        }else{
            nonmem.remove(targetKey);
        }
        u.UserFileWrite(mem, nonmem);
        this.LockerFileWrite(LockerList);


        System.out.println("물품 수거가 완료되었습니다.");
        System.out.println("이용해주셔서 감사합니다.");
        System.exit(0);

         //*/

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
        int th=0;
        String LockerPwd;

        while (true) {
            if(chance!=0) {
                if (th > 3) {
                    System.out.println("비밀번호를 3회 틀리셨습니다. 메뉴로 돌아갑니다.");
                    return false;
                    //break;
                }
            }

            //보관함 비밀번호 입력
            System.out.println(prompt);
            System.out.print(">>");
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
                    if (!LockerPwd.equals(mem.get(targetKey).lockPW))
                        throw new IllegalAccessException();
                } else {
                    if (!LockerPwd.equals(nonmem.get(targetKey).lockPW))
                        throw new IllegalAccessException();
                }

                return true;
                //break;

            } catch (IllegalArgumentException e) {
                System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
            } catch (IllegalAccessException e) {
                System.out.println("비밀번호가 올바르지 않습니다.");


            }
        }
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
    private void Print_AddPayPrompt(int timeDiff, int target){
        /*
        addtional_payment_prompt를 출력하는 메서드
         */
        int additional=0;
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

        String str = "초과 시간에 따른 추가 금액 결제를 진행합니다. \n"+
                (timeDiff-4)*additional + "원 \n"+
                "결제하시려면 Y 또는 y를 입력해주세요. \n"+
                "———————————————————————————\n";

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


    public void deleteLockerBeforeDate(Date newDate) {

        String filename = "Locker.txt";

        ArrayList<String> lockersToDelete = new ArrayList<>();

        try (Scanner scan = new Scanner(new File(filename))) {
            while (scan.hasNextLine()) {
                String str = scan.nextLine();

                String[] temp = str.split(" ");

                // 날짜만 가져와 비교
                Date lockerDate;
                String dateStr = temp[3];
                System.out.println(dateStr);
                String[] dateStrTemp = new String[4];
                int num = 0;

                //이용 중인 보관함이 아니라서 날짜 부분이 "-" 일 경우
                if(temp[3].length() <= 1)
                {
                    LockerList.add(new Locker(temp[0].trim(), temp[1].trim(), temp[2].trim(), temp[3].trim(), temp[4].trim()));//저장구조에 기존 locker정보 저장
                    continue;
                }

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
                oldCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dateStrTemp[3]));
                oldCalendar.set(Calendar.MINUTE, 0);
                oldCalendar.set(Calendar.SECOND, 0);
                lockerDate = oldCalendar.getTime();

                // 예약 날짜가 입력 받은 날보다 앞일 경우에만 저장
                if(!(lockerDate.before(newDate))){
                    LockerList.add(new Locker(temp[0].trim(), temp[1].trim(), temp[2].trim(), temp[3].trim(), temp[4].trim()));//저장구조에 기존 locker정보 저장
                } else
                {
                    LockerList.add(new Locker(temp[0].trim(), temp[1].trim(), "0", "-", "0"));//저장구조에 보관/예약 정보를 수정해서 locker정보 저장
                    lockersToDelete.add(temp[0]);
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("파일 입력이 잘못되었습니다.");
        }

        UserManager tmpUserManager = new UserManager();
        tmpUserManager.deleteUserBeforeDate(lockersToDelete);
        LockerFileWrite(LockerList);
    }

}




