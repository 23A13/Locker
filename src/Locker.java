import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Locker {

    String locknum; //보관함 번호
    String locksize; //보관함 크기(0/1/2-S/M/L)
    String use; //사용여부(0/1/2/3/4-미사용/사용중/예약중/임시폐쇄예정/임시폐쇄중)
    String date; //날짜시각
    String confirmbook; //예약확정 여부(0/1-미확/확정)

    //2차때 추가된 변수들
    String closeddatestart = "-";//폐쇄시작 시각
    String closeddatefinish = "-";//폐쇄종료 시각
    //

    boolean iscanFp = false;
    long timediffMinutes = 0;

    //constructor
    public Locker() {
    }

    public Locker(String locknum, String locksize, String date) {
        this.locknum = locknum;
        this.locksize = locksize;
        this.use = "0";
        this.date = date;
        this.confirmbook = "0";
    }

    public Locker(String locknum, String locksize, String use, String date, String confirmbook) {
        this.locknum = locknum;
        this.locksize = locksize;
        this.use = use;
        this.date = date;
        this.confirmbook = confirmbook;
    }

    public Locker(String locknum, String locksize, String use, String date, String confirmbook, String closeddatestart, String closeddatefinish) {
        this.locknum = locknum;
        this.locksize = locksize;
        this.use = use;
        this.date = date;
        this.confirmbook = confirmbook;
        this.closeddatestart = closeddatestart;
        this.closeddatefinish = closeddatefinish;
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

    public static void print() {


        String fileName = "../Locker/Locker.txt";
        String print = "";
        String close = "";

        try {

            //수정
            String filePath = "../Locker/Locker.txt";
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            String line;
            close += "임시폐쇄 예정: ";


            while ((line = bufferedReader.readLine()) != null) {


                String[] parts = line.split(" ");

                print += "<"+parts[0]+"> ";

                if(parts[1].equals("0")) print += "S ";
                else if(parts[1].equals("1")) print += "M ";
                else print += "L ";

                if(parts[2].equals("0")) print+= "미사용 ";
                else if(parts[2].equals("1")) print+= "사용중 ";
                else if(parts[2].equals("2")) print+= "예약중 ";
                else if(parts[2].equals("3")){
                    print+= "미사용 ";
                    close += "<"+parts[0]+"> ";
                    //close += "("+parts[5]+" ~ "+parts[6]+")\n";
                }
                else if(parts[2].equals("4")){
                    print += "임시폐쇄중 ";
                    //string += "("+parts[5]+" ~ "+parts[6]+")";
                }


                print += "\n";
            }



            bufferedReader.close();

        } catch (FileNotFoundException e) {
            System.err.println("파일을 찾을 수 없습니다: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("파일을 읽는 동안 오류가 발생했습니다: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("예외가 발생했습니다: " + e.getMessage());
        }

        print += """
                ————————————————————————————————————————
                *임시폐쇄 예정 기간 10시간 이전부터는 보관이 불가합니다.
                """;

        print += close;
        System.out.println(print);
        System.out.println();
    }
}
