import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserManager {

    //constructor
    public UserManager(){

    }
    //프로그램 최초 시작 시 user 데이터 txt파일로부터 불러오는 함수
    public void UserFileInput(Map<String,User> mem, Map<String,User> nonmem){
        String filename="./Locker/User.txt";
        try(Scanner scan=new Scanner(new File(filename))){
            while(scan.hasNextLine()) {
                String str=scan.nextLine();
                String[] temp=str.split(" ");
                if(temp[0].trim().equals("1")){
                    mem.put(temp[1].trim(),new User(temp[1].trim(),temp[2].trim(),temp[3].trim(),temp[4].trim()));//최초로 저장구조에 회원 user정보 저장
                }else{
                    nonmem.put(temp[1].trim(),new User(temp[1].trim(),temp[2].trim())); //비회원 user정보->보관함 사용중인 비회원만 정보 저장
                }
            }
        }catch(FileNotFoundException e){
            System.out.println("파일 입력이 잘못되었습니다.");
        }
    }

    //프로그램 종료 시 user 데이터 txt파일에 저장하는 함수
    public void UserFileWrite(Map<String,User> mem, Map<String,User> nonmem){
        try{
            File file = new File("./Locker/User.txt");
            if(!file.exists()){
                System.out.println("파일경로를 다시 확인하세요.");
            }else{
                FileWriter writer =new FileWriter(file, false);//기존 내용 없애고 쓰려면 false
                for (Map.Entry<String, User> entry : mem.entrySet()) {//회원 데이터 저장
                    writer.write(entry.getValue().toString());
                    writer.flush();
                }
                for (Map.Entry<String, User> entry : nonmem.entrySet()) {//비회원 데이터 저장
                    writer.write(entry.getValue().toString());
                    writer.flush();
                }
                writer.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
