import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import service.ExpanseService;

import java.util.concurrent.Callable;

@Command(
        name = "expanse-tracker",
        description = "Command to run the expanses",
        mixinStandardHelpOptions = true,
        subcommands = {
                ExpanseTracker.AddCommand.class,
                ExpanseTracker.ListCommand.class,
                ExpanseTracker.DeleteCommand.class,
                ExpanseTracker.UpdateCommand.class,
                ExpanseTracker.SummaryCommand.class
        }
)
public class ExpanseTracker implements Callable<Integer> {

    private static final ExpanseService expanseService = new ExpanseService();

    @Override
    public Integer call() throws Exception {
        return 0;
    }

    @Command(
            name = "add",
            description = "Command to add the expanses",
            mixinStandardHelpOptions = true
    )
    static class AddCommand implements Callable<Integer> {

        @Option(names = {"-a", "--amount"}, required = true, description = "Amount of the expanse")
        private int amount;

        @Option(names = {"-d", "--description"}, required = true, description = "Description of the expanse")
        private String description;

        @Override
        public Integer call() throws Exception {
            expanseService.newExpanse(description, amount);
            return 0;
        }
    }

    @Command(
            name = "list",
            description = "Lists all of the expanses!",
            mixinStandardHelpOptions = true
    )
    static
    class ListCommand implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            expanseService.printAllExpanses();
            return 0;
        }
    }

    @Command(
            name = "update",
            description = "Updates the expanses with the given id!",
            mixinStandardHelpOptions = true
    )
    static class UpdateCommand implements Callable<Integer> {

        @Option(names = "--id", description = "Id of the Expanse", required = true)
        private int id;

        @Option(names = {"-a", "--amount"}, description = "Amount of the Expanse", required = true)
        private int amount;

        @Option(names = {"-d", "--description"}, required = true, description = "Description of the Expanse")
        private String description;

        @Override
        public Integer call() throws Exception {
            expanseService.updateExpanse(id, description, amount);
            return 0;
        }
    }

    @Command(
            name = "delete",
            mixinStandardHelpOptions = true,
            description = "Delete the description"
    )
    static class DeleteCommand implements Callable<Integer> {

        @Option(names = "--id", required = true, description = "Id of the Expanse")
        private int id;

        @Override
        public Integer call() throws Exception {
            expanseService.delete(id);
            return 0;
        }
    }

    @Command(
            name = "summary",
            mixinStandardHelpOptions = true,
            description = "Gives the summary about all Expanses"
    )
    static class SummaryCommand implements Callable<Integer> {

        @Option(names = "--month", description = "Month of the Expanses")
        private int month;

        @Override
        public Integer call() throws Exception {
            expanseService.summary(month);
            return 0;
        }
    }

}
