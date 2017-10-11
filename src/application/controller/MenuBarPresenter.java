package application.controller;

import application.dialog.DialogHelper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class MenuBarPresenter{
	@FXML MenuItem menu_new;
	@FXML MenuItem menu_open;
	@FXML MenuItem menu_exit;
	@FXML MenuItem menu_about;
	@FXML MenuItem menu_instruct;
	@FXML MenuItem menu_feedback;
	@FXML MenuItem menu_check_update;
	@FXML MenuItem menu_preference;

	@FXML public void onMenuOnclick(ActionEvent event) {
		if(event.getSource() == menu_new){
		}else if(event.getSource() == menu_open){
		}else if(event.getSource() == menu_exit){
			Platform.exit();
		}else if(event.getSource() == menu_about){
			DialogHelper.about();
		}else if(event.getSource() == menu_instruct){
			//
		}else if(event.getSource() == menu_feedback){

		}else if(event.getSource() == menu_check_update){

		}else if(event.getSource() == menu_preference){

		}
	}
}
