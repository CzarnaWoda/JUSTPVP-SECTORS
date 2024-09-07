package pl.blackwater.core.tablist;

import pl.blackwater.core.data.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TablistVariablesParser
{
    private Collection<TablistVariable> tablistVariables;

    public TablistVariablesParser() {
        this.tablistVariables = new ArrayList<TablistVariable>();
    }

    public void add(final TablistVariable variable) {
        this.tablistVariables.add(variable);
    }

    public VariableParsingResult createResultFor(final User user) {
        final Map<String, String> values = new HashMap<String, String>();
        for (final TablistVariable tablistVariable : this.tablistVariables) {
            final String value = tablistVariable.get(user);
            for (final String name : tablistVariable.names()) {
                if (values.containsKey(name)) {
                    FunnyLogger.warning("Conflicting variable name: " + name);
                }
                values.put(name, value);
            }
        }
        return new VariableParsingResult(user, values);
    }
}
