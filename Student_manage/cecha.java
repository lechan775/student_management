package Student_manage;

import java.util.ArrayList;
import java.util.Scanner;

public class cecha {
    static Scanner sc = new Scanner(System.in);

    public static void studentManagement() throws Exception {
        Scanner sc = new Scanner(System.in);
        ArrayList<Student> StudentList = new ArrayList<Student>();
        foop:while(true) {
            System.out.println("welcome to the application named student management");
            System.out.println("please enter your choice");
            System.out.println("1-insert a new student");
            System.out.println("2-delete a student");
            System.out.println("3-search a student");
            System.out.println("4-update a student`message");
            System.out.println("5-show all of the students`message");
            System.out.println("6-exit");
            System.out.print("Please enter your choice:");
            int n = sc.nextInt();
            switch(n) {
                case 1 : {
                    insertStudent(StudentList);
                    break;
                }
                case 2 : {
                    deleteStudent(StudentList);
                    break;
                }
                case 3 : {
                    searchStudent(StudentList);
                    break;
                }
                case 4 : {
                    updateStudent(StudentList);
                    break;
                }
                case 5 : {
                    showStudent(StudentList);
                    break;
                }
                case 6 : break foop;
                default : {
                    System.out.println("Defeat!Please try again!!!");
                    break;
                }
            }
        }
    }
    /**
     * 插入一个学生
     * 先检测id数据库中是否已存在
     * 若已存在重新输入
     * @param studentArrayList
     */
    public static void insertStudent(ArrayList<Student> studentArrayList) throws Exception {
        foot:while (true) {
            System.out.print("please enter the student_id:");
            String id = sc.next();
            boolean flag = id_search(studentArrayList,id);
            if(flag) {
               System.out.println("the student_id is repeated!");
               System.out.println("please try again!");
            } else {
                System.out.print("please enter student`name:");
                String name = sc.next();
                System.out.print("please enter student`age:");
                int age = sc.nextInt();
                System.out.print("please enter student`sex:");
                String sex = sc.next();
                Student stu = new Student(id,name,age,sex);
                studentArrayList.add(stu);
                break foot;
            }
        }
    }

    /**
     * 匹配id来删除学生
     * 可先使用查询函数
     * 若匹配失败返回上一栏
     * @param studentArrayList
     */
    public static void deleteStudent(ArrayList<Student> studentArrayList) throws Exception {
        System.out.print("please enter student's id:");
        String id = sc.next();
        for (int i = 0; i < studentArrayList.size(); i++) {
            if(studentArrayList.get(i).getStudent_id().equals(id)) {
                studentArrayList.remove(i);
                System.out.println("Successful!");
                return;
            }
        }
        System.out.println("Defeat!");
    }

    /**
     * 查询一名学生是否存在
     * 若存在则读取改名同学的相关信息
     * 若不存在则返回查找失败
     * @param studentArrayList
     */
    public static boolean searchStudent(ArrayList<Student> studentArrayList) {
        System.out.print("Please enter the student`s id:");
        String id = sc.next();
        for (int i = 0; i < studentArrayList.size(); i++) {
            if(studentArrayList.get(i).getStudent_id().equals(id)) {
                System.out.println("student_id\tname\tage\tsex\t");
                System.out.println(studentArrayList.get(i).getStudent_id() + '\t' + studentArrayList.get(i).getName() + '\t' + studentArrayList.get(i).getAge() + '\t' + studentArrayList.get(i).getSex());
                return true;
            }
        }
        System.out.println("the student is not found");
        return false;
    }

    /**
     * 更新一名同学的信息
     * 先调用查询函数
     * 再对原有的值进行覆盖
     * @param studentArrayList
     */
    public static void updateStudent(ArrayList<Student> studentArrayList) throws Exception {
        System.out.println("Please enter the student`s id:");
        String id = sc.next();
        for (int i = 0; i < studentArrayList.size(); i++) {
            if(studentArrayList.get(i).getStudent_id().equals(id)) {
                studentArrayList.remove(i);
                insertStudent(studentArrayList);
                return;
            }
        }
    }

    /**
     * 遍历所有同学的信息
     * @param studentArrayList
     */
    public static void showStudent(ArrayList<Student> studentArrayList) {
        int length = studentArrayList.size();
        System.out.println("student_id\tname\tage\tsex\t");
        for (int i = 0; i < length; i++) {
            System.out.println(studentArrayList.get(i).getStudent_id()+'\t'+studentArrayList.get(i).getName()+'\t'+studentArrayList.get(i).getAge()+'\t'+studentArrayList.get(i).getSex());
            System.out.println("\n\n\n\n");
        }
    }

    public static boolean id_search(ArrayList<Student> studentArrayList,String id) {
        for (int i = 0; i < studentArrayList.size(); i++) {
            if(studentArrayList.get(i).getStudent_id().equals(id)) {
                return true;
            }
        }
        return false;
    }
}