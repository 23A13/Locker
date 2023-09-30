public class Locker {

    //constructor
    public Locker() {
    }
    public Locker(int LockerNumber) {

    }

    public void print() {
        /*
         * 여기서 사물함 print
         */
        System.out.println("사물함 출력");
    }

    public boolean BooingCheck(int LockerNumber){
        /*
         * 파일 입력, locker 예약 확인
         * */
        if(LockerNumber==5)
            return true;

        return false;

    }
}
