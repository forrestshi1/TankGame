package TankGame;
import default2.Vector_;


import javax.swing.*;//导入swing包,因为JFrame在这个包里面
import java.awt.*;//导入awt包,因为Graphics在这个包里面
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Vector;

//mypanel画板
//为了不断的重绘子弹，需要将mypanel实现Runnable,当做一个线程使用
public class mypanel extends JPanel implements KeyListener,Runnable {
    //定义坦克
    myTank mytank =null;

    //定义敌人坦克
    Vector<ATank> ATanks = new Vector<>();
    //定义一个存放Node的Vector，用于保存敌人的信息node
    Vector<Node> nodes = new Vector<>();
    int enemysize = 4;

    //定义炸弹,子弹击中时，加入Bomb对象到bombs
    Vector<Bomb> bombs = new Vector<>();
    //炸弹图片，用于显示效果
    Image image1,image2,image3 = null;


    //在画板(构造器)上画出自己的坦克
    public mypanel(String key) {

        File file = new File(Recorder.getRecordFile());
        if (file.exists()) {
            nodes = Recorder.getNodesAndRec();
        } else {
            System.out.println("文件不存在，只能开启新的游戏");
            key = "1";
        }//读取文件，恢复敌人坦克的信息
        Recorder.setATanks(ATanks);//将敌人坦克的Vector传递给Recorder
        mytank= new myTank(500,100);
        mytank.setSpeed(10);

        switch (key){
            case "1":
                Recorder.setEnemyskilled(0);
                for (int i = 0; i < enemysize; i++) {
                    ATank aTank = new ATank((i+1)*100,0);
                    aTank.setATanks(ATanks); //将敌人坦克的Vector传递给敌人坦克
                    aTank.setDirect(0);//设置方向
                    //启动敌人坦克线程
                    new Thread(aTank).start();


                    //给敌人坦克加入子弹
                    Shot shot = new Shot(aTank.getX()+28,aTank.getY()+90,aTank.getDirect());
                    //加入VECTOR
                    aTank.shots.add(shot);
                    //立即启动
                    new Thread(shot).start();
                    //加入
                    ATanks.add(aTank);

                }
                break;
            case "2":
                for (int i = 0; i < nodes.size(); i++) {
                Node node = nodes.get(i);
                ATank aTank = new ATank(node.getX(),node.getY());

                aTank.setATanks(ATanks); //将敌人坦克的Vector传递给敌人坦克

                aTank.setDirect(node.getDirect());//设置方向
                //启动敌人坦克线程
                new Thread(aTank).start();


                //给敌人坦克加入子弹
                Shot shot = new Shot(aTank.getX()+28,aTank.getY()+90,aTank.getDirect());
                //加入VECTOR
                aTank.shots.add(shot);
                //立即启动
                new Thread(shot).start();
                //加入
                ATanks.add(aTank);

            }
                break;
            default:
                System.out.println("输入错误");
        }
        //初始化敌人坦克


        //初始化图片对象
        image1 = Toolkit.getDefaultToolkit().getImage(mypanel.class.getResource("/bomb_1.gif"));
        image2 = Toolkit.getDefaultToolkit().getImage(mypanel.class.getResource("/bomb_2.gif"));
        image3 = Toolkit.getDefaultToolkit().getImage(mypanel.class.getResource("/bomb_3.gif"));

        //
        new AePlayWave("D:\\111.wav").start();


    }

    //重写paint方法
    //，paint方法通常是在需要绘制组件时自动调用的。
    // 这可以是窗口第一次显示、用户移动窗口、重新绘制窗口或更改窗口大小等

    //编写方法，显示坦克击毁信息
    public void showinfo(Graphics g){
        //画出玩家的总成绩
        g.setColor(Color.black);
        Font myfont = new Font("宋体",Font.BOLD,25);
        g.setFont(myfont);

        g.drawString("您累计击毁敌人坦克",1020,30);
        paintTank(1020,60,g,0,1);//画出敌方坦克
        g.setColor(Color.black);
        g.drawString(Recorder.getEnemyskilled()+"",1080,100);

    }
    @Override
    public void  paint(Graphics g) {
        super.paint(g);
        g.fillRect(0,0,1000,750);//默认黑色填充
        showinfo(g);


        //封装画坦克方法
        if (mytank.islive) {//坦克活着的时候才去画
            paintTank(mytank.getX(), mytank.getY(), g, mytank.getDirect(), 0);
        }


        //画出敌人坦克，遍历vector
        for (int i = 0; i < ATanks.size(); i++) {


            if (ATanks.get(i).islive) {//敌人坦克活着的时候才去画
                paintTank(ATanks.get(i).getX(), ATanks.get(i).getY(), g, ATanks.get(i).getDirect(), 1);
                //画出ATANK的所有子弹
                for (int j = 0; j < ATanks.get(i).shots.size(); j++) {
                    Shot shot = ATanks.get(i).shots.get(j);
                    if (shot.islive) {
                        g.fill3DRect(shot.x, shot.y, 4, 4, true);
                    } else {
                        ATanks.get(i).shots.remove(shot);
                    }

                }
            }


        }


        //画出子弹
        for (int i = 0;i < mytank.shots.size();i++){
            Shot shot = mytank.shots.get(i);
            if (shot.islive && shot != null){
                g.fill3DRect(shot.x,shot.y,4,4,true);
            }else {
                mytank.shots.remove(shot);
            }
        }
        //如果有炸弹，就画出来
        for (int i = 0;i < bombs.size();i++){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }//休眠为了爆炸更明显


            Bomb bomb = bombs.get(i);
            if (bomb.life >6){
                g.drawImage(image1,bomb.x,bomb.y,60,60,this);
            }else if (bomb.life>3){
                g.drawImage(image2,bomb.x,bomb.y,60,60,this);
            }else {
                g.drawImage(image3,bomb.x,bomb.y,60,60,this);
            }


            //让炸弹生命值减少
            bomb.lifeDown();
            if (bomb.life == 0){
                bombs.remove(bomb);
            }

        }

    }

    /**
     *
     * @param x 左上角横坐标
     * @param y  左上角纵坐标
     * @param g 画笔
     */
    public void paintTank(int x,int y,Graphics g,int direct,int type) {
        switch (type) {
            case 0:
                g.setColor(Color.pink);
                break;
            case 1:
                g.setColor(Color.cyan);
                break;
        }
        switch (direct){
            case 0://向上
                g.fill3DRect(x,y,60,60,true);
                g.fill3DRect(x+28,y-30,4,30,true);
                break;
            case 1://向下
                g.fill3DRect(x,y,60,60,true);
                g.fill3DRect(x+28,y+60,4,30,true);
                break;
            case 2://向左
                g.fill3DRect(x,y,60,60,true);
                g.fill3DRect(x-30,y+28,30,4,true);
                break;
            case 3://向右
                g.fill3DRect(x,y,60,60,true);
                g.fill3DRect(x+60,y+28,30,4,true);
                break;
        }


    }
        //是否击中坦克,默认炮筒不算坦克
    //判断是否击中坦克，把集合所有子弹取出来，遍历，判断每一颗子弹是否击中坦克
    public void hitEnemyTank(){
        //遍历子弹
        for(int j =0;j <mytank.shots.size();j++){
            Shot shot = mytank.shots.get(j);
            if (shot.islive){
                //遍历敌人坦克
                for (int i = 0; i < ATanks.size(); i++) {
                    ATank atank = ATanks.get(i);
                    if (atank.islive){
                        //判断是否击中
                        hitTank(shot,atank);
                    }
                }
            }
        }

    }
    public void hitmyTank(){
        //遍历敌人子弹
        for (int i = 0; i < ATanks.size(); i++) {
            ATank atank = ATanks.get(i);
            for(int j =0;j <atank.shots.size();j++){
                Shot shot = atank.shots.get(j);
                if (shot.islive){
                    //判断是否击中
                    hitTank(shot,mytank);
                }
            }
        }
    }

    public  void hitTank(Shot s,Tank tank){
        //判断s击中坦克

        if(s.x > tank.getX() && s.x <tank.getX()+60 && s.y >tank.getY()
        && s.y < tank.getY() + 60){
            s.islive = false;
            tank.islive =false;


//            //当敌人坦克消失时，敌人子弹消失，遍历子弹，让子弹死亡，在paint方法中移除，并且子弹线程也要移除
//            for (int i = 0; i < atank.shots.size(); i++) {
//                Shot shot = atank.shots.get(i);
//                shot.islive = false;
//            }
//
//            //销毁坦克并且让数量减少
//            ATanks.remove(tank);
//            enemysize--;

            if (tank instanceof ATank){
                Recorder.addEnemyskilled();
            }




            //
            Bomb bomb = new Bomb(tank.getX(),tank.getY());
            bombs.add(bomb);

        }
    }

    public void  removetank(){
        for (int i = 0;i<ATanks.size();i++){
            ATank atank = ATanks.get(i);
            if (!atank.islive){
                ATanks.remove(atank);
                enemysize--;
            }
        }
    }







    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
    switch (e.getKeyCode()) {
        case KeyEvent.VK_W://向上
            mytank.setDirect(0);


            if (mytank.getY() >0) {
                mytank.moveUp();
            }
            break;
        case KeyEvent.VK_S://向下A
            mytank.setDirect(1);


            if (mytank.getY()+60 < 750) {
                mytank.moveDown();
            }
            break;
        case KeyEvent.VK_D://向右
            mytank.setDirect(3);


            if (mytank.getX() + 60 < 1000) {
                mytank.moveRight();
            }
            break;
        case KeyEvent.VK_A://向左
            mytank.setDirect(2);


            if (mytank.getX() > 0) {
                mytank.moveLeft();
            }
            break;

    }

    if(e.getKeyCode() == KeyEvent.VK_J){

        //判断是否有子弹，没有才能发射
        //if (mytank.shot == null || mytank.shot.islive == false){
            mytank.fire();
        //}


    }

        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    //每隔100ms重绘一次
    public void run() {
        while (true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


            //因为100ms重绘，不断判断是否击中
            if(mytank.shot!= null && mytank.shot.islive){//自己的子弹活着

                //遍历敌人坦克是否击中
                for (int i =0;i<ATanks.size();i++){
                    ATank aTank = ATanks.get(i);
                    hitTank(mytank.shot,aTank);

                }

            }


            hitEnemyTank();
            hitmyTank();
            removetank();
            this.repaint();
        }

    }
}

