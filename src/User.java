public class User {
    String member; //회원 비회원 여부-> 갑자기 저장할 필요가 있나 싶음..흠흠띠
    String memberID; //회원ID
    String memberPW; //회원 비밀번호
    String locknum; //사용중(예약중)인 보관함 번호
    String lockPW; //보관함 비밀번호

    //constructor
    public User() {
    }

    //회원 생성자
    public User(String memberID, String memberPW, String locknum, String lockPW) {
        this.member = "1";
        this.memberID = memberID;
        this.memberPW = memberPW;
        this.locknum = locknum;
        this.lockPW = lockPW;
    }

    //비회원 생성자
    public User(String locknum, String lockPW) {
        this.member = "0";
        this.memberID = null;
        this.memberPW = null;
        this.locknum = locknum;
        this.lockPW = lockPW;
    }

}
