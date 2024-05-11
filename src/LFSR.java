import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class LFSR {
    static int[] poly1 = {1,0,0,0,0,0,1}; // LFSR1
    static int[] poly2 = {0,0,0,1,0,0,0,0,1}; // LFSR2
    static int[] poly3 = {0,1,0,0,0,0,0,0,0,0,1}; // LFSR3
    static int[] arr1, arr2, arr3;
    static int LFSR(int length, int[] arr, int[] poly) {
        int fb = 0;
        for (int i = 0; i < length; i++) {
            fb ^= (poly[i] * arr[length - i - 1]);
        }
        for (int i = 0; i < length - 1; i++)
            arr[i] = arr[i + 1];
        arr[length - 1] = fb;
        return arr[0];
    }
    public static void main(String[] args) throws IOException {
        Scanner ifp = new Scanner(new File("src/inp.txt"));
        PrintWriter ofp = new PrintWriter(new FileWriter("key_seq.txt"));
        PrintWriter ofp1 = new PrintWriter(new FileWriter("lfsr_seq1.txt"));
        PrintWriter ofp2 = new PrintWriter(new FileWriter("lfsr_seq2.txt"));
        PrintWriter ofp3 = new PrintWriter(new FileWriter("lfsr_seq3.txt"));
        System.out.println("Choose 1 for Geffe Generator or choose 2 for other generator:");
        Scanner function = new Scanner(System.in);
        String input=function.nextLine();

        int no_lfsr, l1, l2, l3, no_clk;
        int[] init_st1, init_st2, init_st3;
        int i, j, k, x1, x2, x3, f;
        int cnt1 = 0, cnt2 = 0, cnt3 = 0;


        no_lfsr=ifp.nextInt();
        l1 = ifp.nextInt();

        init_st1 = new int[l1];
        arr1 = new int[l1];
        for (i = l1 - 1; i >= 0; i--) {

            init_st1[i] = ifp.nextInt();
            arr1[i] = init_st1[i];
        }
        l2 = ifp.nextInt();
        init_st2 = new int[l2];
        arr2 = new int[l2];

        for (i = l2 - 1; i >= 0; i--) {
            init_st2[i] =ifp.nextInt();
            arr2[i] = init_st2[i];
        }
        l3 = ifp.nextInt();
        init_st3 = new int[l3];
        arr3 = new int[l3];

        for (i = l3 - 1; i >= 0; i--) {
            init_st3[i] = ifp.nextInt();
            arr3[i] = init_st3[i];
        }
        no_clk = ifp.nextInt();
        int period1 = 0, period2 = 0, period3 = 0;
        for (k = 0; k < no_clk; k++) {
            x1 = LFSR(l1, arr1, poly1);
            ofp1.print(String.format("%d",x1));
            x2 = LFSR(l2, arr2, poly2);
            ofp2.print(String.format("%d",x2));
            x3 = LFSR(l3, arr3, poly3);
            ofp3.print(String.format("%d",x3));
            if(input.equals("1"))
            {
                //non-linear function Geffe generator
                f=(x1*x2)^(x2*x3)^x3;
            }
            else {
                //other non-linear function
                f=x1^x2^x3;
            }
            //generate key stream
            ofp.print(String.format("%d",f));
            if (f == x1) cnt1++;
            if (f == x2) cnt2++;
            if (f == x3) cnt3++;
            for (j = 0; j < l1; j++)
                if (arr1[j] != init_st1[j])
                    break;
            if (j == l1 && period1 == 0)
                period1 = k + 1;
            for (j = 0; j < l2; j++)
                if (arr2[j] != init_st2[j])
                    break;
            if (j == l2 && period2 == 0)
                period2 = k + 1;
            for (j = 0; j < l3; j++)
                if (arr3[j] != init_st3[j])
                    break;
            if (j == l3 && period3 == 0)
                period3 = k + 1;
        }
        float prob1,prob2,prob3;
        prob1=(float) cnt1 / (float)no_clk;
        prob2=(float) cnt2 / (float)no_clk;
        prob3=(float) cnt3 / (float)no_clk;
        System.out.println("In this example we are using the following LFSRs:");
        System.out.println("1) 1+x+x^7 ");
        System.out.println("2) 1+x^4+x^9");
        System.out.println("3) 1+x^2+x^11");
        System.out.printf("\n Probability for occurrence x1(i)=z(i) = %.2f....", prob1);
        System.out.printf("\n Probability for occurrence x2(i)=z(i)= %.2f....", prob2);
        System.out.printf("\n Probability for occurrence x3(i)=z(i)= %.2f....", prob3);

        System.out.printf("\n Minimum period of LFSR1= %d", period1);
        System.out.printf("\n Minimum period of LFSR2= %d", period2);
        System.out.printf("\n Minimum period of LFSR3= %d", period3);


        ifp.close();
        ofp.close();
        ofp1.close();
        ofp2.close();
        ofp3.close();

    }
}
