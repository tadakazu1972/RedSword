package tadakazu1972.redsword;

/**
 * Created by tadakazu on 2015/09/22.
 */
public class Shot {
    public float x, y;
    public float wx, wy;
    public float vx, vy;
    public float l, r, t, b;
    public int base_index; //アニメーション基底 0:右向き 2:左向き
    public int index;
    public int visible;

    public Shot(MyChara m) {
        x = m.x + 24.0f; //最初の剣はこの位置
        y = m.y;
        wx = m.wx + 24.0f;
        wy = m.wy;
        vx = 0.0f;
        vy = 0.0f;
        l = wx;
        r = wx + 7.0f; //最初の剣の当たり判定
        t = wy;
        b = wy + 23.0f;
        base_index=0;
        index = 0;
        visible = 0;
    }

    public void Reset(MyChara m) {
        x = m.x + 24.0f;
        y = m.y;
        wx = m.wx + 24.0f;
        wy = m.wy;
        vx = 0.0f;
        vy = 0.0f;
        l = wx;
        r = wx + 7.0f;
        t = wy;
        b = wy + 23.0f;
        base_index=0;
        index = 0;
        visible = 0; //ステージ数で増えるときには見えるようにセット
    }

    public void move(MyChara m, MainActivity ac, Map map) {
        //発射されていたら飛んでいく
        if (visible!=0) {
            vy = -8.0f;
            vx = 0.0f;
            //当たり判定用マップ座標算出
            int x1 = (int) (wx + vx) / 32; if (x1 < 0) x1 = 0; if (x1 > 9) x1 = 9;
            int y1 = (int) (wy + vy) / 32; if (y1 < 0) y1 = 0; if (y1 > 114) y1 = 114;
            int x2 = (int) (wx + 7.0f + vx) / 32; if (x2 > 9) x2 = 9; if (x2 < 0) x2 = 0;
            //int y2 = (int) (wy + 23.0f + vy) / 32; if (y2 > 114) y2 = 114; if (y2 < 0) y2 = 0;
            //カベ判定
            if (map.MAP[ac.stage][y1][x1] > 0 || map.MAP[ac.stage][y1][x2] > 0) {
                vx = 0.0f;
                vy = 0.0f;
                Reset(m);
            }
            //ワールド座標更新
            wx = wx + vx;
            if (wx < 0.0f) wx = 0.0f;
            if (wx > 9 * 32.0f) wx = 9 * 32.0f;
            wy = wy + vy;
            if (wy < -32.0f) wy=0.0f;
            if (wy > 114 * 32.0f) wy = 114 * 32.0f;
            //ワールド当たり判定移動
            l = wx + vx;
            r = wx + 7.0f + vx;
            t = wy + vy;
            b = wy + 23.0f + vy;
            //座標更新
            x = x + vx;
            y = y + vy;
            //アニメーションインデックス変更処理
            index++;
            if (index > 19) index = 0;
        } else {
            //発射されていないなら、主人公の動きと合わせる
            x = m.x + 24.0f;
            y = m.y;
            wx = m.wx + 24.0f;
            wy = m.wy;
        }
    }
}
