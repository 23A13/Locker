public class User {
    String member; //회원 비회원 여부-> 갑자기 저장할 필요가 있나 싶음..흠흠띠
    String memberID; //회원ID
    String memberPW; //회원 비밀번호
    String locknum; //사용중(예약중)인 보관함 번호
    String lockPW; //보관함 비밀번호
    String cannotUntil="-"; //회원 예약 패널티 날짜

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

    public User(String memberID, String memberPW, String locknum, String lockPW,String cannotUntil) {
        this.member = "1";
        this.memberID = memberID;
        this.memberPW = memberPW;
        this.locknum = locknum;
        this.lockPW = lockPW;
        this.cannotUntil=cannotUntil;
    }

    //비회원 생성자
    public User(String locknum, String lockPW) {
        this.member = "0";
        this.memberID = null;
        this.memberPW = null;
        this.locknum = locknum;
        this.lockPW = lockPW;
    }

    //Getter & Setter
    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getMemberPW() {
        return memberPW;
    }

    public void setMemberPW(String memberPW) {
        this.memberPW = memberPW;
    }

    public String getLocknum() {
        return locknum;
    }

    public void setLocknum(String locknum) {
        this.locknum = locknum;
    }

    public String getLockPW() {
        return lockPW;
    }

    public void setLockPW(String lockPW) {
        this.lockPW = lockPW;
    }

    public String getCannotUntil(){return cannotUntil;}
    public void setCannotUntil(String cannotUntil){this.cannotUntil=cannotUntil;}


    @Override
    public String toString() {
        // TODO Auto-generated method stub
        if(member.equals("1")){
            return member+" "+memberID+" "+memberPW+" "+locknum+" "+lockPW+" "+cannotUntil+"\n";//회원
        }else{
            return member+" "+locknum+" "+lockPW+"\n";//비회원
        }
    }

}
