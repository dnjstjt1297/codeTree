import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class Main {
    static final int[] dx = {-1,1,0,0};
    static final int[] dy = {0,0,-1,1};
    static int n;
    static int q;
    static int[][] board;
    static int[][] micros;
    static int[][] groups;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());
        board = new int[n][n];
        micros = new int[q][4];
        groups = new int[q][2];

        for(int i = 0; i<q;i++){
            st = new StringTokenizer(br.readLine());
            micros[i][0] = Integer.parseInt(st.nextToken());
            micros[i][1] = Integer.parseInt(st.nextToken());
            micros[i][2] = Integer.parseInt(st.nextToken());
            micros[i][3] = Integer.parseInt(st.nextToken());
            groups[i][0] = i+1;
        }

        for(int i = 0; i< q; i++){
            fillMicro(i+1, micros[i][0],micros[i][1],micros[i][2],micros[i][3]);
            moveMicros();
            long score = getScore();
            System.out.println(score);
        }
    }

    public static void fillMicro(int micro, int r1, int c1, int r2, int c2){
        for(int i = r1; i<r2; i++){
            for(int j = c1; j<c2; j++){
                if(board[i][j]!=0) groups[board[i][j]-1][1]--;
                board[i][j] = micro;
                groups[micro-1][1]++;
            }
        }
    }

    public static void moveMicros(){
        int[][] cgroups = new int[q][2];
        for(int i = 0; i<q; i++){
            cgroups[i] = groups[i].clone();
        }
        Arrays.sort(cgroups, (o1, o2) ->{
            if(o2[1]==o1[1]){
                return o1[0]-o2[0];
            }
            return o2[1]-o1[1];
        });

        int[][] newBoard = new int[n][n];

        for(int[] group : cgroups){
            int mcnt = group[1];
            int micro = group[0];
            int mx = 0, my = 0;
            aaa: for(int i = 0; i<n; i++){
                for(int j = 0; j<n; j++){
                    if(board[i][j]==micro){
                        mx=i;
                        my=j;
                        break aaa;
                    }
                }
            }

            aaa: for(int i=0; i<n;i++){
                for(int j=0; j<n;j++){
                    if(newBoard[i][j] != 0) continue;

                    int[][] tmp = new int[n][n];
                    for(int k = 0; k<n;k++){
                        tmp[k] = newBoard[k].clone();
                    }

                    boolean[][] visited = new boolean[n][n];
                    Queue<int[]> queue = new LinkedList<>();
                    queue.add(new int[]{i,j});
                    visited[i][j] = true;
                    int cnt = 0;

                    while(!queue.isEmpty()){
                        int[] cur = queue.poll();
                        tmp[cur[0]][cur[1]] = micro;
                        cnt++;
                        for(int k=0; k<4; k++){
                            int nx = cur[0]+dx[k];
                            int ny = cur[1]+dy[k];

                            if(nx<0 || ny<0 || nx>=n || ny>=n) continue;
                            if(mx+(nx-i)<0 || my+(ny-j)<0 || mx+(nx-i)>=n || my+(ny-j)>=n) continue;
                            if(visited[nx][ny]) continue;
                            if(board[mx+(nx-i)][my+(ny-j)]!=micro) continue;
                            if(newBoard[nx][ny]!=0) continue;
                            visited[nx][ny] = true;
                            queue.add(new int[]{nx,ny});
                        }
                    }
                    if(cnt == mcnt){
                        newBoard = tmp;
                        break aaa;
                    }
                }
            }
        }
        board = newBoard;
    }

    public static long getScore(){
        boolean[][] visited = new boolean[q+1][q+1];
        long score = 0;
        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){

                for(int k = 0; k<4; k++){
                    int nx = i+dx[k];
                    int ny = j+dy[k];
                    if(nx<0 || ny<0 || nx>=n || ny>=n) continue;
                    int g1 = board[nx][ny];
                    int g2 = board[i][j];
                    if(g1==0 || g2==0) continue;
                    if(g1!=g2) {
                        if (visited[g1][g2] || visited[g2][g1]) continue;
                        visited[g1][g2] = true;
                        visited[g2][g1] = true;
                        score+= (long) groups[g1 - 1][1] *groups[g2-1][1];
                    }
                }
            }
        }
        return score;
    }

    public static void print(int[][] board){
        for(int j = 0; j<n; j++){
            for(int i = 0; i<n; i++){
                System.out.print(board[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
