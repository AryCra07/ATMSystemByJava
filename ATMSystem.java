package arycra_07;
 
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
 
public class ATMSystem {
    public static void main(String[] args) {
        //准备系统需要的容器对象，用来存储账户对象
        ArrayList<Account> accounts = new ArrayList<>();
 
        //准备系统的首页，登录，开户
        showMain(accounts);
    }
 
    /**
     * 展示首页内容
     *
     * @param accounts 传入账户集合
     */
    public static void showMain(ArrayList<Account> accounts) {
        Scanner sc = new Scanner(System.in);
        showMain_loop:
        while (true) {
            System.out.println("======================Welcome to my ATM system======================");
            System.out.println("0. Exit the system");
            System.out.println("1. Log in to your account");
            System.out.println("2. Open an account");
            System.out.println("Please input the number of your order: ");
            String order = sc.next();
            switch (order) {
                case "0":
                    //退出
                    System.out.println("Exit successfully. Welcome back again!");
                    break showMain_loop;
                case "1":
                    //登录
                    login(accounts, sc);
                    break;
                case "2":
                    //开户
                    register(accounts, sc);
                    break;
                default:
                    System.out.println("Illegal order. Try again.");
            }
        }
    }
 
    /**
     * 完成用户登录操作
     *
     * @param accounts 传入账户集合
     * @param sc       传入扫描器进行键盘输入
     */
    private static void login(ArrayList<Account> accounts, Scanner sc) {
        //考虑极端情况，用户在不存在账户的情况下强行登录
        if (accounts.size() == 0) {
            //没有任何账户，需要直接返回
            System.out.println("No account exists in the system now. Open an account first.");
            return;
        }
        System.out.println("======================Log in to your account here======================");
        //让用户录入卡号，根据卡号查询账户对象
        while (true) {
            System.out.println("Please input your cardID: ");
            String cardID = sc.next();
            //根据卡号查询账户对象，我们抽象成方法getAccountByCardID
            Account acc = getAccountByCardID(cardID, accounts);
 
            //判断账户对象是否存在，存在则说明没问题
            if (acc != null) {
                while (true) {
                    //账户对象存在，继续输入密码
                    System.out.println("Please input your password: ");
                    String password = sc.next();
                    //判断密码是否正确
                    if (acc.getPassWord().equals(password)) {
                        //密码正确，登录成功
                        //欢迎界面
                        System.out.println("Hello " + acc.getUserName() +
                                ", now you have successfully entered the User Interface!");
                        System.out.println("Your cardID is " + acc.getCardID() + ".");
                        //展示系统登录后的操作界面
                        showUserOrder(sc, acc, accounts);
                        return; //结束登录方法
                    } else {
                        System.out.println("Wrong password. Try again.");
                    }
                }
            } else {
                //账户对象不存在，继续循环
                System.out.println("This account do not exist. Try again.");
            }
        }
    }
 
    /**
     * 登录后展示用户操作界面
     *
     * @param sc       传入扫描器读取用户输入
     * @param acc      传入用户登录的账户对象
     * @param accounts 传入账户集合
     */
    private static void showUserOrder(Scanner sc, Account acc, ArrayList<Account> accounts) {
        while (true) {
            System.out.println("======================User Interface======================");
            System.out.println("1 Query account");
            System.out.println("2 Deposit money");
            System.out.println("3 Withdraw money");
            System.out.println("4 Transfer accounts");
            System.out.println("5 Change password");
            System.out.println("6 Log out");
            System.out.println("7 Delete your account");
            System.out.println("Please input the number of your order:");
            String order = sc.next();
            switch (order) {
                case "1" ->
                        //查询账户
                        queryAccount(acc);
                case "2" ->
                        //存款
                        depositMoney(acc, sc);
                case "3" ->
                        //取款
                        withdrawMoney(acc, sc);
                case "4" ->
                        //转账
                        transferMoney(accounts, acc, sc);
                case "5" ->
                        //修改密码
                        changePassword(acc, sc);
                case "6" -> {
                    //登出
                    System.out.println("Log out successfully. Welcome back again!");
                    return; //结束当前方法返回
                }
                case "7" -> {
                    //注销
                    //删除当前账户即可
                    accounts.remove(acc);
                    System.out.println("Delete your account successfully.");
                    return; //结束当前方法返回
                }
                default -> System.out.println("Illegal order. Try again.");
            }
        }
    }
 
    /**
     * 修改密码功能
     *
     * @param acc 传入当前登录的账户对象
     * @param sc  传入扫描器进行键盘输入
     */
    private static void changePassword(Account acc, Scanner sc) {
        System.out.println("======================Change Password======================");
        while (true) {
            System.out.println("Please input your previous password: ");
            //要求用户输入正在使用的密码
            String prePassword = sc.next();
            //判断密码是否正确
            if (acc.getPassWord().equals(prePassword)) {
                System.out.println("Please input your new password: ");
                String newPassword = sc.next();
                System.out.println("Please confirm your new password: ");
                //请用户确认自己的新密码
                String okNewPassword = sc.next();
                if (newPassword.equals(okNewPassword)) {
                    //确认无误，修改密码
                    acc.setPassWord(newPassword);
                    System.out.println("Change your password successfully!");
                    return; //返回
                } else {
                    System.out.println("The passwords you input twice are inconsistent. Try again.");
                }
            } else {
                System.out.println("Wrong password. Try again.");
            }
        }
    }
 
    /**
     * 转账功能
     *
     * @param accounts 传入账户集合
     * @param acc      传入当前登录的账户对象
     * @param sc       传入扫描器进行键盘输入
     */
    private static void transferMoney(ArrayList<Account> accounts, Account acc, Scanner sc) {
        //判断系统中是否存在两个及以上账户，以支持转账操作
        if (accounts.size() < 2) {
            System.out.println("Sorry, there are no other accounts in the system " +
                    "for you to transfer money!");
            return;
        }
 
        //如果当前账户余额为零
        if (acc.getMoney() == 0) {
            System.out.println("Your account is out of money now!");
            return;
        }
 
        //说明系统中有至少两个账户且当前账户余额不为零，开启转账操作
        while (true) {
            System.out.println("Please input the cardID you want to transfer: ");
            String cardID = sc.next();
            Account account = getAccountByCardID(cardID, accounts); //查询输入的卡号是否存在
            if (account != null) {
                //卡号存在，判断是否在给自己转账
                if (account.getCardID().equals(acc.getCardID())) {
                    //在给自己转账
                    System.out.println("Do not transfer money to yourself!");
                } else {
                    //确认对方名字的第一个字符
                    String name = "*" + account.getUserName().substring(1);
                    System.out.println("Please complete the first character of " +
                            "his or her name: " + name);
                    String firstCharacter = sc.next();
                    //判断
                    if (account.getUserName().startsWith(firstCharacter)) {
                        //验证成功，开始转账
                        System.out.println("Please input the amount you want to transfer: ");
                        double amountMoney = sc.nextDouble();
                        //判断转账金额是否超过当前登录账户的余额或转账限额
                        if (amountMoney > acc.getQuotaMoney()) {
                            System.out.println("Exceed your transfer quota. Your quota is "
                                    + acc.getQuotaMoney() + " dollars. Try again.");
                        } else if (amountMoney > acc.getMoney()) {
                            //超过余额
                            System.out.println("Exceed your balance. Your balance is "
                                    + acc.getMoney() + " dollars now. Try again.");
                        } else {
                            //可以转账
                            acc.setMoney(acc.getMoney() - amountMoney);
                            account.setMoney(account.getMoney() + amountMoney);
                            System.out.println("Successfully transfer! You have transferred "
                                    + amountMoney + " to" + account.getUserName() + ".");
                            queryAccount(acc); //查看自己当前账户信息
                            return;
                        }
                    } else {
                        System.out.println("You get it wrong. Try again.");
                    }
                }
            } else {
                //卡号不存在
                System.out.println("The cardID you input do not exist. Try again.");
            }
        }
    }
 
    /**
     * 取款
     *
     * @param acc 传入当前登录的账户对象
     * @param sc  传入扫描器进行键盘输入
     */
    private static void withdrawMoney(Account acc, Scanner sc) {
        System.out.println("======================Withdraw Money======================");
        //判断账户上还有没有钱
        if (acc.getMoney() > 0) {
            while (true) {
                System.out.println("Please input your withdrawal amount");
                double withdrawlAmount = sc.nextDouble();
                //判断取款金额有没有超过单次取款限额
                if (withdrawlAmount >= acc.getQuotaMoney()) {
                    System.out.println("Exceed your withdrawal quota. Try again. Your quota is "
                            + acc.getQuotaMoney());
                } else {
                    //判断取款金额有没有超过账户余额
                    if (withdrawlAmount >= acc.getMoney()) {
                        System.out.println("Exceed your balance. Your balance is "
                                + acc.getMoney() + " dollars now. Try again.");
                    } else {
                        //可以取钱
                        acc.setMoney(acc.getMoney() - withdrawlAmount);
                        System.out.println("Successful withdrawal! You took out " + withdrawlAmount
                                + " dollars. Now your account balance is " + acc.getMoney() + " dollars.");
                        return; //取钱成功后返回
                    }
                }
            }
        } else {
            System.out.println("Your account is out of money now!");
        }
    }
 
    /**
     * 存款功能
     *
     * @param acc 传入当前登录的账户对象
     * @param sc  传入扫描器进行键盘输入
     */
    private static void depositMoney(Account acc, Scanner sc) {
        System.out.println("======================Deposit Money======================");
        System.out.println("Please input your deposit amount: ");
        double depositMoney = sc.nextDouble();
 
        //直接把金额修改到账户对象的money属性中去
        acc.setMoney(acc.getMoney() + depositMoney);
        System.out.println("Deposit successfully!");
        queryAccount(acc);
    }
 
    /**
     * 查询当前登录的账户信息
     *
     * @param acc 传入当前登录的账户对象
     */
    private static void queryAccount(Account acc) {
        System.out.println("======================Account Details======================");
        System.out.println("Your cardID is " + acc.getCardID());
        System.out.println("Your name is " + acc.getUserName());
        System.out.println("Your account balance is " + acc.getMoney() + " dollars");
        System.out.println("Your current quota is " + acc.getQuotaMoney() + " dollars");
    }
 
    /**
     * 开户功能
     *
     * @param accounts 传入账户集合
     * @param sc       传入扫描器进行键盘输入
     */
    private static void register(ArrayList<Account> accounts, Scanner sc) {
        System.out.println("======================Open an account here======================");
        //键盘录入 姓名 密码 确认密码
        System.out.println("Please input your name: ");
        String name = sc.next();
 
        String password;
        String okPassword;
        while (true) {
            System.out.println("Please input your password: ");
            password = sc.next();
            System.out.println("Please confirm your password: ");
            okPassword = sc.next();
            //判断两次输入的密码是否一致
            if (okPassword.equals(password)) {
                break;
            } else {
                System.out.println("Unsuccessfully confirm your password. Try again.");
            }
        }
 
        System.out.println("Please input your quota of withdrawal and transfer: ");
        double quotaMoney = sc.nextDouble();
 
        //随机生成账户卡号并检查是否与其他账号重复，我们抽象出方法createCardID
        String newCardID = createCardID(accounts);
 
        //创建一个账户对象封装对象信息
        Account account = new Account(newCardID, name, password, quotaMoney);
        accounts.add(account);
        System.out.println("Successfully open an account! Your cardID is " + account.getCardID() + ".");
    }
 
    /**
     * 随机生成卡号并查重
     *
     * @param accounts 传入账户集合
     * @return 返回一个8位新卡号
     */
    public static String createCardID(ArrayList<Account> accounts) {
        while (true) {
            //生成一个8位新卡号
            String newCardID = "";
            Random r = new Random();
            for (int i = 0; i < 8; i++) {
                newCardID += r.nextInt(10);
            }
            //查重，检查新卡号是否重复
            //根据卡号查询账户对象，我们抽象出方法getAccountByCardID
            Account acc = getAccountByCardID(newCardID, accounts);
            //判断账户对象是否存在，为null说明没问题
            if (acc == null) {
                //当前卡号未重复，可以开户
                return newCardID;
            }
        }
    }
 
    /**
     * 在账户集合中检索，判定卡号是否已经存在
     *
     * @param cardID   createCardID方法中新生成的卡号
     * @param accounts 账户集合
     * @return 若重复则返回重复的账户对象，否则返回null
     */
    public static Account getAccountByCardID(String cardID, ArrayList<Account> accounts) {
        //根据卡号查询账户对象，查不到说明这个卡号未重复
        for (Account acc : accounts) {
            if (acc.getCardID().equals(cardID)) {
                return acc;
            }
        }
        return null; //查无此账户，卡号无重复
    }
}