public class Kata
{
    public static long nextSmaller(long n)
    {
        final char[] dgt = Long.toString(n).toCharArray();
        final int[] dgtCnt = new int[10];
        boolean notFound = true;
        int i;
        for(i=dgt.length-1;i>0;i--) {
            dgtCnt[dgt[i]-'0']++;
            if(dgt[i]<dgt[i-1]) {
                notFound = false;
                dgtCnt[dgt[i-1]-'0']++;
                break;
            }
        }
        if(notFound) return -1;
        boolean unset = true;
        for(int j=dgt[i-1]-'0'-1;j>=0;j--) {
            if(dgtCnt[j]>0) {
                dgtCnt[j]--;
                unset = false;
                dgt[i-1] = (char)(j+'0');
                break;
            }
        }
        if(unset) return -1;
        for(int j=9;j>=0;j--) {
            for(;dgtCnt[j]>0; dgtCnt[j]--, i++) {
                dgt[i] = (char)(j+'0');
            }
        }
        if(dgt[0] == '0') return -1;
        return Long.parseLong(new String(dgt));
    }
}