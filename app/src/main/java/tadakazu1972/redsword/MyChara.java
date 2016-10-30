package tadakazu1972.redsword;

/**
 * Created by tadakazu on 2015/09/22.
 */
public class MyChara {
    public float x, y;
    public float wx, wy;
    public float vx, vy;
    public float ay;
    public float l, r, t, b;
    public int hp;
    public int str;
    public int def;
    public int exp;
    public int base_index; //アニメーション基底 0:右向き 2:左向き
    public int index;
    public int second_jump; //2段ジャンプフラグ
    public int ichigo; //いちごフラグ 0:もっていない 1:もっている
    public int status; //0:ノーマル 1:バルーンつかまり中
    public int animal; //動物ディスプレイのナンバー
    public int stairway; //階段フラグ 0:ない 1:階段の上にいる
    public int shotIndex; //ショットの順番

    public MyChara() {
        x = 5*32.0f;
        y = 13*32.0f;
        wx = 5*32.0f;
        wy = 113*32.0f;
        vx = 0.0f;
        vy = 0.0f;
        ay = 0.6f;
        l = x;
        r = x + 31.0f;
        t = y;
        b = y + 31.0f;
        hp = 100;
        str= 10;
        def= 10;
        exp= 0;
        base_index=0;
        index = 0;
        second_jump=0;
        ichigo=0;
        status=0;
        animal=0;
        stairway=0;
        shotIndex=0;
    }

    public void reset() {
        x = 5*32.0f;
        y = 13*32.0f;
        wx = 5*32.0f;
        wy = 113*32.0f;
        vx = 0.0f;
        vy = 0.0f;
        l = x;
        r = x + 31.0f;
        t = y;
        b = y + 31.0f;
        hp = 100;
        str= 10;
        def= 10;
        exp= 0;
        index = 0;
        ichigo=0;
        status=0;
        animal=0;
        stairway=0;
        shotIndex=0;
    }

    public void move(int touch_direction, float view_width, float view_height, Map map, MainActivity ac) {
        if ( status ==0 ) {
            if (touch_direction == 0) {
                vy = 0.0f;
                vx = 0.0f;
                base_index = 0;
            }
            if (touch_direction == 1) {
                vx = 3.0f;
                vy=0.0f;
                base_index = 0;
            }
            if (touch_direction == 2) {
                vx = -3.0f;
                vy=0.0f;
                base_index = 2;
            }
            if (touch_direction == 3) {
                vy = 3.0f;
                vx = 0.0f;
                base_index = 6;
            }
            if (touch_direction == 4) {
                vy = -3.0f;
                vx = 0.0f;
                base_index = 4;
            }
            //当たり判定用マップ座標算出
            int x1=(int)(wx+4.0f+vx)/32; if (x1<0) x1=0; if (x1>9) x1=9;
            int y1=(int)(wy+4.0f+vy)/32; if (y1<0) y1=0; if (y1>114) y1=114;
            int x2=(int)(wx+28.0f+vx)/32; if (x2>9) x2=9; if (x2<0) x2=0;
            int y2=(int)(wy+28.0f+vy)/32; if (y2>114) y2=114; if (y2<0) y2=0;
            //カベ判定
            if (map.MAP[ac.stage][y1][x1] > 0 || map.MAP[ac.stage][y1][x2] > 0 || map.MAP[ac.stage][y2][x1] > 0 || map.MAP[ac.stage][y2][x2] > 0) {
                vx = 0.0f;
                vy = 0.0f;
            }
            //ワールド座標更新
            wx=wx+vx;
            if (wx<0.0f) wx=0.0f;
            if (wx>9*32.0f) wx=9*32.0f;
            wy=wy+vy;
            if (wy<0.0f) wy=0.0f;
            if (wy>114*32.0f) wy=114*32.0f;
            //ワールド当たり判定移動
            l = wx+ 4.0f+vx;
            r = wx+28.0f+vx;
            t = wy+ 4.0f+vy;
            b = wy+28.0f+vy;
            //画面座標更新
            x = x + vx;
            if (x>view_width-32.0f*4) {
                x=view_width-32.0f*4;
                ac.mapx=ac.mapx-vx;
                if (ac.mapx<-9*32+6*32.0f) ac.mapx=-9*32+6*32.0f;
            }
            if (x<64.0f) {
                x=64.0f;
                ac.mapx=ac.mapx-vx;
                if (ac.mapx>64.0f) ac.mapx=64.0f;
            }
            y = y + vy;
            if (y>view_height-4*32.0f) {
                y=view_height-4*32.0f;
                ac.mapy=ac.mapy-vy;
                if (ac.mapy<-114*32+11*32.0f) ac.mapy=-114*32+11*32.0f;
            }
            if (y<view_height-10*32.0f) {
                y=view_height-10*32.0f;
                ac.mapy=ac.mapy-vy;
                if (ac.mapy>114*32.0f) ac.mapy=114*32.0f;
            }
            //アニメーションインデックス変更処理
            index++;
            if (index > 19) index = 0;
        } else if (status ==1) {

        }
    }

    public void SetVx() {
        if (base_index==0) {
            base_index=2;
        } else {
            base_index=0;
        }
    }

    public void SetStatus(int s) { status=s; }

    public void SetStatus1() { status=1; }

    public void SetXY(float dx, float dy) {
        x = dx;
        y = dy;
    }

    public void setAnimal(int i){
        animal=i;
    }

    public void setHp(int n) { hp=hp+n; }
}
