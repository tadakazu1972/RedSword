package tadakazu1972.redsword;

/**
 * Created by tadakazu on 2015/09/22.
 */
public class Map {
    private int[][][] map_data;
    public int[][][] MAP;

    public Map(){
        //MAP配列初期化
        MAP = new int[1][115][10];
        //代入処理
        map_data = new int[][][]{
                //stage01
                {
                        {1,1,1,1,1,1,1,1,1,1,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,14,14,0,0,0,0,},
                        {0,0,0,2,1,1,2,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {14,0,14,0,14,0,14,0,0,0,},
                        {1,2,1,1,2,1,1,2,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,14,0,14,0,14,0,14,},
                        {0,0,2,1,1,2,1,1,2,1,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {14,0,14,0,14,0,14,0,0,0,},
                        {1,2,1,1,2,1,1,2,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,14,0,14,0,14,0,14,},
                        {0,0,2,1,1,2,1,1,2,1,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {14,0,14,0,14,0,14,0,0,0,},
                        {1,2,1,1,2,1,1,2,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,14,14,0,0,0,0,},
                        {0,0,0,2,1,1,2,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,14,0,0,0,0,0,0,14,0,},
                        {1,1,2,0,0,0,0,2,1,1,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {14,0,14,0,0,0,0,14,0,14,},
                        {2,1,1,2,0,0,2,1,1,2,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,14,0,14,0,0,0,0,14,0,},
                        {1,2,1,1,2,0,0,2,1,1,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {14,0,14,0,14,0,14,0,0,0,},
                        {1,2,1,1,2,1,1,2,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,14,0,14,0,14,0,0,0,0,},
                        {2,1,1,2,1,1,2,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {14,0,14,0,14,0,0,0,0,0,},
                        {1,1,2,1,1,2,0,0,0,1,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,14,0,14,0,0,0,0,0,0,},
                        {1,2,1,1,2,0,0,0,2,1,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {14,0,14,0,0,0,0,14,0,14,},
                        {1,1,2,0,0,0,2,1,1,1,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,14,0,0,0,14,0,0,},
                        {0,0,2,1,1,1,1,2,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,14,0,0,0,0,0,0,14,0,},
                        {1,1,2,0,0,0,0,2,1,1,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,14,0,0,14,0,0,0,},
                        {0,0,2,1,1,1,1,2,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,14,0,0,0,0,0,0,14,0,},
                        {1,1,2,0,0,0,0,2,1,1,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,14,0,0,14,0,0,0,},
                        {0,0,2,1,1,1,1,2,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,14,0,14,0,0,0,0,0,0,},
                        {1,2,1,1,2,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,2,1,1,2,1,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {1,2,1,1,2,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,2,1,1,2,1,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {1,2,1,1,2,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0,},
                        {0,0,0,0,0,0,0,0,0,0}
                }
        };
        for (int s=0;s<1;s++) {
            for (int i = 0; i < 115; i++) {
                for (int j = 0; j < 10; j++) {
                    MAP[s][i][j] = map_data[s][i][j];
                }
            }
        }
    }

    public void resetMap(){
        for (int s=0;s<1;s++) {
            for (int i = 0; i < 115; i++) {
                for (int j = 0; j < 10; j++) {
                    MAP[s][i][j] = map_data[s][i][j];
                }
            }
        }
    }
}
