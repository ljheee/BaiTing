package com.baiting.listener;

import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import com.baiting.dialog.CreateListNameDialog;
import com.baiting.font.Fonts;

public class ShowListLabelMouseListener extends MusicMouseListener {

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON3) {
			JPopupMenu pop = new JPopupMenu();
			pop.setPopupSize(150, 80);
			JMenuItem createPlayList = new JMenuItem(getConfigMap().get("pop.menu.play.list.create.label").toString());
			createPlayList.setFont(Fonts.songTi13());
			createPlayList.setHorizontalAlignment(SwingConstants.CENTER);
			createPlayList.addActionListener(new CreateListNameDialog());
			pop.add(createPlayList);
			pop.addSeparator();
			//createPlayList = null;

			JMenuItem listName = new JMenuItem(getConfigMap().get("pop.menu.play.list.name.label").toString());
			listName.setFont(Fonts.songTi13());
			listName.setHorizontalAlignment(SwingConstants.CENTER);
			listName.setEnabled(false);
			pop.add(listName);
			//listName = null;
			
		    JMenuItem defaultListName = new JMenuItem(getConfigMap().get("play.list.default.name").toString());
		    defaultListName.setHorizontalAlignment(SwingConstants.CENTER);
		    defaultListName.setFont(Fonts.songTi13());
		    pop.add(defaultListName);
		   // defaultListName = null;
		    
			pop.show(e.getComponent(), 25, 22);
		}
	}
	
}
