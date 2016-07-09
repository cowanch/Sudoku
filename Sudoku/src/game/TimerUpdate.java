package game;
import java.util.*;
import java.sql.Time;
import javax.swing.*;

public class TimerUpdate extends TimerTask{
	JTextArea text;
	int t;
	String sec;
	String min;
	String hour;
	boolean run;
	public TimerUpdate(JTextArea a){
		text = a;
		t = -1;
		run = false;
	}
	public void run(){
		if(run){
			t++;
			stringTime();
			text.setText(hour+":"+min+":"+sec);
		}
	}
	public void reset(){
		t = -1;
	}
	public void stringTime(){
		sec = Integer.toString(t%60);
		min = Integer.toString((t%3600)/60);
		hour = Integer.toString(t/3600);
		if(sec.length()<2)
			sec = "0"+sec;
		if(min.length()<2)
			min = "0"+min;
		if(hour.length()<2)
			hour = "0"+hour;
	}
	public void start(){ run = true; }
	public void stop(){ run = false; }
}