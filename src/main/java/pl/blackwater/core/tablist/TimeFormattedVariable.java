package pl.blackwater.core.tablist;

import pl.blackwater.core.data.User;

import java.util.function.Function;

public class TimeFormattedVariable implements TablistVariable
{
    private final String[] names;
    private final Function<User, Integer> function;

    public TimeFormattedVariable(final String name, final Function<User, Integer> function) {
        this(new String[] { name }, function);
    }

    public TimeFormattedVariable(final String[] names, final Function<User, Integer> function) {
        this.names = names;
        this.function = function;
    }

    @Override
    public String[] names() {
        return this.names;
    }

    @Override
    public String get(final User user) {
        final long result = this.function.apply(user);
        if (result < 10L) {
            return "0" + String.valueOf(result);
        }
        return String.valueOf(result);
    }
}
