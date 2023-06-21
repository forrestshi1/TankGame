package TankGame;

public class Bomb {
    int x,y;//炸弹坐标
    int life = 9;//炸弹生命周期
    boolean islive =true;

    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //减少生命值
    public void lifeDown(){
        if (life > 0){
            life--;
        }else {
            islive = false;
        }
    }

}
