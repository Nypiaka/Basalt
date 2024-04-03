import service.PackagesService;

import java.util.Set;

public class Main {

    private static final Set<String> AVAILABLE_BRANCHES = Set.of("sisyphus", "p9", "p10");

    private static final String WRONG_FORMAT_MESSAGE = "Wrong input format. Expected: <branch_name1> <branch_name2>";

    private static final String WRONG_BRANCH_NAME = "Wrong branch found. Available branches: " + AVAILABLE_BRANCHES;

    public static boolean validateArgs(String[] args) {
        if (args == null || args.length < 2) {
            System.out.println(WRONG_FORMAT_MESSAGE);
            return false;
        }
        if (!AVAILABLE_BRANCHES.contains(args[0]) || !AVAILABLE_BRANCHES.contains(args[1])) {
            System.out.println(WRONG_BRANCH_NAME);
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        if (!validateArgs(args)) {
            return;
        }
        var firstBranch = args[0];
        var secondBranch = args[1];
        var service = new PackagesService();
        service.compareBranches(firstBranch, secondBranch);
    }
}
