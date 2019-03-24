	class Solution {
	    public boolean canThreePartsEqualSum(int[] A) {
	        int n = A.length;
	        long[] cum = new long[n+1];
	        for(int i = 0;i < n;i++){
	        	cum[i+1] = cum[i] + A[i];
	        }
	        long S = cum[n];
	        if(S % 3 != 0)return false;
	        S /= 3;
	        
	        int pre = 0;
	        for(int i = 1;i < n;i++){
	        	if(cum[i] == S){
	        		pre += i;
	        		break;
	        	}
	        }
	        if(pre == 0)return false;
	        
	        int opre = pre;
	        for(int i = 1;i < n;i++){
	        	if(cum[n] - cum[n-i] == S){
	        		pre += i;
	        		break;
	        	}
	        }
	        if(opre == pre)return false;
	        
	        return pre < n;
	    }
	}
