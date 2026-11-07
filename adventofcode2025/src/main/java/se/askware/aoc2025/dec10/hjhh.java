package se.askware.aoc2025.dec10;

public class hjhh {

    static void main() {
        int numPositions = 4;
        String s = "";
        for (int j = 0; j < numPositions; j++) {
            s += "1";
        }
        Integer i =  Integer.parseInt(s, 2);
        System.out.println(i);
        
        System.out.println(Integer.toBinaryString(i));
        for (int j = 0; j < i; j++) {
            String binaryString = Integer.toBinaryString(j);
            while (binaryString.length() < numPositions){
                binaryString = '0' + binaryString;
            }
            System.out.println(binaryString);
            for (int k = 1; k <= 4; k++) {
//                if (binaryString.charAt(numPositions - k - 1) == '1'){
//                    System.out.println(k + " " + (j&k));
//                }
                if ((j&k) == k){
                    System.out.println(" j&K " + k);
                }
            }
        }
    }
}
