package pl.blackwater.core.managers;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

public class EventManager {
	@Getter public static HashMap<Integer,String> turbomoney = new HashMap<>();
	@Setter @Getter public static String turbomoney_admin = null;
	@Setter @Getter public static Long turbomoney_time = 0L;
	
	@Getter public static HashMap<Integer,String> turbodrop = new HashMap<>();
	@Setter @Getter public static String turbodrop_admin = null;
	@Setter @Getter public static Long turbodrop_time = 0L;
	
	@Getter public static HashMap<Integer,String> turboexp = new HashMap<>();
	@Setter @Getter public static String turboexp_admin = null;
	@Setter @Getter public static Long turboexp_time = 0L;
	
	@Getter public static HashMap<Integer,String> drop = new HashMap<>();
	@Setter @Getter public static String drop_admin = null;
	@Setter @Getter public static Long drop_time = 0L;
	
	@Getter public static HashMap<Integer,String> exp = new HashMap<>();
	@Setter @Getter public static String exp_admin = null;
	@Setter @Getter public static Long exp_time = 0L;
	
	@Getter public static HashMap<Integer,String> infinitystone = new HashMap<>();
	@Setter @Getter public static String infinitystone_admin = null;
	@Setter @Getter public static Long infinitystone_time = 0L;
	
	public static boolean isOnTurboMoney() {
		return turbomoney_time > System.currentTimeMillis();
	}
	
	public static boolean isOnTurboDrop() {
		return turbodrop_time > System.currentTimeMillis();
	}
	
	public static boolean isOnTurboExp() {
		return turboexp_time > System.currentTimeMillis();
	}
	
	public static boolean isOnEventDrop() {
		return drop_time > System.currentTimeMillis();
	}
	
	public static boolean isOnEventExp() {
		return exp_time > System.currentTimeMillis();
	}
	
	public static boolean isOnEventInfinityStone() {
		return infinitystone_time > System.currentTimeMillis();
	}
	
}
