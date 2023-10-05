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
    public static void date_check(){
        System.out.print("현재 날짜와 시각(0~23시)을 10자리의 수로 공백 없이 입력해주세요. (ex.2023091517) : ");

        String today = scan.next();