import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;


public class ExternalSort {

    public static Path extsort(Path t1, int runsize){
        Path t2 = Paths.get("T2.txt");
        Path t3 = Paths.get("T3.txt");
        Path t4 = Paths.get("T4.txt");
        String numString = "";
    try {
        Scanner in1 = new Scanner(t1);
        //Keep number of elements in sum to calculate number of runs needed later
        int sum = 0;

        PrintWriter tape3 = new PrintWriter(t3.toString());
        PrintWriter tape4 = new PrintWriter(t4.toString());

        while(in1.hasNextInt()){    //continue until all integers are put into t3 and t4
            int count = 0;
            numString = "";

            //Group elements in different groups of runsize sizes for t3
            while(in1.hasNextInt() && count < runsize){
                numString += in1.nextInt() + " ";
                count++;
                sum++;
            }
            //Sort the group and put it in t3.txt
            if(numString != ""){
                numString = sort(numString);
                tape3.print(numString);
                tape3.flush();}
            numString = "";
            count = 0;

            //Group next elements for insertion into t4
            while(in1.hasNextInt() && count < runsize){
                numString += in1.nextInt() + " ";
                count++;
                sum++;
            }
            //Sort and put into t4.txt
            if(numString != ""){
                numString = sort(numString);
                tape4.print(numString);
                tape4.flush();}
        }
        tape3.close();
        tape4.close();


        /*Calculate # of runs, if num elements * runsize * 2(which is the current size of t3 and t4) is
          even then # runs is the quotient of the two, if odd, then add 1 run
         */
        int runs = sum % (runsize * 2) == 0 ?
                sum / (runsize * 2) : sum / (runsize * 2) + 1;

        for(int i = 0; i < runs; i++){
            if(i % 2 == 0){
                //Going from t3/t4 to t1/t2
                Scanner in3 = new Scanner(t3);
                Scanner in4 = new Scanner(t4);
                //Boolean variable to flip back forth between tapes when loading data
                boolean printer = false;
                PrintWriter tape1 = new PrintWriter(t1.toString());
                PrintWriter tape2 = new PrintWriter(t2.toString());

                //Write data from t3 and t4 into t1 and t2
                while(in3.hasNextInt() || in4.hasNextInt()){
                    numString = "";
                    int count = 0;
                    //Count increase by 2^i every run to be adding increasingly larger sorted groups
                    while(in3.hasNextInt() && count < runsize * Math.pow(2, i)) {
                        numString += in3.nextInt() + " ";
                        count++;

                    }
                    int count2 = 0;
                    while(in4.hasNextInt() && count2 < runsize * Math.pow(2, i)) {
                        numString += in4.nextInt() + " ";
                        count2++;
                    }

                    numString = sort(numString);

                    //Alternate where to print the groups between each tape
                    if(printer)
                    {tape2.print(numString);
                    tape2.flush();
                }
                    else {
                        tape1.print(numString);
                        tape1.flush();
                    }
                    printer = !printer;
                }
                tape1.close();
                tape2.close();
            }
            if(i % 2 == 1){
                //Going from t1 and t2 to t3 and t4
                numString = "";

                Scanner inone = new Scanner(t1);
                Scanner intwo = new Scanner(t2);
                //Boolean variable to switch between writing to t1 and t2
                boolean printer = false;

                tape3 = new PrintWriter(t3.toString());
                tape4 = new PrintWriter(t4.toString());
                //Same code as lines 75 - 88
                while(inone.hasNextInt() || intwo.hasNextInt()){
                    numString = "";
                    int count = 0;
                    while(inone.hasNextInt() && count < runsize * Math.pow(2,i)){
                        numString += inone.nextInt() + " ";
                        count++;
                    }
                    int count2 = 0;
                    while(intwo.hasNextInt() && count2 < runsize * Math.pow(2,i)){
                        numString += intwo.nextInt() + " ";
                        count2++;
                    }
                    numString = sort(numString);

                    if(!printer) {
                        tape3.print(numString);
                        tape3.flush();
                    }
                    else{
                        tape4.print(numString);
                        tape4.flush();
                    }
                    printer = !printer;
                }
                tape3.close();
                tape4.close();
            }
        }

        //If # runs odd, then the final sorted list is in t1, if even, it's in t3
        if(runs % 2 == 1)
            return t1;
        else
            return t3;



    }
    catch(Exception e){
        System.out.println(e);
    }
    return t1;
    }

    public static String sort(String numString){
        //Put all elements in an array
        String[] stringArray = numString.split(" ");
        int[] intArray = new int[stringArray.length];

        //Convert all the elements into integers for the sort function
        for(int i = 0; i < stringArray.length; i++)
            intArray[i] = Integer.parseInt(stringArray[i]);

        //Sort
        Arrays.sort(intArray);

        //Copy sorted values back into a string
        String sorted = "";
        for(int i = 0; i < stringArray.length; i++)
            sorted += intArray[i] + " ";

        return sorted;
    }

    public static void print(Path sorted){
        try{
            Scanner scan = new Scanner(sorted);
            //Print file name
            System.out.print(sorted.toString() + " ");
            //For every int in Path sorted, print it out
            while(scan.hasNextInt()) {
                System.out.print(scan.nextInt() + " ");
            }
        }
        catch(Exception ex){
            System.out.println(ex);
        }
    }

    public static void main(String args[]){
        //Ask user for integer input and store it in runsize
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter runsize: ");
        int runsize = scan.nextInt();

        Path t1 = Paths.get("T1.txt");

        Path sorted = extsort(t1, runsize);
        print(sorted);
    }

}
