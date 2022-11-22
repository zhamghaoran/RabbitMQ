
import java.util.Scanner;
import java.util.SimpleTimeZone;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ISalary[] salarys=new ISalary[30];
        Scanner sc=new Scanner(System.in);
        int wage1=sc.nextInt();//销售人员初始工资
        int commision=sc.nextInt();//销售人员初始提成
        int days=sc.nextInt();//工人天数
        int wage2=sc.nextInt();//工人每天工资
        for(int i=0;i<salarys.length;i++) {   //30个salarys对象分成Salesman和Worker两类
            if(i%2==0)
                salarys[i]=new Salesman(wage1+10*i,commision);
            else if(i%2==1)
                salarys[i]=new Worker(days+i,wage2);
        }
        TotalFee computer=new TotalFee();
        computer.setDrinks(salarys);
        System.out.printf("费用之和:%.1f",computer.computerTotalFee());
    }
}
/*  请在这里填写答案 请结合主类代码，在代码框完成ISalary接口类，Salesman类，Worker类和TotalFee类 */

interface ISalary {
    double getSalaey();
}
class TotalFee {
    ISalary[] salarys;
    public void setDrinks(ISalary[] t) {
        this.salarys = t;
    }
    public double computerTotalFee() {
        double ans = 0;
        for (ISalary i : salarys) {
            ans += i.getSalaey();
        }
        return ans;
    }

}
class Salesman implements ISalary {
    public int wage;
    public int commission;

    public Salesman(int wage,int commission) {
        this.wage = wage;
        this.commission = commission;
    }
    public double getSalaey() {
        return wage + commission;
    }
}
class Worker implements ISalary {
    public int days;
    public int wage;
    public Worker(int days,int wage) {
        this.days = days;
        this.wage = wage;
    }
    public double getSalaey() {
        return days * wage;
    }

}


