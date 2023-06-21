package TankGame;

public class Shot implements Runnable {
    int x;//子弹横坐标
    int y;//子弹纵坐标
    int direct = 0;//子弹方向
    int speed = 1;//子弹速度

    boolean islive = true;//子弹是否存活


    public Shot(int x, int y, int direct) {
        this.x = x;
        this.y = y;
        this.direct = direct;
    }

    @Override
    public void run() {
        while(true){

            //休眠50毫秒
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(!islive){
                break;
            }//如果子弹死亡，退出线程，不再画出子弹，也不再改变子弹坐标

            //为什么子弹坐标还在显示，

            //根据方向来改变子弹的坐标
            switch (direct) {
                case 0://向上
                    y-=speed;
                    break;
                case 1://向下
                    y+=speed;
                    break;
                case 2://向左
                    x-=speed;
                    break;
                case 3://向右
                    x+=speed;
                    break;
            }
            System.out.println("子弹坐标x="+x+"y="+y);

            if (x<0||x>1000||y<0||y>750 && islive) {
                islive = false;
                break;
            }//子弹越界或者碰到敌人坦克，退出线程

        }
    }
}
