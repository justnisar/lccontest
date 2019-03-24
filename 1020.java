class Solution {
        public boolean canThreePartsEqualSum(int[] A) {
        
        if(A.length < 3){
            return false;
        }
        
        int[] leftSum  = new int[A.length];
        int lsum = 0 ;
        for(int i = 0 ; i < A.length ; i++){
            lsum += A[i];
            leftSum[i] = lsum;
        }
        
        for(int i = 0 ; i < A.length - 2 ; i++){
            for(int j = i+1 ; j < A.length - 1 ; j++){
                int firstPartSum = leftSum[i];
                int middlePartSum = leftSum[j] - leftSum[i];
                int lastpartSum = leftSum[A.length - 1] - leftSum[j];
                if(firstPartSum == middlePartSum &&  middlePartSum == lastpartSum){
                    return true;
                }
            }
        }
        
        return false;
        
        
    }
}
