import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static java.lang.System.exit;

public class LockerManager {

    //로그인한 사용자 아이디
    String loguser;


    static ArrayList<Locker> LockerList = new ArrayList<>(); //Locker정보 저장 구조
    Map<String, User> mem = new HashMap<>(); //회원 정보 저장 구조
    Map<String, User> nonmem = new HashMap<>(); //비회원 정보 저장 구조

    UserManager usermanager = new UserManager();


    //class 선언
    static Scanner sc = new Scanner(System.in);
    static Locker locker = new Locker();
    UserManager userManager = new UserManager();

    UserManager u=new UserManager();

    //constructor

    public LockerManager(String loguser)
    {
        this.loguser = loguser;
    }

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


    //Constructor
    public LockerManager() {

    }
    public LockerManager(String userId, String userPassword) {

    }


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
        /*
        //arrayList 잘 저장됐나 확인용-나중에 삭제
        for(int i=0;i<LockerList.size();i++){
            System.out.println(LockerList.get(i).date);
        }

        //user정보 저장 잘되나 확인용-나중에 삭제
        userManager.UserFileInput(mem,nonmem);
        System.out.println(mem.get("Test0823"));
        System.out.println(nonmem.get("01"));
        nonmem.put("01",new User(nonmem.get("01").locknum,"1234"));
        userManager.UserFileWrite(mem,nonmem);*/

        //로그인 됐는지 확인용 지워야함
        System.out.println(loguser +"님 환영합니다.\n");


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
                break;
            default:
                break;
        }

        LockerFileWrite();
        userManager.UserFileWrite();
    }


    public void menu_2() {

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
                Storage();
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

    private void Pickup() {
    }

    private void Storage() {
    }

    public static void logout() {
        return;
    }
    private void Booking() {
    }

    // 오늘 날짜 입력 시 Locker 데이터 수정 메소드 (날짜는 지났는데 예약 확정이 안된 이용 내역 삭제)
    public void deleteLockerBeforeDate(Date newDate) {

        String filename = "Locker.txt";

        ArrayList<String> lockersToDelete = new ArrayList<>();
        ArrayList<Locker> tmpLockerList = new ArrayList<>();
        dateLockerFileInput(tmpLockerList);

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

}