package tadakazu1972.redsword;

import android.app.Activity;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends Activity implements GLSurfaceView.Renderer {

    final float VIEW_WIDTH = 320;
    final float VIEW_HEIGHT = 480;
    private float lastPointX, lastPointY;
    public int touch_direction;

    private TouchWatcher m_TouchWatcher;
    private TouchManager m_TouchMngr;
    private TouchButton m_Button;

    private GLSurfaceView m_View;
    private DrawDevice m_DrawDevice;
    private AppResource m_AppRes;
    private TextureResource m_TexRes;

    private Model[] m_sMap = new Model[3];
    private Model[] m_sBg = new Model[2];
    public Map m_MAP;
    public float mapx;
    public float mapy;

    protected MyChara m;
    private Model[] m_sKumako = new Model[8];
    protected Shot[] shot = new Shot[16];
    private Model[] m_sShot = new Model[4];
    public static final int KN = 100;
    //protected Kaonyan[] k = new Kaonyan[KN];
    private Model[] m_sKaonyan = new Model[4];
    public int kaonyan_number; //かおにゃん登場数
    public static final int SN = 5;
    protected Star[] s = new Star[SN];
    private Model[] sStar = new Model[7]; //7パターンの絵
    public int star_number;
    public int star_counter;
    public static final int BN = 10;
    protected Bird[] bird = new Bird[BN];
    private Model[] m_sBird = new Model[4];
    public int bird_number;
    public static final int CN = 35;
    protected Cake[] c = new Cake[CN];
    protected Baloon b;
    private Model m_sBaloon;
    private Model[] m_sRibbon = new Model[2];
    protected Cloud[] cloud = new Cloud[3];
    private Model[] m_sCloud = new Model[3];
    public static final int IN = 100;
    private Model m_sTitle;
    private Model m_sEndroll;
    private float endY;
    public int counter; //ケーキゲット数
    public int stage; //ステージ数
    public int gs; //ゲームステータス
    public int loop; //周回数記憶用
    private SoundPool mSoundPool;
    private int mSoundId1;
    private int mSoundId2;
    private int mSoundId3;
    private int mSoundId4;
    private int mSoundId5;
    private int mSoundId6;

    public float[] nx =new float[100], ny=new float[100];
    public float[] nwx=new float[100], nwy=new float[100];
    public float[] nvx=new float[100], nvy=new float[100];
    public float[] nl=new float[100], nr=new float[100], nt=new float[100], nb=new float[100];
    public int[] nbase_index=new int[100]; //アニメーション基底 0:右向き 2:左向き
    public int[] nindex=new int[100];
    public int[] nmove_direction=new int[100];
    public int[] nvisible=new int[100];
    public int[] nranX=new int[100], nranY=new int[100];
    //タイマー
    private Timer mainTimer;
    private MainTimerTask mainTimerTask;
    private Handler mHandler = new Handler();

    //タイマータスク派生クラス run()に定周期で処理したい内容を記述
    public class MainTimerTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post (new Runnable() {
                public void run() {
                    if (gs == 1) { //ゲーム中なら以下を実行
                        //ショットが発射されていなかったら発射
                        if (shot[m.shotIndex].visible!=1){
                            shot[m.shotIndex].visible=1;
                            m.shotIndex++;
                            if (m.shotIndex>15) m.shotIndex=0;
                        }
                    }
                }
            });
        }
    }

    public MainActivity() {
        super();
        m_View = null;
        m_DrawDevice = null;

        m_TouchWatcher  = null;
        m_TouchMngr     = null;
        m_Button        = null;
        m_AppRes        = null;
        m_TexRes        = null;

        m = null;
        m_MAP = new Map();
        mapx=0.0f;
        mapy=-100*32.0f;
        counter = 0;
        stage = 0;
        kaonyan_number= 100;
        star_number=5;
        star_counter=0;
        bird_number=0;
        gs=0;
        endY=480.0f;
        loop=0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //タッチ関係初期設定
        lastPointX=0.0f; lastPointY=0.0f;
        touch_direction=0;
        //アーサー生成
        m = new MyChara();
        //ショット生成
        for (int i=0;i<16;i++) shot[i] = new Shot(m);
        //モンスター生成
        //for (int i=0;i<KN;i++) k[i]=new Kaonyan(m_MAP,this);
        this.setNeko(m_MAP);
        //星生成
        for (int t=0;t<SN;t++) s[t]=new Star();
        //鳥生成
        for (int k=0;k<BN;k++) bird[k]=new Bird();
        //バルーン生成
        b = new Baloon();
        //雲生成
        for ( int q=0;q<3;q++) cloud[q]=new Cloud(q);
        m_View = new GLSurfaceView( this );
        m_View.setRenderer( this );
        m_DrawDevice = new DrawDevice();
        m_TouchWatcher  = new TouchWatcher();
        m_TouchMngr     = new TouchManager( m_TouchWatcher );
        m_Button        = new TouchButton( m_TouchMngr );
        {
            m_Button.SetRect( 0, 0, ( int ) DrawDevice.DRAW_WIDTH, ( int ) DrawDevice.DRAW_HEIGHT );
        }
        m_AppRes        = new AppResource( this );
        m_TexRes        = new TextureResource( m_AppRes, m_DrawDevice );
        setContentView( m_View );
        //タイマー
        mainTimer = new Timer();
        mainTimerTask = new MainTimerTask();
        mainTimer.schedule(mainTimerTask, 300, 300);
    }

    public void onResume() {
        super.onResume();
        //隙間時間に音声データ読み込み
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSoundId1 = mSoundPool.load(getApplicationContext(), R.raw.get2, 0);
        mSoundId2 = mSoundPool.load(getApplicationContext(), R.raw.get, 0);
        mSoundId3 = mSoundPool.load(getApplicationContext(), R.raw.pote01, 0);
        mSoundId4 = mSoundPool.load(getApplicationContext(), R.raw.clear, 0);
        mSoundId5 = mSoundPool.load(getApplicationContext(), R.raw.key, 0);
        mSoundId6 = mSoundPool.load(getApplicationContext(), R.raw.baloon, 0);
        //others
        m_View.onResume();
    }

    public void onPause() {
        m_View.onPause();
        super.onPause();
        mSoundPool.release();
    }

    @Override
    public void onSurfaceCreated( GL10 gl, EGLConfig config ) {
        m_DrawDevice.Create(gl);
    }

    @Override
    public void onSurfaceChanged( GL10 gl, int width, int height) {
        Rect recr = new Rect();
        m_View.getWindowVisibleDisplayFrame( recr );

        m_DrawDevice.UpdateDrawArea(gl, width, height, recr.top);
    }

    @Override
    public boolean  onTouchEvent( MotionEvent event )
    {
        final float orgTouchPosX    = event.getX();
        final float orgTouchPosY    = event.getY();
        PointF touchPos        = m_DrawDevice.GetSprite().ScreenPosToDrawPos( new PointF( orgTouchPosX, orgTouchPosY ));

        float touchedX = event.getX();
        float touchedY = event.getY();
        //前回タッチとの差分を算出
        float temp_vx, temp_vy;
        temp_vx = Math.abs(lastPointX-touchedX);
        temp_vy = Math.abs(lastPointY-touchedY);

        TouchWatcher.ACTION action;

        switch( event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //タイトルのとき
                if (gs==0) {
                    this.PlaySound(2);
                    gs=1;
                }
                //武器屋
                if (gs==3) {
                    if (touchPos.x>180 && touchPos.y>400) {
                        touch_direction=3;
                        gs=1;
                    }
                }
                action  = TouchWatcher.ACTION.ACTION_DOWN;
                break;
            case MotionEvent.ACTION_MOVE: //タッチされた
                if (temp_vx > temp_vy) {
                    if (lastPointX - touchedX < 0) {
                        touch_direction = 1; //right
                    } else {
                        touch_direction = 2; //left
                    }
                } else {
                    if (lastPointY - touchedY < 0) {
                        touch_direction = 3; //down
                    } else {
                        touch_direction = 4; //up
                    }
                }
                //タッチ座標更新
                lastPointX = touchedX;
                lastPointY = touchedY;
                action  = TouchWatcher.ACTION.ACTION_MOVE;
                break;
            case MotionEvent.ACTION_UP:
                action  = TouchWatcher.ACTION.ACTION_UP;    break;
            default:
                return true;
        }

        if( !m_TouchWatcher.AddAction( action, touchPos ))
        {
            System.err.println( "TouchEventOverFlow!!" );
        }

        return true;
    }

    @Override
    public void onDrawFrame( GL10 gl ) {
        //シーンの更新
        m_DrawDevice.Begin( gl );
        {
            m_TouchMngr.Update();

            //画像読み込み
            if (m_sKumako[0] == null) {
                loadImage();
            }
            //描画
            //*****************************************************************************
            //タイトル
            if (gs==0) {
                //背景
                m_sBg[0].Draw(0, 0, m_DrawDevice);
                //雲
                for (int q=0;q<3;q++) {
                    m_sCloud[q].Draw(cloud[q].x,cloud[q].y, m_DrawDevice);
                    cloud[q].Move1(this);
                }
                //タイトル
                m_sTitle.Draw(0, 0, m_DrawDevice);
                //アーサー
                int index = m.base_index + m.index / 10;
                m_sKumako[index].Draw(m.x, m.y, m_DrawDevice);
                //移動
                //アーサー
                m.move(touch_direction, VIEW_WIDTH, VIEW_HEIGHT, m_MAP, this);

                //********************************************************************************
                //通常ゲーム
            } else if (gs==1) {
                //背景
                m_sBg[1].Draw(0, 0, m_DrawDevice);
                //MAP
                for (int i = 0; i < 115; i++) {
                    for (int j = 0; j < 10; j++) {
                        int mapid = m_MAP.MAP[stage][i][j];
                        if (mapid == 0) {
                            m_sMap[0].Draw(j*32+mapx, i*32+mapy, m_DrawDevice);
                        }
                        if (mapid == 1) {
                            m_sMap[1].Draw(j*32+mapx, i*32+mapy, m_DrawDevice);
                        }
                        if (mapid == 2) {
                            m_sMap[2].Draw(j*32+mapx, i*32+mapy, m_DrawDevice);
                        }
                        if (mapid == 14) {
                            m_sMap[2].Draw(j*32+mapx, i*32+mapy, m_DrawDevice);
                        }
                    }
                }
                //バルーン
                m_sBaloon.Draw(b.x, b.y, m_DrawDevice);
                int b_index = b.index / 10;
                m_sRibbon[b_index].Draw(b.x, b.y + 200.0f, m_DrawDevice);
                //ねこ
                for (int i1 = 0; i1 < kaonyan_number; i1++) {
                    //Kaonyan i = k[i1];
                    //if (i.visible==1) {
                    //    int k_index = i.base_index + i.index / 10;
                    //    m_sKaonyan[k_index].Draw(i.x+mapx, i.y+mapy, m_DrawDevice);
                    //}
                    if (nvisible[i1]==1){
                        int k_index=nbase_index[i1]+nindex[i1]/10;
                        m_sKaonyan[k_index].Draw(nx[i1]+mapx, ny[i1]+mapy, m_DrawDevice);
                    }
                }
                //鳥
                for (int k1 = 0; k1 < bird_number; k1++) {
                    Bird k = bird[k1];
                    int bird_index = k.base_index + k.index / 10;
                    m_sBird[bird_index].Draw(k.x, k.y, m_DrawDevice);
                }
                //アーサー
                int index = m.base_index + m.index / 10;
                m_sKumako[index].Draw(m.x, m.y, m_DrawDevice);
                //星
                for (int t1 = 0; t1 < star_number; t1++) {
                    Star i = s[t1];
                    if (i.visible==1) {
                        int k_index = i.index / 10;
                        sStar[k_index].Draw(i.x, i.y, m_DrawDevice);
                    }
                }
                //ショット
                for (int i=0;i<16;i++) {
                    m_sShot[0].Draw(shot[i].x, shot[i].y, m_DrawDevice);
                }
                //雲
                for (int q=0;q<3;q++) {
                    m_sCloud[q].Draw(cloud[q].x,cloud[q].y, m_DrawDevice);
                    cloud[q].Move2(this);
                }
                //移動
                //アーサー
                m.move(touch_direction, VIEW_WIDTH, VIEW_HEIGHT, m_MAP, this);
                //ショット
                for (int i=0;i<16;i++) {
                    shot[i].move(m, this, m_MAP);
                }
                //モンスター
                for (int i2 = 0; i2 < kaonyan_number; i2++) {
                    //Kaonyan i = k[i2];
                    //if (i.visible==1) {
                    //    i.move(m, this, VIEW_WIDTH, VIEW_HEIGHT, m_MAP, s[star_counter]);
                    //}
                    if (nvisible[i2]==1){
                        this.nekoMove(i2, m, m_MAP);
                    }
                }
                //星
                for (int t2 = 0; t2 < star_number; t2++) {
                    Star i = s[t2];
                    if (i.visible==1) {
                        i.Move();
                    }
                }
                //鳥
                for (int k2 = 0; k2 < bird_number; k2++) {
                    bird[k2].Move(m, this, VIEW_WIDTH, VIEW_HEIGHT, m_MAP);
                }
                //バルーン
                b.Move(m, this);

                //エンディング
            } else if (gs==2) {
                //背景
                m_sBg[1].Draw(0, 0, m_DrawDevice);
                //雲
                for (int q=0;q<3;q++) {
                    m_sCloud[q].Draw(cloud[q].x,cloud[q].y, m_DrawDevice);
                    cloud[q].Move2(this);
                }
                //バルーン
                m_sBaloon.Draw(b.x, b.y, m_DrawDevice);
                int b_index = b.index / 10;
                m_sRibbon[b_index].Draw(b.x, b.y + 200.0f, m_DrawDevice);
                //リボンのアニメーション処理
                b.index++;
                if ( b.index > 19 ) b.index =0;
                //くまちゃん
                int index = m.base_index + m.index / 10;
                m_sKumako[index].Draw(b.x+84, b.y+200, m_DrawDevice);
                //エンドロール
                m_sEndroll.Draw(0, endY, m_DrawDevice);
                endY=endY-0.6f;
                b.y=b.y-0.3f; //バルーンの上昇速度はエンドロールより遅め
                if (endY<-960.0f) {
                    endY=480.0f;
                    //バルーン画面上の待機位置へ
                    b.SetY(-260.0f);
                    gs=0;
                }
                //武器屋
            } else if (gs==3) {

            }
        }
        m_DrawDevice.End();

        //  タッチイベント監視人更新
        m_TouchWatcher.Update();
    }

    public void loadImage(){
        m_sKumako[0] = new Model();
        m_sKumako[1] = new Model();
        m_sKumako[2] = new Model();
        m_sKumako[3] = new Model();
        m_sKumako[4] = new Model();
        m_sKumako[5] = new Model();
        m_sKumako[6] = new Model();
        m_sKumako[7] = new Model();
        m_sKumako[0].Create("arthur07", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);
        m_sKumako[1].Create("arthur08", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);
        m_sKumako[2].Create("arthur07", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);
        m_sKumako[3].Create("arthur08", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);
        m_sKumako[4].Create("arthur07", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);
        m_sKumako[5].Create("arthur08", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);
        m_sKumako[6].Create("arthur07", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);
        m_sKumako[7].Create("arthur08", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);
        m_sShot[0] = new Model();
        m_sShot[1] = new Model();
        m_sShot[2] = new Model();
        m_sShot[3] = new Model();
        m_sShot[0].Create("shotSword01", 0.0f, 0.0f, 0, 0, 8, 24, m_TexRes);
        m_sShot[1].Create("shotSword01", 0.0f, 0.0f, 0, 0, 8, 24, m_TexRes);
        m_sShot[2].Create("shotSword01", 0.0f, 0.0f, 0, 0, 8, 24, m_TexRes);
        m_sShot[3].Create("shotSword01", 0.0f, 0.0f, 0, 0, 8, 24, m_TexRes);
        m_sKaonyan[0] = new Model();
        m_sKaonyan[1] = new Model();
        m_sKaonyan[2] = new Model();
        m_sKaonyan[3] = new Model();
        for (int i=0;i<3;i++){
            m_sMap[i] = new Model();
        }
        m_sMap[0].Create("field01", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);
        m_sMap[1].Create("brick", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);
        m_sMap[2].Create("bigtree", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);

        m_sKaonyan[0].Create("neko01", 0.0f, 0.0f, 0, 0, 16, 16, m_TexRes);
        m_sKaonyan[1].Create("neko02", 0.0f, 0.0f, 0, 0, 16, 16, m_TexRes);
        m_sKaonyan[2].Create("neko03", 0.0f, 0.0f, 0, 0, 16, 16, m_TexRes);
        m_sKaonyan[3].Create("neko04", 0.0f, 0.0f, 0, 0, 16, 16, m_TexRes);
        for (int i=0;i<7;i++){
            sStar[i] = new Model();
        }
        sStar[0].Create("star01", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);
        sStar[1].Create("star02", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);
        sStar[2].Create("star03", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);
        sStar[3].Create("star04", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);
        sStar[4].Create("star05", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);
        sStar[5].Create("star06", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);
        sStar[6].Create("star07", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);
        m_sBird[0] = new Model();
        m_sBird[1] = new Model();
        m_sBird[2] = new Model();
        m_sBird[3] = new Model();
        m_sBird[0].Create("bird03", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);
        m_sBird[1].Create("bird04", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);
        m_sBird[2].Create("bird01", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);
        m_sBird[3].Create("bird02", 0.0f, 0.0f, 0, 0, 32, 32, m_TexRes);
        m_sBg[0] = new Model();
        m_sBg[1] = new Model();
        m_sBg[0].Create("bg_stage01", 0.0f, 0.0f, 0, 0, 320, 480, m_TexRes);
        m_sBg[1].Create("bg_ending", 0.0f, 0.0f, 0, 0, 320, 480, m_TexRes);
        m_sBaloon = new Model();
        m_sBaloon.Create("bigbaloon", 0.0f, 0.0f, 0, 0, 200, 200, m_TexRes);
        m_sRibbon[0] = new Model();
        m_sRibbon[1] = new Model();
        m_sRibbon[0].Create("baloonribbon01", 0.0f, 0.0f, 0, 0, 200, 96, m_TexRes);
        m_sRibbon[1].Create("baloonribbon02", 0.0f, 0.0f, 0, 0, 200, 96, m_TexRes);
        m_sCloud[0] = new Model();
        m_sCloud[1] = new Model();
        m_sCloud[2] = new Model();
        m_sCloud[0].Create("cloud01", 0.0f, 0.0f, 0, 0, 120, 60, m_TexRes);
        m_sCloud[1].Create("cloud02", 0.0f, 0.0f, 0, 0, 120, 60, m_TexRes);
        m_sCloud[2].Create("cloud03", 0.0f, 0.0f, 0, 0, 120, 60, m_TexRes);
        m_sTitle = new Model();
        m_sTitle.Create("title", 0.0f, 0.0f, 0, 0, 320, 480, m_TexRes);
        m_sEndroll = new Model();
        m_sEndroll.Create("endroll", 0.0f, 0.0f, 0, 0, 320, 960, m_TexRes);
    }

    public void SetTouch() {
        if (touch_direction==1) {
            touch_direction=2;
        } else {
            touch_direction=1;
        }
    }

    public void GotoNextStage() {
        //エンディングへ
        stage=9;
        //ループ数増加
        loop++;
        if (loop>30) loop=30;
        //ループすると鳥増える
        bird_number++;
        if (bird_number > BN ) bird_number=BN;
        //バルーンのY座標を画面下に設定
        b.SetY(480.0f);
        //雲のvyを下に行くように変更
        for(int q=0;q<3;q++) {
            Random r = new Random();
            cloud[q].SetVY((float)r.nextInt(3)+1.0f);
        }
        //エンドロールへ
        gs=2;

        //マップ再セット
        //ケーキ再セット
        counter=0;
        for (Cake i:c) {
            i.ResetCake(m_MAP);
        }
        //鳥再セット
        for ( int k=0;k<bird_number;k++) {
            bird[k].Reset();
        }
        //くまちゃん再セット
        m.reset();
        stage=9;
        //マップ再セット
        m_MAP.resetMap();
    }

    public void PlaySound(int id){
        mSoundPool.play(id, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public void setStage(int i) {
        stage=stage+i;
        //マップ再セット
        //m_MAP.SetStage(stage);
    }

    public void setGs(int i){
        gs=i;
    }

    public void setStar_counter(int i) { star_counter=i; }

    public void setTouch_direction(int i) { touch_direction=i; }

    public void gotoNextLevel(){
        //キー再セット
        for (Cake i:c) {
            i.ResetCake(m_MAP);
        }
        //モンスター再セット
        for (int i=0;i<kaonyan_number;i++) {
            //k[i].Reset(m_MAP,this);
        }

    }

    public void setNeko(Map map) {
        for (int i=0;i<100;i++) {
                Random rndX = new Random();
                Random rndY = new Random();
                nranX[i] = rndX.nextInt(10);
                nranY[i] = rndY.nextInt(115);
            nx[i] = nranX[i] * 32.0f;
            ny[i] = nranY[i] * 32.0f;
            nwx[i] = nx[i];
            nwy[i] = ny[i];
            nvx[i] = 0.0f;
            nvy[i] = 0.0f;
            nl[i] = nwx[i];
            nr[i] = nwx[i] + 15.0f;
            nt[i] = nwy[i];
            nb[i] = nwy[i] + 15.0f;
            nbase_index[i] = 0;
            nindex[i] = 0;
            Random rndDirection = new Random();
            nmove_direction[i] = rndDirection.nextInt(4);
            nvisible[i] = 1; //
        }
    }

    public void nekoMove(int i, MyChara m, Map map) {
        if (nmove_direction[i] == 0) {
            nvx[i] = 1.0f;
            nvy[i]=0.0f;
            nbase_index[i] = 0;
        }
        if (nmove_direction[i] == 1) {
            nvx[i] = -1.0f;
            nvy[i]=0.0f;
            nbase_index[i] = 2;
        }
        if (nmove_direction[i] == 2) {
            nvy[i] = 1.0f;
            nvx[i] = 0.0f;
            nbase_index[i] = 0;
        }
        if (nmove_direction[i] == 3) {
            nvy[i] = -1.0f;
            nvx[i] = 0.0f;
            nbase_index[i] = 2;
        }
        //当たり判定用マップ座標算出
        int x1=(int)(nwx[i]+4.0f+nvx[i])/32; if (x1<0) x1=0; if (x1>9) x1=9;
        int y1=(int)(nwy[i]+4.0f+nvy[i])/32; if (y1<0) y1=0; if (y1>114) y1=114;
        int x2=(int)(nwx[i]+28.0f+nvx[i])/32; if (x2>9) x2=9; if (x2<0) x2=0;
        int y2=(int)(nwy[i]+28.0f+nvy[i])/32; if (y2>114) y2=114; if (y2<0) y2=0;
        //カベ判定
        if (map.MAP[stage][y1][x1]>0||map.MAP[stage][y1][x2]>0||map.MAP[stage][y2][x1]>0||map.MAP[stage][y2][x2]>0) {
            nvx[i] = 0.0f;
            nvy[i] = 0.0f;
            Random rndDirection = new Random();
            nmove_direction[i] = rndDirection.nextInt(4);
        }
        //ワールド座標更新
        nwx[i]+=nvx[i];
        if (nwx[i]<0.0f) nwx[i]=0.0f;
        if (nwx[i]>9*32.0f) nwx[i]=9*32.0f;
        nwy[i]+=nvy[i];
        if (nwy[i]<0.0f) nwy[i]=0.0f;
        if (nwy[i]>114*32.0f) nwy[i]=114*32.0f;
        //ワールド当たり判定移動
        nl[i] = nwx[i]+ 4.0f+nvx[i];
        nr[i] = nwx[i]+28.0f+nvx[i];
        nt[i] = nwy[i]+ 4.0f+nvy[i];
        nb[i] = nwy[i]+28.0f+nvy[i];
        //プレイヤーとの当たり判定
        if ( nl[i] < m.r && m.l < nr[i] && nt[i] < m.b && m.t < nb[i] ) {
            if (gs!=0) PlaySound(3);
            nvisible[i]=0;
        }
        //ショットとの当たり判定
        for (int j=0;j<15;j++){
            if ( nl[i] < shot[j].r && shot[j].l < nr[i] && nt[i] < shot[j].b && shot[j].t < nb[i] ) {
                if (gs!=0) PlaySound(3);
                nvisible[i]=0;
            }
        }
        //座標更新
        nx[i]+=nvx[i];
        ny[i]+=nvy[i];
        //アニメーションインデックス変更処理
        nindex[i]++;
        if ( nindex[i] > 19 ) nindex[i] =0;
    }
}
