import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    public static String currentTimeString;
    public static Date currentTimeDate;
    static Scanner scan = new Scanner(System.in);
    public static void main(String[] args) {

        System.out.println("정상 실행 확인");

        date_check();

        UserManager user = new UserManager();
        user.menu1();


    }

    // 날짜 체크 함수
    public static void date_check(){
        System.out.println("현재 날짜와 시각(0~23시)을 10자리의 수로 공백 없이 입력해주세요. (ex.2023091517) :\n");

        String today = scan.next();

        boolean flag = todayCheck(today);

        while(!flag) {
            System.out.print("현재 날짜와 시각(0~23시)을 10자리의 수로 공백 없이 입력해주세요. (ex.2023091517) :");
            today = scan.next();
            flag =	todayCheck(today);
            if(flag) {
                break;
            }
        }

        scan.nextLine();

    }

    private static boolean todayCheck(String today) {

        boolean flag = false;
        Date oldDate = null;	// 기존 날짜
        Date newDate = null;	// 새로 입력 받은 날짜

        String oldD = null; 	// 기존 날짜 데이터 저장용
        String dTrim = today.trim();	//입력 받은 string의 공백 제거

        File timeFile = new File("Date.txt");

        // 글자 수가 10이 아닐 경우 false 반환
        if(dTrim.length() != 10) {
            System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
            System.out.println();
            return flag;
        }

        //기존 날짜 불러오기
        try(Scanner scan = new Scanner(timeFile))
        {
            String str = null;
            String[] temp = new String[4];

            while(scan.hasNextLine())
            {
                str = scan.nextLine();
                oldD = str;
                int num = 0;

                // 년, 월, 일, 시간 4구간으로 잘라서 배열에 저장
                for(int i=0; i<4; i++) {
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
            oldCalendar.set(Calendar.MINUTE, 0);
            oldCalendar.set(Calendar.SECOND, 0);
            oldDate = oldCalendar.getTime();

        }
        catch(FileNotFoundException e)
        {
            System.out.println("파일 명을 확인하세요.");
        }

        // 위와 같은 방식으로 새로 입력한 날짜 확인
        try {
            int num = 0;
            String[] temp = new String[4];

            for(int i=0; i<4; i++) {
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
            newCalendar.set(Calendar.YEAR, Integer.parseInt("20" + temp[0]));
            newCalendar.set(Calendar.MONTH, Integer.parseInt(temp[1]) - 1); // subtract 1 from month because Calendar class starts at 0
            newCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(temp[2]));
            newCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(temp[3]));
            newCalendar.set(Calendar.MINUTE, 0);
            newCalendar.set(Calendar.SECOND, 0);
            newDate = newCalendar.getTime();

            //내가 임의로 일단 선언쓰..
            currentTimeDate = newDate;

        } catch(Exception e) {
            System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.\n");
            System.out.println();
            return flag;
        }

        // 기존 날짜가 입력 받은 날보다 이전일 경우 true
        if(oldDate.before(newDate)){
            flag = true;
        } else if(oldDate.equals(newDate)){
            flag = false;	// 같은 날일 경우
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
        beforeRemove(dTrim, oldD);

        // 예약 내역 수정 함수
        deleteLinesBeforeDate(dTrim);

        // 오늘 날짜 변수에 string 값 저장
        currentTimeString = dTrim;

        System.out.println();

        // flag를 이용해 반복 조절
        return flag;
    }

    private static void deleteLinesBeforeDate(String dTrim) {
    }

    private static void beforeRemove(String dTrim, String oldD) {
    }


}