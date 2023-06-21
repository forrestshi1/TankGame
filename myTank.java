package TankGame;


import java.util.Vector;

public class myTank extends Tank {
    Shot shot = null;
    Vector<Shot> shots = new Vector<>();
    public myTank(int x,int y) {
        super(x,y);
    }




    //开火
    public void fire(){

        if (shots.size()>=5){
            return;//最多5发子弹
        }
        //根据当前mytank位置和方向创建shot
        switch (getDirect()){
            case 0: //向上
                shot = new Shot(getX() + 28, getY()-34, 0);
                break;
            case 2: //向左
                shot = new Shot(getX() - 34, getY() + 28, 2);

                break;
            case 1: //向下
                shot = new Shot(getX() + 28, getY() + 90, 1);

                break;
            case 3: //向右
                shot = new Shot(getX()+90, getY() + 28, 3);
                break;
        }
        //加入到shots
        shots.add(shot);
        //启动线程
        Thread t = new Thread(shot);
        t.start();
    }

}
