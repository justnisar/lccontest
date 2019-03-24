class Solution {
	    public int maxScoreSightseeingPair(int[] A) {
	    	int max = Integer.MIN_VALUE;
	    	int temp = Integer.MIN_VALUE/2;
	        for(int i = 0;i < A.length;i++){
	        	max = Math.max(max, temp + A[i] - i);
	        	temp = Math.max(temp, A[i] + i);
	        }
	        return max;
	    }
	}	
