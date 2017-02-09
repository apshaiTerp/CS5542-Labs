import java.io.File;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Created by AC010168 on 2/4/2017.
 */
public class GenerateLinearDataSet {

    public static void main(String[] args) {
        try {
            PrintWriter writer = new PrintWriter(new File("C:\\Users\\ac010168\\Desktop\\linearDataSet.txt"));
            int[] avgTemps   = {66, 68, 69, 71, 75, 76, 76, 73};
            int[] monthRange = new int[17];

            Random rand = new Random();

            for (int month =  0; month < avgTemps.length; month++) {
                //Build a temprange
                monthRange[0]  = avgTemps[month] - 3;
                monthRange[1]  = avgTemps[month] - 2;
                monthRange[2]  = avgTemps[month] - 2;
                monthRange[3]  = avgTemps[month] - 1;
                monthRange[4]  = avgTemps[month] - 1;
                monthRange[5]  = avgTemps[month] - 1;
                monthRange[6]  = avgTemps[month];
                monthRange[7]  = avgTemps[month];
                monthRange[8]  = avgTemps[month];
                monthRange[9]  = avgTemps[month];
                monthRange[10] = avgTemps[month];
                monthRange[11] = avgTemps[month] + 1;
                monthRange[12] = avgTemps[month] + 1;
                monthRange[13] = avgTemps[month] + 1;
                monthRange[14] = avgTemps[month] + 2;
                monthRange[15] = avgTemps[month] + 2;
                monthRange[16] = avgTemps[month] + 3;

                for (int day = 0; day < 30; day++) {
                    int dayTemp = monthRange[rand.nextInt(17)];

                    for (int chimp = 1; chimp <= 6; chimp++) {
                        int corrFactor = (dayTemp - 63) / 2;
                        int dailyInteractions = dayTemp - 63 + rand.nextInt(45);

                        writer.println("" + dayTemp + "," + dailyInteractions);
                    }
                }

            }

            writer.flush();
            writer.close();

        } catch (Throwable t) {
            t.printStackTrace();
        }

    }
}
