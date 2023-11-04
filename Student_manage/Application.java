package Student_manage;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * @author User
 */
public class Application {
    public static void main(String[] args) throws Exception {
        ArrayList<User> list = new ArrayList<User>();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("welcome to Student Management System");
            System.out.println("please enter your choice : 1.login\t2.register\t3.forget password\t4.exit");
            String choose = sc.next();
            switch (choose) {
                case "1" -> login(list);
                case "2" -> register(list);
                case "3" -> forgetPassword(list);
                case "4" -> {
                    System.out.println("thanks for your using!");
                    System.exit(0);
                }
                default -> System.out.println("NOT FOUND!");
            }
        }
    }

    private static void forgetPassword(ArrayList<User> list) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter your username:");
        String username = sc.next();
        boolean flag1 = contains(list, username);
        if(!flag1) {
            System.out.println("the username is not in the list, please try again later");
        } else {
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getUserName().equals(username)) {
                    while (true) {
                        System.out.print("Please enter your personId: ");
                        String personId = sc.next();
                        if(personId.equalsIgnoreCase(list.get(i).getPersonId())) {
                            while (true) {
                                System.out.print("Please enter your phoneNumber: ");
                                String phoneNumber = sc.next();
                                if(phoneNumber.equals(list.get(i).getPhoneNumber())) {
                                    while(true) {
                                        System.out.print("Please enter your new password: ");
                                        String password1 = sc.next();
                                        System.out.print("Please enter your new password again: ");
                                        String password2 = sc.next();
                                        if(password1.equals(password2)) {
                                            User user = new User(username, password1, personId , phoneNumber);
                                            list.set(i, user);
                                            System.out.println("Password is changed!\n\nYou can register again!");
                                            return;
                                        }
                                        System.out.println("The passwords entered twice do not match, please try again.");
                                    }
                                } else {
                                    System.out.println("Invalid phone number!\n\nPlease try again!\n\n\n");
                                }
                            }
                        } else {
                            System.out.println("Invalid personId!\n\nPlease try again!\n\n\n");
                        }
                    }
                }
            }
        }
    }

    private static void register(ArrayList<User> list) {
        System.out.println("register!");

        //用户名，密码，身份证，手机号码存放到用户对象中
        //把对象存放到集合之中
        Scanner sc = new Scanner(System.in);
        String userName , password , identity , phoneNumber;
        while (true) {
            System.out.print("please enter your username:");
            userName = sc.next();
            boolean flag1 = checkUsername(userName);
            if(!flag1) {
                System.out.println("DEFAULT!\nPLEASE TRY AGAIN!");
                continue;
            }

            //校验用户名是否唯一
            boolean flag2 = contains(list,userName);
            if(flag2) {
                System.out.println("User Name: " + userName + " is existed");
            } else {
                System.out.println("User Name: " + userName + " is useful!");
                break;
            }
        }

        //password
        while (true) {
            System.out.print("please enter password:");
            password = sc.next();
            System.out.print("please enter password again:");
            String againPassword = sc.next();
            if(!password.equals(againPassword)) {
                System.out.println("Passwords do not match, please re-enter.");
            } else {
                System.out.println("Password is right!");
                break;
            }
        }

        //身份证号码验证
        /*
          长度18位
          不能以0为开头
          前17位，必须都是数字
          最后一位可以是数字也可以是X或x
         */
        while(true) {
            System.out.print("Please enter your identity card:");
            identity = sc.next();
            boolean flag1 = idCheck(identity);
            if(!flag1) {
                System.out.println("Illegal input, please try again.");
            } else {
                System.out.println("Identity card is right");
                break;
            }
        }

        // phone number
        /*
        length == 11
        !startWith("0")
         */
        while(true) {
            System.out.print("Please enter your phone number:");
            phoneNumber = sc.next();
            boolean flag1 = phoneNumberCheck(phoneNumber);
            if(!flag1) {
                System.out.println("The entered phone number is invalid, please re-enter.");
            } else {
                System.out.println("Register Successfully!");
                break;
            }
        }
        User user = new User(userName,password,identity,phoneNumber);
        list.add(user);

        printList(list);
    }

    private static void printList(ArrayList<User> list) {
        for (User user : list) {
            System.out.println(user.getUserName() + ", " + user.getPassword() + ", "
                    + user.getPersonId() + ", " + user.getPhoneNumber());
        }
    }

    private static boolean phoneNumberCheck(String phoneNumber) {
        int len = phoneNumber.length();
        if (len != 11) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            char c = phoneNumber.charAt(i);
            if(!(c >= '0' && c <= '9')) {
                return false;
            }
        }
        return true;
    }

    private static boolean idCheck(String identity) {
        int len = identity.length();
        if(len != 18) {
            return false;
        }
        if (identity.startsWith("0")) {
            return false;
        }
        for (int i = 1; i < len - 1; i++) {
            if(!(identity.charAt(i) >= '0' && identity.charAt(i) <= '9')) {
                return false;
            }
        }
        char endChar = identity.charAt(len - 1);
        return (endChar >= '0' && endChar <= '9') || endChar == 'x' || endChar == 'X';
    }

    private static boolean contains(ArrayList<User> list, String username) {
        //循环遍历对象
        for (User user : list) {
            String rightUsername = user.getUserName();
            if (rightUsername.equals(username)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkUsername(String username) {
        //长度验证
        int nameLength = username.length();
        if(nameLength < 3 || nameLength > 15) {
            return false;
        }

        //检验字符串
        for(int i = 0; i < username.length(); i++) {
            char c = username.charAt(i);
            if(!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9'))) {
                return false;
            }
        }

        //用户名长度满足，内容满足数字+数字
        int count = 0;
        for (int i = 0; i < nameLength; i++) {
            char c = username.charAt(i);
            if((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                count++;
                break;
            }
        }
        return count > 0;
    }

    private static void login(ArrayList<User> list) throws Exception {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Please enter your username:");
            String userName = sc.next();
            boolean flag1 = contains(list,userName);
            if(!flag1) {
                System.out.println("The username " + userName + " is not registered!");
                System.out.println("Please register it first!");
                return;
            }
            System.out.print("Please enter your password:");
            String password = sc.next();
            String rightCode = getCode();
            System.out.println("The temp code is: " + rightCode);
            System.out.print("Please enter the code: ");
            String code = sc.next();

            if(rightCode.equalsIgnoreCase(code)) {
                User u = new User(userName, password, "", "");
                boolean flag2 = checkPassword(list, u);
                if(!flag2) {
                    System.out.println("Invalid account or password! Please try again!");
                } else {
                    cecha.studentManagement();
                    return;
                }
            } else {
                System.out.println("Invalid temp code! Please try again!");
            }
        }
    }

    private static boolean checkPassword(ArrayList<User> list,User u) {
        for(User user : list) {
            String userPassword = user.getPassword();
            if(userPassword.equals(u.getPassword())) {
                return true;
            }
        }
        return false;
    }

    private static String getCode() {
        ArrayList<Character> arr = new ArrayList<Character>();
        for (int i = 0; i < 26; i++) {
            arr.add((char)('a'+i));
            arr.add((char)('A'+i));
        }

        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for(int i = 0 ; i < 4 ; i ++) {
            int index = r.nextInt(arr.size());
            char c = arr.get(index);
            sb.append(c);
        }

        int num = r.nextInt(10);
        sb.append(num);

        char[] str = sb.toString().toCharArray();
        int tempNum = r.nextInt(5);
        char temp = str[tempNum];
        str[tempNum] = str[str.length - 1];
        str[str.length - 1] = temp;

        return new String(str);
    }
}