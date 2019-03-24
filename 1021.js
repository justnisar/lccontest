/**
 * @param {number[]} A
 * @return {number}
 */
var maxScoreSightseeingPair = function(A) {
    let max = 0;
    for(let i = 0 ; i < A.length - 1 ; i++){
      for(let j = i + 1 ; j < A.length ; j++){
        let current = A[i] + A[j] + i - j;
        if(current > max){
          max = current;
        }
      }
    }
    return max;
};
