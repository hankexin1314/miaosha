import java.util.Arrays;
import java.util.LinkedList;
import java.util.zip.GZIPInputStream;

public class Demo {

    public void mergeSort(int[] nums) {

        mergeSort(nums, 0, nums.length - 1);
        new GZIPInputStream(new Fileo)
    }

    private void mergeSort(int[] nums, int l, int r) {

        if(l >= r) return;
        int mid = (r - l) / 2 + l;
        mergeSort(nums, l, mid);
        mergeSort(nums, mid + 1, r);

        if(nums[mid] > nums[mid + 1]) merge(nums, l ,mid, r);
    }

    private String test() {
        return "[]";
    }

    private void merge(int[] nums, int l, int mid, int r) {

        int[] aux = Arrays.copyOfRange(nums, l,r + 1);
        int i = l, j = mid + 1;
        for(int k = l; k <= r; k++) {

            if(i > mid) {
                nums[k] = aux[j - l];
                j ++;
            }
            else if(j > r) {
                nums[k] = aux[i - l];
                i ++;
            }
            else if(aux[i - l] <= aux[j - l]) {
                nums[k] = aux[i - l];
                i ++;
            }
            else {
                nums[k] = aux[j - l];
                j ++;
            }
        }
    }

    public void printNumbers(int n) {
        if(n <= 0) throw new IllegalStateException("n is illegal.");
        
        char[] num = new char[n];
        Arrays.fill(num, '0');
        helper(num, 0);
    }

    private void helper(char[] num, int index) {

        if(index == num.length) {
            printNum(num);
            return;
        }
        for(int i = 0; i < 10; i++) {
            num[index] = (char)('0' + i);
            helper(num, index + 1);
        }
    }

    private void printNum(char[] num) {
        StringBuilder sb = new StringBuilder();
        boolean isZero = true;
        for(int i = 0; i < num.length; i++) {
            if(isZero && num[i] == '0') continue;
            isZero = false;
            sb.append(num[i]);
        }
        if(sb.length() != 0)
            System.out.println(sb.toString());
    }






    public boolean isMatch(String s, String p) {

        if(s == null || p == null) return false;
        if(s.length() == 0 && p.length() == 0) return true;
        if(s.length() == 0 || p.length() == 0) return false;
        char[] cs = s.toCharArray(), cp = p.toCharArray();
        return dfs(cs, cp, cs.length - 1, cp.length - 1, '#');
    }

    private boolean dfs(char[] s, char[] p, int si, int pi, char c) {

        if(si == -1 && pi == -1) return true;
        if(pi == -1) return false;
        if(si >= 0) {
            if(c == '#') {
                if(s[si] == p[pi] || p[pi] == '.')
                    return dfs(s, p, si - 1, pi - 1, c);
                else if(p[pi] == '*') {
                    return dfs(s, p, si, pi - 1, p[pi - 1]);
                }
                else
                    return false;
            }
            else {
                if(c == s[si] || c == '.')
                    // 匹配   不匹配被*标记的
                    return dfs(s, p, si - 1, pi, c) || dfs(s, p, si, pi - 1, '#');
                else
                    return dfs(s, p, si, pi - 1, '#');
            }
        }
        else {
            if(c != '#')
                return dfs(s, p, si, pi - 1, '#');
            if(p[pi] == '*')
                return dfs(s, p, si, pi - 1, p[pi - 1]);
            return false;
        }
    }

    public double[] twoSum(int n) {

        if(n <= 0) return null;
        int[] auxA = new int[6 * n + 1], auxB = new int[6 * n + 1];
        boolean useA = true;
        for(int i = 1; i <= n; i++) { // 第i个骰子
            for(int j = i; j <= 6 * i; j++) { // 和为j
                for(int k = 1; k <= 6; k++) { // 这个骰子的点数
                    if(k >= j) break;
                    if(useA) auxA[j] += auxB[j - k];
                    else auxB[j] += auxA[j - k];
                }
            }
            if(useA) Arrays.fill(auxB, 0);
            else Arrays.fill(auxA, 0);
            useA = !useA;
        }
        double[] res = new double[5 * n + 1];
        double m = Math.pow(6, n);
        for(int i = 0; i < 5 * n + 1; i++) {
            if(useA) res[i] = (double)auxB[i + n] / m;
            else res[i] = (double)auxA[i + n] / m;
        }
        return res;
    }

    public static void main(String[] args) {

        Demo demo = new Demo();
        System.out.println(Arrays.toString(demo.twoSum(1)));

    }
}
