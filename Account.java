package arycra_07;
/*
账户类
 */
public class Account {
    private String cardID; //卡号
    private String userName; //户主
    private String passWord; //密码
    private double money; //余额
    private double quotaMoney; //单次取款or转账限额
 
    public Account() {
    }
 
    public Account(String cardID, String userName, String passWord, double quotaMoney) {
        this.cardID = cardID;
        this.userName = userName;
        this.passWord = passWord;
        this.quotaMoney = quotaMoney;
    }
 
    public String getCardID() {
        return cardID;
    }
 
    public void setCardID(String cardID) {
        this.cardID = cardID;
    }
 
    public String getUserName() {
        return userName;
    }
 
    public void setUserName(String userName) {
        this.userName = userName;
    }
 
    public String getPassWord() {
        return passWord;
    }
 
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
 
    public double getMoney() {
        return money;
    }
 
    public void setMoney(double money) {
        this.money = money;
    }
 
    public double getQuotaMoney() {
        return quotaMoney;
    }
 
    public void setQuotaMoney(double quotaMoney) {
        this.quotaMoney = quotaMoney;
    }
 
}