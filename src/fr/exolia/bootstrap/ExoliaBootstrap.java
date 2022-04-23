package fr.exolia.bootstrap;

import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.application.integrated.FileDeleter;
import fr.theshark34.swinger.Swinger;
import re.alwyn974.openlauncherlib.LaunchException;
import re.alwyn974.openlauncherlib.external.ClasspathConstructor;
import re.alwyn974.openlauncherlib.external.ExternalLaunchProfile;
import re.alwyn974.openlauncherlib.external.ExternalLauncher;
import re.alwyn974.openlauncherlib.minecraft.util.GameDirGenerator;
import re.alwyn974.openlauncherlib.util.CrashReporter;
import re.alwyn974.openlauncherlib.util.explorer.ExploredDirectory;
import re.alwyn974.openlauncherlib.util.explorer.Explorer;

import java.io.File;

import static fr.theshark34.swinger.Swinger.getResource;
import static fr.theshark34.swinger.Swinger.setResourcePath;

public class ExoliaBootstrap {

    private static re.alwyn974.openlauncherlib.util.SplashScreen splash;
    public static final File EX_B_DIR = new File(GameDirGenerator.createGameDir("ExoliaV2"), "Launcher");
    public static final CrashReporter EX_B_REPORTER = new CrashReporter("Exolia Bootstrap", EX_B_DIR);

    public static void main(String[] args) {
        Swinger.setSystemLookNFeel();
        setResourcePath("/fr/exolia/bootstrap/resources/");
        showSplash();
        try {
            update();
        }
        catch (Exception e) {
            EX_B_REPORTER.catchError(e, "Impossible de mettre à jour le launcher.");
        }

        try {
            launch();
        }
        catch (LaunchException e) {
            EX_B_REPORTER.catchError(e, "Impossible de lancer le launcher.");
        }
    }

    private static void showSplash() {
        splash = new re.alwyn974.openlauncherlib.util.SplashScreen("Exolia Bootstrap", getResource("splash.png"));
        splash.setBackground(Swinger.TRANSPARENT);
        splash.setIconImage(getResource("icon.png"));
        splash.setLayout(null);
        splash.setVisible(true);

    }

    private static void update() throws Exception {
        SUpdate su = new SUpdate("https://exolia.site/app/webroot/bootstrap", EX_B_DIR);
        su.addApplication(new FileDeleter());
        su.start();
    }

    private static void launch() throws LaunchException {
        ClasspathConstructor constructor = new ClasspathConstructor();
        ExploredDirectory gameDir = Explorer.dir(EX_B_DIR);
        constructor.add(gameDir.get("launcher.jar"));
        ExternalLaunchProfile profile = new ExternalLaunchProfile("fr.exolia.launcher.LauncherFrame", constructor.make());
        ExternalLauncher launcher = new ExternalLauncher(profile);
        Process b = launcher.launch();
        splash.setVisible(false);
        try {
            b.waitFor();
        }
        catch (InterruptedException ignored) {
        }
        System.exit(0);
    }
}
