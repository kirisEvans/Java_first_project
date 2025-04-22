import view.login.HomeFrame;

import java.io.File;

public class Main {
    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            LoginFrame loginFrame = new LoginFrame(280, 280);
//            loginFrame.setVisible(true);
//            MapModel mapModel = new MapModel(new int[][]{
//                    {1, 2, 2, 1},
//                    {1, 3, 2, 2},
//                    {1, 3, 4, 4},
//                    {0, 0, 4, 4}
//            });
//            GameFrame gameFrame = new GameFrame(600, 450, mapModel);
//            gameFrame.setVisible(false);
//            loginFrame.setGameFrame(gameFrame);
//        });
        HomeFrame homeFrame = new HomeFrame();
        homeFrame.setVisible(true);
        System.out.println(new File("Resources/Character/ginka.bmp").exists());
    }
}
