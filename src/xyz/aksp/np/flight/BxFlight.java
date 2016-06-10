package xyz.aksp.np.flight;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

import java.io.File;

public class BxFlight extends PluginBase implements Listener{
    @Override
    public void onLoad(){
        this.getLogger().info("插件开启中...");
    }

    @Override
    public void onEnable(){
        this.getLogger().info("插件开启成功");
        this.getServer().getPluginManager().registerEvents(this, this);
        File file = new File(getDataFolder()+"/Flight/");
        File file1 = new File(getDataFolder()+"/config");
        if (!file.exists()){
            file.mkdirs();
            file1.mkdirs();
            Config config = new Config(getDataFolder()+"/config/"+"config.yml",Config.YAML);
            config.set("number",0);
            config.set("is",1);//判断，0为是，1为否
            config.save();
            this.getLogger().info("数据创建成功");
        }else {
            File number = new File(getDataFolder()+"/Flight");
            String[] s = file.list();
            this.getLogger().info("浮空字数量="+s.length);
        }
    }

    @Override
    public void onDisable(){
        this.getLogger().info("插件关闭");
    }

    @EventHandler
    public boolean onPlayerJoin(PlayerJoinEvent event){
        File file = new File(getDataFolder()+"/Flight");
        String[] s = file.list();
        Config config = new Config(getDataFolder()+"/config/"+"config.yml",Config.YAML);
        if (s.length>=1) {
            for (int i = 1; i <=s.length; i++) {
                Config config1 = new Config(getDataFolder() + "/Flight/" + i + ".yml", Config.YAML);
                if (config1.get("on-off").equals("on")) {
                    Player player = event.getPlayer();
                    Vector3 vector3 = new Vector3((double) config1.get("x"), (double) config1.get("y"), (double) config1.get("z"));
                    player.getLevel().addParticle(new FloatingTextParticle(vector3, (String) config1.get("content")), player);
                }

            }
        }
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Config config = new Config(getDataFolder()+"/config/"+"config.yml",Config.YAML);
        Player player = this.getServer().getPlayer(sender.getName());
        switch (command.getName()){
            case "flight":
                if (args[0].equals("off")){
                    if (args[1]==null){
                        sender.sendMessage("用法：/flight off <浮空字名字>");
                    }else {
                        File file = new File(getDataFolder()+"/Flight");
                        String[] s = file.list();
                        for (int i=1;i <=s.length;i++){
                            Config config1 = new Config(getDataFolder() + "/Flight/" + i + ".yml", Config.YAML);
                            if (config1.get("name").equals(args[1])) {
                                config1.set("on-off", "off");
                                config1.save();
                                sender.sendMessage("请重新进入游戏");
                            }else {
                                sender.sendMessage("没有这个浮空字");
                            }
                        }
                    }
                }else if (args[0].equals("on")){
                    if (args[1]==null){
                        sender.sendMessage("用法：/flight on <浮空字名字>");
                    }else {
                        File file = new File(getDataFolder()+"/Flight");
                        String[] s = file.list();
                        for (int i=1;i <=s.length;i++){
                            Config config1 = new Config(getDataFolder() + "/Flight/" + i + ".yml", Config.YAML);
                            if (config1.get("name").equals(args[1])) {
                                config1.set("on-off", "on");
                                config1.save();
                                sender.sendMessage("请重新进入游戏");
                            }else {
                                sender.sendMessage("没有这个浮空字");
                            }
                        }
                    }
                } else {
                    try {
                        double x = player.getX();
                        double y = player.getY();
                        double z = player.getZ();
                        Config config1 = new Config(getDataFolder() + "/Flight/" + config.get("number") + ".yml", Config.YAML);
                        config1.set("name", args[0]);
                        config1.set("x", x);
                        config1.set("y", y + 1);
                        config1.set("z", z);
                        config1.set("content", args[1]);
                        config1.set("on-off", "on");
                        config1.save();
                        int number = (int) config.get("number") + 1;
                        config.set("number", number);
                        config.save();
                        player.getLevel().addParticle(new FloatingTextParticle(new Vector3((double) config1.get("x"), (double) config1.get("y"), (double) config1.get("z")), args[1]));
                        sender.sendMessage("浮空字"+args[0]+"创建完成");
                    } catch (Exception e) {
                        sender.sendMessage("用法：/flight <浮空字名字> <内容>");
                    }
                    return true;
                }
        }
        return true;
    }
}
