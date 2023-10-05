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