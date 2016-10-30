package tadakazu1972.redsword;

import java.util.Random;

/**
 * Created by tadakazu on 2015/09/22.
 */
public class Kaonyan {
    public float x, y;
    public float wx, wy;
    public float vx, vy;
    public float l, r, t, b;
    public int base_index; //アニメーション基底 0:右向き 2:左向き
    public int index;
    public int move_direction;
    public int visible;
    public int ranX, ranY;

    public Kaonyan(Map map, MainActivity ac) {
        do {
            Random rndX = new Random();
            Random rndY = new Random();
            ranX = rndX.nextInt(20);
            ranY = rndY.nextInt(20);
        } while (map.MAP[ac.stage][ranY][ranX]>0);
        x = ranX*32.0f;
        y = ranY*32.0f;
        wx = x;
        wy = y;
        vx = 0.0f;
        vy = 0.0f;
        l = wx;
        r = wx + 15.0f;
        t = wy;
        b = wy + 15.0f;
        base_index=0;
        index = 0;
        Random rndDirection = new Random();
        move_direction = rndDirection.nextInt(4);
        visible = 1; //
    }

    public void Reset(Map map, MainActivity ac) {
        do {
            Random rndX = new Random();
            Random rndY = new Random();
            ranX = rndX.nextInt(20);
            ranY = rndY.nextInt(20);
        } while (map.MAP[ac.stage][ranY][ranX]>0);
        x = ranX*32.0f;
        y = ranY*32.0f;
        wx = x;
        wy = y;
        vx = 0.0f;
        vy = 0.0f;
        l = wx;
        r = wx + 15.0f;
        t = wy;
        b = wy + 15.0f;
        base_index=0;
        index = 0;
        Random rndDirection = new Random();
        move_direction = rndDirection.nextInt(4);
        visible = 1; //ステージ数で増えるときには見えるようにセット
    }

    public void move(MyChara m, MainActivity ac, float view_width, float view_height, Map map, Star s) {
        if (move_direction == 0) {
            vx = 1.0f;
            vy=0.0f;
            base_index = 0;
        }
        if (move_direction == 1) {
            vx = -1.0f;
            vy=0.0f;
            base_index = 2;
        }
        if (move_direction == 2) {
            vy = 1.0f;
            vx = 0.0f;
            base_index = 0;
        }
        if (move_direction == 3) {
            vy = -1.0f;
            vx = 0.0f;
            base_index = 2;
        }
        //当たり判定用マップ座標算出
        int x1=(int)(wx+4.0f+vx)/32; if (x1<0) x1=0; if (x1>19) x1=19;
        int y1=(int)(wy+4.0f+vy)/32; if (y1<0) y1=0; if (y1>19) y1=19;
        int x2=(int)(wx+28.0f+vx)/32; if (x2>19) x2=19; if (x2<0) x2=0;
        int y2=(int)(wy+28.0f+vy)/32; if (y2>19) y2=19; if (y2<0) y2=0;
        //カベ判定
        if (map.MAP[ac.stage][y1][x1]>0||map.MAP[ac.stage][y1][x2]>0||map.MAP[ac.stage][y2][x1]>0||map.MAP[ac.stage][y2][x2]>0) {
            vx = 0.0f;
            vy = 0.0f;
            Random rndDirection = new Random();
            move_direction = rndDirection.nextInt(4);
        }
        //ワールド座標更新
        wx=wx+vx;
        if (wx<0.0f) wx=0.0f;
        if (wx>19*32.0f) wx=19*32.0f;
        wy=wy+vy;
        if (wy<0.0f) wy=0.0f;
        if (wy>19*32.0f) wy=19*32.0f;
        //ワールド当たり判定移動
        l = wx+ 4.0f+vx;
        r = wx+28.0f+vx;
        t = wy+ 4.0f+vy;
        b = wy+28.0f+vy;
        //プレイヤーとの当たり判定
        if ( l < m.r && m.l < r && t < m.b && m.t < b ) {
            if (ac.gs!=0) ac.PlaySound(3);
            visible=0;
        }
        //座標更新
        x = x + vx;
        y = y + vy;
        //アニメーションインデックス変更処理
        index++;
        if ( index > 19 ) index =0;
    }
}
