import java.util.*;
import java.io.*;
public class Main {
    static int dx[]= {0,0,1,-1};
    static int dy[]= {1,-1,0,0};

    public static void main(String[] args)throws IOException {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        int F = Integer.parseInt(st.nextToken());

        int[][] uSpace = new int[N][N];
        for(int i=0;i<N;i++){
            st = new StringTokenizer(br.readLine());
            for(int j=0;j<N;j++){
                uSpace[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        int[][][] tBlock = new int[5][M][M];
        for(int i=0;i<M;i++){
            st = new StringTokenizer(br.readLine());
            for(int j=0;j<M;j++){
                tBlock[1][i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for(int i=0;i<M;i++){
            st = new StringTokenizer(br.readLine());
            for(int j=0;j<M;j++){
                tBlock[3][i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for(int i=0;i<M;i++){
            st = new StringTokenizer(br.readLine());
            for(int j=0;j<M;j++){
                tBlock[0][i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for(int i=0;i<M;i++){
            st = new StringTokenizer(br.readLine());
            for(int j=0;j<M;j++){
                tBlock[2][i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for(int i=0;i<M;i++){
            st = new StringTokenizer(br.readLine());
            for(int j=0;j<M;j++){
                tBlock[4][i][j] = Integer.parseInt(st.nextToken());
            }
        }

        Abnormal[] abnormals = new Abnormal[F];
        for(int i=0;i<F;i++){
            st = new StringTokenizer(br.readLine());
            abnormals[i] = new Abnormal();
            abnormals[i].x = Integer.parseInt(st.nextToken());
            abnormals[i].y = Integer.parseInt(st.nextToken());
            abnormals[i].d = Integer.parseInt(st.nextToken());
            abnormals[i].t = Integer.parseInt(st.nextToken());
        }


        int[][] board = initBorad(tBlock,uSpace,N,M);

        int answer = bfs(board,uSpace,abnormals,N,M,F);
        System.out.println(answer);

    }

    public static int[][] initBorad(int[][][] tBlock, int[][] uSpace, int N, int M){
        int[][] board = new int[N+2*M][N+2*M];
        
        int bx = -1, by = -1;
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                if(uSpace[i][j]==3){
                    bx = i;
                    by = j;
                    break;
                }
            }
            if(bx!=-1 && by!=-1) break;
        }

        for(int i=0;i<=3;i++){
            for(int j=0;j<i;j++) tBlock[i] = rotateBoard(tBlock[i],M);
        }

        for(int i=0;i<N+2*M;i++){
            for(int j=0;j<N+2*M;j++){
                if(i<bx && j<by) board[i][j] = uSpace[i][j];
                else if(i<bx && j>=by+M && j<by+2*M) board[i][j] = uSpace[i][j-M];
                else if(i<bx && j>=by+3*M) board[i][j] = uSpace[i][j-2*M];
                else if(i>=bx+M && i<bx+2*M && j<by) board[i][j] = uSpace[i-M][j];
                else if(i>=bx+3*M && j<by) board[i][j] = uSpace[i-2*M][j];
                else if(i>=bx+M && i<bx+2*M && j>=by+3*M) board[i][j] = uSpace[i-M][j-2*M];
                else if(i>=bx+3*M && j>=by+M && j<by+2*M) board[i][j] = uSpace[i-2*M][j-M];
                else if(i>=bx+3*M && j>=by+3*M) board[i][j] = uSpace[i-2*M][j-2*M];
                //북
                else if(i>=bx && i<bx+M && j>=by+M && j<by+2*M) board[i][j] = tBlock[2][i-bx][j-by-M];
                //서
                else if(i>=bx+M && i<bx+2*M && j>=by && j<by+M) board[i][j] = tBlock[3][i-bx-M][j-by];
                //동
                else if(i>=bx+M && i<bx+2*M && j>=by+2*M && j<by+3*M) board[i][j] = tBlock[1][i-bx-M][j-by-2*M];
                //남
                else if(i>=bx+2*M && i<bx+3*M && j>=by+M && j<by+2*M) board[i][j] = tBlock[0][i-bx-2*M][j-by-M];
                //위
                else if(i>=bx+M && i<bx+2*M && j>=by+M && j<by+2*M) board[i][j] = tBlock[4][i-bx-M][j-by-M];
                

                else board[i][j] = 5;
                
            }
        }

        return board;
    }

    public static int[][] rotateBoard(int[][] board, int N){
        int[][] result = new int[N][N];
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                result[i][j] = board[j][N-1-i];
            }
        }
        return result;
    }

    public static int bfs(int[][] board, int[][] uSpace, Abnormal[] abnormals, int N, int M, int F){
        int result = -1;
        
        int bx = -1, by = -1;
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                if(uSpace[i][j]==3){
                    bx = i;
                    by = j;
                    break;
                }
            }
            if(bx!=-1 && by!=-1) break;
        }

        int sx =-1, sy = -1, ex = -1, ey = -1;
        for(int i=0;i<N+2*M;i++){
            for(int j=0;j<N+2*M;j++){
                if(board[i][j]==4){
                    ex = i;
                    ey = j;
                }
                else if(board[i][j]==2){
                    sx = i;
                    sy = j;
                }
            }
        }


        ArrayList<int[]> abnormalInfos = makeAbnormalInfo(board,abnormals,bx,by,N,M,F);
        Collections.sort(abnormalInfos, new Comparator<int[]>(){
            @Override
            public int compare(int[] o1, int[] o2){
                return o1[2]-o2[2];
            }
        });

        

        boolean[][] visited = new boolean[N+2*M][N+2*M]; 
        Queue<Person> q = new LinkedList<>();
        q.add(new Person(sx,sy,0));
        

        while(!q.isEmpty()){
            Person cp = q.poll();
            if(visited[cp.x][cp.y]) continue;
            visited[cp.x][cp.y] = true;

            if(cp.x ==ex && cp.y ==ey){
                result = cp.time;
                break;
            }
            
            for(int i=0;i<4;i++){
                int nx = cp.x+dx[i];
                int ny = cp.y+dy[i];
                Person np = findNextLocation(board,abnormalInfos,i,nx,ny,cp.time+1,bx,by,N,M);
                if(np.time == -1) continue; //움직일 수 없을 때
                q.add(np);
            }
        }

        return result;
    }

    public static Person findNextLocation(int[][] board, ArrayList<int[]> abnormalInfos, int d, int nx, int ny, int time, int bx, int by, int N, int M){
        if(nx<0|| ny<0||nx>=N+2*M||ny>=N+2*M) return new Person(-1,-1,-1);

        if(nx>=bx && nx<bx+M && ny == by+M-1 && d==1){ //북->서
            ny = by+M-Math.abs(bx+M-nx);
            nx = bx+M;
        }
        else if(nx>=bx && nx<bx+M && ny == by+2*M && d==0){ //북->동
            ny = by+2*M-1+Math.abs(bx+M-nx);
            nx = bx+M;
        }
        else if(nx>=bx+2*M && nx<bx+3*M && ny == by+M-1 && d==1){ //남->서
            ny = by+M-Math.abs(bx+2*M-1-nx);
            nx = bx+2*M-1;
        }
        else if(nx>=bx+2*M && nx<bx+3*M && ny == by+2*M && d==0){ //남->동
            ny = by+2*M-1+Math.abs(bx+2*M-1-nx);
            nx = bx+2*M-1;
        }
        else if(ny>=by && ny<by+M && nx == bx+M-1 && d==3){ //서->북
            nx = bx+M-Math.abs(by+M-ny);
            ny = by+M;
        }
        else if(ny>=by && ny<by+M && nx == bx+2*M && d==2){ //서->남
            nx = bx+2*M-1+Math.abs(by+M-ny);
            ny = by+M;
        }
        else if(ny>=by+2*M && ny<by+3*M && nx == bx+M-1 && d==3){ //동->북
            nx = bx+M-Math.abs(by+2*M-1-ny);
            ny = by+2*M-1;
        }
        else if(ny>=by+2*M && ny<by+3*M && nx == bx+2*M && d==2){ //동->남
            nx = bx+2*M-1+Math.abs(by+2*M-1-ny);
            ny = by+2*M-1;
        }
        else{
            if(board[nx][ny]==5){
                while(true){
                    nx+=dx[d];
                    ny+=dy[d];
                    if(board[nx][ny]!=5) break;
                }
            }
        }

        if(board[nx][ny] == 1){
            return new Person(-1,-1,-1);
        }

        for(int[] abnormalInfo : abnormalInfos){
            if(abnormalInfo[2]>time) break;
            else{
                if(abnormalInfo[0] == nx && abnormalInfo[1] == ny) return new Person(-1,-1,-1);
            }
        }

        return new Person(nx,ny,time);
    }

    public static ArrayList<int[]> makeAbnormalInfo(int[][] board, Abnormal[] abnormals, int bx, int by, int N, int M, int F){
        ArrayList<int[]> info = new ArrayList<>();

        for(int i=0;i<F;i++){
            int nx = abnormals[i].x;
            int ny = abnormals[i].y;
            if(nx>=bx && nx<bx+M) nx+=M;
            else if(nx>=bx+M) nx+=2*M;

            if(ny>=by && ny<by+M) ny+=M;
            else if(ny>=by+M) ny+=2*M;
            

            info.add(new int[] {nx,ny,0});
            int cnt = 1;
            while(true){
                int d = abnormals[i].d;
                nx = nx+dx[d];
                ny = ny+dy[d];
                if(nx<0||ny<0||nx>=N+2*M||ny>=N+2*M) break;
                if(board[nx][ny]==5) continue;
                if(board[nx][ny]==0) info.add(new int[] {nx,ny,cnt*abnormals[i].t});
                else break;
                cnt++;
            }
        }
        return info;
    }
}

class Person{
    int x;
    int y;
    int time;
    public Person(int x, int y, int time){
        this.x = x;
        this.y = y;
        this.time = time;
    }
}

class Abnormal{
    int x;
    int y;
    int d;
    int t;
}