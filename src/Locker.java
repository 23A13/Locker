import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Locker {

    String locknum; //보관함 번호
    String locksize; //보관함 크기(0/1/2-S/M/L)
    String use; //사용여부(0/1/2-미사용/사용중/예약중)
    String date; //날짜시각
    String confirmbook; //예약확정 여부(0/1-미확/확정)
    boolean iscanFp = false;
    int timediffMinutes = 0;

    //constructor
    public Locker() {
    }
    public Locker(String locknum, String locksize,String date){
        this.locknum=locknum;
        this.locksize=locksize;
        this.use="0";
        this.date=date;
        this.confirmbook="0";
    }
    public Locker(String locknum, String locksize,String use,String date,String confirmbook){
        this.locknum=locknum;
        this.locksize=locksize;
        this.use=use;
        this.date=date;
        this.confirmbook=confirmbook;
    }

    public String getLocknum() {
        return locknum;
    }

    public void setLocknum(String locknum) {
        this.locknum = locknum;
    }

    public String getLocksize() {
        return locksize;
    }

    public void setLocksize(String locksize) {
        this.locksize = locksize;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getConfirmbook() {
        return confirmbook;
    }

    public void setConfirmbook(String confirmbook) {
        this.confirmbook = confirmbook;
    }

    public static void print(){
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("| 01        | 02        | 03        | 04        | 13               | 14               |");
        System.out.println("|           |           |           |           |                  |                  |");
        System.out.println("|           |           |           |           |                  |                  |");
        System.out.println("|           |           |           |           |                  |                  |");
        System.out.println("|-----------------------------------------------|                  |                  |");
        System.out.println("| 05        | 06        | 07        | 08        |                  |                  |");
        System.out.println("|           |           |           |           |                  |                  |");
        System.out.println("|           |           |           |           |                  |                  |");
        System.out.println("|           |           |           |           |                  |                  |");
        System.out.println("|-----------|-----------|-----------|-----------|------------------|------------------|");
        System.out.println("| 09        | 10        | 11        | 12        | 15               | 16               |");
        System.out.println("|           |           |           |           |                  |                  |");
        System.out.println("|           |           |           |           |                  |                  |");
        System.out.println("|           |           |           |           |                  |                  |");
        System.out.println("|           |           |           |           |                  |                  |");
        System.out.println("|           |           |           |           |                  |                  |");
        System.out.println("|           |           |           |           |                  |                  |");
        System.out.println("|           |           |           |           |                  |                  |");
        System.out.println("|           |           |           |           |                  |                  |");
        System.out.println("---------------------------------------------------------------------------------------");

        String fileName = "../Locker/User.txt";

        try {

            //수정
            String filePath = "../Locker/User.txt";
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

//            System.out.print("이용중인 보관함 번호:");


            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            String line;

            boolean found = false;

            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[0].equals("1")) {
                    if(!parts[3].equals("-"))
                    {
                        String lockerNumber = parts[3];
                        System.out.println("이용중인 보관함 번호: " + lockerNumber);
                        found = true;
                    }
                }
                else if (parts[0].equals("0")) {
                    String lockerNumber = parts[1];
                    System.out.println("이용중인 보관함 번호: " + lockerNumber);
                    found = true;
                }
            } bufferedReader.close();

            if (!found) {
                System.out.print("이용 중인 보관함이 존재하지 않습니다.");
            }
        } catch (FileNotFoundException e) {
            System.err.println("파일을 찾을 수 없습니다: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("파일을 읽는 동안 오류가 발생했습니다: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("예외가 발생했습니다: " + e.getMessage());
        }
        System.out.println();
    }
}
