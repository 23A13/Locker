import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.locks.Lock;

public class Locker {

    public int LockerNumber;
    public Date StartTime; //사용시작시각 (yyyymmddtt)
    public boolean isUsing;
    public int size;
    private int password;

    public int getpwd() { return password; }

    public void setpwd(int pwd){
        this.password = pwd;
    }

    //constructor
    public Locker() {
    }
    public Locker(int LockerNumber) {
        this.LockerNumber = LockerNumber;
        if (LockerNumber <= 8) size = 1;
        else if (LockerNumber <= 12) size = 2;
        else if (LockerNumber <= 16) size = 3;
    }

    public void print() {
        /*
         * 여기서 사물함 print
         */
        System.out.println("사물함 출력");
    }

    //보관함 비밀번호 설정


    public boolean BooingCheck(int LockerNumber){
        /*
         * 파일 입력, locker 예약 확인
         */
        if(LockerNumber==5)
            return true;

        return false;

    }

}
