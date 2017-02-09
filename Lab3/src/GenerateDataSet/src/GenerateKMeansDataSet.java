import java.io.File;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Created by AC010168 on 2/4/2017.
 */
public class GenerateKMeansDataSet {

    public static void main(String[] args) {
        try {
            PrintWriter writer = new PrintWriter(new File("C:\\Users\\ac010168\\Desktop\\kmeanDataSet.txt"));

            //We want to define a few social circles common for chimps
            //Chimp 1 typically interacts only with 2 and 4 (Small percentage any other chimp
            //Chimp 2 is the mother of 3, so high percentage there (60%), but could be any other (rarely 5)
            //Chimp 3 almost always interacts with Chimp 2 (65%), Chimp 5 (30%), Other Chimps (5%)
            //Chimp 4 is the mother of 5, so high percentage there (60%), but could be any other (rarely 3)
            //Chimp 3 almost always interacts with Chimp 2 (65%), Chimp 3 (30%), Other Chimps (5%)
            //Chimp 6 interacts with all others evenly

            //We're going to generate 1000 data points randomly
            Random rand = new Random();

            for (int i = 0; i < 100; i++) {
                int chimp1  = rand.nextInt(6) + 1;
                int percent = rand.nextInt(100) + 1;
                int chimp2  = 0;

                switch (chimp1) {
                    case 1 :
                        if (percent <= 41)
                            chimp2 = 2;
                        else if (percent <= 82)
                            chimp2 = 4;
                        else if (percent <= 88)
                            chimp2 = 3;
                        else if (percent <= 94)
                            chimp2 = 5;
                        else
                            chimp2 = 6;
                        break;
                    case 2 :
                        if (percent <= 68)
                            chimp2 = 3;
                        else if (percent <= 78)
                            chimp2 = 1;
                        else if (percent <= 88)
                            chimp2 = 4;
                        else if (percent <= 98)
                            chimp2 = 6;
                        else
                            chimp2 = 5;
                        break;
                    case 3 :
                        if (percent <= 65)
                            chimp2 = 2;
                        else if (percent <= 95)
                            chimp2 = 5;
                        else if (percent <= 98)
                            chimp2 = 6;
                        else if (percent <= 99)
                            chimp2 = 1;
                        else
                            chimp2 = 4;
                        break;
                    case 4 :
                        if (percent <= 68)
                            chimp2 = 5;
                        else if (percent <= 78)
                            chimp2 = 1;
                        else if (percent <= 88)
                            chimp2 = 2;
                        else if (percent <= 98)
                            chimp2 = 6;
                        else
                            chimp2 = 3;
                        break;
                    case 5 :
                        if (percent <= 65)
                            chimp2 = 4;
                        else if (percent <= 95)
                            chimp2 = 3;
                        else if (percent <= 98)
                            chimp2 = 6;
                        else if (percent <= 99)
                            chimp2 = 1;
                        else
                            chimp2 = 2;
                        break;
                    case 6 :
                        if (percent <= 20)
                            chimp2 = 1;
                        else if (percent <= 40)
                            chimp2 = 2;
                        else if (percent <= 60)
                            chimp2 = 3;
                        else if (percent <= 80)
                            chimp2 = 4;
                        else
                            chimp2 = 5;
                        break;
                    default : break;
                }

                writer.println ("" + chimp1 + "," + chimp2);
            }

            writer.flush();
            writer.close();

        } catch (Throwable t) {
            t.printStackTrace();
        }


    }
}
