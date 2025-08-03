package it.tutta.colpa.del.caffe.start;

import it.tutta.colpa.del.caffe.start.boundary.MainPage;
import it.tutta.colpa.del.caffe.start.control.Engine;
import it.tutta.colpa.del.caffe.start.control.MainPageController;

/**
 * @author giovav
 * @since 16/07/25
 */
public class StartHandler {
    public static void main(String[] args){
        MainPage mainPage = new MainPage();
        MainPageController controller = new Engine(mainPage);
        mainPage.linkController(controller);
    }
}
