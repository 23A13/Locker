public class Locker {

    String locknum; //보관함 번호
    String locksize; //보관함 크기(0/1/2-S/M/L)
    String use; //사용여부(0/1/2-미사용/사용중/예약중)
    String date; //날짜시각

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

    String confirmbook; //예약확정 여부(0/1-미확/확정)

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


}