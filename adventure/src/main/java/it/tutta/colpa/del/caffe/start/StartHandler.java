package it.tutta.colpa.del.caffe.start;

import it.tutta.colpa.del.caffe.start.boundary.MainPage;
import it.tutta.colpa.del.caffe.start.control.MainPageController;

/**
 * @author giovav
 * @since 16/07/25
 */
public class StartHandler {
    public static void main(String[] args){
        MainPageController controller = new MainPageController();
        MainPage mainPage = new MainPage(controller);
        controller.setFrame(mainPage);
    }
}
