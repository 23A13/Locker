import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class UserManager {

    static Scanner scan = new Scanner(System.in);

    // 비회원, 회원 정보 저장
    Map<String, User> memMap = new HashMap<>();
    Map<String, User> nonmemMap = new HashMap<>();

    //로그인 중인 회원 아이디
    User loguser = null;

    //constructor
    public UserManager(){
        // 처음 시작에
        this.UserFileInput();
    }

    //프로그램 최초 시작 시 user 데이터 txt파일로부터 불러오는 함수
    public void UserFileInput(){
        String filename="User.txt";
        try(Scanner scan=new Scanner(new File(filename))){
            while(scan.hasNextLine()) {
                String str=scan.nextLine();
                String[] temp=str.split(" ");
                if(temp[0].trim().equals("1")){
                    memMap.put(temp[1].trim(),new User(temp[1].trim(),temp[2].trim(),temp[3].trim(),temp[4].trim()));//최초로 저장구조에 회원 user정보 저장
                }else if (temp[0].trim().equals("0")){
                    nonmemMap.put(temp[1].trim(),new User(temp[1].trim(),temp[2].trim())); //비회원 user정보->보관함 사용중인 비회원만 정보 저장
                }else
                {
                    continue;
                }
            }
        }catch(FileNotFoundException e){
            System.out.println("파일 입력이 잘못되었습니다.");
        }
    }

    //프로그램 종료 시 user 데이터 txt파일에 저장하는 함수
    public void UserFileWrite(){
        try{
            File file = new File("User.txt");
            if(!file.exists()){
                System.out.println("파일경로를 다시 확인하세요.");
            }else{
                FileWriter writer =new FileWriter(file, false);//기존 내용 없애고 쓰려면 false
                for (Map.Entry<String, User> entry : memMap.entrySet()) {//회원 데이터 저장
                    writer.write(entry.getValue().toString());
                    writer.flush();
                }
                for (Map.Entry<String, User> entry : nonmemMap.entrySet()) {//비회원 데이터 저장
                    writer.write(entry.getValue().toString());
                    writer.flush();
                }
                writer.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    // 날짜/시간 입력 후에 지난 내역들 파일에서 삭제하는 메소드
    public void deleteUserBeforeDate(ArrayList<String> lockersToDelete)
    {
        try{
            File file = new File("User.txt");
            if(!file.exists()){
                System.out.println("파일경로를 다시 확인하세요.");
            }else{
                FileWriter writer =new FileWriter(file, false);//기존 내용 없애고 쓰려면 false
                for (Map.Entry<String, User> entry : memMap.entrySet()) {//회원 데이터 저장
                    // 지워야 할 이용 내역이 있다면
                    if(lockersToDelete.contains(entry.getValue().locknum))
                    {
                        entry.getValue().locknum = "-";
                        entry.getValue().lockPW = "-";
                        writer.write(entry.getValue().toString());
                        writer.flush();
                    } else // 없다면
                    {
                        writer.write(entry.getValue().toString());
                        writer.flush();
                    }
                }
                for (Map.Entry<String, User> entry : nonmemMap.entrySet()) {//비회원 데이터 저장
                    // 지워야 할 이용이 아니면
                    if(!(lockersToDelete.contains(entry.getValue().locknum)))
                    {
                        writer.write(entry.getValue().toString());
                        writer.flush();
                    }
                }
                writer.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void menu1() {
        // TODO Auto-generated method stub

        String choice = null;

        while(true) {

            //메뉴 1에서 선택한 선택지를 벗어날 지 말지 결정하는 flag
            boolean menuEndFlag = false;

            try {
                while(!menuEndFlag)
                {
                    System.out.println("1.회원가입\n2.회원\n3.비회원\n4.종료");

                    choice = scan.nextLine().trim();

                    if(choice.equals("1"))
                    {
                        menuEndFlag = signup();
                    }
                    else if(choice.equals("2"))
                    {
                        menuEndFlag = login();
                        System.out.println();

                        if(menuEndFlag)
                        {
                            LockerManager memLockerManager = new LockerManager(loguser.memberID);
                            memLockerManager.Menu_Mem();
                        }

                        menuEndFlag = false;
                        loguser = null; // (회원 메뉴에서 로그아웃 시 로그인 중인 회원 정보를 null로)
                        // (이렇게 하면 로그아웃 하면 메뉴 1로 돌아가게 됨. 아마도..?)
                        // (민진님은 회원 메뉴에서 로그아웃 하면 그냥 return 되게 만드시면 될 것 같습니다!)
                    }
                    else if(choice.equals("3"))
                    {
                        LockerManager nonMemLockerManager = new LockerManager();
                        nonMemLockerManager.menu_2(); // 비회원 메뉴 출력

                    }
                    else if(choice.equals("4"))
                    {
                        // 프로그램 종료 메소드
                        menuEndFlag = programEnd();
                    }
                    else
                    {
                        System.out.println("올바른 입력이 아닙니다");
                    }
                }

            }
            catch(InputMismatchException e)
            {
                System.out.println("올바른 입력이 아닙니다");
                scan.nextLine();
            }
        }
    }

    private boolean programEnd() {
        System.out.print("종료하시려면 Y 또는 y를 입력해주세요 >> ");
        String endInput = scan.nextLine().trim();
        if(endInput.equals("Y")||endInput.equals("y"))
        {
            // 파일 저장 후 저장
            this.UserFileWrite();
            System.out.println("프로그램을 종료합니다.");
            System.exit(0);
        }

        //Y/y 가 아닐 경우 프로그램 종료 하지 않고 false 리턴
        return false;
    }

    //회원가입 메소드
    public boolean signup() {

        System.out.print("—— 회원가입 ——\n" +
                "* 이전 메뉴로 돌아가려면 Q 또는 q를 입력하세요.\n\n" +
                "아이디를 설정하세요. 영문자, 숫자를 사용할 수 있습니다.\n" +
                "아이디 >> ");

        String id = scan.nextLine();

        if(id.equals("q")||id.equals("Q"))
        {
            return false;
        }

        if (memMap.get(id) != null) {
            System.out.println("이미 사용 중인 아이디입니다");
            return false;
        }

        if (!isNumberLetterCheck(id)) {
            System.out.println("아이디는 영문자와 숫자만 사용 가능합니다.");
            return false;
        }

        System.out.print("* 영문자, 숫자를 조합하여 비밀번호를 만드세요 (8~25자)\n" +
                "비밀번호 : ");

        String pw = scan.nextLine();

        if(!(pw.length() >= 8&&pw.length() <=25))
        {
            System.out.println("비밀번호 형식이 올바르지 않습니다.");
            return false;
        }

        if (!isNumberLetterCheck(pw)) {
            System.out.println("비밀번호 형식이 올바르지 않습니다.");
            return false;
        }

        System.out.print("비밀번호 재확인>> ");

        String pwRe = scan.nextLine();

        while(!pwRe.equals(pw))
        {
            System.out.println("비밀번호가 일치하지 않습니다. 다시 시도해 보세요.");
            System.out.print("비밀번호 재확인>> ");
            pwRe = scan.nextLine();
        }

        User newSingUpUser = new User(id, pw, "-", "-");

        memMap.put(id, newSingUpUser);
        this.UserFileWrite();

        System.out.println("회원가입이 완료되었습니다.\n");

        return true;

    }

    //아이디, 비밀번호 형식 확인 메소드
    private boolean isNumberLetterCheck(String id) {
        // 문자열의 각 문자를 for 루프로 돌아가면서 확인
        for (char c : id.toCharArray()) {
            if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || Character.isDigit(c))) {
                return false; // 영문자나 숫자가 아닌 문자가 발견되면 false 반환
            }
        }
        return true; // 문자열에 영문자와 숫자 외의 문자가 없으면 true 반환
    }

    public boolean login() {
        System.out.print("——ID 로그인——\n" +
                "* 이전 메뉴로 돌아가려면 Q 또는 q를 입력하세요.\n" +
                "아이디를 입력하세요.\n" +
                "아이디>>");

        String id = scan.nextLine();

        if(id.equals("q")||id.equals("Q"))
        {
            return false;
        }

        System.out.print("\n비밀번호 >>");

        String pw = scan.nextLine();

        if(memMap.get(id) != null) {
            //로그인 성공 시 loguser에 로그인 성공한 User 객체 대입
            if(memMap.get(id).memberPW.equals(pw))
                this.loguser = memMap.get(id);

            else {
                System.out.println("아이디 또는 비밀번호가 올바르지 않습니다.");
                return false;
            }
        }
        else {
            System.out.println("아이디 또는 비밀번호가 올바르지 않습니다.");
            return false;
        }
        return true;
    }
}
