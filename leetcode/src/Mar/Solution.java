import java.util.*;

/*给定一些标记了宽度和高度的信封，宽度和高度以整数对形式 (w, h) 出现。当另一个信封的宽度和高度都比这个信封大的时候，这个信封就可以放进另一个信封里，如同俄罗斯套娃一样。

        请计算最多能有多少个信封能组成一组“俄罗斯套娃”信封（即可以把一个信封放到另一个信封里面）。

        说明:
        不允许旋转信封。

        输入: envelopes = [[5,4],[6,4],[6,7],[2,3]]
        输出: 3
        解释: 最多信封的个数为 3, 组合为: [2,3] => [5,4] => [6,7]。
*/
class Solution {
    public int maxEnvelopes(int[][] envelopes) {
        if (envelopes.length == 0)return 0;
        int re = 1;
        Arrays.sort(envelopes, (o1, o2) -> o1[0] - o2[0]);
        int [] count = new int[envelopes.length];
        Arrays.fill(count,1);
        count [0] = 1;
        for (int i = 1; i < count.length; i++) {
            for (int j = 0; j < i; j++) {
                if (envelopes[j][0]<envelopes[i][0]&&envelopes[j][1]<envelopes[i][1]){
                    count[i] = Math.max(count[j]+1,count[i]);
                }
            }
            re = Math.max(re, count[i]);
        }
        return re;
    }
}
