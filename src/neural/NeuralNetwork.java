package src.neural;

import src.datareader.DataReader;
import src.math.Matrix;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
/**
 * @author: Han 
 * @author: john
 * @author: Weixia
 * @author: Muad
 */
public class NeuralNetwork{
   
   /**
    * the number of nodes in input layer is n 
    * the range of weightInput2Hidden should be +-(1/sqrt(n))
    * initial weight should never same and should never be 0
    */
    private double learningRate = 0.18f;
	private int numberOfInputs;
	private int numberOfOutputs;
	
	private Matrix weightInput_2_Hidden1;
	private Matrix weightHidden1_2_Output;
	
	private Matrix bias_Hidden1;
	private Matrix bias_Output;
	
	public DataReader DataToTrain; // wieder private machen
	
	private int Epochs = 30;
	
	public NeuralNetwork(DataReader dtt){
		int ni = dtt.ListOfDataTrain.get(0).getInput().length;
		int no = dtt.ListOfDataTrain.get(0).getOutput().length;
		int nh = (ni + no)/2; 
		this.numberOfInputs = ni ;
		this.numberOfOutputs = no;
		
		try{
			this.weightInput_2_Hidden1 = DataReader.readweights("weightInput_2_Hidden1.txt");
			this.weightHidden1_2_Output = DataReader.readweights("weightHidden1_2_Output.txt");
		}catch(Exception e){
			System.out.println("new weight");
			this.weightInput_2_Hidden1 = new Matrix().randomMatrix(ni,nh);
			this.weightHidden1_2_Output = new Matrix().randomMatrix(nh,no);
		}
		// random_weights
		try{
		this.bias_Hidden1 = DataReader.readweights("bias_Hidden1.txt");
		this.bias_Output = DataReader.readweights("bias_Output.txt");
		}catch(Exception e){
			System.out.println("new bias");
			this.bias_Hidden1 = new Matrix().randomMatrix(1,nh) ;
			this.bias_Output = new Matrix().randomMatrix(1,no);
		}
		//random_biases
		this.DataToTrain = dtt;
	}
	
   
    // forwardpropagation in separate function for testing later.
	public Matrix forwardcalculation(double[] inputs){
			

		Matrix input_m = new Matrix().ArraytoMatrix(inputs);
		
		Matrix hidden1 = input_m.matMult(this.weightInput_2_Hidden1); 
			   hidden1 = this.bias_Hidden1.matAdd(hidden1);
		       hidden1 = hidden1.sigmoid();	   
		
		Matrix outs = hidden1.matMult(this.weightHidden1_2_Output);
		       outs = this.bias_Output.matAdd(outs);
		       outs = outs.sigmoid();
		
		return outs;
	}

	public void train(){
		
		Collections.shuffle(DataToTrain.ListOfDataTrain);
		for(int i = 0; i < this.Epochs; i++){
			
			for(int j = 0; j < DataToTrain.ListOfDataTrain.size(); j++){
			//Matrix input_m = new Matrix(this.numberOfInputs,1).ArraytoMatrix(this.DataToTrain.ListOfData.get(j).getInput());
			Matrix input_m =new Matrix().ArraytoMatrix(this.DataToTrain.ListOfDataTrain.get(j).getInput());
			// forwardpropagation
			Matrix hidden1 = input_m.matMult(this.weightInput_2_Hidden1); 
				   hidden1 = this.bias_Hidden1.matAdd(hidden1);
				   hidden1 = hidden1.sigmoid();
	   
			
			Matrix outs = hidden1.matMult(this.weightHidden1_2_Output);
				   outs = this.bias_Output.matAdd(outs);
				   outs = outs.sigmoid();
				   
			// backwardpropaagation
			
			Matrix givenOutputs_m =new Matrix().ArraytoMatrix(this.DataToTrain.ListOfDataTrain.get(j).getOutput());
			
			Matrix outpute_error = givenOutputs_m.matSub(outs);
			
			Matrix output_grad = outs.sigmoidDerivation();
				   output_grad = output_grad.matMult(outpute_error);
				   output_grad = output_grad.matMultNum(learningRate);	
				   
			Matrix hidden2_transposed = hidden1.transpose();
			
			Matrix Datlta_OtoH2 = hidden2_transposed.matMult(output_grad);  
			
			this.weightHidden1_2_Output = this.weightHidden1_2_Output.matAdd(Datlta_OtoH2);
			this.bias_Output = this.bias_Output.matAdd(output_grad);
				   
			Matrix weightHidden1_2_Output_tranposed = this.weightHidden1_2_Output.transpose();
			
			
			Matrix hidden1_error = outpute_error.matMult(weightHidden1_2_Output_tranposed);
			
			Matrix hidden1_grad = hidden1.sigmoidDerivation();
				   hidden1_grad = hidden1_grad.matMult(hidden1_error);
				   hidden1_grad = hidden1_grad.matMultNum(learningRate);
					
			Matrix inputs_transposed = input_m.transpose();
			

			
			Matrix Datlta_H1toI = inputs_transposed.matMult(hidden1_grad);	
			this.weightInput_2_Hidden1 = this.weightInput_2_Hidden1.matAdd(Datlta_H1toI);

			this.bias_Hidden1 = this.bias_Hidden1.matAdd(hidden1_grad);
			}
		}
		
		DataReader.writeweights(this.weightInput_2_Hidden1, "weightInput_2_Hidden1");
		DataReader.writeweights(this.weightHidden1_2_Output,"weightHidden1_2_Output");
		DataReader.writeweights(this.bias_Hidden1, "bias_Hidden1");
		DataReader.writeweights(this.bias_Output,"bias_Output");

		
	}

} 