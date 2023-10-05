import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class UserManager {

    static Scanner scan = new Scanner(System.in);

    // 비회원, 회원 정보 저장
    static Map<String, User> memMap = new HashMap<>();
    static Map<String, User> nonmemMap = new HashMap<>();

    //로그인 중인 회원 아이디
    User loguser = null;

    //constructor
    public UserManager(){
        // 처음 시작에
        this.UserFileInput();
    }

    // 날짜시간 입력 시 예약 내역 수정용
    public UserManager(boolean flag)
    {

    }