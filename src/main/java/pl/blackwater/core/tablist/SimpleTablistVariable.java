package pl.blackwater.core.tablist;

import pl.blackwater.core.data.User;

import java.util.function.Function;

public final class SimpleTablistVariable implements TablistVariable
{
    private final String[] names;
    private final Function<User, String> function;

    public SimpleTablistVariable(final String name, final Function<User, String> function) {
        this(new String[] { name }, function);
    }

    public SimpleTablistVariable(final String[] names, final Function<User, String> function) {
        this.names = names;
        this.function = function;
    }

    @Override
    public String[] names() {
        return this.names;
    }

    @Override
    public String get(final User user) {
        return this.function.apply(user);
    }
}
