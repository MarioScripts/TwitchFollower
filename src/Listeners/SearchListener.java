package Listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import View.*;

public class SearchListener implements MouseListener {
    private View view;

    public SearchListener(View view){
        this.view = view;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(view.searchIsVisible()){
            view.hideSearch();
        }else{
            view.showSearch();

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}