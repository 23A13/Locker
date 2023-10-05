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

    //로그인한 사용자 아이디
    String loguser;


    static ArrayList<Locker> LockerList = new ArrayList<>(); //Locker정보 저장 구조
    Map<String, User> mem = new HashMap<>(); //회원 정보 저장 구조
    Map<String, User> nonmem = new HashMap<>(); //비회원 정보 저장 구조

    UserManager usermanager = new UserManager();

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
                exit(0);
                break;
            default:
                break;
        }

        LockerFileWrite();
        userManager.UserFileWrite();

    }
    //비회원메뉴
    public void menu_nonMem() {

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


    //예약 메소드
    public void Booking(){

        //기존 예약 내역 확인
        if(!mem.get(loguser).locknum.equals("-")){
            System.out.println("이미 예약 내역이 존재합니다.\n\n");
            Menu_Mem();
            exit(0);
        }
