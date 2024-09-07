package pl.blackwaterapi.incognitothreads;

import com.google.common.collect.ImmutableList;
import pl.blackwater.core.Core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IndependentThread extends Thread {
	
	private static IndependentThread instance;
	private static List<IncognitoAction> temp = new ArrayList<>();
	private final Object locker = new Object();
	private List<IncognitoAction> actions = new ArrayList<>();
	
	public IndependentThread() {
		instance = this;
	}
	
	public static IndependentThread getInstance() {
		try {
			if (instance == null)throw new UnsupportedOperationException("IndependentThread is not setup!");
			return instance;
		} catch (Exception ex) {
			Core.exception("IndependentThread", ex.getStackTrace());
		}
		return null;
	}
	
	private static void action(IncognitoAction... actions) {
		IndependentThread it = getInstance();
		for (IncognitoAction action : ImmutableList.copyOf(temp)) {
			if (!Objects.requireNonNull(it).actions.contains(action)) {
				it.actions.add(action);
			}
		}
		for (IncognitoAction action : actions) {
			if (!Objects.requireNonNull(it).actions.contains(action)) it.actions.add(action);
		}
		temp.clear();
		synchronized (Objects.requireNonNull(getInstance()).locker){
			getInstance().locker.notify();
		}
	}
	
	public static void action(IncognitoActionType type) {
		action(new IncognitoAction(type));
	}
	
	public static void action(IncognitoActionType type, Object... values) {
		action(new IncognitoAction(type, values));
	}
	
	public static void actions(IncognitoActionType type) {
		IncognitoAction action = new IncognitoAction(type);
		if (!temp.contains(action)) temp.add(action);
	}
	
	public static void actions(IncognitoActionType type, Object... values) {
		IncognitoAction action = new IncognitoAction(type, values);
		if (!temp.contains(action)) temp.add(action);
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				List<IncognitoAction> currently = new ArrayList<>(actions);
				actions.clear();
				execute(currently);

				synchronized (locker) {
					locker.wait();
				}
			} catch (InterruptedException e) {
				Core.exception("IndependentThread", e.getStackTrace());
			}
		}
	}
	
	private void execute(List<IncognitoAction> actions) {
		for (IncognitoAction action : actions) {
			try {
				if (action == null)continue;
				action.execute();
			} catch (Exception e) {
				Core.exception("IndependentThread", e.getStackTrace());
			}
		}
	}
}
