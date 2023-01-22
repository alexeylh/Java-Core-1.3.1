import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MyGames {
    private String rootPath;
    private String setupState;
    private String errorMessage;
    private StringBuilder log;

    public MyGames(String rootPath) { // rootPath - путь к папке установки (Games) включительно
        this.rootPath = rootPath;
        this.setupState = "New";
        log = new StringBuilder();
    }

    public boolean setup() {
        // Список путей и файлов для создания в формате: { путь [, имя файла1, имя файла2, ... ] }
        String logfileDir = "temp";
        String logfile = "temp.txt";
        String[][] files = new String[][]{{"src"}, {"res"}, {"savegames"}, {logfileDir, logfile}, {"src/test"},
                {"src/main", "Main.java", "Utils.java"}, {"res/drawables"}, {"res/vectors"}, {"res/icons"}
        };
        for (int i = 0; i < files.length; i++) {
            files[i][0] = files[i][0].replace("/", File.separator);
        }

        File rootDir = new File(rootPath);
        if (!rootDir.exists()) {
            errorMessage = "Каталог для установки не создан. Создайте его вручную.";
            return false;
        }
        if (!rootDir.isDirectory()) {
            errorMessage = rootPath + " не является папкой.";
            return false;
        }
        for (File item : rootDir.listFiles()) { // указанная папка для установки что-то содержит
            errorMessage = "Папка " + rootPath + " не пустая.\n" + "Укажите другой путь или удалите содержимое, затем перезапустите установку.";
            return false;
        }

        boolean result = true;
        for (int i = 0; i < files.length; i++) {
            result = createDirOrFile(files[i]);
            if (!result) break;
        }
        if (result) {
            try(FileWriter writer = new FileWriter(rootPath + File.separator + logfileDir
                    + File.separator + logfile, false)) {
                String text = log.toString();
                writer.write(text);
                writer.flush();
            } catch (IOException ex) {
                errorMessage = ex.getMessage();
            }
        }
        if (result) {
            setupState = "Installed";
        } else {
            setupState = "Error";
        }
        return result;
    }

    private boolean createDirOrFile(String[] items) {
        // первый элемент - папка, если не создана - создаеётся
        // второй элемент - файл, если задан - создаётся в указанной папке
        String path = rootPath + File.separator + items[0];
        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                errorMessage = "Ошибка: папка " + items[0] + " не создана\n";
                log.append(errorMessage);
                return false;
            }
            log.append("Создана папка " + items[0] + "\n");
        }
        for (int i = 1; i < items.length; i++) {
            File newFile = new File(path, items[i]);
            try {
                if (newFile.createNewFile()) {
                    log.append("Создан файл " + items[0] + File.separator + items[i] + "\n");
                }
            } catch (IOException ex) {
                errorMessage = "Ошибка: файл " + items[0] + File.separator + items[i] + " не создан\n" + ex.getMessage();
                log.append(errorMessage);
                return false;
            }
        }
        return true;
    }

    public String getErrorMessage() { return errorMessage; }
}
