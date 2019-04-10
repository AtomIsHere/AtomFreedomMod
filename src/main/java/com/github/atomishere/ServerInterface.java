package com.github.atomishere;

import com.github.atomishere.util.FLog;
import com.github.atomishere.util.FUtil;
import com.github.atomishere.util.FLog;
import com.github.atomishere.util.FUtil;

public class ServerInterface extends FreedomService
{

    public static final String COMPILE_NMS_VERSION = "v1_12_R1";

    public ServerInterface(AtomFreedomMod plugin)
    {
        super(plugin);
    }

    @Override
    protected void onStart()
    {
    }

    @Override
    protected void onStop()
    {
    }

    public static void warnVersion()
    {
        final String nms = FUtil.getNmsVersion();

        if (!COMPILE_NMS_VERSION.equals(nms))
        {
            FLog.warning(AtomFreedomMod.pluginName + " is compiled for " + COMPILE_NMS_VERSION + " but the server is running version " + nms + "!");
            FLog.warning("This might result in unexpected behaviour!");
        }
    }

//    public void setOnlineMode(boolean mode)
//    {
//        final PropertyManager manager = getServer().getPropertyManager();
//        manager.setProperty("online-mode", mode);
//        manager.savePropertiesFile();
//    }
//
//    public int purgeWhitelist()
//    {
//        String[] whitelisted = getServer().getPlayerList().getWhitelisted();
//        int size = whitelisted.length;
//        for (EntityPlayer player : getServer().getPlayerList().players)
//        {
//            getServer().getPlayerList().getWhitelist().remove(player.getProfile());
//        }
//
//        try
//        {
//            getServer().getPlayerList().getWhitelist().save();
//        }
//        catch (Exception ex)
//        {
//            FLog.warning("Could not purge the whitelist!");
//            FLog.warning(ex);
//        }
//        return size;
//    }
//
//    public boolean isWhitelisted()
//    {
//        return getServer().getPlayerList().getHasWhitelist();
//    }
//
//    public List<?> getWhitelisted()
//    {
//        return Arrays.asList(getServer().getPlayerList().getWhitelisted());
//    }
//
//    public String getVersion()
//    {
//        return getServer().getVersion();
//    }
//
//    private MinecraftServer getServer()
//    {
//        return ((CraftServer) Bukkit.getServer()).getServer();
//    }

}
