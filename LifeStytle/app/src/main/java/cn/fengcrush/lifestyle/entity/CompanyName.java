package cn.fengcrush.lifestyle.entity;


/**
 *json：
 * com：顺丰
 * no：SF
 */

public class CompanyName {
    private String com;
    private String no;
    private boolean ischeck = false;

    public boolean isIscheck() {
        return  ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}
