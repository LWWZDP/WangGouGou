import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @Auther：王嘉俊
 * @Student_Number: 2018213438
 * @Date: Created in 23:05 2020/10/10
 */


/**
 * 交叉中值模型法的整体算法
 */
public class Algorithm {
    /**
     * 主程序
     */
    public static void main(String[] args) {
        Algorithm algorithm = new Algorithm();
        String fileName = "test2";//待读取的文件test2（PPT上的作业题），也可以把这里换成test（PPT上的例题）
        algorithm.readFile(fileName);//读取文件中的数据
        algorithm.calculate();//交叉中值法计算
    }



    private ArrayList<Point> points = new ArrayList<Point>();//每个点的数据，包括坐标和需求量值
    private ArrayList<Integer> rank_x_DESC = new ArrayList<Integer>();//对x坐标做降序后对应的point中的角标排序
    private ArrayList<Integer> rank_x_ASC = new ArrayList<Integer>();//对x坐标做升序后对应的point中的角标排序
    private ArrayList<Integer> rank_y_DESC = new ArrayList<Integer>();//对y坐标做降序后对应的point中的角标排序
    private ArrayList<Integer> rank_y_ASC = new ArrayList<Integer>();//对y坐标做升序后对应的point中的角标排序
    private static final int X = 0;
    private static final int Y = 1;
    private static final int DESC = 0;
    private static final int ASC = 1;
    private double total_qantity = 0;//所有累加的需求量，等于中值的2倍
    private double average_quantity = 0;//中值
    int length;//数据点的个数

    /**
     *  读取文档中的数据
     */
    public void readFile(String fileName){
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int id_number = 0;
            //逐行读取
            while ((tempString = reader.readLine())!=null) {
                String[] str = tempString.split(",");
                Point point = new Point();
                point.setX_coordinate(Double.parseDouble(str[0]));
                point.setY_coordinate(Double.parseDouble(str[1]));
                point.setQuantity(Double.parseDouble(str[2]));
                point.setId(id_number++);
                rank_x_ASC.add(point.getId());
                rank_x_DESC.add(point.getId());
                rank_y_ASC.add(point.getId());
                rank_y_DESC.add(point.getId());
                points.add(point);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        length = points.size();


        /**
         * 分别根据X、Y进行升序和降序排序
         */
        sort(X,DESC);
        sort(X,ASC);
        sort(Y,DESC);
        sort(Y,ASC);
    }

    /**
     * 针对X、Y和升降序模式的排序算法
     */
    private void sort(int XorY,int mode){
        if (XorY == X){
            for (int i = 0; i < length; i++) {
                for (int j = i; j < length; j++) {
                    if (mode == DESC) {
                        double a = points.get(rank_x_DESC.get(i)).getX_coordinate();
                        double b = points.get(rank_x_DESC.get(j)).getX_coordinate();
                        if (a < b) {
                            int mid = points.get(rank_x_DESC.get(i)).getId();
                            rank_x_DESC.set(i, points.get(rank_x_DESC.get(j)).getId());
                            rank_x_DESC.set(j, mid);
                        }
                    }
                    else {
                        double a = points.get(rank_x_ASC.get(i)).getX_coordinate();
                        double b = points.get(rank_x_ASC.get(j)).getX_coordinate();
                        if (a > b) {
                            int mid = points.get(rank_x_ASC.get(i)).getId();
                            rank_x_ASC.set(i, points.get(rank_x_ASC.get(j)).getId());
                            rank_x_ASC.set(j, mid);
                        }
                    }
                }
            }
        }
        else {
            for (int i = 0; i < length; i++) {
                for (int j = i; j < length; j++) {
                    if (mode == DESC) {
                        double a = points.get(rank_y_DESC.get(i)).getY_coordinate();
                        double b = points.get(rank_y_DESC.get(j)).getY_coordinate();
                        if (a < b) {
                            int mid = points.get(rank_y_DESC.get(i)).getId();
                            rank_y_DESC.set(i, points.get(rank_y_DESC.get(j)).getId());
                            rank_y_DESC.set(j, mid);
                        }
                    }
                    else {
                        double a = points.get(rank_y_ASC.get(i)).getY_coordinate();
                        double b = points.get(rank_y_ASC.get(j)).getY_coordinate();
                        if (a > b) {
                            int mid = points.get(rank_y_ASC.get(i)).getId();
                            rank_y_ASC.set(i, points.get(rank_y_ASC.get(j)).getId());
                            rank_y_ASC.set(j, mid);
                        }
                    }
                }
            }
        }
    }




    /**
     * 主要计算逻辑
     */
    public void calculate(){
        for (Point point : points) {
           total_qantity += point.getQuantity();
        }
        average_quantity = total_qantity/2;//求出需求量的一半，即中值

        double total_x_DESC = 0;//x降序排列时累加的需求量
        double total_x_ASC = 0;//x升序排列时累加的需求量
        double total_y_DESC = 0;//y降序排列时累加的需求量
        double total_y_ASC = 0;//y升序排列时累加的需求量
        double min_x_DESC = 0;//x降序排列的中值最小值
        double max_x_DESC = 0;//x降序排列的中值最大值
        double min_y_DESC = 0;//y降序排列的中值最小值
        double max_y_DESC = 0;//y降序排列的中值最大值
        double min_x_ASC = 0;//x升序排列的中值最小值
        double max_x_ASC = 0;//x升序排列的中值最大值
        double min_y_ASC = 0;//y升序排列的中值最小值
        double max_y_ASC = 0;//y升序排列的中值最大值


        /**
         * 求出降序x的中值区间
         */
        for (Integer integer : rank_x_DESC) {
            total_x_DESC += points.get(integer.intValue()).getQuantity();
            int compare_result = Double.compare(total_x_DESC,average_quantity);
            if (compare_result > 0){
                min_x_DESC = points.get(integer.intValue()).getX_coordinate();
                break;
            }
            else{
                max_x_DESC = points.get(integer.intValue()).getX_coordinate();
            }
        }

        /**
         * 求出升序x的中值区间
         */
        for (Integer integer : rank_x_ASC) {
            total_x_ASC += points.get(integer.intValue()).getQuantity();
            int compare_result = Double.compare(total_x_ASC,average_quantity);
            if (compare_result > 0){
                max_x_ASC = points.get(integer.intValue()).getX_coordinate();
                break;
            }
            else{
                min_x_ASC = points.get(integer.intValue()).getX_coordinate();
            }
        }

        /**
         * 求出降序y的中值区间
         */
        for (Integer integer : rank_y_DESC) {
            total_y_DESC += points.get(integer.intValue()).getQuantity();
            int compare_result = Double.compare(total_y_DESC,average_quantity);
            if (compare_result > 0){
                min_y_DESC = points.get(integer.intValue()).getY_coordinate();
                break;
            }
            else{
                max_y_DESC = points.get(integer.intValue()).getY_coordinate();
            }
        }

        /**
         * 求出升序y的中值区间
         */
        for (Integer integer : rank_y_ASC) {
            total_y_ASC += points.get(integer.intValue()).getQuantity();
            int compare_result = Double.compare(total_y_ASC,average_quantity);
            if (compare_result > 0){
                max_y_ASC = points.get(integer.intValue()).getY_coordinate();
                break;
            }
            else{
                min_y_ASC = points.get(integer.intValue()).getY_coordinate();
            }
        }



        double[] range_x = getintersection(min_x_ASC,max_x_ASC,min_x_DESC,max_x_DESC);//最优选址的x坐标范围
        double[] range_y = getintersection(min_y_ASC,max_y_ASC,min_y_DESC,max_y_DESC);//最优选址的y坐标范围

        output_result(range_x,range_y);//根据x、y坐标的范围打印出结果，区分点、线（两种）、面的四种情况
    }


    /**
     *求出x和y升降序的交集
     */
    private double[] getintersection(double min_x,double max_x,double min_y,double max_y){
        double result_min = Double.max(min_x,min_y);
        double result_max = Double.min(max_x,max_y);
        double[] ret = new double[2];
        if (result_max < result_min){
            ret = null;
        }
        else {
            ret[0] = result_min;
            ret[1] = result_max;
        }
        return ret;
    }

    /**
     * 根据x、y坐标的范围打印出结果，区分点、线（两种）、面的四种情况
     */
    private void output_result(double[] result_x,double[] result_y){
        if (Double.compare(result_x[0],result_x[1]) == 0){
            if(Double.compare(result_y[0],result_y[1]) == 0){
                System.out.println("最优选址为点("+result_x[0]+","+result_y[0]+")");
            }
            else {
                System.out.println("最优选址为线段("+result_x[0]+","+result_y[0]+"),"+"("+result_x[0]+","+result_y[1]+")");
            }
        }
        else{
            if (Double.compare(result_y[0],result_y[1]) == 0){
                System.out.println("最优选址为线段("+result_x[0]+","+result_y[0]+"),"+"("+result_x[1]+","+result_y[0]+")");
            }
            else{
                System.out.println("最优选址为点"+"("+result_x[0]+","+result_y[0]+"),"+"("+result_x[0]+","+result_y[1]+"),"+"("+result_x[1]+","+result_y[0]+"),"+"("+result_x[1]+","+result_y[1]+")"+"组成的矩形区域");
            }
        }
    }

}
