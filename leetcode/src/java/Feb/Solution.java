package Feb;
/*给定两个字符串 s1 和 s2，写一个函数来判断 s2 是否包含 s1 的排列。

        换句话说，第一个字符串的排列之一是第二个字符串的子串。*/

import java.util.HashMap;

public class Solution {
    public static void main(String[] args) {
        System.out.println(
                new Solution().checkInclusion("ab","ab")
        );

    }
    public boolean checkInclusion(String s1, String s2) {
        if (s1.length()>s2.length()){
            return false;
        }
        int windowWidth = s1.length();
        int front = 0,end = front + windowWidth - 1;
        //map for s1, mapUpdate for s2
        //actually char[] can be used here, but for more complex string, it is not useful
        HashMap<Character, Integer> map = new HashMap<>();
        HashMap<Character, Integer> mapUpdate = new HashMap<>();
        for (int i = 0; i < s1.length(); i++) {
            map.put(s1.charAt(i),map.getOrDefault(s1.charAt(i),0)+1);
            mapUpdate.put(s2.charAt(i),mapUpdate.getOrDefault(s2.charAt(i),0)+1);
        }
        while(end < s2.length()){
            if (isSame(map, mapUpdate)){
                return true;
            }else{
                //update mapUpdate here
                char c = s2.charAt(front);
                if (mapUpdate.get(c) == 1){
                    mapUpdate.remove(c);
                }else{
                    mapUpdate.put(c,mapUpdate.get(c) - 1);
                }
                front++;
                end++;
                if (end < s2.length()){
                    mapUpdate.put(s2.charAt(end),
                            mapUpdate.getOrDefault(s2.charAt(end),0)+1);
                }

            }
        }
        return false;
    }

    private boolean isSame(HashMap<Character, Integer> map, HashMap<Character, Integer> mapUpdate) {
        for (Character c: map.keySet()){
            if (!mapUpdate.containsKey(c)){
                return false;
            }else if (!mapUpdate.get(c).equals(map.get(c))){
                return false;
            }
        }
        return true;
    }
}
