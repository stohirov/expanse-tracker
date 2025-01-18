import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
        name = "expanse-tracker",
        description = "Command to run the expanses",
        mixinStandardHelpOptions = true,
        subcommands = {ExpanseTracker.AddCommand.class}
)
public class ExpanseTracker implements Runnable {



    @Override
    public void run() {

    }

    @Command(
            name = "add",
            description = "Command to add the expanses",
            mixinStandardHelpOptions = true
    )
    static class AddCommand {

    }
}
