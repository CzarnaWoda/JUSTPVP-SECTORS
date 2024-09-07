package pl.blackwater.core.commands;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pl.blackwater.core.Core;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.RankSetManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.UUID;

public class YouTubeCommand extends PlayerCommand implements Colors {

	public YouTubeCommand() {
		super("youtube", "informacje o randze premium", "/yt", "core.youtube");
	}
	@Getter
	@Setter
	private static boolean youtube;
	private static HashMap<UUID, Long> times = new HashMap<>();

	@Override
	public boolean onCommand(Player player, String[] args) {
		if(!youtube){
			return Util.sendMsg(player, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Komenda zostala tymczasowo wylaczona!")));
		}
		if(args.length != 1){
			return Util.sendMsg(player, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Aby zdobyć range " + ImportantColor + BOLD + "YouTube " + MainColor + "musisz wrzucic film który w tytule ma " + ImportantColor + " 'JustPvP.PL' " + MainColor + " oraz w opisie zawiera " + ImportantColor + "'Nick: " + player.getDisplayName() + "'" + MainColor + ", nastepnie wpisz " + ImportantColor + "/youtube [id filmu]" + MainColor  + ", dla przykladu film który ma link " + ImportantColor + "https://www.youtube.com/watch?v=XXXX" + MainColor + " posiada ID " + ImportantColor + "'XXXX'" + MainColor + ", link do triller'u: " + ImportantColor + "shorturl.at/xIKOZ")));
		}
		if(times.get(player.getUniqueId()) != null && times.get(player.getUniqueId()) >= System.currentTimeMillis()){
			return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Komenda /youtube moze byc wykonywana co 5 minut!");
		}
		times.put(player.getUniqueId(), System.currentTimeMillis()+5*60*1000L);
		if(UserManager.getUser(player).getRank().equalsIgnoreCase("youtube")){
			return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Posiadasz juz range YouTube");
		}
		final String id = args[0];
		if(Core.getYouTubeConfig().isSection(id)) {
			return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Na to ID filmu został juz przypisany YouTuber!");
		}
		final JSONParser parser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) parser.parse(getContent("https://www.googleapis.com/youtube/v3/videos?part=snippet%2Cstatistics&id=" + id + "&key=AIzaSyAAvpqu9No4cRAFlH1fsARv3ZgzU-1Tx1E"));
		} catch (Exception e) {
			e.printStackTrace();
			return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Wyslano niepoprawny ID filmu lub film nie spelnia wymagań");
		}
		final JSONObject pageInfo = (JSONObject) jsonObject.get("pageInfo");
		if(Integer.parseInt(String.valueOf(pageInfo.get("totalResults"))) == 0){
			return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Wyslano niepoprawny ID filmu lub film nie spelnia wymagań");
		}
		jsonObject = (JSONObject) ((JSONArray) jsonObject.get("items")).get(0);
		jsonObject = (JSONObject) jsonObject.get("snippet");
		if(String.valueOf(jsonObject.get("description")).contains(player.getDisplayName()) && String.valueOf(jsonObject.get("title")).contains("JustPvP.PL")){
			String channelID = String.valueOf(jsonObject.get("channelId"));
			JSONObject channel = null;
			try {
				jsonObject = (JSONObject) parser.parse(getContent("https://www.googleapis.com/youtube/v3/channels?part=snippet%2CcontentDetails%2Cstatistics&id=" + channelID + "&key=AIzaSyAAvpqu9No4cRAFlH1fsARv3ZgzU-1Tx1E"));
			} catch (ParseException e) {
				e.printStackTrace();
				return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Wyslano niepoprawny ID filmu lub film nie spelnia wymagań");
			}
			channel = (JSONObject) ((JSONArray) jsonObject.get("items")).get(0);
			channel = (JSONObject) channel.get("statistics");
			if(Integer.parseInt(String.valueOf(channel.get("subscriberCount"))) < 1000){
				return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Aby otrzymac range YouTube musisz posiadac przynajmniej 1000 subskrybcji");
			}
			Core.getYouTubeConfig().createSection(id, player.getDisplayName());
			RankSetManager.setRank(UserManager.getUser(player), "YouTube-BOT", Core.getRankManager().getRank("youtube"), Util.parseDateDiff("14d",true));
			return Util.sendMsg(player, SpecialSigns + "->> " + MainColor + "Otrzymales range " + ImportantColor + "YouTube");
		}else{
			return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Wyslano niepoprawny ID filmu lub film nie spelnia wymagań");
		}
	}

	private static String getContent(String urlName) {
		String body = null;
		try{
			URL url = new URL(urlName);
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			body = toString(in,encoding);
			in.close();
		} catch (Exception ignored){}
		return body;
	}
	private static String toString(InputStream in, String encoding) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[8192];
		int len = 0;
		while ((len = in.read(buf)) != -1)
			baos.write(buf,0,len);
		in.close();
		return new String(baos.toByteArray(),encoding);
	}

}
