import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

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
    public static void date_check() {
        System.out.print("현재 날짜와 시각(00:00~23:59)을 12자리의 수로 공백 없이 입력해주세요. (ex.202309151730) : ");

        String today = scan.nextLine();

        boolean flag = todayCheck(today);

        while (!flag) {
            System.out.print("현재 날짜와 시각(00:00~23:59)을 12자리의 수로 공백 없이 입력해주세요. (ex.202309151730) : ");
            today = scan.nextLine();
            flag = todayCheck(today);
            if (flag) {
                break;
            }
        }

        //scan.nextLine();

    }

    private static boolean todayCheck(String today) {

        boolean flag = false;
        Date oldDate = null;    // 기존 날짜
        Date newDate = null;    // 새로 입력 받은 날짜

        String oldD = null;    // 기존 날짜 데이터 저장용
        String dTrim = today.trim();    //입력 받은 string의 공백 제거

        File timeFile = new File("../Locker/Date.txt");

        // 글자 수가 12가 아닐 경우 false 반환
        if (dTrim.length() != 12) {
            System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.");
            System.out.println();
            return flag;
        }

        // 2050년 이상인지 아닌지 확인
        int testYear = Integer.parseInt(dTrim.substring(0, 4));
        if (testYear > 2050) {
            System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.");
            System.out.println();
            return flag;
        }

        //기존 날짜 불러오기
        try (Scanner scan = new Scanner(timeFile)) {
            String str = null;
            String[] temp = new String[5];

            while (scan.hasNextLine()) {
                str = scan.nextLine();
                oldD = str;
                int num = 0;

                // 년, 월, 일, 시간, 분 5구간으로 잘라서 배열에 저장
                for (int i = 0; i < 5; i++) {
                    if (i == 0) {
                        temp[i] = str.substring(num, num + 4);
                        num += 4;
                    } else {
                        temp[i] = str.substring(num, num + 2);
                        num += 2;
                    }
                }
            }


            // 파일이 비었을 경우는 고려 안함
            // 처음부터 파일에 일단 날짜 하나 입력 해놓고 배포하는 방식

            // 배열에 저장한 숫자 이용해서 날짜 객체 생성
            Calendar oldCalendar = new GregorianCalendar(Integer.parseInt(temp[0]),
                    Integer.parseInt(temp[1]) - 1, Integer.parseInt(temp[2]));
            oldCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(temp[3]));
            //00~09는 한 자리 수로 바꾸기
            oldCalendar.set(Calendar.MINUTE, (Integer.parseInt(temp[4], 10)));

            oldCalendar.set(Calendar.SECOND, 0);
            oldCalendar.set(Calendar.MILLISECOND, 0);
            oldDate = oldCalendar.getTime();

        } catch (FileNotFoundException e) {
            System.out.println("파일 명을 확인하세요.");
        }

        // 위와 같은 방식으로 새로 입력한 날짜 확인
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
            newCalendar.set(Calendar.YEAR, Integer.parseInt(temp[0]));
            newCalendar.set(Calendar.MONTH, Integer.parseInt(temp[1]) - 1); // subtract 1 from month because Calendar class starts at 0
            newCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(temp[2]));
            newCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(temp[3]));
            newCalendar.set(Calendar.MINUTE, (Integer.parseInt(temp[4], 10)));
            newCalendar.set(Calendar.SECOND, 0);
            newCalendar.set(Calendar.MILLISECOND, 0);
            newDate = newCalendar.getTime();

        } catch (Exception e) {
            System.out.println("올바른 입력이 아닙니다. 다시 한 번 입력해주세요.");
            System.out.println();
            return flag;
        }

        // 기존 날짜가 입력 받은 날보다 이전일 경우 true
        if (oldDate.before(newDate)) {
            flag = true;
        } else if (oldDate.equals(newDate)) {
            flag = true;    // 같은 날일 경우
        } else {
            flag = false;    // 이외의 모든 경우
        }

        if (flag) {
            // 날짜 올바름
            System.out.println("올바른 입력입니다.");
        } else {
            // 날짜 틀림
            System.out.println("지난 날짜입니다. 다시 한 번 입력해주세요.");
            System.out.println();
            return flag;
        }

        // Date.txt 파일에 기존 날짜 지우고 새로 날짜 입력
        beforeDateRemove(dTrim, oldD);

        // 예약 내역 & 임시폐쇄 보관함 수정 함수
        deleteUserLockerBeforeDate(newDate);

        // 오늘 날짜 변수에 string, Date 값 저장
        currentTimeString = dTrim;
        currentTimeDate = newDate;


        System.out.println();

        // flag를 이용해 반복 조절
        return flag;
    }

    private static void deleteUserLockerBeforeDate(Date newDate) {

        LockerManager tmpLockerManager = new LockerManager();
        tmpLockerManager.deleteLockerBeforeDate(newDate);
    }

    private static void beforeDateRemove(String dTrim, String oldD) {
        try {
            // txt 파일 읽어오기
            BufferedReader reader = new BufferedReader(new FileReader("../Locker/Date.txt"));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
            reader.close();

            // 읽어온 파일에서 기존 날짜 찾아서 새로운 날짜로 대체하기
            String updatedContent = content.toString().replace(oldD, dTrim);

            // 새로 쓰인 내용을 txt 파일에 입력하기
            BufferedWriter writer = new BufferedWriter(new FileWriter("../Locker/Date.txt"));
            writer.write(updatedContent);
            writer.close();

        } catch (Exception e) {
            System.out.println("파일에 문제가 생겼습니다.");
        }
    }


}