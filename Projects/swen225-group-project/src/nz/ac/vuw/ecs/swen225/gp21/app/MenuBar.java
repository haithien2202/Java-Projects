package nz.ac.vuw.ecs.swen225.gp21.app;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import nz.ac.vuw.ecs.swen225.gp21.persistency.SaveGame;  

// TODO: Auto-generated Javadoc
/**
 * The Class MenuBar.
 */
public class MenuBar extends JMenuBar {  
     
     /** The submenu. */
     JMenu menu, submenu;  
     
     /** The i 8. */
     JMenuItem i1, i2, i3, i4, i5, i6, i7,i8;  
          
          /**
           * Instantiates a new menu bar.
           */
          public MenuBar(){  
        	  menu=new JMenu("Menu"); 
        	  submenu=new JMenu("Play");  
        	  i1 = new JMenuItem(new AbstractAction("Help"){
        		  private static final long serialVersionUID = 1L;
        			public void actionPerformed(ActionEvent e) {
        				AppWindow.helpFrame.display();
        				if (Main.g.isStarted())Main.resumeNPauseGame(true);
        			}	
        	  });
        	  i2 = new JMenuItem(new AbstractAction("Save") {
      			private static final long serialVersionUID = 1L;
      			public void actionPerformed(ActionEvent e) {
      				if (!Main.g.isStarted()) return;
      				try {
      					SaveGame saveGame = new SaveGame();
      					 Main.r.saveRecord(false);
      					saveGame.save();
						 Main.r.saveRecord(false);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
      			}
          });
        	  i3 = new JMenuItem(new AbstractAction("Load") {
        			private static final long serialVersionUID = 1L;
          			public void actionPerformed(ActionEvent e) {
          				 if (!Main.g.isStarted()) Main.loadGame();
          				 else  {
          					 Main.resumeNPauseGame(true);
          					 String[] buttons = {"Yes", "No", "Cancel"};

          					 int rc = JOptionPane.showOptionDialog(AppWindow.frame, "Do you want to save current game?", "Confirmation",
          							 JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[2]);
          					 if (rc == -1) {
         			    	  Main.resumeNPauseGame(false);
          					}else if (rc == 0) {
          					   try {
          						 SaveGame saveGame = new SaveGame();
          						 Main.r.saveRecord(false);
          						 saveGame.save();
         					} catch (IOException e1) {
         						// TODO Auto-generated catch block
         						e1.printStackTrace();
         					}
         			    	   Main.loadGame();
         			    	}else if (rc == 1) {
         			    		Main.loadGame();
         			    	}else if (rc == 2) {
         			    	   Main.resumeNPauseGame(false);
         			    	}
          				}
          			}
              });
        	  i4 = new JMenuItem(new AbstractAction("Reset") {
      			private static final long serialVersionUID = 1L;
      			public void actionPerformed(ActionEvent e) {
      				if (!Main.g.isStarted()) return;
      					Main.g.resetLevel();
      			}
        	  });
        	  i8 = new JMenuItem(new AbstractAction("Replay") {
      			private static final long serialVersionUID = 1L;
      			public void actionPerformed(ActionEvent e) {
      				if (!Main.g.isStarted()) loadRecord();
      				else {
      				Main.resumeNPauseGame(true);
 					 String[] buttons = {"Yes", "No", "Cancel"};

 					 int rc = JOptionPane.showOptionDialog(AppWindow.frame, "Do you want to save current game?", "Confirmation",
 							 JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[2]);
 					 if (rc == -1) {
			    	  Main.resumeNPauseGame(false);
 					}else if (rc == 0) {
 					   try {
			    		 SaveGame s = new SaveGame();
			    		 Main.r.saveRecord(false);
			    		 s.save();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
 					   	loadRecord();
 					    AppWindow.gamePane.pause(false);
			    	}else if (rc == 1) {
			    		loadRecord();
			    		AppWindow.gamePane.pause(false);
			    	}else if (rc == 2) {
			    	   Main.resumeNPauseGame(false);
			    	}
      				}	
      				
      				
      	 		}
        	  });
        	  
        	  i5 = new JMenuItem(new AbstractAction("Exit") {
        			private static final long serialVersionUID = 1L;
        			public void actionPerformed(ActionEvent e) {
        				int a = JOptionPane.showConfirmDialog(AppWindow.frame, "Are you sure you want to exit?");
        				if(a == JOptionPane.YES_OPTION) System.exit(0);
        	 		}
        	  });
        	  i6 = new JMenuItem(new AbstractAction("Level 1") {
        	 		  private static final long serialVersionUID = 1L;
          			  public void actionPerformed(ActionEvent e) {
          				if (!Main.g.isStarted()) Main.startGame(1);
          				else {
          				Main.resumeNPauseGame(true);
     					 String[] buttons = {"Yes", "No", "Cancel"};

     					 int rc = JOptionPane.showOptionDialog(AppWindow.frame, "Do you want to save current game?", "Confirmation",
     							 JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[2]);
     					 if (rc == -1) {
    			    	  Main.resumeNPauseGame(false);
     					}else if (rc == 0) {
     					   try {
    			    		 SaveGame s  =  new SaveGame();
    			    		 Main.r.saveRecord(false);
    			    		 s.save();
    					} catch (IOException e1) {
    						// TODO Auto-generated catch block
    						e1.printStackTrace();
    					}
     					    Main.startGame(1);
    			    	}else if (rc == 1) {
    			    		Main.startGame(1);
    			    	}else if (rc == 2) {
    			    	   Main.resumeNPauseGame(false);
    			    	}
          				}	
          			 }
               }); 
        	  i7 = new JMenuItem(new AbstractAction("Level 2") {
        			private static final long serialVersionUID = 1L;
          			public void actionPerformed(ActionEvent e) {
          				if (!Main.g.isStarted()) Main.startGame(2);
          				else {
          				Main.resumeNPauseGame(true);
    					 String[] buttons = {"Yes", "No", "Cancel"};

    					 int rc = JOptionPane.showOptionDialog(AppWindow.frame, "Do you want to save current game?", "Confirmation",
    							 JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[2]);
    					 if (rc == -1) {
    						 Main.resumeNPauseGame(false);
    					}else if (rc == 0) {
    					   try {
    						   		SaveGame s = new SaveGame();
    						   		Main.r.saveRecord(false);
    						   		s.save();
    				     } catch (IOException e1) {
   						// TODO Auto-generated catch block
    				    	 e1.printStackTrace();
   						}
    					   Main.startGame(2);
    					}else if (rc == 1) {
   			    			Main.startGame(2);
   			    		}else if (rc == 2) {
   			    			Main.resumeNPauseGame(false);
   			    		}
          				
          				}
          			}
              });
        	  submenu.add(i6); submenu.add(i7);  
        	  menu.add(submenu);  
        	  menu.add(i1); menu.add(i2); menu.add(i3); menu.add(i4); menu.add(i8); menu.add(i5); 	  
        	  this.add(menu);  
          }		 
          
          /**
           * Load record.
           */
          private void loadRecord() {
        	  JFileChooser fileChooser = new JFileChooser();
				String path = "recorder" + Main.fs + "finishedRecord" + Main.fs;
				fileChooser.setCurrentDirectory(new File(path));
				int result = fileChooser.showOpenDialog(null);
				File selectedFile = null;
				if (result == JFileChooser.APPROVE_OPTION) {
					Main.g.setStarted(false);
					AppWindow.gamePane.updateTime(null);
				    selectedFile = fileChooser.getSelectedFile();
					AppWindow.gamePane.repPane.setVisible(true);
					Main.isAuto = true;
					Main.r.loadRecord(selectedFile);
					Main.rep = 0;
					Main.replay();
				} else {
					Main.resumeNPauseGame(false);
				}
          }
}