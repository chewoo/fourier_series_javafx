/*
 * A Fourier Series Demo for square wave 
 * Made by Che Wu.
 */
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

public class FourierSeriesTriangleWave extends Application{
    
    ArrayList<Double> drawingY;
    GraphicsContext gc;
    double currentTheta;
    double startRadius;
    double circleX;
    double circleY;
    double startDrawingWaveX;
    int fourierN = 1;
    int circleRotateSpeed = 2;
    Stop[] stops = new Stop[] {
        new Stop(0, Color.WHITE),
        new Stop(1, Color.BLACK)
     };
     LinearGradient gradient =
     new LinearGradient(0.5, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
    @Override
    public void start(Stage stage) throws Exception {
        drawingY = new ArrayList<Double>();
        currentTheta = 360;
        startRadius = 150;
        circleX = 220;
        circleY = 250;
        startDrawingWaveX=520;

        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #000000");
        //layout.setPadding(new Insets(10));

        Canvas canvas = new Canvas(1000,500);
        layout.setCenter(canvas);
        gc = canvas.getGraphicsContext2D();

        Slider nSlider = new Slider();
        nSlider.setMin(0);
        nSlider.setMax(100);
        nSlider.setValue(1);
        nSlider.setBlockIncrement(1);
        nSlider.setMajorTickUnit(25);
        nSlider.setMinorTickCount(5);
        nSlider.setPrefWidth(300);
        
        Slider speedSlider = new Slider();
        speedSlider.setMin(0);
        speedSlider.setMax(6);
        speedSlider.setValue(2);
        speedSlider.setBlockIncrement(1);
        speedSlider.setPrefWidth(300);
        speedSlider.setMinorTickCount(0);
        speedSlider.setMajorTickUnit(1);
        speedSlider.setSnapToTicks(true);
        
        VBox sliderBox = new VBox(nSlider,speedSlider);
        sliderBox.setSpacing(20);

        Label nLabel = new Label("N = 1");
        nLabel.setTextFill(Color.WHITE);

        Label   speedLabel = new Label("Rotate Speed = 2");
        speedLabel.setTextFill(Color.WHITE);

        VBox labelBox = new VBox(nLabel,speedLabel);
        labelBox.setSpacing(20);

        Button startButton = new Button("Start");
        startButton.setStyle("-fx-background-color: #ffffff");
        
        Button pauseButton = new Button("Pause");
        pauseButton.setStyle("-fx-background-color: #ffffff");

        FlowPane upPane = new FlowPane(labelBox,sliderBox,startButton,pauseButton);
        upPane.setPadding(new Insets(10,0,0,0));
        upPane.setHgap(100);
        upPane.setAlignment(Pos.CENTER);
        upPane.setStyle("-fx-background-color: #000000");
        layout.setTop(upPane);

        nSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                fourierN = (int)nSlider.getValue();
                nLabel.setText("N = "+(int)nSlider.getValue());
            }
        });

        speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                circleRotateSpeed = (int)speedSlider.getValue();
                speedLabel.setText("Rotate Speed = "+(int)speedSlider.getValue());
            }            
        });

        
        Scene scene = new Scene(layout,1000,600,Color.BLACK);
        scene.setFill(Color.BLACK);
        
        DrawTimer timer = new DrawTimer();

        stage.setTitle("Fourier Series for Triangle Wave");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        timer.start();
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                timer.start();
            }
        });
        pauseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                timer.stop();
            }
        });
        
        
        
    }
    private class DrawTimer extends AnimationTimer{
        @Override
        public void handle(long arg0) {
            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            gc.setStroke(Color.WHITE);
            double currentCircleX = circleX;
            double currentCircleY = circleY;
            double x=currentCircleX;
            double y=currentCircleY;
            for(int i=0;i<fourierN;i++){
                double n = 2*i+1;
                double radius = startRadius * Math.pow(-1,(int)((n-1)/2))*(8/(Math.PI*Math.PI*n*n));
                //radius = Math.abs(radius);

                gc.strokeOval(currentCircleX-Math.abs(radius),currentCircleY-Math.abs(radius), Math.abs(radius)*2, Math.abs(radius)*2);
                x += radius*Math.cos(Math.toRadians(currentTheta*n));
                y += radius*Math.sin(Math.toRadians(currentTheta*n));
                gc.strokeLine(currentCircleX, currentCircleY,x,y);
                gc.setFill(Color.WHITE);
                gc.fillOval(x-2 ,y-2,4,4);
                currentCircleX = x;
                currentCircleY = y;
                
            }   
            drawingY.add(0,y);
            if(drawingY.size()>500){
                drawingY.remove(500);
            }
            
            gc.beginPath();
            gc.moveTo(startDrawingWaveX,y);
            for(int j=0;j<drawingY.size();j++){
                gc.lineTo(j+startDrawingWaveX,drawingY.get(j));
                gc.moveTo(j+startDrawingWaveX,drawingY.get(j));
            }
            gc.closePath();
            gc.setLineWidth(2);
            gc.setStroke(gradient);
            gc.stroke();
            gc.setStroke(Color.WHITE);
            gc.strokeLine(x,y, startDrawingWaveX, y);
            currentTheta-=circleRotateSpeed;
            if(currentTheta<=0){
                currentTheta=360;
            }
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
// javac --module-path $env:PATH_TO_FX --add-modules=javafx.controls,javafx.fxml FourierSeriesSquareWave.java