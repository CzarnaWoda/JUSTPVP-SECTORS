package pl.blackwaterapi.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import pl.blackwater.core.Core;


public class Ticking
  implements Runnable
{
  @SuppressWarnings({ "unchecked", "rawtypes" })
public Ticking()
  {
    this.lastPoll = System.nanoTime();
    (history = new LinkedList()).add(20.0D);
  }
  
  public void start()
  {
    Bukkit.getScheduler().runTaskTimer(Core.getPlugin(), this, 1000L, 50L);
  }
  
  public void run()
  {
    long startTime = System.nanoTime();
    long timeSpent = (startTime - this.lastPoll) / 1000L;
    if (timeSpent == 0L) {
      timeSpent = 1L;
    }
    if (getHistory().size() > 10) {
      getHistory().remove();
    }
    double tps = 5.0E7D / timeSpent;
    if (tps <= 21.0D) {
      getHistory().add(tps);
    }
    this.lastPoll = startTime;
    double avg = 0.0D;
    for (Double f : getHistory()) {
      if (f != null) {
        avg += f;
      }
    }
    df.setRoundingMode(RoundingMode.HALF_UP);
    result = df.format(avg / getHistory().size());
    resultd = avg / getHistory().size();
  }
  
  public static String getTPS()
  {
	double d = resultd;
	if(d <= 20.0 && d >= 17.0){
		return Util.fixColor("&a" + result);
	}else if (d < 17.0 && d >= 14.0){
		return Util.fixColor("&6" + result);
	}
	else if (d < 14.0 && d >= 10.0){
		return Util.fixColor("&c" + result);
	}else if (d < 10.0){
		return Util.fixColor("&4" + result);
	}
	return Util.fixColor("&a" + result);
  }
  public static double getResult()
  {
	  return resultd;
  }
  public static LinkedList<Double> getHistory(){
	  return history;
  }
  
  private static DecimalFormat df = new DecimalFormat("#,###.##");
  private transient long lastPoll;
  private static LinkedList<Double> history;
  private static String result = "20.0";
  private static double resultd = 20.0;
}
