class Solution {
            public boolean queryString(String S, int N) {
        
        for(int i = 1 ; i <= N ; i++){
            String str = Integer.toBinaryString(i);
            if(!S.contains(str)){
                return false;
            }
        }
        return true;
    }
}
