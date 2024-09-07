package pl.blackwater.core.tablist;

import pl.blackwater.core.data.User;
import pl.blackwater.guilds.managers.GuildManager;

import java.util.function.Function;

public final class GuildDependentTablistVariable implements TablistVariable
{
    private final String[] names;
    private final Function<User, String> whenInGuild;
    private final Function<User, String> whenNotInGuild;

    public GuildDependentTablistVariable(final String name, final Function<User, String> whenInGuild, final Function<User, String> whenNotInGuild) {
        this(new String[] { name }, whenInGuild, whenNotInGuild);
    }

    public GuildDependentTablistVariable(final String[] names, final Function<User, String> whenInGuild, final Function<User, String> whenNotInGuild) {
        this.names = names.clone();
        this.whenInGuild = whenInGuild;
        this.whenNotInGuild = whenNotInGuild;
    }

    @Override
    public String[] names() {
        return this.names.clone();
    }

    @Override
    public String get(final User user) {
        if (user != null && user.getGuild() != null) {
            return this.whenInGuild.apply(user);
        }
        return this.whenNotInGuild.apply(user);
    }
}
