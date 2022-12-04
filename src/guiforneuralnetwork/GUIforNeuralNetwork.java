package guiforneuralnetwork;
import src.datareader.*;
import src.neural.NeuralNetwork;
import src.math.Matrix;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class GUIforNeuralNetwork extends Application {
 
    public void start(Stage primaryStage) {
        //create canvas to drew on 
        Canvas canvas = new Canvas(400,400);
        Label result = new Label("    the answer is : ");
        //button to save the pic
        Button btn1 = new Button("guess pic");
        btn1.setStyle("-fx-font-size:19;-fx-background-color: #e79423;");
        btn1.setOnAction(new EventHandler<ActionEvent>() {   
            @Override
            public void handle(ActionEvent event) {
                 File file = new File("CanvasImage.png");
                  if(file != null){
                    try {
                        WritableImage newimage = new WritableImage(400, 400);
                        canvas.snapshot(null, newimage);
                        RenderedImage image = SwingFXUtils.fromFXImage(newimage, null);
                        ImageIO.write(image, "png", file);
                    } catch (IOException ex) {
                    }   
                } 
                BufferedImage test = null;
		try {	
                test = ImageIO.read(new File("CanvasImage.png"));
                } catch (IOException e) {}  
                DataReader bla = new DataReader();
		NeuralNetwork ML = new NeuralNetwork(bla);
                double[] g = bla.cutSideSkala50(test);
                Matrix x =  ML.forwardcalculation(g); 
                double[] d = x.MatrixtoArray();  
                result.setText("the answer  is  :  "+bla.guesstoString(d));
           }
        });
        
    DropShadow shadow = new DropShadow();
    btn1.addEventHandler(MouseEvent.MOUSE_ENTERED,new EventHandler<MouseEvent>() {
        public void handle(MouseEvent e) {
            btn1.setEffect(shadow);
        }
    });
    //Removing the shadow when the mouse cursor is off
    btn1.addEventHandler(MouseEvent.MOUSE_EXITED, 
        new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                btn1.setEffect(null);
            }
    });
        
        
        //main Pane or frame for the canvase
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 600, 600);
        root.setStyle("-fx-background-color: beige ");
        //some properties for drewing 
        GraphicsContext graph;
        graph = canvas.getGraphicsContext2D();
        graph.setFill(Color.WHITE);
        graph.fillRect(0,0,400,400);
        //clear button.
        Button btn2 = new Button("clear");
        btn2.setStyle("-fx-font-size:19;-fx-background-color: #e79423;");
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                 graph.setFill(Color.WHITE);
                 graph.fillRect(0,0,400,400); 
                 result.setText("    the answer is : ");   
            }
         });
        
       
        btn2.addEventHandler(MouseEvent.MOUSE_ENTERED,new EventHandler<MouseEvent>() {
        public void handle(MouseEvent e) {
            btn2.setEffect(shadow);
        }
         });
         //Removing the shadow when the mouse cursor is off
         btn2.addEventHandler(MouseEvent.MOUSE_EXITED, 
        new EventHandler<MouseEvent>() {
             public void handle(MouseEvent e) {
                btn2.setEffect(null);
            }
         });
         
                
        
        graph.setStroke(Color.BLACK);
        graph.setLineWidth(15);
        scene.setOnMousePressed(e->{
            graph.beginPath();
            graph.lineTo(e.getSceneX() - 100 ,e.getSceneY() - 100 );
            graph.stroke();
        });
        
        scene.setOnMouseDragged(e->{
            graph.lineTo(e.getSceneX() - 100,e.getSceneY() - 100 );
            graph.stroke();
        });
        //line for the buttons
       
        result.setStyle("-fx-font-size:21");
        GridPane g = new GridPane();
        g.addRow(0,btn1,btn2);
        g.add(result,0,1);
        g.setHgap(65);// gab between the buttons
        g.setVgap(480);
        g.setAlignment(Pos.TOP_CENTER);
     
        root.getChildren().addAll(canvas,g);//puts all in.
       // result.relocate(100, 10); 
        primaryStage.setTitle("Montagsmaler!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
       
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    

}