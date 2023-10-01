public class User {
    int member; //회원 비회원 여부
    String memberID; //회원ID
    int memberPW; //회원 비밀번호
    int locknum; //사용중(예약중)인 보관함 번호
    int lockPW; //보관함 비밀번호

    //constructor
    public User(){}

    //회원 생성자
    public User(int member,String memberID,int memberPW,int locknum,int lockPW){
        this.member=member;
        this.memberID=memberID;
        this.memberPW=memberPW;
        this.locknum=locknum;
        this.lockPW=lockPW;
    }

    //비회원 생성자
    public User(int member,int locknum,int lockPW){
        this.member=member;
        this.locknum=locknum;
        this.lockPW=lockPW;
    }
}
