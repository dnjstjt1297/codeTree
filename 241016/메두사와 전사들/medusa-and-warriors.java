import java.util.*;
import java.io.*;

class Warrier{
    int x;
    int y;
    boolean rockStatus = false;
    boolean liveStatus = true;

    public Warrier(int x, int y){
        this.x = x;
        this.y = y;
    }
}

public class Main {
    static int[] dx = {-1,1,0,0};
    static int[] dy = {0,0,-1,1};
    static boolean[][] sight;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        int sx = Integer.parseInt(st.nextToken());
        int sy = Integer.parseInt(st.nextToken());
        int ex = Integer.parseInt(st.nextToken());
        int ey = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        Warrier[] warriers = new Warrier[M];
        for(int i =0; i<M;i++){
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            warriers[i] = new Warrier(x,y);
        }

        int[][] board = new int[N][N];
        for(int i=0;i<N;i++){
            st = new StringTokenizer(br.readLine());
            for(int j=0;j<N;j++){
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        ArrayList<int[]> route = findRouteOfMedusa(board,N,sx,sy,ex,ey);
        sight = new boolean[N][N];

        if(route.isEmpty()) System.out.println(-1);
        for(int[] r : route){
            if(r[0]== sx && r[1] == sy) continue;
            if(r[0]== ex && r[1] == ey){
                System.out.println(0);
                break;
            }
            for(Warrier w: warriers){
                if(w.x == r[0] && w.y== r[1]) w.liveStatus = false;
            }

            int b = makeRock(warriers, N, r[0], r[1]);
            int[] ac = moveWarries(warriers,N, r[0], r[1]);
            System.out.println(ac[0]+" "+b+" "+ac[1]);
        }
        
    }

    public static ArrayList<int[]> findRouteOfMedusa(int[][] board, int N, int sx, int sy, int ex, int ey) {
        Queue<int[]> q1 = new LinkedList<>();
        q1.add(new int[]{sx, sy});
        
        boolean[][] visited = new boolean[N][N];
        visited[sx][sy] = true; 
        ArrayList<int[]> route = new ArrayList<>();
        route.add(new int[]{sx, sy}); 
        
        Queue<ArrayList<int[]>> q2 = new LinkedList<>();
        q2.add(route);

        while (!q1.isEmpty()) {
            int[] cm = q1.poll();
            ArrayList<int[]> cr = q2.poll();

            if (cm[0] == ex && cm[1] == ey) {
                return cr;
            }

            for (int i = 0; i < 4; i++) {
                int nx = cm[0] + dx[i];
                int ny = cm[1] + dy[i];
                if (nx < 0 || ny < 0 || nx >= N || ny >= N || visited[nx][ny] || board[nx][ny] == 1) continue;

                q1.add(new int[]{nx, ny});
                ArrayList<int[]> nr = new ArrayList<>(cr);
                nr.add(new int[]{nx, ny});
                q2.add(nr);
                visited[nx][ny] = true; 
            }
        }

        return new ArrayList<>();
    }


    public static int countRock(Warrier[] warriers, int N, int mx, int my, char compase, boolean isMakeRock){
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++) sight[i][j] = false;
        }
        int rockCnt = 0;
        Arrays.sort(warriers, new Comparator<Warrier>(){
            @Override
            public int compare(Warrier o1, Warrier o2){
                if(compase == 'D'){
                    return o1.x-o2.x;
                }
                else if(compase == 'U') {
                    return o2.x-o1.x;
                }
                else if(compase == 'R'){
                    return o1.y-o2.y;
                }
                else if(compase == 'L'){
                    return o2.y-o1.y;
                }
                return 0;
            }
        });

        if(compase == 'U'){
            for(int i=mx-1; i>=0; i--){
                for(int j=0;j<=mx-i;j++){
                    if(my+j<N) sight[i][my+j] = true;
                    if(my-j>=0) sight[i][my-j] = true;
                }
            }

            for(Warrier w : warriers){
                if(!w.liveStatus) continue;
                if(!sight[w.x][w.y]) continue;
                rockCnt++;
                if(isMakeRock) w.rockStatus = true;

                for(int i = w.x-1;i>=0;i--){
                    if(w.y==my) sight[i][w.y] = false;
                    if(w.y>my){
                        for(int j=0;j<=w.x-i;j++){
                            if(w.y+j<N) sight[i][w.y+j] = false;
                        }
                    }
                    else if(w.y<my){
                        for(int j=0;j<=w.x-i;j++){
                            if(w.y-j>=0) sight[i][w.y-j] = false;
                        }
                    }
                }
            }
        }

        if(compase == 'D'){
            for(int i=mx+1; i<N; i++){
                for(int j=0;j<=i-mx;j++){
                    if(my+j<N) sight[i][my+j] = true;
                    if(my-j>=0) sight[i][my-j] = true;
                }
            }

            for(Warrier w : warriers){
                if(!w.liveStatus) continue;
                if(!sight[w.x][w.y]) continue;
                rockCnt++;
                if(isMakeRock) w.rockStatus = true;

                for(int i = w.x+1;i<N;i++){
                    if(w.y==my) sight[i][w.y] = false;
                    if(w.y>my){
                        for(int j=0;j<=i-w.x;j++){
                            if(w.y+j<N) sight[i][w.y+j] = false;
                        }
                    }
                    else if(w.y<my){
                        for(int j=0;j<=i-w.x;j++){
                            if(w.y-j>=0) sight[i][w.y-j] = false;
                        }
                    }
                }
            }
        }

        if(compase == 'L'){
            for(int i=my-1; i>=0; i--){
                for(int j=0;j<=my-i;j++){
                    if(mx+j<N) sight[mx+j][i] = true;
                    if(mx-j>=0) sight[mx-j][i] = true;
                }
            }

            for(Warrier w : warriers){
                if(!w.liveStatus) continue;
                if(!sight[w.x][w.y]) continue;
                rockCnt++;
                if(isMakeRock) w.rockStatus = true;

                for(int i = w.y-1;i>=0;i--){
                    if(w.x==mx) sight[w.x][i] = false;
                    if(w.x>mx){
                        for(int j=0;j<=w.y-i;j++){
                            if(w.x+j<N) sight[w.x+j][i] = false;
                        }
                    }
                    else if(w.x<mx){
                        for(int j=0;j<=w.y-i;j++){
                            if(w.x-j>=0) sight[w.x-j][i] = false;
                        }
                    }
                }
            }
            
        }

        if(compase == 'R'){
            for(int i=my+1; i<N; i++){
                for(int j=0;j<=i-my;j++){
                    if(mx+j<N) sight[mx+j][i] = true;
                    if(mx-j>=0) sight[mx-j][i] = true;
                }
            }
            
            for(Warrier w : warriers){
                if(!w.liveStatus) continue;
                if(!sight[w.x][w.y]) continue;
                rockCnt++;
                if(isMakeRock) w.rockStatus = true;

                for(int i = w.y+1;i<N;i++){
                    if(w.x==mx) sight[w.x][i] = false;
                    if(w.x>mx){
                        for(int j=0;j<=i-w.y;j++){
                            if(w.x+j<N) sight[w.x+j][i] = false;
                        }
                    }
                    else if(w.x<mx){
                        for(int j=0;j<=i-w.y;j++){
                            if(w.x-j>=0) sight[w.x-j][i] = false;
                        }
                    }
                }
            }
            
        }

        return rockCnt;
    }

    public static int makeRock(Warrier[] warriers, int N, int mx, int my){
        int dCnt=countRock(warriers, N,  mx,  my,'D',false);
        int uCnt=countRock(warriers, N,  mx,  my,'U',false);
        int lCnt=countRock(warriers, N,  mx,  my,'L',false);        
        int rCnt=countRock(warriers, N,  mx,  my,'R',false);

        int maxCnt = dCnt;
        maxCnt = Math.max(maxCnt,uCnt);
        maxCnt = Math.max(maxCnt,lCnt);
        maxCnt = Math.max(maxCnt,rCnt);

        if(maxCnt==uCnt){
            return countRock(warriers, N, mx, my,'U',true);
        }
        else if(maxCnt==dCnt){
            return countRock(warriers, N, mx, my,'D',true);
        }
        else if(maxCnt==lCnt){
            return countRock(warriers, N, mx, my,'L',true);
        }
        else if(maxCnt==rCnt){
            return countRock(warriers, N, mx, my,'R',true);
        }

        return maxCnt;
    }

    static public int[] moveWarries(Warrier[] warriers,int N, int mx, int my){
        int moveCnt = 0;
        int deadCnt = 0;
        for(Warrier w: warriers){
            if(!w.liveStatus) continue;
            if(w.rockStatus){
                w.rockStatus = false;
                continue;
            }

            if(w.x>mx && w.x-1>=0 && !sight[w.x-1][w.y]){
                w.x--;
                moveCnt++;
            }
            else if(w.x<mx && w.x+1<N && !sight[w.x+1][w.y]){
                w.x++;
                moveCnt++;
            }
            else if(w.y>my && w.y-1>=0 && !sight[w.x][w.y-1]){
                w.y--;
                moveCnt++;
            }
            else if(w.y<my && w.y+1<N && !sight[w.x][w.y+1]){
                w.y++;
                moveCnt++;
            }
            if(w.x == mx && w.y == my) {
                w.liveStatus = false;
                deadCnt++;
                continue;
            }

            if(w.y>my && w.y-1>=0 && !sight[w.x][w.y-1]){
                w.y--;
                moveCnt++;
            }
            else if(w.y<my && w.y+1<N && !sight[w.x][w.y+1]){
                w.y++;
                moveCnt++;
            }
            else if(w.x>mx && w.x-1>=0 && !sight[w.x-1][w.y]){
                w.x--;
                moveCnt++;
            }
            else if(w.x<mx && w.x+1<N && !sight[w.x+1][w.y]){
                w.x++;
                moveCnt++;
            }   
            if(w.x == mx && w.y == my) {
                w.liveStatus = false;
                deadCnt++;
            }
        }

        return new int[]{moveCnt,deadCnt};
    }

}