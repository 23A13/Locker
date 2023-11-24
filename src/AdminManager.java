import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Lock;

public class AdminManager {
    static Scanner sc = new Scanner(System.in);

    public AdminManager()
    {
    }

    static int count=0;

    UserManager u = new UserManager();
    LockerManager l = new LockerManager();



    public void menu()
    {
        if(count==0)
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

        while(true){

            try{
                System.out.print(">>");
                number = sc.nextInt();
                sc.nextLine();

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
                // 강제수거 메소드
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

    public void printAdminLocker(){
        System.out.println("---------------------- 보관함 목록 ----------------------");
        String size=null;
        int timeDiff = 0;
        String iscanforce = "강제수거 불가능";
        for(Locker lc : l.LockerList){
            if(lc.locksize=="0")
                size = "S";
            else if(lc.locksize == "1")
                size = "M";
            else if(lc.locksize == "2")
                size = "L";

            Date currentTime = l.StringToDate(Main.currentTimeString);
            Date startTime = l.StringToDate(lc.date);
            long timeDiffMillis = currentTime.getTime() - startTime.getTime();
            int timeDiffMinutes = (int) (timeDiffMillis / (60 * 1000));
            int timeDiffHours = (int)(Math.ceil((double) timeDiffMillis / (60 * 60 * 1000)));
            timeDiff = (int) (currentTime.getTime() - startTime.getTime())/3600000;

            if (timeDiff > 10) {
                iscanforce = "강제수거 가능";
                lc.iscanFp = true;
            }

            System.out.println(lc.locknum+"번 / "+size+" / "+lc.date+" / "+iscanforce);
        }
    }
    public void ExitWrite(){
        u.UserFileWrite();
        l.LockerFileWrite();
        System.exit(0);
    }
}
