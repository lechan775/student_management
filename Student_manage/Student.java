package Student_manage;

class Student {
    private String student_id;
    private String name;
    private int age;
    private String sex;

    public Student() {
    }

    public Student(String student_id, String name, int age, String sex) {
        this.student_id = student_id;
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
