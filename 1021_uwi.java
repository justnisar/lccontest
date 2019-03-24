	class Solution {
	    public int smallestRepunitDivByK(int K) {
	    	long x = 0;
	        for(int len = 1;len <= 1000000;len++){
	        	x = (x * 10 + 1) % K;
	        	if(x == 0){
	        		return len;
	        	}
	        }
	        return -1;
	    }
	}
