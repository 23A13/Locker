public class Locker {
    String locknum; //보관함 번호
    int locksize; //보관함 크기(0/1/2-S/M/L)
    int use; //사용여부(0/1/2-미사용/사용중/예약중)
    int date; //날짜시각
    int confirmbook; //예약확정 여부(0/1-미확/확정)

    //constructor
    public Locker() {
    }
    public Locker(String locknum, int locksize,int date){
        this.locknum=locknum;
        this.locksize=locksize;
        this.use=0;
        this.date=date;
        this.confirmbook=0;
    }
    public Locker(String locknum, int locksize,int use,int date,int confirmbook){
        this.locknum=locknum;
        this.locksize=locksize;
        this.use=use;
        this.date=date;
        this.confirmbook=confirmbook;
    }


    /*
    public boolean BookingCheck(int LockerNumber){
        /*
        * 파일 입력, locker 예약 확인
        * *
        if(LockerNumber==5) return true;

        return false;

    }

    public void BookingFileInput(int LockerNumber){
        /*
        * 여기서 해당 락커 파일에 입력..
        * *
    }

    public void print(){
        /*
         * 여기서 사물함 print
         *
        System.out.println("사물함 출력");
                
    }*/



}
