package application.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class MenuBarPresenter{
	@FXML MenuItem menu_new;
	@FXML MenuItem menu_open;
	@FXML MenuItem menu_exit;

	@FXML public void onMenuOnclick(ActionEvent event) {
		if(event.getSource() == menu_new){
			System.out.println("======== new ========");
		}else if(event.getSource() == menu_open){
			System.out.println("======== open ========");
		}else if(event.getSource() == menu_exit){
			System.out.println("======== exit ========");
		}
	}
}
