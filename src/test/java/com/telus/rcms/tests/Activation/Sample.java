package com.telus.rcms.tests.Activation;
import java.util.Random;


public class Sample {
    public static void main(String args[]) {
        Random rad = new Random();
      String subscriptionIDPrefix02 = "86";
      String subscriptionID=null;
      int len =0;
      
      do{
      subscriptionID = subscriptionIDPrefix02 + String.valueOf(10000000 + rad.nextInt(89999999));
      System.out.println("Random SUBID : " + subscriptionID);
      len = subscriptionID.length();
        }while(true);
    }
}
