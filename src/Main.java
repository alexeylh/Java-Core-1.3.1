class Main {
    public static void main(String[] args) {
        MyGames game = new MyGames("D:\\Games");
        if (game.setup()) {
            System.out.println("Игра установлена.");
        } else {
            System.out.println("Ошибка установки:\n" + game.getErrorMessage());
        };
    }
}
