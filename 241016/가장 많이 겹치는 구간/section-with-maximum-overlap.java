import java.util.*;
import java.io.*;
public class Main {
    public static void main(String[] args)throws IOException {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        
        int[] lines = new int[200000];
        int inf = 0;
        for(int i=0;i<n;i++){
            StringTokenizer st = new StringTokenizer(br.readLine());
            int s = Integer.parseInt(st.nextToken());
            int e = Integer.parseInt(st.nextToken());
            lines[s] = 1;
            lines[e] = -1;
            inf = Math.max(inf,e);
        }

        int max =0;
        for(int i =1;i<=inf;i++){
            lines[i] +=lines[i-1];
            max = Math.max(max,lines[i]);
        }

        int result = 0;

        for(int i = 0;i<=inf;i++){
            if(max==lines[i]){
                if(i>0 && lines[i-1]==lines[i]) continue;
                else result++;
            }
        }
        System.out.println(result);
        

    }
}