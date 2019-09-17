/* ----------------------------------------------------------------------------
 * write a simulation program
 * customers come need service at a station (single server) anywhere from 0 to 5 min and anywhere from 0 to 8 min is the arrival times between customers.
 * use your program to find the avg amount of time a customer is in the system, the variance of that time, the avg time the server is idle and the variance of this time.
 * run for 100,000 customers leaving the system.
 * include debugging output for the first 15 customers leaving the system of their arrival time, their service requirement clock time updates and the queues
 *
 * This program -  simulates a single-server from 0 to 5 minutes for 100,000 customers leaving the system
 * The output prints arrival time, their service requirement clock time updates and the queues for the first 15 customers
 *
 * Name              : Main.java
 * Authors           : Anthony Ramnarain
 * Language          : Java
 * Latest Revision   : 09-17-19
 * ------------------------------------------------------------------------
 */

import java.text.DecimalFormat;
import java.util.Random;
import java.lang.Math;

import static javax.sound.midi.ShortMessage.START;

public class Main {

    public static void main(String[] args) {

        long index = 0;                               /* job index            */
        double arrival = START;                       /* time of arrival      */
        double delay;                                 /* delay in queue       */
        double service;                               /* service time         */
        double wait;                                  /* delay + service      */
        double departure = START;                     /* time of departure    */
        Ssq1Sum sum = new Ssq1Sum();
        sum.initSumParas();

        Ssq1 s = new Ssq1();
        Random r = new Random();
        r.setSeed(123456789);

        while (index < Ssq1.LAST) {
            index++;
            arrival = Ssq1.getArrival(r);
            if (arrival < departure)
                delay = departure - arrival;         /* delay in queue    */
            else
                delay = 0.0;                         /* no delay          */
            service = s.getService(r);
            wait = delay + service;
            departure = arrival + wait;              /* time of departure */
            sum.delay += delay;
            sum.wait += wait;
            sum.service += service;
        }
        sum.interarrival = arrival - START;

        DecimalFormat f = new DecimalFormat("###0.00");

        System.out.println("\nfor " + index + " jobs");
        System.out.println("   average interarrival time =   " + f.format(sum.interarrival / index));
        System.out.println("   average wait ............ =   " + f.format(sum.wait / index));
        System.out.println("   average delay ........... =   " + f.format(sum.delay / index));
        System.out.println("   average service time .... =   " + f.format(sum.service / index));
        System.out.println("   average # in the node ... =   " + f.format(sum.wait / departure));
        System.out.println("   average # in the queue .. =   " + f.format(sum.delay / departure));
        System.out.println("   utilization ............. =   " + f.format(sum.service / departure));
    }

}

class Ssq1Sum {                                  /* sum of ...           */
    double delay;                                  /*   delay times        */
    double wait;                                   /*   wait times         */
    double service;                                /*   service times      */
    double interarrival;                           /*   interarrival times */

    void initSumParas() {
        delay = 0.0;
        wait = 0.0;
        service = 0.0;
        interarrival = 0.0;
    }
}

class Ssq1 {

    static long LAST = 10000;                    /* number of jobs processed */
    private static double START = 0.0;                   /* initial time             */

    private static double sarrival = START;


    private static double exponential(Random r) {
        /* ---------------------------------------------------
         * generate an Exponential random variate, use m > 0.0
         * ---------------------------------------------------
         */
        return (-2.0 * Math.log(1.0 - r.nextDouble()));
    }

    private double uniform(Random r) {
        /* ------------------------------------------------
         * generate an Uniform random variate, use a < b
         * ------------------------------------------------
         */
        return (1.0 + (2.0 - 1.0) * r.nextDouble());
    }


    static double getArrival(Random r) {
        /* ------------------------------
         * generate the next arrival time
         * ------------------------------
         */
//    static double sarrival = START;

        sarrival += exponential(r);
        return (sarrival);
    }


    double getService(Random r) {
        /* ------------------------------
         * generate the next service time
         * ------------------------------
         */
        return (uniform(r));
    }

}