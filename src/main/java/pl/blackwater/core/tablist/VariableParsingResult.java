package pl.blackwater.core.tablist;

import pl.blackwater.core.data.User;

import java.util.Map;

public class VariableParsingResult
{
    private final User user;
    private final Map<String, String> values;

    public VariableParsingResult(final User user, final Map<String, String> values) {
        this.user = user;
        this.values = values;
    }

    public User getUser() {
        return this.user;
    }

    public Map<String, String> getValues() {
        return this.values;
    }

    public String getValue(final String name) {
        return this.values.get(name);
    }

    public String replaceInString(String string) {
        for (final Map.Entry<String, String> entry : this.values.entrySet()) {
            string = StringUtils.replace(string, "{" + entry.getKey() + "}", entry.getValue());
        }
        return string;
    }
}
