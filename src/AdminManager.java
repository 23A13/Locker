import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Lock;

import static java.lang.Integer.parseInt;

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
                temporary_closure();
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

    public void temporary_closure(){

        //임시폐쇄 함수에서 필요한 프롬포트들
        String temporary_closure_prompt = """
                --------------------------------------------------------------
                물품을 임시폐쇄할 날짜와 시각을 12자리의 수로 공백 없이 입력해주세요. (ex. 202309151730)
                                
                * 이전 메뉴로 돌아가려면 Q 또는 q를 입력하세요.
                --------------------------------------------------------------
               """;

        //임시폐쇄 함수에서 필요한 변수들

        int closuredate = 0; //임시 폐쇄 기간
        int closureLockerNum = 0; //임시 폐쇄 보관함

        int flow = 1; //흐름에 따라 값 변경 1. 임시 폐쇄 기간 입력 2. 임시 폐쇠 보관함 선택 3. 보관함 번호 재확인 4. 완료


        //1. 임시 폐쇄 기간 입력
        if(flow == 1){
            while(true){
                System.out.print(temporary_closure_prompt);
                System.out.print(">>");

                String strclosuredate; //Q, q 구분을 위한 string값
                strclosuredate = sc.nextLine();

                //예외1. 과거 예외2. 형식 처리
                if(closure_DateCheck(strclosuredate)){
                    closuredate = Integer.parseInt(strclosuredate);
                    flow = 2;
                    break;
                }
            }
        }

        //2. 임시 폐쇠 보관함 선택
        if(flow == 2){
            while(true){

                printAdminLocker();
                System.out.print(">>");

                closureLockerNum = sc.nextInt();
                //sc.nextLine();

                /*
                //예외1. 과거 예외2. 형식 처리
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

                 */
            }


        }





    }

    private static boolean closure_DateCheck(String today) {

        /*
        boolean flag = false;
        Date oldDate = null;	// 기존 날짜
        Date newDate = null;	// 새로 입력 받은 날짜

        String oldD = null; 	// 기존 날짜 데이터 저장용
        String dTrim = today.trim();	//입력 받은 string의 공백 제거

        File timeFile = new File("../Locker/Date.txt");

        // 글자 수가 12가 아닐 경우 false 반환
        if(dTrim.length() != 12) {
            System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.");
            System.out.println();
            return flag;
        }

        // 2050년 이상인지 아닌지 확인
        int testYear = Integer.parseInt(dTrim.substring(0,4));
        if(testYear > 2050) {
            System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.");
            System.out.println();
            return flag;
        }

        //기존 날짜 불러오기
        try(Scanner scan = new Scanner(timeFile))
        {
            String str = null;
            String[] temp = new String[5];

            while(scan.hasNextLine())
            {
                str = scan.nextLine();
                oldD = str;
                int num = 0;

                // 년, 월, 일, 시간, 분 5구간으로 잘라서 배열에 저장
                for(int i=0; i<5; i++) {
                    if(i==0)
                    {
                        temp[i] = str.substring(num, num+4);
                        num += 4;
                    }
                    else
                    {
                        temp[i] = str.substring(num, num+2);
                        num += 2;
                    }
                }
            }




            // 파일이 비었을 경우는 고려 안함
            // 처음부터 파일에 일단 날짜 하나 입력 해놓고 배포하는 방식

            // 배열에 저장한 숫자 이용해서 날짜 객체 생성
            Calendar oldCalendar = new GregorianCalendar(Integer.parseInt(temp[0]),
                    Integer.parseInt(temp[1])-1, Integer.parseInt(temp[2]));
            oldCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(temp[3]));
            //00~09는 한 자리 수로 바꾸기
            oldCalendar.set(Calendar.MINUTE, (Integer.parseInt(temp[4], 10)));

            oldCalendar.set(Calendar.SECOND, 0);
            oldCalendar.set(Calendar.MILLISECOND, 0);
            oldDate = oldCalendar.getTime();

        }
        catch(FileNotFoundException e)
        {
            System.out.println("파일 명을 확인하세요.");
        }

        // 위와 같은 방식으로 새로 입력한 날짜 확인
        try {
            int num = 0;
            String[] temp = new String[5];

            for(int i=0; i<5; i++) {
                if(i==0)
                {
                    temp[i] = dTrim.substring(num, num+4);
                    num += 4;
                }
                else
                {
                    temp[i] = dTrim.substring(num, num+2);
                    num += 2;
                }

            }

            // 달력에 존재하는 날짜인지 확인
            Calendar newCalendar = new GregorianCalendar();
            newCalendar.setLenient(false); // set leniency mode to strict
            newCalendar.set(Calendar.YEAR, Integer.parseInt(temp[0]));
            newCalendar.set(Calendar.MONTH, Integer.parseInt(temp[1]) - 1); // subtract 1 from month because Calendar class starts at 0
            newCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(temp[2]));
            newCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(temp[3]));
            newCalendar.set(Calendar.MINUTE, (Integer.parseInt(temp[4], 10)));
            newCalendar.set(Calendar.SECOND, 0);
            newCalendar.set(Calendar.MILLISECOND, 0);
            newDate = newCalendar.getTime();

        } catch(Exception e) {
            System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.");
            System.out.println();
            return flag;
        }

        // 기존 날짜가 입력 받은 날보다 이전일 경우 true
        if(oldDate.before(newDate)){
            flag = true;
        } else if(oldDate.equals(newDate)){
            flag = true;	// 같은 날일 경우
        } else {
            flag = false;	// 이외의 모든 경우
        }

        if(flag) {
            // 날짜 올바름
            System.out.println("올바른 입력입니다.");
        }
        else {
            // 날짜 틀림
            System.out.println("지난 날짜입니다. 다시 한 번 입력해주세요.");
            System.out.println();
            return flag;
        }

        // Date.txt 파일에 기존 날짜 지우고 새로 날짜 입력
        beforeDateRemove(dTrim, oldD);

        // 예약 내역 수정 함수
        deleteUserLockerBeforeDate(newDate);

        // 오늘 날짜 변수에 string, Date 값 저장
        currentTimeString = dTrim;
        currentTimeDate = newDate;


        System.out.println();

        // flag를 이용해 반복 조절
        return flag;
        */
        return true;
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
