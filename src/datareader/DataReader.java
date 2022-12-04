package src.datareader;

import src.math.Matrix;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
// import for scale
import java.awt.Graphics2D;
import java.awt.*;
import java.util.Scanner;
//weight reader writer
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

import src.neural.NeuralNetwork;
/**
 * this is a class for reading the data of test set and trainset
 * it also provides functions, pre-processing of data and images, post-processing of weight
 */
public class DataReader {

	public ArrayList<Data> ListOfDataTrain;
	public ArrayList<Data> ListOfDataTest;
	
	/**
	 * constructor of class DataReader
	 */
	public DataReader(){
		this.ListOfDataTest= readtestset();
		this.ListOfDataTrain= readtrainset();
	}
	/**
	 * convert all images in a path into a list of Data
	 * @param path a path with images of dataset
	 * @return a list of data
	 */

	public static ArrayList<Data> readData(String path){
		ArrayList<Data> trainDataList = new ArrayList<Data>();
		int output = 0;
        File folder = new File(path);
        for (File file : folder.listFiles()){
            try {
				
                double[] inputs = ScaleAndtoArr(ImageIO.read(file));
				
                Data data = new Data(inputs, getOutput(folder.getName()), folder.getName());
                trainDataList.add(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		return trainDataList;
	}
	/**
	 * convert the images in path "trainset" into list Data
	 * @return list of trainData
	 */
	public static ArrayList<Data> readtrainset(){
		ArrayList<Data> trainDataList = new ArrayList<Data>();
		File folder = new File("trainset");
		for(File file : folder.listFiles()){
			ArrayList<Data> tmp = readData(file.getAbsolutePath());
			trainDataList.addAll(tmp);
		}
		return trainDataList;
	}
	/**
	 * convert the images in path "testset" into list Data
	 * @return list of trainData
	 */
	public static ArrayList<Data> readtestset(){
		ArrayList<Data> trainDataList = new ArrayList<Data>();
		File folder = new File("testset");
		for(File file : folder.listFiles()){
			ArrayList<Data> tmp = readData(file.getAbsolutePath());
			trainDataList.addAll(tmp);
		}
		return trainDataList;
	}
	/**
	 * get the index of output with the name of the image
	 * 
	 * @param name the name of the image
	 * @return the index of an image 
	 */
	
	   private static double[] getOutput(String name) {
        double[] output = new double[6];
		for(int p = 0;p!= 6;p++)output[p]=0;
		int i =0;
		if(name.equals(new String("car"))){
			i = 0;
		}else if(name.equals(new String("fish"))){
			i = 1;
		}else if(name.equals(new String("heart"))){
			i = 2;
		}else if(name.equals(new String("house"))){
			i = 3;
		}else if(name.equals(new String("smile"))){
			i = 4;
		}else if(name.equals(new String("x"))){
			i = 5;
		}else{
			return output;
		}
        output[i] = 1;
        return output;
    }
	/**
	 * convert an image in form of png into a binary float[]
	 * black is 1, white is 0
	 * @param image
	 * @return float[] data
	 */
	
	public static double[] PngtoArr(BufferedImage image){	
		int height = image.getHeight();
		int width = image.getWidth();
		double[] data = new double[height*width];
		int count = 0;
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if(image.getRGB(col, row) <= -16777216+5000000){
					data[count] = 1;
				} else {
				data[count] = 0;
				}
				count++;
			}
		}
		return data;
	}
	/**
	 * make the size of an image into 50*50
	 * @param img
	 * @return BufferedImage scaled: a scaled image in type of BufferedImage
	 */
	public static BufferedImage Scaleto50x50(BufferedImage img) {
        Image tmp = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        BufferedImage scaled = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D idk = scaled.createGraphics();
        idk.drawImage(tmp, 0, 0, null);
        idk.dispose();
        return scaled;
    }
	/**
	 * scale the image and convert it into array
	 * @param image
	 * @return float[] result
	 */
	public static double[] ScaleAndtoArr(BufferedImage image){
		BufferedImage scaled =Scaleto50x50(image);

		double[] result = PngtoArr(scaled);
		return result;
	}
		/**
	 * convert the files that contain the weight in file into float list
	 * @param path
	 * @return Matrix result
	 */
	public static Matrix readweights(String path){
		File file = new File(path);
		double[][] result = new double[0][0];
			try {
                Scanner read = new Scanner(file);
				Scanner read2 = new Scanner(file);
				String[] split = read.nextLine().split(" ");
				int row = 1;
				int col = split.length;
				for (;read.hasNextLine();read.nextLine()) {
				row++;
				}	
                result = new double[row][col];
				
				for(int i = 0; i != row; i++){
					for(int p = 0; p != col; p++){
						result[i][p] = read2.nextDouble();
					}
				}
				
            } catch (IOException e) {
            }
			
			Matrix ret= new Matrix(result);
		return ret;
    }
	
	/**
	 * save the current weight into file "weight.txt"
	 * @param matrix current weight
	 */
	public static void writeweights(Matrix matrix,String name){
    PrintWriter pWriter = null;

        try {
            pWriter = new PrintWriter(new BufferedWriter(new FileWriter(name+".txt")));
			for(int i = 0; i != matrix.get_rows(); i++){
				for(int p = 0; p != matrix.get_cols() ; p++){
					pWriter.printf("%.16f ", matrix.get_matrix()[i][p]);
				}
				pWriter.println();
			}
			
	
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (pWriter != null){
                pWriter.flush();
                pWriter.close();
            }
        }
	}
	
    /**
     * this function would cut the white side of an image
     * firstly convert image into binary image, 
     * then select the area without white side and save image, then skalieren to 50*50
     * @param bufferedImage an image
     * @return BufferedImage output_cut_skala
     * @throws Exception
     */
	 
    public static double[] cutSideSkala50 (BufferedImage bufferedImage) {
        // binary image
        BufferedImage binarImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        //save value of binary image
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            for (int j = 0; j < bufferedImage.getWidth(); j++) {
                int rgb = bufferedImage.getRGB(j, i);// int getRGB(int x,int y)
		        binarImage.setRGB(j, i, rgb);// int getRGB(int x,int y)
            }
        }

        //select area
        int x_big = 0;
        int x_small = 0;
        int y_big = 0;
        int y_small = 0;
        for (int i = 0; i < binarImage.getHeight(); i++) {
            for (int j = 0; j < binarImage.getWidth(); j++) {
                if(binarImage.getRGB(j, i)== -16777216){ // int getRGB(weight,height)
                    if(x_small == 0 || x_small > j){
                        x_small = j;
                    }
                    if(x_big < j){
                        x_big = j;
                    }
                    if(y_big < i){
                        y_big = i;
                    }
                    if(y_small == 0 || y_small > i){
                        y_small = i;
                    }
                }
            }
        }
        // show the result of x and y
        //System.out.printf(String.format("x_small:%d,x_big:%d,y_big:%d,y_small:%d\n",x_small,x_big,y_big,y_small));

        // select image, save as [BufferedImage cutImage]
        int x_diff = x_big - x_small;
        int y_diff = y_big - y_small;
        int[][] data = new int[x_diff][y_diff];
        BufferedImage cutImage = new BufferedImage(x_diff, y_diff, BufferedImage.TYPE_BYTE_BINARY);
        for (int i = 0; i < binarImage.getHeight(); i++) {
            if(i < y_big && i > y_small){
                for (int j = 0; j < binarImage.getWidth(); j++) {
                    if(j < x_big && j > x_small ){
                        int rgb_cut = binarImage.getRGB(j, i);
                        cutImage.setRGB(j-x_small, i-y_small, rgb_cut);
                    } // j range
                }// for j
            }//x range
        } // for i

        // save cut on computer
        //File file3 = new File("output_cut.png");
        //ImageIO.write(cutImage,"png",file3);

        // skala to 50*50, save as [BufferedImage output_cut_skala]
        Image image_skala = cutImage.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        BufferedImage output_cut_skala = new BufferedImage(50, 50, BufferedImage.TYPE_BYTE_BINARY);
        Graphics g = output_cut_skala.getGraphics();
        g.drawImage(image_skala, 0, 0, null);
        g.dispose();
        //ImageIO.write(output_cut_skala, "png", new FileOutputStream("output_skala"));
        return PngtoArr(output_cut_skala);

    }
	/**
	 * calculate the accuracy of this neural network.
	 * print the accuracy
	 * @param ML a NeuralNetworkML
	 * @param bla a DataReader
	 */
	private static boolean arraycomp(double[] input,double[] output){/// runden
		if(input.length == output.length){
			for(int i = 0; i != input.length; i++){
				if(input[i]>output[i]+0.3 || input[i]<output[i]-0.3){
					System.out.println("false");
					return false;
					}
			}
			System.out.println("true");
			return true;
		}
		System.out.println("false wrong size of output");
		return false;
	}
	
	public static void accuracy(NeuralNetwork ML, DataReader bla){
		int count = 0;
		for(int i = 0; i != bla.ListOfDataTest.size();i++){
			Matrix guess = ML.forwardcalculation(bla.ListOfDataTest.get(i).getInput());
			System.out.print(bla.ListOfDataTest.get(i).getname()+" ");
			if(arraycomp(guess.MatrixtoArray(),bla.ListOfDataTest.get(i).getOutput()))count++;				
		}
		double accuracy = (double)count/(double)bla.ListOfDataTest.size()*(double)100 ;
		System.out.printf("%d von %d richtig erraten\n",count,bla.ListOfDataTest.size());
		System.out.printf("%.2f percent accuracy\n", accuracy);
			
	}

	/**
	 * this Methode give the name of the guess.
	 * print the accuracy
	 * @param guesses 
	 * @param String with name
	 */

	public String guesstoString(double[] guesses){
		
		double max = 0;
		int indexofmax = 0;
		for(int i = 0;i < guesses.length;i++){
			if(guesses[i] > max){
				max = guesses[i];
				indexofmax = i;
			}
		}
		
		if(indexofmax == 0){ return "car";}
		if(indexofmax == 1){ return "fish";}
		if(indexofmax == 2){ return "heart";}
		if(indexofmax == 3){ return "house";}
		if(indexofmax == 4){ return "smile";}
		if(indexofmax == 5){ return "X";}
	
	return"i can't guess, try again!";
	}
	
}