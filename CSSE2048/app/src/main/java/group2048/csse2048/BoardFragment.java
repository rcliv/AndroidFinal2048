package group2048.csse2048;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by andrewtoomey on 4/12/17.
 */

public class BoardFragment extends Fragment {

    private IMainGame listener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BoardView boardView = new BoardView(getContext());
        View view =  inflater.inflate(R.layout.board_fragment, container, true);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.boardFragment);
        layout.addView(boardView);
        boardView.game.newGame();
        listener.setCurrentGame(boardView.game);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IMainGame) {
            listener = (IMainGame) context;
        }

    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }
}
