import java.io.*;
import java.util.Arrays;

public class LFSR {
    static int[] poly1 = {1,0,0,0,0,0,1}; // feedback polynomial of degree 7
    static int[] poly2 = {0,0,0,1,0,0,0,0,1}; // feedback polynomial of degree 9
    static int[] poly3 = {0,1,0,0,0,0,0,0,0,0,1}; // feedback polynomial of degree 11


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
        BufferedReader ifp = new BufferedReader(new FileReader("src/inp.txt"));
        BufferedWriter ofp = new BufferedWriter(new FileWriter("key_seq.txt"));
        BufferedWriter ofp1 = new BufferedWriter(new FileWriter("lfsr_seq1.txt"));
        BufferedWriter ofp2 = new BufferedWriter(new FileWriter("lfsr_seq2.txt"));
        BufferedWriter ofp3 = new BufferedWriter(new FileWriter("lfsr_seq3.txt"));

        int no_lfsr, l1, l2, l3, no_clk;
        int[] init_st1, init_st2, init_st3;
        int i, j, k, x1, x2, x3, f;
        int cnt1 = 0, cnt2 = 0, cnt3 = 0;


        /**************OPENING THE DATA FILES*****************/

        if (ifp == null) {
            System.out.println("\n Unable to open input file.\n");
            System.exit(0);
        }

        /*****************READING THE INPUT FILE******************/

        String line = ifp.readLine();
        no_lfsr = Integer.parseInt(line);
        l1 = Integer.parseInt(ifp.readLine());

        init_st1 = new int[l1];
        String [] niza1=ifp.readLine().split(" ");
        arr1 = new int[l1];
        for (i = l1 - 1; i >= 0; i--) {

            init_st1[i] = Integer.parseInt(niza1[i]);
            arr1[i] = init_st1[i];
        }
        l2 = Integer.parseInt(ifp.readLine());
        init_st2 = new int[l2];
        arr2 = new int[l2];
        String [] niza2=ifp.readLine().split(" ");
        for (i = l2 - 1; i >= 0; i--) {
            init_st2[i] = Integer.parseInt(niza2[i]);
            arr2[i] = init_st2[i];
        }
        l3 = Integer.parseInt(ifp.readLine());
        init_st3 = new int[l3];
        arr3 = new int[l3];
        String [] niza3=ifp.readLine().split(" ");
        for (i = l3 - 1; i >= 0; i--) {
            init_st3[i] = Integer.parseInt(niza3[i]);
            arr3[i] = init_st3[i];
        }

        no_clk = Integer.parseInt(ifp.readLine());
        System.out.printf("\n%d\n",no_clk);
        //test
//        int[] test={1, 0, 0, 1, 0, 1, 0};
//        int[] polytest={1,0,0,0,0,0,1};
//        //System.out.printf("%d",test.length);
//        for(k=0;k<no_clk;k++){
//            x1=LFSR(7,test,polytest);
//            System.out.printf("%d",x1);
//        }

//
       /******************************CLOCKING THE LFSRs*******************/

        int period1 = 0, period2 = 0, period3 = 0;
        for (k = 0; k < no_clk; k++) {
            x1 = LFSR(l1, arr1, poly1);
            System.out.printf("%d\t", x1);
            ofp1.write(String.format("%d",x1));

            x2 = LFSR(l2, arr2, poly2);
            ofp2.write(String.format("%d",x2));

            x3 = LFSR(l3, arr3, poly3);
            ofp3.write(String.format("%d",x3));

            f=(x1*x2)^(x2*x3)^x3; //non-linear function Geffe generator

            ofp.write(String.format("%d",f));

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
        System.out.printf("cnt1 %d \n cnt2 %d \n cnt3 %d",cnt1,cnt2,cnt3);

        System.out.printf("\n Probability for occurrence x1(t)=z(t) = %.2f....%d", (float) cnt1 / (float)no_clk, cnt1);
        System.out.printf("\n Probability for occurrence x2(t)=z(t)= %.2f....%d", (float) cnt2 / (float)no_clk, cnt2);
        System.out.printf("\n  Probability for occurrence x3(t)=z(t)= %.2f....%d", (float) cnt3 / (float) no_clk, cnt3);

        System.out.printf("\n Minimum period of LFSR1= %d\n", period1);
        System.out.printf("\n Minimum period of LFSR2= %d\n", period2);
        System.out.printf("\n Minimum period of LFSR3= %d\n", period3);


        ifp.close();
        ofp.close();
        ofp1.close();
        ofp2.close();
        ofp3.close();

    }
}
