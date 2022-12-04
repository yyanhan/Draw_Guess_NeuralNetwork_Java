package src.datareader;
/**
 * this class define a data type Data
 * which contains input data as , output and the name of the image
 */
public class Data {

    private double[] input;
    private double[] output;
    private final String name;
    /**
     * this is constructor of Data class
     * @param input
     * @param output
     * @param name this is the name, which the image represent
     */
    public Data(double[] input, double[] output, String name) {
        this.input = input;
        this.output = output;
        this.name = name;
    }
    /**
     * getter of input
     * @return input in double[]
     */
    public double[] getInput() {
        return input;
    }
    /**
     * setter of input
     * @param input in double[]
     */
    public void setInput(double[] input) {
        this.input = input;
    }
    /**
     * getter of output
     * @return output in double[]
     */
    public double[] getOutput() {
        return output;
    }
    /**
     * setter of output
     * @param output in double[]
     */
    public void setOutput(double[] output) {
        this.output = output;
    }
    /**
     * getter of name
     * @return name in String
     */
    public String getname() {
        return name;
    }
}