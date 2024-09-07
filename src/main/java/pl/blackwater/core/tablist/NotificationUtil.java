package pl.blackwater.core.tablist;

import com.google.common.collect.Lists;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public final class NotificationUtil
{
    private static final Class<?> PACKET_PLAY_OUT_TITLE_CLASS;
    private static final Class<?> PACKET_PLAY_OUT_CHAT_CLASS;
    private static final Class<?> TITLE_ACTION_CLASS;
    private static final Class<?> CHAT_MESSAGE_TYPE_CLASS;
    private static final Method CREATE_BASE_COMPONENT_NMS;
    private static final Method CREATE_BASE_COMPONENT_CRAFTBUKKIT;
    private static final String BASE_COMPONENT_JSON_PATTERN = "{\"text\": \"{TEXT}\"}";

    public static List<Object> createTitleNotification(final String text, final String subText, final int fadeIn, final int stay, final int fadeOut) {
        final List<Object> packets = Lists.newArrayList();
        final Object titlePacket = PacketCreator.of(NotificationUtil.PACKET_PLAY_OUT_TITLE_CLASS).create().withField("a", NotificationUtil.TITLE_ACTION_CLASS.getEnumConstants()[0]).withField("b", createBaseComponent(text, false)).withField("c", fadeIn).withField("d", stay).withField("e", fadeOut).getPacket();
        final Object subtitlePacket = PacketCreator.of(NotificationUtil.PACKET_PLAY_OUT_TITLE_CLASS).create().withField("a", NotificationUtil.TITLE_ACTION_CLASS.getEnumConstants()[1]).withField("b", createBaseComponent(subText, false)).withField("c", fadeIn).withField("d", stay).withField("e", fadeOut).getPacket();
        packets.addAll(Arrays.asList(titlePacket, subtitlePacket));
        return packets;
    }

    public static Object createBaseComponent(final String text, final boolean keepNewLines) {
        final String text2 = (text != null) ? text : "";
        try {
            return keepNewLines ? Array.get(NotificationUtil.CREATE_BASE_COMPONENT_CRAFTBUKKIT.invoke(null, text2, true), 0) : NotificationUtil.CREATE_BASE_COMPONENT_NMS.invoke(null, StringUtils.replace("{\"text\": \"{TEXT}\"}", "{TEXT}", text2));
        }
        catch (IllegalAccessException | InvocationTargetException ex3) {
            final ReflectiveOperationException ex2;
            final ReflectiveOperationException ex = ex3;
            FunnyLogger.exception(ex.getMessage(), ex.getStackTrace());
            return null;
        }
    }

    public static Object createActionbarNotification(final String text) {
        Object actionbarPacket;
        if (NotificationUtil.CHAT_MESSAGE_TYPE_CLASS != null) {
            actionbarPacket = PacketCreator.of(NotificationUtil.PACKET_PLAY_OUT_CHAT_CLASS).create().withField("a", createBaseComponent(text, false)).withField("b", NotificationUtil.CHAT_MESSAGE_TYPE_CLASS.getEnumConstants()[2]).getPacket();
        }
        else {
            actionbarPacket = PacketCreator.of(NotificationUtil.PACKET_PLAY_OUT_CHAT_CLASS).create().withField("a", createBaseComponent(text, false)).withField("b", (byte)2).getPacket();
        }
        return actionbarPacket;
    }

    static {
        PACKET_PLAY_OUT_TITLE_CLASS = Reflections.getNMSClass("PacketPlayOutTitle");
        PACKET_PLAY_OUT_CHAT_CLASS = Reflections.getNMSClass("PacketPlayOutChat");
        TITLE_ACTION_CLASS = ("v1_8_R1".equals(Reflections.getFixedVersion()) ? Reflections.getNMSClass("EnumTitleAction") : Reflections.getNMSClass("PacketPlayOutTitle$EnumTitleAction"));
        if ("v1_12_R1".equals(Reflections.getFixedVersion())) {
            CHAT_MESSAGE_TYPE_CLASS = Reflections.getNMSClass("ChatMessageType");
        }
        else {
            CHAT_MESSAGE_TYPE_CLASS = null;
        }
        CREATE_BASE_COMPONENT_NMS = Reflections.getMethod(Reflections.getNMSClass("IChatBaseComponent$ChatSerializer"), "a", String.class);
        CREATE_BASE_COMPONENT_CRAFTBUKKIT = Reflections.getMethod(Reflections.getCraftBukkitClass("util.CraftChatMessage"), "fromString", String.class, Boolean.TYPE);
    }
}

