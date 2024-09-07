package pl.blackwater.core.tablist;

import pl.blackwater.core.data.User;

public interface TablistVariable
{
    String[] names();

    String get(final User p0);
}
