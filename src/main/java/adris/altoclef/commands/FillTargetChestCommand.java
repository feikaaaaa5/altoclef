package adris.altoclef.commands;


import adris.altoclef.AltoClef;
import adris.altoclef.Debug;
import adris.altoclef.Playground;
import adris.altoclef.TaskCatalogue;
import adris.altoclef.commandsystem.*;
import adris.altoclef.tasks.SchematicBuildTask;
import adris.altoclef.tasks.chest.FillTargetChestTask;
import adris.altoclef.tasksystem.Task;
import adris.altoclef.ui.MessagePriority;
import adris.altoclef.util.ItemTarget;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class FillTargetChestCommand extends Command {

    //hehehe... chat length is 256 so more commands than that cannot be passed anyway.
    private static ArgBase[] genArgBase() throws CommandException {
        final ArgBase[] args = new Arg[256];
        for (int i = 0; i < args.length; i++) {
            args[i] = new Arg(String.class, "arg" + i, "", i);
        }

        return args;
    }

    public FillTargetChestCommand() throws CommandException {
        super("autofill", "Sources materials to fill a target chest automatically. Usage: @autofill material1 material1 ...", genArgBase());
    }

    @Override
    protected void call(AltoClef mod, ArgParser parser) throws CommandException {
        //ItemTarget t = new ItemTarget();
        /*String name = "";
        try {
            name = parser.get(String.class);
        } catch (CommandException e) {
            Debug.logError("Cannot parse parameter. Input format: '@build house.schem'");
        }*/

        final List<Item> itemList = new ArrayList<>();

        if (parser.getArgUnits().length < 1) {
            mod.log("You need to specify at least one resource.", MessagePriority.OPTIONAL);
            mod.log("Use @list to get a list of available resources.", MessagePriority.OPTIONAL);
            finish();
            return;
        }

        for (final String param : parser.getArgUnits()) {
            if (param == null) {
                break;
            }

            if (!TaskCatalogue.taskExists(param)) {
                mod.log("\"" + param + "\" is not a catalogued resource. Can't get it yet, sorry! If it's a generic block try using baritone.", MessagePriority.OPTIONAL);
                mod.log("Use @list to get a list of available resources.", MessagePriority.OPTIONAL);
                finish();
                return;
            }

            itemList.add((new ItemTarget(param)).getMatches()[0]);
        }

        //final String macroName = parser.getArgUnits();
        final Item[] items = itemList.toArray(new Item[itemList.size()]);
        final ItemTarget itemTarget = new ItemTarget(items);

        mod.runUserTask(new FillTargetChestTask(itemTarget));
    }
}
