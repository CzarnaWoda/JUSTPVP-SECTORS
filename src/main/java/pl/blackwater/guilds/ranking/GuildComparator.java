package pl.blackwater.guilds.ranking;

import pl.blackwater.guilds.data.Guild;

import java.util.Comparator;

    public class GuildComparator
            implements Comparator<Guild>
    {
        public int compare(Guild g0, Guild g1)
        {
            final Integer p0 = g0.getPoints();
            final Integer p1 = g1.getPoints();
            return p1.compareTo(p0);
        }
}
