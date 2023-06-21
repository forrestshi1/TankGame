package TankGame;

import java.io.*;
import java.util.Vector;

/**
 * 用于记录相关信息，交互文件
 *
 */
public class Recorder {
    //定义变量，记录敌人的击毁数量
    private static int enemyskilled = 0;
    //定义IO对象
    private static FileWriter fw = null;//节点流
    private static BufferedWriter bw = null;//处理流
    private static BufferedReader br = null;
    private static String recordFile = "d:\\record.txt";//记录文件
    private static Vector<ATank> ATanks = null;
    private static Vector<Node> nodes = new Vector<>();//定义一个Node的Vector，用于保存敌人的信息node

    public static void setATanks(Vector<ATank> ATanks) {
        Recorder.ATanks = ATanks;
    }

    public static String getRecordFile() {
        return recordFile;
    }

    //增加一个方法，用于读取recordFile, 恢复相关信息
    public static Vector<Node> getNodesAndRec(){
        try {
            br = new BufferedReader(new FileReader(recordFile));
            enemyskilled = Integer.parseInt(br.readLine());//读取第一行,敌人击毁数量
            //循环读取文件，生成nodes 集合
            String line = " ";//
            while ((line = br.readLine()) != null) {
                String[] xyd = line.split(" ");
                Node node = new Node(Integer.parseInt(xyd[0]), Integer.parseInt(xyd[1]),
                        Integer.parseInt(xyd[2]));
                nodes.add(node);//放入nodes Vector
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }
        return nodes;
    }


    //游戏退出的时候，将击毁敌人数量写入文件,并且保存坐标
    public static void keeprecord()  {
        try {
            bw = new BufferedWriter(new FileWriter(recordFile));
            bw.write(enemyskilled + "\r\n");
            //遍历敌人坦克的Vector ,然后根据情况保存即可.
            //OOP, 定义一个属性 ，然后通过setXxx得到 敌人坦克的Vector
            for (int i = 0; i < ATanks.size(); i++) {
                //取出敌人坦克
                ATank aTank = ATanks.get(i);
                if (aTank.islive) { //建议判断.
                    //保存该enemyTank信息
                    String record = aTank.getX() + " " + aTank.getY() + " " + aTank.getDirect();
                    //写入到文件
                    bw.write(record + "\r\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getEnemyskilled() {
        return enemyskilled;
    }                                                                                             

    public static void setEnemyskilled(int enemyskilled) {
        Recorder.enemyskilled = enemyskilled;
    }

    //记录击毁敌人数量
    public static void addEnemyskilled(){
        enemyskilled++;
    }
}
