import java.io.*;
import java.util.Scanner;

public class FindingLFSR {
    static int[] poly1 = {1,0,0,0,0,0,1}; // feedback polynomial of degree 7
    static int[] poly2 = {0,0,0,1,0,0,0,0,1}; // feedback polynomial of degree 9
    static int[] poly3 = {0,1,0,0,0,0,0,0,0,0,1}; // feedback polynomial of degree 11
    static int[] arr1, arr2, arr3;
    static int l1=7;                        // LENGTH OF LFSR-1
    static int l2=9;                       // LENGTH OF LFSR-2
   static int l3=11;        // LENGTH OF LFSR-3
   static  int cycles=2000;
    static int LFSR(int length, int[] arr, int[] poly) {
        int fb = 0;
        for (int i = 0; i < length; i++)
            fb ^= (poly[i] * arr[length - i - 1]);
        for (int i = 0; i < length - 1; i++)
            arr[i] = arr[i + 1];
        arr[length - 1] = fb;
        return arr[0];
    }
    void transformIntToBin(int x, int n, int[] bit) {
        for (int i = 0; i < n; i++) {
            bit[i] = (x >> i) & 0x1;
        }
    }
    public static void main(String[] args)
    {
       BufferedReader ifp=null;
        PrintWriter ofp=null;
        try {
            ifp = new BufferedReader(new FileReader("/home/kristina/Desktop/FINKI/Semestar6/K/FindingOccurence/key_seq.txt"));
            ofp = new PrintWriter(new FileWriter("result.txt"));

            int[] init_st1, init_st2, init_st3;
            int[] deriv_st1, deriv_st2, deriv_st3;
            int i, k = 0, x1, x2, x3, f;
            int cnt1 = 0, cnt2 = 0, cnt3 = 0;
            char ch;
            int st, st1, st2;
            int[] z;
            init_st1 = new int[l1];
            deriv_st1 = new int[l1];
            arr1 = new int[l1];

            init_st2 = new int[l2];
            deriv_st2 = new int[l2];
            arr2 = new int[l2];

            init_st3 = new int[l3];
            deriv_st3 = new int[l3];
            arr3 = new int[l3];
            //Read the bits from the previosly generated keystream and store it in z[]
            z = new int[cycles];
            for (i = 0; i < cycles; i++) {
                ch = (char) ifp.read();
                z[i] = ch - 48;
            }

            //Find the initial state of LFSR-1
            ofp.write("Initial value of LFSR-1: ");
            findInitialSequence(ofp, init_st1, deriv_st1, z, l1, arr1, poly1);

            // find the initial state of LFSR-3
            ofp.write("Initial value of LFSR-3: ");
            findInitialSequence(ofp, init_st3, deriv_st3, z, l3, arr3, poly3);

            //Find the initial state of lfsr-2 with exhaustive research
            for(st2=1;st2<Math.pow(2, l2);st2++)
            {
                for(i=0;i<l2;i++)
                {
                    arr2[i]=init_st2[i]=(st2>>i)&0x1;
                }
                for(i=l1-1;i>=0;i--)
                    arr1[i]=deriv_st1[i];

                for(i=l2-1;i>=0;i--)
                    arr2[i]=init_st2[i];

                for(i=l3-1;i>=0;i--)
                    arr3[i]=deriv_st3[i];
                for(k=0;k<cycles;k++)
                {
                    x1=LFSR(l1,arr1,poly1);
                    x2=LFSR(l2,arr2,poly2);
                    x3=LFSR(l3,arr3,poly3);
                    f=(x1*x2)^(x2*x3)^x3;
                    if(f!=z[k])
                        break;

                }
                if(k==cycles)
                {
                    ofp.write("Found\n");
                    ofp.write("Initial state for LFSR-2: ");
                    for(i=l2-1;i>=0;i--)
                    {
                        deriv_st2[i]=init_st2[i];
                       ofp.write(String.format("%d\t",init_st2[i]));
                    }
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ifp != null)
                    ifp.close();
                if (ofp != null)
                    ofp.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void findInitialSequence(PrintWriter ofp, int[] init_st, int[] deriv_st, int[] z, int l, int[] arr, int[] poly) throws IOException {
        int st;
        int i;
        int cnt;
        int t;
        int x;
        for(st=0; st<Math.pow(2, l); st++)
        {
            for(i=0; i< l; i++)
            {
                arr[i]=init_st[i]=(st>>i)&0x1;

            }
            cnt=0;
            for(t=0;t<cycles;t++)
            {
                x=LFSR(l, arr, poly);
                if(z[t]==x)
                    cnt++;
            }
            float prob=(float)cnt/(float)cycles;
            if(prob>0.7 && prob<0.8)
            {
                for (i= l -1; i>=0; i--)
                {
                    deriv_st[i]=init_st[i];
                    ofp.print(init_st[i]+"\t");
                }
                ofp.print(String.format("   %d:\t p(z(i)=x2(i))=%.2f\n", st, prob));

            }
        }
    }
}
