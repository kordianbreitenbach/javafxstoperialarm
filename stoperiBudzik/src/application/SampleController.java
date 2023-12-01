package application;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import java.util.PropertyResourceBundle;
import java.util.Enumeration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;

import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.File;


public class SampleController {

    @FXML
    private Tab stoper;

    @FXML
    private Tab alarm;
    
	@FXML
	private Text stoperstoper;
    @FXML
    private Text podajczasalarm;
    @FXML
    private Text alarmalarm;
  
	@FXML
	private Button startstopstoper;

	@FXML
	private Button pauzakontynuujstoper;

	@FXML
	private Text licznikstoper;
	@FXML
	private Button startstopalarm;
	@FXML
	private Button pauzakontynuujalarm;
	@FXML
	private Text licznikalarm;
	@FXML
	private Text pozostaloczasualarm;
	 @FXML
	    private TextField godzinyalarm;
	 @FXML
	    private TextField minutaalarm;
	 @FXML
	    private TextField sekundaalarm;
	 
	 private Timeline timeline;
	 private boolean startstoper=false;
	 private boolean pauzestoper=false;
	 private boolean starpalarm=false;
	 private boolean pauzealarm=false;
	   private String startbuttt;
	   private String stopbuttt;
	   private String pauzabuttt;
	   private String kontynuujbuttt;
	    private int hours = 0;
	    private int minutes = 0;
	    private int seconds = 0;
	    private int milliseconds = 0;
	    boolean teststart=false;
	    boolean alarmstart=false;
	    private MediaPlayer mediaPlayer;
	    private boolean isPlaying = false;
	    private int hoursalarm=00;
	    private int minutesalarm=00;
	    private int secondsalarm=00;

	    private Timeline countdownTimeline;
	    
	    
	    public void initialize() {
	    	Locale currentLocale = Locale.getDefault();
	    	ResourceBundle bundle = ResourceBundle.getBundle("messages",currentLocale);
	    	String podajCzasValue = bundle.getString("podajCzas");
	    	String startbutt = bundle.getString("start");
	    	String stopbutt = bundle.getString("stop");
	    	String pauzabutt = bundle.getString("pauza");
	    	String kontynuujbutt = bundle.getString("kontynuuj");
	    	String godzinka = bundle.getString("godzina");
	    	String minutka = bundle.getString("minuta");
	    	String sekundka = bundle.getString("sekunda");
	    	String pozostalo=bundle.getString("pozostalo");
	    	String alarmo=bundle.getString("alarm");
	    	String stopero=bundle.getString("stoper");
	    	 System.out.println("Wartość dla klucza 'podajCzas': " +stopbutt);
	    	 startbuttt=startbutt;
	    	 stopbuttt=stopbutt;
	    	 pauzabuttt=pauzabutt;
	    	 kontynuujbuttt=kontynuujbutt;
	    	 podajczasalarm.setText(podajCzasValue);
	    	 startstopstoper.setText(startbutt);
	    	 startstopalarm.setText(startbutt);
	    	 godzinyalarm.setPromptText(godzinka);
	    	 minutaalarm.setPromptText(minutka);
	    	 sekundaalarm.setPromptText(sekundka);
	    	 pozostaloczasualarm.setText(pozostalo);
	    	 alarmalarm.setText(alarmo);
	    	 stoperstoper.setText(stopero);
	    	 alarm.setText(alarmo);
	    	 stoper.setText(stopero);
	    	 pauzakontynuujalarm.setText(pauzabutt);
	    	 pauzakontynuujstoper.setText(pauzabutt);
	        // Initialize the timeline with a KeyFrame that updates the stopwatch every millisecond
	        timeline = new Timeline(new KeyFrame(Duration.millis(1), this::updateStopwatch));
	        timeline.setCycleCount(Timeline.INDEFINITE);
	        startstopstoper.setOnAction(event ->startStopStoperClicked());
	        pauzakontynuujstoper.setOnAction(event ->pauzaKontynuujStoperClicked());
	      
	        String musicFile = "yo_phone_linging.mp3";
	        Media sound = new Media(getClass().getResource(musicFile).toExternalForm());
	        mediaPlayer = new MediaPlayer(sound);

	      
	        
	        countdownTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateCountdown()));
	        countdownTimeline.setCycleCount(Timeline.INDEFINITE);

	        // Set up action for start/stop button
	        startstopalarm.setOnAction(event -> toggleMusicAndCountdown());
	        pauzakontynuujalarm.setOnAction(event->togglepauzacountdown());
	        godzinyalarm.setTextFormatter(new TextFormatter<Integer>(new StringConverter<Integer>() {
	            @Override
	            public String toString(Integer object) {
	                if (object == null) {
	                    return "";
	                }
	                return object.toString();
	            }

	            @Override
	            public Integer fromString(String string) {
	                try {
	                    // sparubuje parsowac watosc inputa na double
	                    return Integer.parseInt(string);
	                } catch (NumberFormatException e) {
	                    // jeśli sa tam litery odzuc
	                    return null;
	                }
	            }
	        }));

	        //dodaj listenera  by sprawdzic czy sa jakies nie numerowe wartosci
	        godzinyalarm.textProperty().addListener((observable, oldValue, newValue) -> {
	            try {
	                if (newValue.isEmpty()) {
	                    // Handle the case when the field is empty
	                    return;
	                }

	                int newval = Integer.parseInt(newValue);
	                
	            } catch (NumberFormatException e) {
	                // jeśli input ma jakąś nie numeryczną wartość cofnij do poprzedniej wartości
	            	godzinyalarm.setText(oldValue);
	            }
	        });
	        minutaalarm.setTextFormatter(new TextFormatter<Integer>(new StringConverter<Integer>() {
	            @Override
	            public String toString(Integer object) {
	                if (object == null) {
	                    return "";
	                }
	                return object.toString();
	            }

	            @Override
	            public Integer fromString(String string) {
	                try {
	                    // sparubuje parsowac watosc inputa na double
	                    return Integer.parseInt(string);
	                } catch (NumberFormatException e) {
	                    // jeśli sa tam litery odzuc
	                    return null;
	                }
	            }
	        }));

	        //dodaj listenera  by sprawdzic czy sa jakies nie numerowe wartosci
	        minutaalarm.textProperty().addListener((observable, oldValue, newValue) -> {
	            try {
	                if (newValue.isEmpty()) {
	                    // Handle the case when the field is empty
	                    return;
	                }

	                int newval = Integer.parseInt(newValue);
	                if (newval < 0 || newval > 59) {
	                    // jeśli wartość jest spoza zakresu 0-60, cofnij do poprzedniej wartości
	                	minutaalarm.setText(oldValue);
	                }
	            } catch (NumberFormatException e) {
	                // jeśli input ma jakąś nie numeryczną wartość cofnij do poprzedniej wartości
	            	minutaalarm.setText(oldValue);
	            }
	        });
	        sekundaalarm.setTextFormatter(new TextFormatter<Integer>(new StringConverter<Integer>() {
	            @Override
	            public String toString(Integer object) {
	                if (object == null) {
	                    return "";
	                }
	                return object.toString();
	            }

	            @Override
	            public Integer fromString(String string) {
	                try {
	                    // sparubuje parsowac watosc inputa na double
	                    return Integer.parseInt(string);
	                } catch (NumberFormatException e) {
	                    // jeśli sa tam litery odzuc
	                    return null;
	                }
	            }
	        }));

	        //dodaj listenera  by sprawdzic czy sa jakies nie numerowe wartosci
	        sekundaalarm.textProperty().addListener((observable, oldValue, newValue) -> {
	            try {
	                if (newValue.isEmpty()) {
	                    // Handle the case when the field is empty
	                    return;
	                }

	                int newval = Integer.parseInt(newValue);
	                if (newval < 0 || newval > 59) {
	                    // jeśli wartość jest spoza zakresu 0-60, cofnij do poprzedniej wartości
	                    sekundaalarm.setText(oldValue);
	                }
	            } catch (NumberFormatException e) {
	                // jeśli input ma jakąś nie numeryczną wartość cofnij do poprzedniej wartości
	                sekundaalarm.setText(oldValue);
	            }
	        });
	        
	       
	        
	        
	    }

	    @FXML
	    private void startStopStoperClicked() {
	    	  if (startstoper==false) {
	 	            // Button is in the "pressed" state, set text to "Stop"
	 	        	startstopstoper.setText(stopbuttt);
	 	        	startstoper=true;
	 	        } else {
	 	            // Button is in the "released" state, set text to "Start"
	 	        	startstopstoper.setText(startbuttt);
	 	        	startstoper= false;
	 	        }
	        if (timeline.getStatus() == Timeline.Status.RUNNING) {
	            // Stop the stopwatch
	            timeline.pause();
	            teststart=false;
	           
	        } else {
	            // Start or resume the stopwatch
	            timeline.play();
	            teststart=true;
	        }
	    }

	    @FXML
	    private void pauzaKontynuujStoperClicked() {
	    	  if (pauzestoper==false) {
	 	            // Button is in the "pressed" state, set text to "Stop"
	    		  pauzakontynuujstoper.setText(kontynuujbuttt);
	    		  pauzestoper=true;
	    		 	
	 	        } else {
	 	            // Button is in the "released" state, set text to "Start"
	 	        	pauzakontynuujstoper.setText(pauzabuttt);
	 	        	pauzestoper= false;
	 	        }
	        // Pause or continue the stopwatch
	    	if( teststart==true) {
	        if (timeline.getStatus() == Timeline.Status.PAUSED) {
	            timeline.play();
	        } else {
	            timeline.pause();
	        }}
	    }

	    private void updateStopwatch(ActionEvent event) {
	        // Update the stopwatch values
	        milliseconds++;
	        if (milliseconds == 1000) {
	            milliseconds = 0;
	            seconds++;
	            if (seconds == 60) {
	                seconds = 0;
	                minutes++;
	                if (minutes == 60) {
	                    minutes = 0;
	                    hours++;
	                }
	            }
	        }

	        // Format the values and update the Text node
	        String formattedTime = String.format("%02d:%02d:%02d:%03d", hours, minutes, seconds, milliseconds);
	        licznikstoper.setText(formattedTime);
	    }
	    
	    private void toggleMusicAndCountdown() {
	    	if (starpalarm==false) {
 	            // Button is in the "pressed" state, set text to "Stop"
	    		startstopalarm.setText(stopbuttt);
	    		starpalarm=true;
 	        } else {
 	            // Button is in the "released" state, set text to "Start"
 	        	startstopalarm.setText(startbuttt);
 	        	starpalarm= false;
 	        }
	        if (isPlaying) {
	            // Pause the music and stop the countdown
	            mediaPlayer.pause();
	            countdownTimeline.pause();
	            mediaPlayer.seek(Duration.ZERO); 
	            alarmstart=false;
	            godzinyalarm.setEditable(true);
	            minutaalarm.setEditable(true);
	            sekundaalarm.setEditable(true);
	        } else {
	            // Start or resume the countdown
	            updateCountdown();
	            countdownTimeline.play();
	            alarmstart=true;
	            godzinyalarm.setEditable(false);
	            minutaalarm.setEditable(false);
	            sekundaalarm.setEditable(false);
	            // Check if the countdown is already at 00:00:00 before starting the music
	            
	        }
	        isPlaying = !isPlaying;
	    }
	    private void   togglepauzacountdown(){
	    	 if (pauzealarm==false) {
	 	            // Button is in the "pressed" state, set text to "Stop"
	    		 pauzakontynuujalarm.setText(kontynuujbuttt);
	    		 pauzealarm=true;
	    		 	
	 	        } else {
	 	            // Button is in the "released" state, set text to "Start"
	 	        	pauzakontynuujalarm.setText(pauzabuttt);
	 	        	pauzealarm= false;
	 	        }
	    	 if(alarmstart==true) {if (isPlaying) {
		            // Pause the music and stop the countdown
		            mediaPlayer.pause();
		            countdownTimeline.pause();
		            isPlaying=false;
		           
		        } else {
		            // Start or resume the countdown
		            updateCountdown();
		            countdownTimeline.play();
		            isPlaying=true;
		            // Check if the countdown is already at 00:00:00 before starting the music
		            
		        }
		    
	    	
	    	 }
	    }
	    private void updateCountdown() {
	        try {
	            // Now you can use hoursValue, minutesValue, and secondsValue in your logic
	            // For example:
	            if (godzinyalarm.getText().isEmpty()) {
	                godzinyalarm.setText("0");
	            }
	            if (minutaalarm.getText().isEmpty()) {
	                minutaalarm.setText("0");
	            }
	            if (sekundaalarm.getText().isEmpty()) {
	                sekundaalarm.setText("0");
	            }

	            hoursalarm = Integer.parseInt(godzinyalarm.getText());
	            minutesalarm = Integer.parseInt(minutaalarm.getText());
	            secondsalarm = Integer.parseInt(sekundaalarm.getText());

	            // Countdown logic
	            if (secondsalarm > 0) {
	                secondsalarm--;
	            } else {
	                if (minutesalarm > 0) {
	                    minutesalarm--;
	                    secondsalarm = 59;
	                } else {
	                    if (hoursalarm > 0) {
	                        hoursalarm--;
	                        minutesalarm = 59;
	                        secondsalarm = 59;
	                    } else {
	                        // When the countdown reaches 00:00:00, stop the music and the countdown
	                    	 mediaPlayer.play();
	                        countdownTimeline.pause();
	                        isPlaying=true;
	                        return; // Exit the method to prevent further updates
	                    }
	                }
	            }

	            // Update the text fields with the current values
	            godzinyalarm.setText(String.format("%02d", hoursalarm));
	            minutaalarm.setText(String.format("%02d", minutesalarm));
	            sekundaalarm.setText(String.format("%02d", secondsalarm));
	            licznikalarm.setText(Integer.toString(hoursalarm) + ":" + Integer.toString(minutesalarm) + ":" + Integer.toString(secondsalarm));
	        } catch (NumberFormatException e) {
	            // Handle the exception, for example, set default values or show an error message
	            // For example:
	            godzinyalarm.setText("0");
	            minutaalarm.setText("0");
	            sekundaalarm.setText("0");
	            hoursalarm = 0;
	            minutesalarm = 0;
	            secondsalarm = 0;
	        }
	    }
	  
	   
}
