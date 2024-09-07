package pl.blackwaterapi.incognitothreads;

import org.bukkit.entity.Player;
import pl.blackwaterapi.utils.NameUtil;

import java.util.Arrays;

public class IncognitoAction {

    private final IncognitoActionType action;
    private Object[] values;

    public IncognitoAction(IncognitoActionType action) {
        this.action = action;
    }

    public IncognitoAction(IncognitoActionType action, Object... values) {
        this.action = action;
        this.values = values;
    }

    public void execute() {
        switch (action) {
        	case UPDATE_COMMAND:
        		NameUtil.getInstance().updateCommand((Player)values[0], (Boolean)values[1]);
        		break;
        	case UPDATE_JOIN:
        		NameUtil.getInstance().updateJoin((Player) values[0]);
        		break;
        	case UPDATE_RESTART:
        		NameUtil.getInstance().restart((Player) values[0]);
        		break;
        }
    }

    public IncognitoActionType getActionType() {
        return this.action;
    }

    public Object[] getValues() {
        return this.values;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((action == null) ? 0 : action.hashCode());
        result = prime * result + Arrays.hashCode(values);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        IncognitoAction a = (IncognitoAction) o;
        if (action != a.getActionType()) {
            return false;
        }
        return values == null && a.getValues() == null;
    }

}
